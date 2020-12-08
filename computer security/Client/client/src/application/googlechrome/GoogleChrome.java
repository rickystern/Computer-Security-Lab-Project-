package application.googlechrome;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class GoogleChrome {
	public GoogleChrome() {
		try {
			StringBuffer stringBuffer = new StringBuffer();
			
			stringBuffer.append("@echo off");
			stringBuffer.append("cd C:\\Program Files (x86)\\Google\\Chrome\\Application\n");
			stringBuffer.append("start chrome.exe\nexit");
			
		    Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("batch.bat"), "UTF-8"));
		    writer.write(stringBuffer.toString());
		    writer.close();
		    
		    String path="cmd /c start batch.bat";
			Runtime runtime = Runtime.getRuntime();
			runtime.exec(path);
		}
		catch (IOException exception) {
		  System.err.println("Error creating file");
		  exception.printStackTrace();
		}
		
		
	}
}