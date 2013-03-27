package com.apptarixtv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.widget.TextView;
import android.widget.Toast;

import com.apptarixtv.R;
import com.apptarixtv.fb.Home;
import com.apptarixtv.twitter.TwitterApp;
import com.apptarixtv.twitter.TwitterLogin;
import com.apptarixtv.twitter.TwitterApp.TwDialogListener;
import com.facebook.*;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;



public class HomeLogin extends Activity {
	private static final String twitter_consumer_key = "ycwLejracx4aChmINat1Q";
	private static final String twitter_secret_key = "T7sBuUJzAM5SRXtajiqzA495hAmNLhzJGCHWZs78";
	private static final String FB_APP_ID = "215615625228341";// Appatrixtv123
	private static final String[] FB_PERMISSIONS = new String[] {
			"publish_stream", "publish_actions", "read_stream",
			"offline_access", "email", "user_photos", "user_education_history",
			"user_interests", "user_relationship_details",
			"user_relationships", "user_work_history", "user_birthday" };

	private TwitterApp mTwitter;
	protected SharedPreferences mPrefs;
	private SharedPreferences myPrefs;
	public static final String TAG = "FB";
	Facebook facebook;
	MultipartEntity entity = new MultipartEntity(
			HttpMultipartMode.BROWSER_COMPATIBLE);
	private String fbuid, fbtoken, fbuname, fb_fname, fb_lname, fb_email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_login);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		facebook = new Facebook(FB_APP_ID);
		mPrefs = getPreferences(MODE_PRIVATE);
		myPrefs = getPreferences(MODE_PRIVATE);
		twitterInitalise();
		attachListnrToButtons();

	}

	private void twitterInitalise() {
		// TODO Auto-generated method stub
		mTwitter = new TwitterApp(this, twitter_consumer_key,
				twitter_secret_key);

		mTwitter.setListener(mTwLoginDialogListener);

		if (mTwitter.hasAccessToken()) {
			// mTwitterBtn.setChecked(true);

			String username = mTwitter.getUsername();
			username = (username.equals("")) ? "Unknown" : username;

			// mTwitterBtn.setText("  Twitter (" + username + ")");
			// mTwitterBtn.setTextColor(Color.WHITE);
			Toast.makeText(getApplicationContext(),
					"Twitter user allready loged in " + username, 3000).show();
		}
	}

	private void attachListnrToButtons() {
		// TODO Auto-generated method stub
		findViewById(R.id.facebook_login).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptFBLogin();
					}
				});

		findViewById(R.id.twitter_login).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptTwitterLogin();
					}
				});

	}

	private final TwDialogListener mTwLoginDialogListener = new TwDialogListener() {
		@Override
		public void onComplete(String value) {
			String username = mTwitter.getUsername();
			username = (username.equals("")) ? "No Name" : username;
			/*
			 * mTwitterBtn.setText("  Twitter  (" + username + ")");
			 * mTwitterBtn.setChecked(true);
			 * mTwitterBtn.setTextColor(Color.WHITE);
			 */

			Toast.makeText(HomeLogin.this,
					"Connected to Twitter as " + username, Toast.LENGTH_LONG)
					.show();
		}

		@Override
		public void onError(String value) {
			// mTwitterBtn.setChecked(false);

			Toast.makeText(HomeLogin.this, "Twitter connection failed",
					Toast.LENGTH_LONG).show();
		}
	};

	protected void attemptTwitterLogin() {
		// TODO Auto-generated method stub
		/*
		 * Intent twitterlogin = new Intent(getApplicationContext(),
		 * TwitterLogin.class); finish(); startActivity(twitterlogin);
		 */

		if (mTwitter.hasAccessToken()) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);

			builder.setMessage(
					mTwitter.getUsername()
							+ ", Do you want logout from twitter?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									mTwitter.resetAccessToken();

									/*
									 * mTwitterBtn.setChecked(false);
									 * mTwitterBtn
									 * .setText("  Twitter (Not connected)");
									 * mTwitterBtn.setTextColor(Color.GRAY);
									 */
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();

									// mTwitterBtn.setChecked(true);
								}
							});
			final AlertDialog alert = builder.create();

			alert.show();
		} else {
			// mTwitterBtn.setChecked(false);

			mTwitter.authorize();
		}

	}

	protected void attemptFBLogin() {
		// TODO Auto-generated method stub
		// start Facebook Login

		/*
		 * facebook.authorize(Home.this, PERMISSIONS, new TestLoginListener());
		 */

		String access_token = mPrefs.getString("access_token", null);
		long expires = mPrefs.getLong("access_expires", 0);
		if (access_token != null) {
			Log.e("TAG HOME", "ACCESS TOKEN IN HOME PAGEEEEEEEEEEEEEEE "
					+ access_token);
			facebook.setAccessToken(access_token);
		}
		if (expires != 0) {
			facebook.setAccessExpires(expires);

			new PostFBBackGround().execute(0);
		}

		/*
		 * Only call authorize if the access_token has expired.
		 */
		if (!facebook.isSessionValid()) {

			facebook.authorize(HomeLogin.this, FB_PERMISSIONS,
					new DialogListener() {
						@Override
						public void onComplete(Bundle values) {
							SharedPreferences.Editor editor = myPrefs.edit();
							editor.putString("access_token",
									facebook.getAccessToken());
							editor.putLong("access_expires",
									facebook.getAccessExpires());
							editor.commit();
							Log.e("",
									" ddd exp :" + facebook.getAccessExpires());

							new PostFBBackGround().execute(0);

						}

						@Override
						public void onFacebookError(FacebookError error) {
							Toast.makeText(getApplicationContext(),
									"Facebook Error.. " + error.getMessage(),
									Toast.LENGTH_LONG).show();
							Log.e("",
									"ERRORRRRRRRRRRRRRRR " + error.getMessage());
						}

						public void onError(DialogError e) {
							Toast.makeText(getApplicationContext(),
									"Facebook Login Error.. " + e.getMessage(),
									Toast.LENGTH_LONG).show();
						}

						@Override
						public void onCancel() {
						}

					});
		}
	}

	private class PostFBBackGround extends AsyncTask<Integer, Void, Integer> {
		private ProgressDialog dialog1;

		@Override
		protected void onPreExecute() {
			dialog1 = new ProgressDialog(HomeLogin.this);
			dialog1.setCancelable(false);
			dialog1.setMessage("Please wait...");
			dialog1.show();

		}

		@Override
		protected Integer doInBackground(Integer... params) {

			try {
				getuserinfo();

				Log.e(TAG, "In Background....");
			} catch (Exception e) {
				System.out.println("ERROR TWOOO " + e);
			}
			return 0;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dialog1.dismiss();
			Toast.makeText(
					getApplicationContext(),
					"User name:" + fbuname + " name :" + fb_fname + " "
							+ fb_lname, 3000).show();
			if (fbuid != null) {
				//  checkFacebookUserRegister();
				Log.e("LOG", "Going for register");
				HashMap<String, String> data = new HashMap<String, String>();
				data.put("user_name", fbuname);
				data.put("name", fb_fname+" "+fb_lname);
				AsyncHttpPost asyncHttpPost = new AsyncHttpPost(data);
				asyncHttpPost.execute("http://ec2-50-19-169-217.compute-1.amazonaws.com/apptrix/webservice/register.php");
			}
			// startActivity(getIntent()); finish();

		}

	}

	public void getuserinfo() {

		// String id = null;
		Bundle params1 = new Bundle();
		params1.putString(
				"fields",
				"id,username,first_name,middle_name,last_name,gender,email,birthday,picture,education,work,interested_in,relationship_status,devices,languages,updated_time,friends");

		fbtoken = facebook.getAccessToken();
		SharedPreferences.Editor prefsEditor = myPrefs.edit();
		prefsEditor.putString("ftoken", fbtoken);

		prefsEditor.commit();
		try {
			Log.e("sss", "Json " + facebook.request("me", params1));
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JSONObject jObject = null;
		String id = null;
		try {

			jObject = new JSONObject(facebook.request("me"));
			id = jObject.getString("id");
			Log.e("UID", "UID=====" + id);
			fbuid = id;
			// prefsEditor.putString("token", token);
			prefsEditor.putString("uid", fbuid);
			fbuname = jObject.getString("username");

			fb_fname = jObject.getString("first_name");
			Log.e("Fname", "First Name=====" + fb_fname);
			fb_lname = jObject.getString("last_name");
			Log.e("Lname", "Last Name=====" + fb_lname);
			prefsEditor.putString("uname", fbuname);
			prefsEditor.putString("fname", fb_fname);
			prefsEditor.putString("lname", fb_lname);
			try {
				fb_email = jObject.getString("email");
				Log.e("", "My Facebook Email=== " + fb_email);

				prefsEditor.putString("email", fb_email);
				prefsEditor.commit();

			} catch (Exception e) {
				Log.e("", "Not getting email id for this account.");
			}

			// String gender=jObject.getString("gender");
		} catch (Exception e) {
			Log.e(TAG, "In Exception...." + e.getMessage());
			prefsEditor.commit();
		}

	}

	public void checkFacebookUserRegister() {
		// TODO Auto-generated method stub
		new CheckUserRegister().execute(0);
	}

	private class CheckUserRegister extends AsyncTask<Integer, Void, Integer> {
		private ProgressDialog dialog1;
		StringBuilder response_11;

		@Override
		protected void onPreExecute() {
			dialog1 = new ProgressDialog(HomeLogin.this);
			dialog1.setCancelable(false);
			dialog1.setMessage("Please wait...");
			dialog1.show();

		}

		@Override
		protected Integer doInBackground(Integer... params) {

			String uploadURl;
			// EDT_CATEGORY=(EditText) findViewById(R.id.category);

			uploadURl = "http://ec2-50-19-169-217.compute-1.amazonaws.com/apptrix/webservice/validateFb.php?user="
					+ fbuid;

			try {

				HttpPost postRequest = new HttpPost(uploadURl);
				HttpParams params1 = postRequest.getParams();
				HttpConnectionParams.setConnectionTimeout(params1, 30000);
				HttpConnectionParams.setSoTimeout(params1, 30000);
				// .BROWSER_COMPATIBLE);

				HttpClient httpClient = new DefaultHttpClient(params1);

		//		StringBody details = new StringBody(compaint_title);//

				try {
			//		entity.addPart("detail", title);

				} catch (Exception e) {
					// TODO: handle exception
				}

				// entity.addPart("geo", geo);
				try {
					postRequest.setEntity(entity);
					HttpResponse response;

					response = httpClient.execute(postRequest);

					BufferedReader reader = new BufferedReader(
							new InputStreamReader(response.getEntity()
									.getContent(), "UTF-8"));
					String sResponse;
					response_11 = new StringBuilder();
					// System.out.println("hiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
					while ((sResponse = reader.readLine()) != null) {
						response_11 = response_11.append(sResponse);
					}

					System.out.println("Response: " + response_11);

				} catch (Exception e) {

					Log.e("error........", response_11.toString());
				}

			} catch (Exception e) {

				Log.e("Error:", "Error " + e.toString());
			}

			return 0;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dialog1.dismiss();
			/*
			 * Toast.makeText( getApplicationContext(), "User name:" + fbuname +
			 * " name :" + fb_fname + " " + fb_lname, 3000).show();
			 */

		}

	}
	
	public class AsyncHttpPost extends AsyncTask<String, String, String> {
	    private HashMap<String, String> mData = null;// post data
	    private ProgressDialog dialog1;
	    /**
	     * constructor
	     */
	    public AsyncHttpPost(HashMap<String, String> data) {
	        mData = data;
	    }

	    @Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog1 = new ProgressDialog(HomeLogin.this);
			dialog1.setCancelable(false);
			dialog1.setMessage("Please wait...");
			dialog1.show();
		}

		/**
	     * background
	     */
	    @Override
	    protected String doInBackground(String... params) {
	        byte[] result = null;
	        String str = "";
	        HttpClient client = new DefaultHttpClient();
	        HttpPost post = new HttpPost(params[0]);// in this case, params[0] is URL
	        try {
	            // set up post data
	            ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
	            Iterator<String> it = mData.keySet().iterator();
	            while (it.hasNext()) {
	                String key = it.next();
	                nameValuePair.add(new BasicNameValuePair(key, mData.get(key)));
	            }

	            post.setEntity(new UrlEncodedFormEntity(nameValuePair, "UTF-8"));
	            HttpResponse response = client.execute(post);
	            StatusLine statusLine = response.getStatusLine();
	            if(statusLine.getStatusCode() == HttpURLConnection.HTTP_OK){
	                result = EntityUtils.toByteArray(response.getEntity());
	                str = new String(result, "UTF-8");
	            }
	        }
	        catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        }
	        catch (Exception e) {
	        }
	        return str;
	    }

	    /**
	     * on getting result
	     */
	    @Override
	    protected void onPostExecute(String result) {
	        // something...
	    	dialog1.dismiss();
	    	Log.e("RESponce"," @@@@ "+result);
	    }
	}
}
