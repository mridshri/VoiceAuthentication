package com.VoiceVerified;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class VoiceVerified extends Activity {
	
	
	private String userId, samplePrompt;
	private boolean success;
	private String brokerUrl = "https://pspdemo.csidentity.com/xmlbroker/broker";
	private String accesskey = "d41d8cd98f00b204e9800998ecf8427e";
	private String customer = "csidentitydev";
	Bundle parameters = new Bundle();
	Enroll enroll;
	
	
	private ProgressDialog dialog;
    final Context context = VoiceVerified.this;
    PlaySound ps = new PlaySound(context);
    
    
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        EditText et = (EditText)findViewById(R.id.userIdText);
        userId = et.getText().toString();
        parameters.putString("brokerUrl", brokerUrl);
        parameters.putString("accesskey", accesskey);
        parameters.putString("customer", customer);
        parameters.putString("userId", userId);
        enroll = new Enroll(parameters, context);
        
    }
    
    
    public void onStart(){
    	super.onStart();
    	Button enrollment = (Button)findViewById(R.id.enroll);
        Button verify = (Button)findViewById(R.id.verify);
        enrollment.setOnClickListener(startEnrollment);
        
    }
    
    View.OnClickListener startEnrollment = new View.OnClickListener() {
		
    	@Override
    	public void onClick(View enrollment) {
    		showDialog(0);
    		startEnrollment();
    	}
	};

	protected void startEnrollment() {
		Thread backGround = new Thread(){
			@Override
			public void run() {
				ps.welcomeMessage();
				try {
					enroll.getEnrollmentRequest();
					/*samplePrompt = parameters.getString("samplePrompt");
					Message prompt = handler.obtainMessage();
					prompt.obj = samplePrompt;
					handler.sendMessage(prompt);*/
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handler.sendEmptyMessage(0);
			}
		};
		backGround.start();
		
	}
	
	

	@Override
	protected Dialog onCreateDialog(int id){
		dialog = new ProgressDialog(context);
		if(id == 0){
            dialog.setMessage("Welcome to Voice Verified");
            dialog.setIndeterminate(true);
            dialog.setCancelable(true);
			return dialog;
		}
		return null;
	}
	
	private Handler handler = new Handler(){

		public void handleMessage(Message msg){
			dialog.dismiss();
			/*Message fromChild = handler.obtainMessage();
			Log.i("Prompt","hi"+(String)fromChild.obj);*/
			/*CharSequence text = "Please say "+sampleprompt;
			int duration = Toast.LENGTH_LONG;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();*/
		}
		
	};


}


