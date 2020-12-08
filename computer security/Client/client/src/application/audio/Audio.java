package application.audio;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import application.network.Network;

public class Audio {
	private int audioCounter;
	private AudioFormat audioFormat;
	private TargetDataLine microphone;
	
	public Audio(int audioCounter) {
		this.audioCounter = audioCounter;
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(10000);
				}
				catch (InterruptedException exception) {
					exception.printStackTrace();
				}
				stopRecording();
			}
		}).start();
		captureAudio();
	}
	
	private AudioFormat getAudioFormat() {
		float sampleRate = 16000.0F;
		int sampleInbits = 16;
		int channels = 1;
		boolean signed = true;
		boolean bigEndian = false;
		
		return new AudioFormat(sampleRate, sampleInbits, channels, signed, bigEndian);
	}
	
	public void captureAudio() {
		try {
			this.audioFormat = this.getAudioFormat();
			
			DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, this.audioFormat);
		
			this.microphone = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
			this.microphone.open(this.audioFormat);
			this.microphone.start();
			
			AudioSystem.write(new AudioInputStream(this.microphone), AudioFileFormat.Type.WAVE, new File("Recording-"+this.audioCounter+".wav"));
		}
		catch(LineUnavailableException exception) {
			exception.printStackTrace();
		}
		catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	
	public void stopRecording() {
		this.microphone.stop();
		this.microphone.close();
		
		new Network("AUDIO!"+this.audioCounter);
	}
}