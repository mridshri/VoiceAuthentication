package com.VoiceVerified;
import javax.sound.sampled.*;
import javax.sound.sampled.spi.AudioFileWriter;

import java.io.*;
import java.util.*;

class SoundRecorder extends Thread {

	//AudioApplet applet;
	private TargetDataLine line;
	private ByteArrayOutputStream os;
	long milliseconds;
	
	public SoundRecorder(TargetDataLine line, int seconds) {
		try {
			milliseconds = seconds * 1000;
			this.line = line;
			os = new ByteArrayOutputStream();
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
	}
	
	public synchronized void start() {
		line.start();
		super.start();
	}

	public byte[] getRecording() {
		return os.toByteArray();
	}
	
	public void run() {
		try {
			boolean keeprecording = true;
			int bytesread = 0;
			byte[] bytes = new byte[800];
			long timeused, currenttime;
			long starttime = (new Date()).getTime();
			long volume, tempvolume; 
			int w;
			final long MAX_VOLUME = 20000;

			//applet.setMeter(0);
			//applet.setLevel(0);
			while (keeprecording && bytesread != -1) {
				currenttime = (new Date()).getTime();
				timeused = currenttime - starttime;
				if (timeused < milliseconds) {
					// Show progress
					//applet.setMeter((new Float(timeused * 100 / milliseconds)).intValue());
					// Read input
					bytesread = line.read(bytes, 0, bytes.length);
					if (bytesread >= 0) {
						// Show Volume
						volume = 0;
						for(w=0; w<bytesread; w += 2) {
								tempvolume = Math.abs(bytes[w] + (bytes[w+1]*256));
								if (tempvolume > volume) {
									volume = tempvolume;						
								}
						}
					//	applet.setLevel((new Float(volume * 100 / MAX_VOLUME)).intValue());
						// Write out data
						os.write(bytes, 0, bytesread);
					}
				} else {
					//applet.setMeter(100);
					//applet.setLevel(0);
					keeprecording = false;
				}
			}
			line.stop();
			line.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}