package com.apptarixtv;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.apptarixtv.R;
import com.apptarixtv.constant.ApptarixConstant;
import com.apptarixtv.constant.DoPost;
import com.apptarixtv.constant.HttpContn;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FbDialog;
import com.facebook.android.Util;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


public class FbLoginScreen extends Activity {
	final Context context = this;
	Button btn_FACEBOOK;
	
	public static final String TAG = null;
	Facebook facebook = new Facebook(ApptarixConstant.APP_ID);
	private String token;
//	protected SharedPreferences mPrefs;
	private SharedPreferences fbPrefs;
	private String uname,fname,lname,email;
	private String uid;
	private String access_token;
	private boolean isfbexpire;
	int TIMEOUT_MILLISEC = 10000; // = 10 seconds
	private String gender;
	private String dob;
	private String relationship;
	private URL img_value;
	private boolean isfirsttime;
	protected String jsonresp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fblogin);
		
		btn_FACEBOOK=(Button) findViewById(R.id.btnFBLogin);
		 
		 fbPrefs = this.getSharedPreferences("fbPrefs", MODE_PRIVATE);
	//	btn_FACEBOOK=(Button) findViewById(R.id.btn_fb);
		 
		  isfirsttime=fbPrefs.getBoolean("isfirsrtime", true);
		  access_token = fbPrefs.getString("access_token", null);
		  Log.e(TAG, "Access Token="+access_token);
		  if(isfirsttime){
			  
			  
		  }
		  if(access_token!=null)
		new CheckFBTokenAlive().execute(""); 
		
		fbButtonClick();
	}
	
	

	class CheckFBTokenAlive extends AsyncTask<String,Void,String>{
		ProgressDialog fbdialog;
		String fbresp;
		@Override
		protected void onPreExecute() {
			
			fbdialog=new ProgressDialog(FbLoginScreen.this);
			fbdialog.setMessage("Authenticating with facebook..");
			fbdialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			
			Log.e(TAG, "in background..");
			fbresp=new HttpContn().coonectHttp("https://graph.facebook.com/oauth/access_token_info?client_id="+ApptarixConstant.APP_ID+"&access_token="+access_token);
			Log.e(TAG, "Response for access token="+fbresp);
			if(fbresp.equalsIgnoreCase("ERROR")){
				isfbexpire=true;
			}
			else{
				parseFbResponse(fbresp);
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			
			fbdialog.cancel();
			
			if(isfbexpire){
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		 
					// set title
					alertDialogBuilder.setTitle("Facebook Login Expired!");
		 
					// set dialog message
					alertDialogBuilder
						.setMessage("Your Facebook Login has been expired Please login to continue.")
						.setCancelable(false)
						.setPositiveButton("OK",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								// if this button is clicked, close
								// current activity
//								FbLoginScreen.this.finish();
								fbButtonClick();
								
							}
						  });
					alertDialogBuilder.show();
			}else{
				

				  //temporarily commenting below line
				new CheckFbUserOnserver().execute("");
				
//				 if(isfirsttime){
//					 SharedPreferences.Editor prefsEditor = fbPrefs.edit();
//					 prefsEditor.putBoolean("isfirsttime", false);
//					 prefsEditor.commit();
//					 Intent startIntent=new Intent(getApplicationContext(), HelpActivity.class);
//				        startActivity(startIntent);
//						finish();
//				 }else{
//					 Intent startIntent=new Intent(getApplicationContext(), CheckFrnd.class);
//				        startActivity(startIntent);
//						finish();
//				 }
				
			}
			
		}
		
	}
	
	
	//checking valid fb user or not
	
	class CheckFbUserOnserver extends AsyncTask<String, Void, String>{
		 public  ProgressDialog chkdialog;
		String resp;
		@Override
		protected void onPreExecute() {
			chkdialog=new ProgressDialog(FbLoginScreen.this);
			
			chkdialog.setMessage("Fetching Facebook Detail");
			chkdialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			
			
			getuserinfo();
			//resp=validateMyFBUser();
			if(uid!=null)
			resp=new HttpContn().coonectHttp(ApptarixConstant.FB_USER_CHECK+uid);
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			JSONObject jresult;
			String isvalidstr="";
			int status = 0;
			if(resp!=null){
			
			try {
				
				jresult = new JSONObject(resp);
				try {
					 isvalidstr=jresult.getString("user");
				} catch (Exception e) {
					// TODO: handle exception
				}
				try {
					status=jresult.getInt("status");
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				Log.e(TAG, "valid or not : "+isvalidstr);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			try {
				chkdialog.dismiss();
			} catch (Exception e) {
				// TODO: handle exception
			}finally{
				if(chkdialog!=null)
					chkdialog.cancel();
				chkdialog=null;
			}
			
			
			if(isvalidstr.equalsIgnoreCase("Valid")||status==1){
				
				 if(isfirsttime){
					 SharedPreferences.Editor prefsEditor = fbPrefs.edit();
					 prefsEditor.putBoolean("isfirsttime", false);
					 prefsEditor.commit();
					 Intent startIntent=new Intent(getApplicationContext(), HelpActivity.class);
				        startActivity(startIntent);
						finish();
				 }else{
					 Intent startIntent=new Intent(getApplicationContext(), CheckFrnd.class);
				        startActivity(startIntent);
						finish();
				 }

			}else{
				 SharedPreferences.Editor prefsEditor = fbPrefs.edit();
				 prefsEditor.putBoolean("isfirsttime", false);
				 prefsEditor.commit();
//				 Intent startIntent=new Intent(getApplicationContext(), HelpActivity.class);
//			        startActivity(startIntent);
//					finish();
				new DoRegistration().execute("");
			}
		}
		
	}
	
	
	
	public String validateMyFBUser(){
		String url = "http://ec2-50-19-169-217.compute-1.amazonaws.com/apptrix/webservice/validateFb.php";
		JSONObject json = new JSONObject();
		//json.put("UserName", "test2");
		//json.put("FullName", "1234567");
		
		try {
			json.put("user_id", uid);
//		json.put("admin_user_id","unique_id");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return new DoPost().doPost(json.toString(), url);
	}


	
	//Registration for user
	
	class DoRegistration extends AsyncTask<String, Void, String>{
		ProgressDialog dialog;
		String resp=null;
		int responsecode;
		String responsemsg;
		int status;
		String appuserid;
		String fbUserid;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog=new ProgressDialog(FbLoginScreen.this);
			dialog.setMessage("Regestering For user..");
			dialog.show();
			
		}
		@Override
		protected String doInBackground(String... params) {
			
			//resp=registerUser();
			registerUser();
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dialog.cancel();
			
			try {
				JSONObject obj=new JSONObject(resp);
				 responsecode=obj.getInt("responseCode");
				 responsemsg=obj.getString("responseMessage");
				 if(responsecode==200)
				 status=obj.getInt("status");
				 appuserid=obj.getString("id");
				 fbUserid=obj.getString("fb_user_id");
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(responsecode==200&&status==1){
				
				
				Toast.makeText(getApplicationContext(), "Rgistration done successfully",Toast.LENGTH_LONG).show();
				Intent startIntent=new Intent(getApplicationContext(), WorldWatchActivity.class);
		        startActivity(startIntent);
				finish();
			}else{
				Toast.makeText(getApplicationContext(), "Rgistration Failed Please Try again",Toast.LENGTH_LONG).show();
			}
			

		}
		
	}
	private void fbButtonClick() {
		
		//Facebook Login 
		btn_FACEBOOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				/*facebook.authorize(Home.this, PERMISSIONS,
								new TestLoginListener());*/
						
				String access_token = fbPrefs.getString("access_token", null);
		        long expires = fbPrefs.getLong("access_expires", 0);
		        if(access_token != null) {
		        	Log.e("TAG HOME", "ACCESS TOKEN IN HOME PAGEEEEEEEEEEEEEEE "+access_token);
		            facebook.setAccessToken(access_token);
		        }
		        if(expires != 0) {
		            facebook.setAccessExpires(expires);
		            
		            //temporarily commenting below line
//		            new CheckFbUserOnserver().execute("");
		            if(isfirsttime){
						 SharedPreferences.Editor prefsEditor = fbPrefs.edit();
						 prefsEditor.putBoolean("isfirsttime", false);
						 prefsEditor.commit();
						 Intent startIntent=new Intent(getApplicationContext(), HelpActivity.class);
					        startActivity(startIntent);
							finish();
					 }else{
						 Intent startIntent=new Intent(getApplicationContext(), CheckFrnd.class);
					        startActivity(startIntent);
							finish();
					 }
//		            new DoRegistration().execute("");
		        }
		        
		        
		        /*
		         * Only call authorize if the access_token has expired.
		         */
		        if(!facebook.isSessionValid()) {

		            facebook.authorize(FbLoginScreen.this, ApptarixConstant.PERMISSIONS, new DialogListener() {
		                @Override
		                public void onComplete(Bundle values) {
		                    SharedPreferences.Editor editor = fbPrefs.edit();
		                    editor.putString("access_token", facebook.getAccessToken());
		                    editor.putLong("access_expires", facebook.getAccessExpires());
		                    editor.commit();
		                    Log.e("", " ddd exp :"+facebook.getAccessExpires());
		                   
		                    //temporarily commenting below line
		                    new CheckFbUserOnserver().execute("");
//		                    Intent startIntent=new Intent(getApplicationContext(), CheckFrnd.class);
//		    		        startActivity(startIntent);
//		    				finish();
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
	

	public String registerUser() {
		RequestParams params = new RequestParams();
		 params.put("apikey","ac2fdfd5fec83138415b9f98c82f0aac");    
		 params.put("fb_user_id", uid);                              
		 params.put("name", fname);                                  
		 params.put("last_name",lname );                             
		 params.put("first_name",fname);                             
		 params.put("gender", gender);                               
		 params.put("language", "Hindi");                            
		 params.put("username", uname);                              
		 params.put("profile_pic_url", "picture location");          
		 params.put("education", "");                                
		 params.put("email", email);                                 
		 params.put("relationship_status", relationship);            
		 params.put("work", "");                                     
		 params.put("dob", dob);                                     
		 AsyncHttpClient client = new AsyncHttpClient();
//		 JsonHttpResponseHandler responsehandler=new JsonHttpResponseHandler();
		 client.post(ApptarixConstant.REGISTER_USER, params, new AsyncHttpResponseHandler() {
		     @Override
		     public void onStart() {
		         // Initiated the request
		     }

		     @Override
		     public void onSuccess(String response) {
		         // Successfully got a response
		    	 jsonresp=response;
		    	 Log.d(TAG, "Response :"+jsonresp);
		     }
		 
		     @Override
		     public void onFailure(Throwable e, String response) {
		         // Response failed :(
		     }

		     @Override
		     public void onFinish() {
		         // Completed the request (either success or failure)
		     }
		 });
//		Log.d(TAG, "response :"+responsehandler);
		
		return jsonresp;

		
	}


	public void parseFbResponse(String fbresp) {
		try {
			JSONObject root=new JSONObject(fbresp);
			long fbexpiry=root.getLong("expires_in");
			Log.e(TAG, "FBExpiry= "+fbexpiry);
			
			if(fbexpiry>0){
				Log.e(TAG, "System time= "+System.currentTimeMillis());
				//if(System.currentTimeMillis()>fbexpiry)
				isfbexpire=false;
			}else{
				isfbexpire=true;
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private class PostFBBackGround extends AsyncTask<Integer, Void, Integer>
		{
			private ProgressDialog dialog1;
		
			@Override
			protected void onPreExecute()
			{   
				dialog1=new ProgressDialog(FbLoginScreen.this);
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
			
				 
				Intent startIntent=new Intent(getApplicationContext(), WorldWatchActivity.class);
		        startActivity(startIntent);
				finish();

				//  startActivity(getIntent()); finish();
				
			}
			
			
		}


	public void getuserinfo() {
	      
	     
	      //String id = null;
	      Bundle params1 = new Bundle();
	      params1.putString("fields","picture");
	     
	      SharedPreferences.Editor prefsEditor = fbPrefs.edit();
	     
	      facebook.setAccessToken(access_token);
	      
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
		         
		         gender=jObject.getString("gender");
		         try {
		        	 dob=jObject.getString("birthday");
				} catch (Exception e) {
					// TODO: handle exception
				}
		         try {
		        	 relationship=jObject.getString("relationship_status");
				} catch (Exception e) {
					// TODO: handle exception
				}
		        
		        
		         
		         prefsEditor.putString("uname", uname);
		         prefsEditor.putString("fname", fname);
		         prefsEditor.putString("lname", lname);
		         try {
		        	 email=jObject.getString("email");
		        	 Log.e("", "My Facebook Email=== "+email);
		        	 prefsEditor.putString("email", email);
		        	
		        	
				} catch (Exception e) {
					Log.e("", "Not getting email id for this account.");
				}
		        
				
				 prefsEditor.commit();

				  try {
			          img_value = new URL("http://graph.facebook.com/"+uid+"/picture?type=small");
			       
			 } catch (MalformedURLException e) {
				 prefsEditor.commit();
			 }
			 try {
				 SharedPreferences.Editor prefsEditor1 = fbPrefs.edit();
			 	 Bitmap mIcon1 =BitmapFactory.decodeStream(img_value.openConnection().getInputStream());
			        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			        mIcon1.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object   
			        byte[] b = baos.toByteArray();
			        prefsEditor1.putString("propic", Base64.encodeToString(b, Base64.DEFAULT ));
			        prefsEditor1.commit();
			        Log.e(TAG, "after profile pic");
			        prefsEditor1.commit();
			        
			 } catch (IOException e) {
				
			 } 
				
		        
		        
		       //  String gender=jObject.getString("gender");
		} catch (Exception e)
		{
			Log.e(TAG, "In Exception...."+e.getMessage());
			prefsEditor.commit();
		}
//		prefsEditor.commit();
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
