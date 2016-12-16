package com.VoiceVerified;

import java.io.IOException;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

public class Enroll extends Thread{
	
	private String sound, sampleprompt, brokerUrl, accesskey, customer, userId, transactionid, pin;
	private boolean passedphrase = false;
	private int maxretries, tries, i, maxprompts, counter;
	private boolean enrolled = false;
	private Context context;
	byte[] soundArray;
	private PSPClient pspclient;
	
	/*set Enrollment request parameters*/

	public Enroll(Bundle parameters, Context context) {
		this.brokerUrl = parameters.getString("brokerUrl");
		this.accesskey = parameters.getString("accesskey");
		this.customer = parameters.getString("customer");
		this.userId = parameters.getString("userId");
		this.context = context;
	}

	AudioRecorder ar = new AudioRecorder();
	AudioLength al = new AudioLength();
	
	
	public void getEnrollmentRequest() throws Exception{
		PlaySound ps = new PlaySound(context);
		
		pspclient = new PSPClient(brokerUrl);
		Bundle callinRequestParameters = new Bundle();
		pspclient.clearParameters();
		pspclient.setRequestType("EnrollRequest");
		pspclient.setParameter("accesskey", accesskey);
		pspclient.setParameter("userid", userId);
		pspclient.setParameter("customer", customer);
		pspclient.setParameter("calltype", "embedded");
		pspclient.setParameter("reenroll", "false");		
		pspclient.sendRequest();

		//If the user is already registered

		if(pspclient.getResponseType().equals("ErrorResponse")){

			pspclient.getResponseValue("errorcode");

			if(pspclient.getResponseValue("errorcode").equals("10800")){

				pspclient.clearParameter("reenroll");
				pspclient.setParameter("reenroll", "true");
				pspclient.sendRequest();

			}
		}
		transactionid = pspclient.getResponseValue("transactionid");
		pin = pspclient.getResponseValue("pin");

		pspclient.clearParameters();
		pspclient.setRequestType("InitiateCallinRequest");
		pspclient.setParameter("accesskey", accesskey);
		pspclient.setParameter("customer", customer);
		pspclient.setParameter("pin", pin);
		pspclient.sendRequest();

		sampleprompt = pspclient.getResponseValue("sampleprompt");
		maxprompts = Integer.parseInt(pspclient.getResponseValue("enrollmentphrases"));
		maxretries = Integer.parseInt(pspclient.getResponseValue("retries"));

		callinRequestParameters.putString("samplePrompt", sampleprompt);
		callinRequestParameters.putInt("maxPrompts", maxprompts);
		callinRequestParameters.putInt("maxretries", maxretries);
		
		
		for (i = 1; i <= maxprompts; i++) {
			if (i > 1) {
				// Get next phrase
				pspclient.clearParameters();
				pspclient.setRequestType("GetEnrollmentPromptRequest");
				pspclient.setParameter("customer", customer);
				pspclient.setParameter("accesskey", accesskey);
				pspclient.setParameter("transactionid", transactionid);
				pspclient.setParameter("index", String.valueOf(i));
				pspclient.sendRequest();
				if (pspclient.getResponseType().equals("ErrorResponse")) {
					Log.i("SamplePrompt", "Error");
					// Play error
					//setStatus("Enrollment Failed", -1);
					//playError("0");
					//return false;
				} else {
					sampleprompt = pspclient.getResponseValue("sampleprompt");
				}
			}
			tries = 0;
			passedphrase = false;
			while(tries<maxretries && !passedphrase){
				if (i <= 3) {
					//ps.playSound(R.raw.vv2001);
				} else if (i <= 6) {
					//ps.playSound(R.raw.vv2011);
				}
				//ps.playPrompt(sampleprompt);
				ps.playSound(R.raw.pleasesay);
				al.length(1500);
				ps.playSound(R.raw.beep);
				
				Thread t = new Thread(new Runnable(){
					public void run(){
						try {
							soundArray = ar.recording();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				t.start();
				synchronized(t){
					t.wait(5000);
				}
				ar.isRunning();
				t.join();
				sound = pspclient.encodeSound(soundArray);
				pspclient.clearParameters();
				pspclient.setRequestType("EnrollAddPhraseRequest");
				pspclient.setParameter("customer", customer);
				pspclient.setParameter("accesskey", accesskey);
				pspclient.setParameter("transactionid", transactionid);
				pspclient.setParameter("sequencenumber", String.valueOf(i));
				pspclient.setParameter("sampleprompt", sampleprompt);
				pspclient.setParameter("voicesample", sound);
				pspclient.sendRequest();	
				tries ++;
			}
		}
		

		//return callinRequestParameters;
	}


/*	public String getEnrollmentPromptRequest(int counter) throws Exception{

		//Bundle enrollmentPromptParameters = new Bundle();
		String samplePrompt ;
		// Get next phrase
		pspclient.clearParameters();
		pspclient.setRequestType("GetEnrollmentPromptRequest");
		pspclient.setParameter("customer", customer);
		pspclient.setParameter("accesskey", accesskey);
		pspclient.setParameter("transactionid", transactionid);
		pspclient.setParameter("index", String.valueOf(counter));
		pspclient.sendRequest();
		if (pspclient.getResponseType().equals("ErrorResponse")) {
			Log.i("SamplePrompt", "Error");
			// Play error
			//setStatus("Enrollment Failed", -1);
			//playError("0");
			return null;
		} else {
			samplePrompt = pspclient.getResponseValue("sampleprompt");
			return samplePrompt;
		}
	}


	public String enrollmentAddPhraseRequest(Bundle addPhraseParameters) throws Exception{

		this.transactionid = addPhraseParameters.getString("transactionid");
		this.counter = addPhraseParameters.getInt("counter");
		this.sampleprompt = addPhraseParameters.getString("sampleprompt");
		this.sound = addPhraseParameters.getString("sound");

		pspclient.clearParameters();
		pspclient.setRequestType("EnrollAddPhraseRequest");
		pspclient.setParameter("customer", customer);
		pspclient.setParameter("accesskey", accesskey);
		pspclient.setParameter("transactionid", transactionid);
		pspclient.setParameter("sequencenumber", String.valueOf(counter));
		pspclient.setParameter("sampleprompt", sampleprompt);
		pspclient.setParameter("voicesample", sound);
		pspclient.sendRequest();

		return pspclient.getResponseType();


	}*/
}
