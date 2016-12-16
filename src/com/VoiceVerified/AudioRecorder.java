package com.VoiceVerified;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;


public class AudioRecorder {

	private int bufferRead = 0;
	AudioLength al = new AudioLength();
	CanonicalWaveHeader header = new CanonicalWaveHeader();
	Boolean running ;
	AudioRecord recorder;
	File file = new File("sdcard/output.wav");
	
	public byte[] recording() throws Exception {	
    	try {
    		running = true;
    		OutputStream fos = new FileOutputStream(file);
    		BufferedOutputStream bos = new BufferedOutputStream(fos);
    		DataOutputStream dos = new DataOutputStream(bos);
    		byte[] waveHeader = header.createHeader();
    		dos.write(waveHeader);
			int bufferSize = AudioRecord.getMinBufferSize(8000, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT);
			byte[] tempBuffer = new byte[bufferSize];
			recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT,bufferSize);
			recorder.startRecording();
			
			while(running){
				
				bufferRead = recorder.read(tempBuffer, 0, bufferSize);				
				for (int idxBuffer = 0; idxBuffer < bufferRead; ++idxBuffer) {
					dos.write(tempBuffer[idxBuffer]);
				}
			}
			recorder.stop();
			//dos.flush();
			dos.close();	
			recorder.release();
		
			/*byte[] waveSound = new byte[waveHeader.length+tempBuffer.length];
			System.arraycopy(waveHeader, 0, waveSound, 0, waveHeader.length);
			System.arraycopy(tempBuffer, 0, waveSound, waveHeader.length, tempBuffer.length);
			*/
			
			int fileLength = (int)(file.length());
			byte[] waveSound = new byte[fileLength];
			InputStream fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);
			DataInputStream dis = new DataInputStream(bis);
			int i = 0;
			while(dis.available()>0){
				waveSound[i] = dis.readByte();
				//Log.i(""+i,""+waveSound[i]);
				i++;
			}
			
			return waveSound;
			
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	public boolean isRunning() {
		// TODO Auto-generated method stub
		
		
		
		running = false;
		return running;
	}	
}
