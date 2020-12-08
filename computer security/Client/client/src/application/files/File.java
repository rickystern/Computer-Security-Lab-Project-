package application.files;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class File {

	public File(String text) {
		try {
			FileWriter fileWriter = new FileWriter("keylogger.txt", true);
		    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		    
		    bufferedWriter.write(text+"\n");
		    
		    bufferedWriter.close();
		    fileWriter.close();
		}
		catch(IOException ioException) {
			ioException.printStackTrace();
		}
	}
}
