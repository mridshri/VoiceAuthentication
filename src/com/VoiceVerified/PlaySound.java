package com.VoiceVerified;

import android.content.Context;
import android.media.MediaPlayer;

public class PlaySound {
	private Context context;
	private String suffix;
	private int id;
	static MediaPlayer mp;
	AudioLength al = new AudioLength();
	
	public PlaySound(Context context){
		this.context = context;
	}
	
	
	public void playSound(int soundId) {
		mp = MediaPlayer.create(context, soundId);
		mp.start();
	}
	
	public void release(){
		
		mp.release();
		
	}
	
	/*Plays audio prompted by server*/
	public void playPrompt(String prompt) throws Exception {
		
		for (int i = 0; i < prompt.length(); i++) {
			if (i == 0) {
				suffix = "_u1";
			} else if (i == prompt.length() - 1) {
				suffix = "_d1";
			} else {
				suffix = "_m1";
			}
			//playSound(R.raw.vv);
		}
	
	
	}
	
	public void welcomeMessage() {
		//PlaySound ps = new PlaySound(context); 
		//Enroll enroll = new Enroll();
		try {
			
			playSound(R.raw.vv2000);
			al.length(2000);
			release();
			
			playSound(R.raw.vv2002);
			al.length(4700);
			release();
			
			playSound(R.raw.vv2016);
			al.length(4000);
			release();
			
		}catch(Exception e){
			e.printStackTrace();	
		}		
	}

}
