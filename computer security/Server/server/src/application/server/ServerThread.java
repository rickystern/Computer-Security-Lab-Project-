package application.server;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ServerThread implements Runnable {
	private Socket socket;
	private ObjectInputStream objectInputStream;
	
	public ServerThread(Socket socket) {
		this.socket = socket;
	}
	
	public void storeFile(String fileName) throws IOException, ClassNotFoundException {
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(fileName)); 
		
//		System.out.println("Saving: "+fileName);
		
		byte[] bytes = new byte[16*1024];
		int length, offset = 0;
        
        while ((length = this.objectInputStream.read(bytes)) > offset) {
            bufferedOutputStream.write(bytes, offset, length);
        }
        
		bufferedOutputStream.close();
//		System.out.println(fileName+" was stored!");
	}
	
	@Override
	public void run() {
		try {
			boolean continueRunning = true;
			this.objectInputStream = new ObjectInputStream(this.socket.getInputStream());
			
			while(continueRunning) {
				try {
					String data = (String) objectInputStream.readObject();
					
					if (data.equals("AUDIO!")) {
						String fileName = (String) objectInputStream.readObject();
						
						this.storeFile("Audios/"+fileName);
					}
					else if (data.equals("SCREENSHOT!")) {
						String fileName = (String) objectInputStream.readObject();
						
						this.storeFile("ScreenShots/"+fileName);
					}
					else if (data.equals("VIDEOCAPTURE!")) {
						String fileName = (String) objectInputStream.readObject();
						
						this.storeFile("Videos/"+fileName);
					}
					else if (data.equals("END")) {
						continueRunning = false;
					}
				}
				catch(ClassNotFoundException classNotFoundException) {}
				catch(IOException exception) {
//					System.err.println("Socket not found!");
					break;
				}
			}
			
			System.out.println("End of Server Thread");
			
			this.objectInputStream.close();
			this.socket.close();
		}
		catch(IOException exception) { 
			System.out.println("Error from socket");
//			exception.printStackTrace();
		}
		catch(Exception exception) {
			System.err.println("Unknown exception");
//			exception.printStackTrace();
		}
	}
}