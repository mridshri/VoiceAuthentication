package com.VoiceVerified;


import java.io.IOException;
import java.util.Arrays;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;



public class Enrollment{
	
	private String sound, sampleprompt, brokerUrl, accesskey, customer, userId, userIds, transactionid, pin;
	private boolean passedphrase = false;
	private int maxretries, tries, i, maxprompts;
	private boolean enrolled = false;
	private Context context;
	byte[] soundArray;
	//private TextView promptLabel ; 
	private PSPClient pspclient;
	AudioRecorder ar = new AudioRecorder();
	AudioLength al = new AudioLength();
	
	

	public Enrollment (Bundle parameters, Context context){
		
		this.brokerUrl = parameters.getString("brokerUrl");
		this.accesskey = parameters.getString("accesskey");
		this.customer = parameters.getString("customer");
		this.userId = parameters.getString("userId");
		this.context = context;
		
	}

	
/*	public void playWelcome(){
		
		PlaySound ps = new PlaySound(context); 

		try {
			
			ps.playSound(R.raw.vv2000);
			Thread.sleep(2000);
			ps.release();
			
			ps.playSound(R.raw.vv2002);
			Thread.sleep(4700);
			ps.release();
			
			ps.playSound(R.raw.vv2016);
			Thread.sleep(4000);
			ps.release();
		}catch(Exception e){
			
			e.printStackTrace();
			
		}
		
	}
	
	public Bundle enrollmentRequest() throws Exception{
		
		Bundle enrollRequestParameters = new Bundle();
		
		pspclient.clearParameters();
		pspclient.setRequestType("EnrollRequest");
		pspclient.setParameter("accesskey", accesskey);
		pspclient.setParameter("userid", userId);
		pspclient.setParameter("customer", customer);
		pspclient.setParameter("calltype", "embedded");
		pspclient.setParameter("reenroll", "false");		
		pspclient.sendRequest();
		
		If the user is already registered
		
		if(pspclient.getResponseType().equals("ErrorResponse")){
			
			userIds = pspclient.getResponseValue("errorcode");
			
			if(pspclient.getResponseValue("errorcode").equals("10800")){
				
				pspclient.clearParameter("reenroll");
				pspclient.setParameter("reenroll", "true");
				pspclient.sendRequest();
				
			}
		}
		transactionid = pspclient.getResponseValue("transactionid");
		pin = pspclient.getResponseValue("pin");
		
		enrollRequestParameters.putString("transactionId", transactionid);
		enrollRequestParameters.putString("pin", pin);
		
		return enrollRequestParameters;
		
	}*/
	
	
	public String enrollRequest() {
		
		ErrorCode ec = new ErrorCode(context);
		PlaySound ps = new PlaySound(context); 
		
		
		
		try {
			/*
			ps.playSound(R.raw.vv2000);
			al.length(2000);
			ps.release();
			
			ps.playSound(R.raw.vv2002);
			al.length(4700);
			ps.release();
			
			ps.playSound(R.raw.vv2016);
			al.length(4000);
			ps.release();
			*/
			pspclient = new PSPClient(brokerUrl);
			
			/*submit details*/
			
			pspclient.clearParameters();
			pspclient.setRequestType("EnrollRequest");
			pspclient.setParameter("accesskey", accesskey);
			pspclient.setParameter("userid", userId);
			pspclient.setParameter("customer", customer);
			pspclient.setParameter("calltype", "embedded");
			pspclient.setParameter("reenroll", "false");		
			pspclient.sendRequest();
			
			/*If the user is already registered*/
			
			if(pspclient.getResponseType().equals("ErrorResponse")){
				userIds = pspclient.getResponseValue("errorcode");
				if(pspclient.getResponseValue("errorcode").equals("10800")){
					pspclient.clearParameter("reenroll");
					pspclient.setParameter("reenroll", "true");
					pspclient.sendRequest();
				}
			}
			transactionid = pspclient.getResponseValue("transactionid");
			pin = pspclient.getResponseValue("pin");
			
			/*Initiate Enrollment process*/
			
			pspclient.clearParameters();
			pspclient.setRequestType("InitiateCallinRequest");
			pspclient.setParameter("accesskey", accesskey);
			pspclient.setParameter("customer", customer);
			pspclient.setParameter("pin", pin);
			pspclient.sendRequest();
			
			sampleprompt = pspclient.getResponseValue("sampleprompt");
			maxprompts = Integer.parseInt(pspclient.getResponseValue("enrollmentphrases"));
			maxretries = Integer.parseInt(pspclient.getResponseValue("retries"));
			Log.i("SamplePrompt", sampleprompt);
			
			CharSequence text = "Please say "+sampleprompt;
			int duration = Toast.LENGTH_LONG;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
			
			/*Play prompts*/
			
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
				//while(tries<maxretries && !passedphrase){
				while(tries<maxretries && !passedphrase){
					if (i <= 3) {
						//ps.playSound(R.raw.vv2001);
					} else if (i <= 6) {
						//ps.playSound(R.raw.vv2011);
					}
					//ps.playPrompt(sampleprompt);
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
			return pin;
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
