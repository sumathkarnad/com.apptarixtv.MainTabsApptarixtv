package com.apptarixtv;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class InviteFrnd extends Activity{
	
	Button btninButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invite_frnd);
		 btninButton=(Button) findViewById(R.id.btn_postinvitation);
		
		btninButton.setOnClickListener(new OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				
				
				new InviteBackGround().execute("");
			}
		});
	}
	
	class InviteBackGround extends AsyncTask<String, Void, String>{
		final ProgressDialog dialog = new ProgressDialog(InviteFrnd.this);
		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
			dialog.setMessage("Sending Invitation...");
			dialog.show();
			try {
				Thread.sleep(8000);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			
			
		}

		@Override
		protected String doInBackground(String... params) {
			
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dialog.cancel();
			

			Intent intent=new Intent(getApplicationContext(),WorldWatchActivity.class);
			startActivity(intent);
		}
		
	}

}
