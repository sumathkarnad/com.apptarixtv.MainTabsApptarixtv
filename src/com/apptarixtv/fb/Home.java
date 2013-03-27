package com.apptarixtv.fb;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Home extends Activity {
	Button btn_FACEBOOK;
	public static final String APP_ID ="215615625228341";//Appatrixtv123
//	public static final String APP_ID ="386296548094348";// ;// ichangemycity
	private static final String[] PERMISSIONS = new String[] {
			"publish_stream","publish_actions", "read_stream", "offline_access" ,"email","user_photos"};
	public static final String TAG = null;
	Facebook facebook = new Facebook(APP_ID);
	private String token;
	protected SharedPreferences mPrefs;
	private SharedPreferences myPrefs;
	private String uname,fname,lname,email;
	private String uid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.home);
		 mPrefs = getPreferences(MODE_PRIVATE);
		 myPrefs= getPreferences(MODE_PRIVATE);
	//	btn_FACEBOOK=(Button) findViewById(R.id.btn_fb);
		fbButtonClick();
	}

	private void fbButtonClick() {
		btn_FACEBOOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				/*facebook.authorize(Home.this, PERMISSIONS,
								new TestLoginListener());*/
						
				String access_token = mPrefs.getString("access_token", null);
		        long expires = mPrefs.getLong("access_expires", 0);
		        if(access_token != null) {
		        	Log.e("TAG HOME", "ACCESS TOKEN IN HOME PAGEEEEEEEEEEEEEEE "+access_token);
		            facebook.setAccessToken(access_token);
		        }
		        if(expires != 0) {
		            facebook.setAccessExpires(expires);
		            
		            new PostFBBackGround().execute(0);
		        }
		        
		        
		        /*
		         * Only call authorize if the access_token has expired.
		         */
		        if(!facebook.isSessionValid()) {

		            facebook.authorize(Home.this, PERMISSIONS, new DialogListener() {
		                @Override
		                public void onComplete(Bundle values) {
		                    SharedPreferences.Editor editor = myPrefs.edit();
		                    editor.putString("access_token", facebook.getAccessToken());
		                    editor.putLong("access_expires", facebook.getAccessExpires());
		                    editor.commit();
		                    Log.e("", " ddd exp :"+facebook.getAccessExpires());
		                   
		                    new PostFBBackGround().execute(0);
		                    
		                }
		    
		                @Override
		                public void onFacebookError(FacebookError error) {
		                	Toast.makeText(getApplicationContext(), "Facebook Error.. "+error.getMessage(), Toast.LENGTH_LONG).show();
		                	Log.e("", "ERRORRRRRRRRRRRRRRR "+error.getMessage());
		                }
		    
		                public void onError(DialogError e) {
		                	Toast.makeText(getApplicationContext(), "Facebook Login Error.. "+e.getMessage(), Toast.LENGTH_LONG).show();
		                }
		    
		                @Override
		                public void onCancel() {}

						
		            });
		        }
		        
		        
				
			}
		});
		// TODO Auto-generated method stub
		
	}
	

	private class PostFBBackGround extends AsyncTask<Integer, Void, Integer>
		{
			private ProgressDialog dialog1;
		
			@Override
			protected void onPreExecute()
			{   
				dialog1=new ProgressDialog(Home.this);
				dialog1.setCancelable(false);
		        dialog1.setMessage("Please wait...");
		        dialog1.show();
		       
			}
			

			@Override
			protected Integer doInBackground(Integer... params) {
				
			   try{
				   getuserinfo();
				 
				 Log.e(TAG, "In Background....");
			   }
			   catch(Exception e)
			   {
				   System.out.println("ERROR TWOOO "+e);
			   }
				return 0;
			}
			
			
			@Override
			protected void onPostExecute(Integer result)
			{
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				dialog1.dismiss();
				
				

				//  startActivity(getIntent()); finish();
				
			}
			
			
		}


	public void getuserinfo() {
	      
	     
	      //String id = null;
	      Bundle params1 = new Bundle();
	      params1.putString("fields","picture");
	      token=facebook.getAccessToken();
	      SharedPreferences.Editor prefsEditor = myPrefs.edit();
	      prefsEditor.putString("ftoken", token);
	    
	      prefsEditor.commit();
	      try {
	              Log.e("sss","Json "+ facebook.request("me"));
	      } catch (MalformedURLException e1) {
	              // TODO Auto-generated catch block
	              e1.printStackTrace();
	      } catch (IOException e1) {
	              // TODO Auto-generated catch block
	              e1.printStackTrace();
	      }
	      JSONObject jObject = null;
	      String id=null;
	      try
		{
	    	  
			   jObject = new JSONObject(facebook.request("me"));
		         id=jObject.getString("id");
		         Log.e("UID", "UID====="+id);
		         uid=id;
		       //  prefsEditor.putString("token", token);
		         prefsEditor.putString("uid",uid);
		         uname=jObject.getString("name");
		         
		         fname=jObject.getString("first_name");
		         Log.e("Fname", "First Name====="+fname);
		         lname=jObject.getString("last_name");
		         Log.e("Lname", "Last Name====="+lname);
		         prefsEditor.putString("uname", uname);
		         prefsEditor.putString("fname", fname);
		         prefsEditor.putString("lname", lname);
		         try {
		        	 email=jObject.getString("email");
		        	 Log.e("", "My Facebook Email=== "+email);
		        	 prefsEditor.putString("email", email);
		        	 prefsEditor.commit();
		        	
				} catch (Exception e) {
					Log.e("", "Not getting email id for this account.");
				}
		        
		        
		        
		       //  String gender=jObject.getString("gender");
		} catch (Exception e)
		{
			Log.e(TAG, "In Exception...."+e.getMessage());
			prefsEditor.commit();
		}
		  
		/*  try
		{
			  JSONObject Location;
		        Location=new JSONObject(jObject.getString("location"));
		        String location_name=Location.getString("name");
		        Log.e("################################", "LOCATION NAMEEEEEEEEEEEEE "+location_name);
		} catch (Exception e)
		{
			// TODO: handle exception
		}*/
	      
	     
		
		
		
//		
//		  try {
//	          img_value = new URL("http://graph.facebook.com/"+uid+"/picture?type=large");
//	       
//	 } catch (MalformedURLException e) {}
//	 try {
//	 	 Bitmap mIcon1 =BitmapFactory.decodeStream(img_value.openConnection().getInputStream());
//	 	
//
//	        Profile_pic=(ImageView) findViewById(R.id.profile_pic);
//
//	      // SharedPreferences.Editor prefsEditor = myPrefs.edit();
//	        Profile_pic.setImageBitmap(mIcon1);
//	        
//	        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
//	        mIcon1.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object   
//	        byte[] b = baos.toByteArray();
//	        prefsEditor.putString("propic", Base64.encodeToString(b, Base64.DEFAULT ));
//	        prefsEditor.commit();
//	        Profile_pic.setVisibility(View.VISIBLE);
//	        Log.e(TAG, "after profile pic");
//	        
//	      
//	        
//	      
//	        
//	 } catch (IOException e) {} 
//		
		
		

}

}
