package com.VoiceVerified;

import android.content.Context;

public class ErrorCode {
	
	private Context context;

	public ErrorCode(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	PlaySound ps = new PlaySound(context);
	

	public void playError(String errPrompt) throws Exception {
		Integer err = new Integer(errPrompt);
		int e = err.intValue();
		switch (e) {
		case 2003:
			//showMessage("A system error has occurred.");
			ps.playSound(R.raw.vv2003);
			break;
		case 2005:
			//showMessage("We are sorry.  Your voice pattern does not match the one on file.");
			ps.playSound(R.raw.vv2005);
			break;
		case 2013:
			//showMessage("We are unable to complete your enrollment because of problems with your sound system.");
			ps.playSound(R.raw.vv2013);
			break;
		case 2070:
			//showMessage("You’ve spoken too softly");
			ps.playSound(R.raw.vv2070);
			break;
		case 2071:
			//showMessage("Sorry we didn't hear you");
			ps.playSound(R.raw.vv2071);
			break;
		case 2072:
			//showMessage("Sorry I didn't get that");
			ps.playSound(R.raw.vv2072);
			break;	
		case 2080:
			//showMessage("You’ve spoken too loudly");
			ps.playSound(R.raw.vv2080);
			break;
		case 2090:
			//showMessage("Please speak further away from the microphone.");
			ps.playSound(R.raw.vv2090);
			break;
		case 2100:
			//showMessage("Please speak closer to the microphone.");
			ps.playSound(R.raw.vv2100);
			break;
		case 2200:
			//showMessage("There is a problem with your recording.");
			ps.playSound(R.raw.vv2200);
			break;
		case 2250:
			//showMessage("There was too much background noise detected while you were speaking.");
			ps.playSound(R.raw.vv2250);
			break;
		case 2300:
			//showMessage("You’ve spoken too quickly.");
			ps.playSound(R.raw.vv2300);
			break;
		case 2400:
			//showMessage("You’ve spoken too long.");
			ps.playSound(R.raw.vv2400);
			break;
		case 2600:
			//showMessage("Sorry, the PIN you entered is not valid.");
			ps.playSound(R.raw.vv2600);
		default:
			//showMessage("A system error has occurred.");
			ps.playSound(R.raw.vv2003);
			break;
		} //end switch
	}

}
