package application.video;

import java.io.IOException;

import javax.imageio.ImageIO;

import com.github.sarxos.webcam.Webcam;

import application.network.Network;

public class Video {
	public Video(int counter) {
    	Webcam webcam = Webcam.getDefault();
    	webcam.open();
    	
    	int value = 0;
    	
    	if (counter == 1) {
    		value = 1;
    	}
    	else if (counter == 2) {
    		value = 12;
    	}
    	else {
    		value = (12 * (counter-1));
    	}
    	
    	for (int index = value; index < (counter*12); index++) {
    		try {
    			ImageIO.write(webcam.getImage(), "PNG", new java.io.File("VideoCapture"+index+".png"));
    		}
        	catch (IOException exception) {
        		exception.printStackTrace();
    		}
    	}
    	webcam.close();
    	
    	new Network("VIDEOCAPTURE!"+Integer.toString(value)+"-"+Integer.toString(counter*12));
	}
}