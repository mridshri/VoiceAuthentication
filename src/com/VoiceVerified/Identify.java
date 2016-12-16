/*Identity class identifies an enrolled user by voice sample.*/



package com.VoiceVerified;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

//import com.voiceverified.pspclient.*;

public class Identify {
	
	
	String sound, sampleprompt, brokerUrl, accesskey, customer, userId, userIds;
	boolean passed = false;
	private Context context;
	TextView promptLabel ; 
	
	
	
	public Identify (Bundle parameters, Context context){
		this.brokerUrl = parameters.getString("brokerUrl");
		this.accesskey = parameters.getString("accesskey");
		this.customer = parameters.getString("customer");
		this.userId = parameters.getString("userId");
		this.context = context;
	}
	
	
	public boolean identify() throws Exception{
		//Log.i("hiiiiiiiii", userId+brokerUrl);
		//String temp = context.getText(R.id.promptvalue).toString();
		//Log.i("temp", temp);
		PSPClient pspclient = new PSPClient(brokerUrl);
		
		pspclient.clearParameters();
		pspclient.setRequestType("GetSamplePromptRequest");
		pspclient.setParameter("customer", customer);
		pspclient.setParameter("accesskey", accesskey);
		//pspclient.sendRequest();
	
		//sampleprompt = pspclient.getResponseValue("sampleprompt");
		pspclient.clearResponse();
		//Log.i("hiii", sampleprompt);
		// Play welcome message
		playSound(R.raw.vv2000, context);
		//playPrompt(sampleprompt);
		//TextView promptLabel = (TextView)findViewById(R.id.promptvalue);
		//promptLabel.setText(R.id.promptvalue);
		// One attempt only
		//pauseIfRequested();
		//showMessage("Please say: " + sampleprompt);
		
		/*Play promt and Display prompt pending****************************************/
		
		/*Record Sound**************************************************************/
		
		/*pspclient.clearParameters();
		pspclient.setRequestType("MultiVerifySampleRequest");
		pspclient.setParameter("customer", customer);
		pspclient.setParameter("accesskey", accesskey);
		pspclient.setParameter("userids", userIds);
		pspclient.setParameter("sampleprompt", sampleprompt);
		pspclient.setParameter("voicesample", sound);
		//Log.i("indentity Response", pspclient.getResponseType());
		pspclient.sendRequest();
		*/
		return passed;
	}


	private void playPrompt(String prompt) throws Exception {
		
			String suffix;
			for (int i = 0; i < prompt.length(); i++) {
				if (i == 0) {
					suffix = "_u1";
				} else if (i == prompt.length() - 1) {
					suffix = "_d1";
				} else {
					suffix = "_m1";
				}
				//playSound();
			}
		
		
	}


	private void playSound(int soundId, Context context) {
		
		MediaPlayer mp = MediaPlayer.create(context, soundId);
		mp.start();
		mp.release();
	}
	
	
	

}
