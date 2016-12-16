package com.VoiceVerified;

public class AudioLength {
	long initialTime, finalTime;
	
	public int length(int duration){
		initialTime =  System.currentTimeMillis();
		int i = 0;
	    do{
	        finalTime = System.currentTimeMillis();
	        i++;
	    }
	    while ((finalTime - initialTime) < (duration));
	    return i;
	}
}
