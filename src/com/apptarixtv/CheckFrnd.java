package com.apptarixtv;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.SharedPreferences;

import com.apptarixtv.R;
import com.apptarixtv.constant.ApptarixConstant;
import com.apptarixtv.util.FacebookFrnd;
import com.apptarixtv.util.ImageLoader;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class CheckFrnd extends Activity {

	protected static final String TAG = "CheckFrnd";
	ListView frnd_list_view;
	ArrayList<HashMap<String, String>> fendlist;
	TextView txtfbresult;
	int totalfrnd;
	String fbtxt = "We have found " + totalfrnd
			+ " of you facebook friends on APPTARIX TV."
			+ "  Connect with them and invite others.";
	Button btnNext, btnSkip;
	int[] imgprof = { R.drawable.ashish, R.drawable.amit, R.drawable.pravin,
			R.drawable.sajid, R.drawable.ware, R.drawable.shree,
			R.drawable.mohan, R.drawable.himanshu, R.drawable.rohit,
			R.drawable.kunal };
	Facebook facebook;
	private SharedPreferences fbPrefs;
	private String access_token;
	ArrayList<String> fb_friendArray;
	String fb_id;
	protected String jsonresp;
	private String frndJson;
	ArrayList<FacebookFrnd> fbfrindlist;
	LazyAdapter frndLazyadapter;
	private String apptrixuserid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.check_frnd);
		txtfbresult = (TextView) findViewById(R.id.txtfbreport);
		txtfbresult.setText(fbtxt);
		btnNext = (Button) findViewById(R.id.btnnext);
		btnSkip = (Button) findViewById(R.id.btnskip);
		fendlist = new ArrayList<HashMap<String, String>>();
		// dumyDataTemp();
		// frnd_list_view=(ListView) findViewById(R.id.lst_frnd);
		// frnd_list_view.setAdapter(frndAdapter);
		fbPrefs = this.getSharedPreferences("fbPrefs", MODE_PRIVATE);
		facebook = new Facebook(ApptarixConstant.APP_ID);
		fb_id = fbPrefs.getString("uid", null);
		fbfrindlist = new ArrayList<FacebookFrnd>();
		new GetFriendList().execute("");

		// new GetFrndList().execute();

		btnNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				try {
					inviteFriends(CheckFrnd.this, fbfrindlist);
					
				} catch (Exception e) {
					Intent intent = new Intent(getApplicationContext(),
							WorldWatchActivity.class);
					startActivity(intent);
				}

			}
		});

		btnSkip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						WorldWatchActivity.class);
				startActivity(intent);
			}
		});

	}

	class GetFriendList extends AsyncTask<String, Void, String> {

		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(CheckFrnd.this);
			dialog.setMessage("Getting all Facebook Friends List..");
			dialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			getFriendList();
			checkFriendList();
			if (jsonresp != null)
				parseResponse();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if (dialog != null)
				dialog.cancel();
			dialog = null;
			super.onPostExecute(result);
//			if(apptrixuserid!=null){
//				SharedPreferences.Editor prefsEditor = fbPrefs.edit();
//				prefsEditor.putString("apptarix_uid", apptrixuserid);
//				prefsEditor.commit();
//			}
			
			frndLazyadapter = new LazyAdapter(CheckFrnd.this);
			frnd_list_view = (ListView) findViewById(R.id.lst_frnd);
			frnd_list_view.setAdapter(frndLazyadapter);
		}

	}

	class LazyAdapter extends BaseAdapter {

		private Activity activity;
		private LayoutInflater inflater = null;
		public ImageLoader imageLoader;

		public LazyAdapter(Activity a) {
			activity = a;

			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			imageLoader = new ImageLoader(activity.getApplicationContext());
		}

		public int getCount() {

			return fbfrindlist.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View vi = convertView;
			if (convertView == null)
				vi = inflater.inflate(R.layout.frnd_list_row, null);

			TextView frndNmae = (TextView) vi.findViewById(R.id.txtfrndname);
			frndNmae.setText(fbfrindlist.get(position).getName());
			// channel_program_name_textview.setText(programgrid.get(position)
			// .getProgramName());
			ImageView image = (ImageView) vi.findViewById(R.id.imgfrnd);
			// chaneltext.setText(programgrid.get(position).getChannelName());
			imageLoader.DisplayImage(
					"http://graph.facebook.com/"
							+ fbfrindlist.get(position).getId()
							+ "/picture?type=small", image);

			final ToggleButton btnfbreq = (ToggleButton) vi
					.findViewById(R.id.btninvite);
			if (fbfrindlist.get(position).getSstatus() == 0) {
				btnfbreq.setBackgroundResource(R.drawable.invite);
				btnfbreq.setChecked(btnfbreq.isChecked());
				Log.e("", "in 0");
			} else {
				btnfbreq.setBackgroundResource(R.drawable.connect);
				btnfbreq.setChecked(btnfbreq.isChecked());
				btnfbreq.setChecked(btnfbreq.isChecked());
				Log.e("", "in 1");
			}

			btnfbreq.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					if (isChecked) {
						if (fbfrindlist.get(position).getSstatus() == 0) {
							btnfbreq.setBackgroundResource(R.drawable.invite);
							btnfbreq.setChecked(btnfbreq.isChecked());
							fbfrindlist.get(position).setMystatus(4);
							
							Log.e(TAG, "FrndName :"+fbfrindlist.get(position).getName()+"=4");
						} else {
							btnfbreq.setBackgroundResource(R.drawable.connect);
							btnfbreq.setChecked(btnfbreq.isChecked());
							btnfbreq.setChecked(btnfbreq.isChecked());
							fbfrindlist.get(position).setMystatus(2);
							Log.e(TAG, "FrndName :"+fbfrindlist.get(position).getName()+"=2");						
							}
					} else {
						if (fbfrindlist.get(position).getSstatus() == 0) {
							btnfbreq.setBackgroundResource(R.drawable.dont);
							btnfbreq.setChecked(btnfbreq.isChecked());
							fbfrindlist.get(position).setMystatus(5);
							Log.e(TAG, "FrndName :"+fbfrindlist.get(position).getName()+"=5");						
							} else {
							btnfbreq.setBackgroundResource(R.drawable.disconnect);
							btnfbreq.setChecked(btnfbreq.isChecked());
							btnfbreq.setChecked(btnfbreq.isChecked());
							fbfrindlist.get(position).setMystatus(3);
							Log.e(TAG, "FrndName :"+fbfrindlist.get(position).getName()+"=3");						
							}
					}
				}
			});

			return vi;
		}

	}

	private void getFriendList() {
		JSONObject jsonObj = null;
		try {
			access_token = fbPrefs.getString("access_token", null);
			facebook.setAccessToken(access_token);
			try {
				jsonObj = Util.parseJson(facebook.request("me/friends"));
				frndJson = facebook.request("me/friends");
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FacebookError e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		JSONArray jArray = null;
		try {
			jArray = jsonObj.getJSONArray("data");
			frndJson = jArray.toString();
//			Log.d(TAG, "frndjson :" + jArray.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		fb_friendArray = new ArrayList<String>();
		for (int i = 0; i < jArray.length(); i++) {

			try {
				JSONObject json_data = jArray.getJSONObject(i);
				// fb_friendArray.add(""+jArray.getJSONObject(i));

				FacebookFrnd frnd = new FacebookFrnd();
				frnd.setId(json_data.getInt("id"));
				frnd.setName(json_data.getString("name"));
//				Log.d(TAG,
//						"" + json_data.getString("name") + "-"
//								+ json_data.getString("id"));
				frnd.setSstatus(0);
				fbfrindlist.add(frnd);
				// {"id":"1205871736","name":"Vikram Pawar"}

				// Log.v("THIS IS DATA", i+" : "+jArray.getJSONObject(i));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public void parseResponse() {
		try {
			JSONObject root = new JSONObject(jsonresp);
			String responsecode = root.getString("responseCode");
			 apptrixuserid = root.getString("id");
			
			
			
			Log.i(TAG, "id===="+apptrixuserid);
			if(responsecode.equals("200")){
			JSONArray frndjArray = new JSONArray(root.getString("friendsList"));
			if (frndjArray.length() > 0) {
				for (int i = 0; i < frndjArray.length(); i++) {
					fbfrindlist.get(i).setSstatus(Integer.parseInt(frndjArray.getJSONObject(i).getString(""+ fbfrindlist.get(i).getId())));
					Log.i(TAG, "status :"+frndjArray.getJSONObject(i).getString(""+ fbfrindlist.get(i).getId()));
					}
				}
			}
		} catch (JSONException e) {
			Log.e(TAG, "error : "+Log.getStackTraceString(e));
			e.printStackTrace();
		}
	}

	public void checkFriendList() {
		RequestParams params = new RequestParams();
		params.put("apikey", ApptarixConstant.API_KEY);
		params.put("fb_user_id", fb_id);
		params.put("fb_friends", frndJson);

		AsyncHttpClient client = new AsyncHttpClient();
		client.post(ApptarixConstant.FB_CONNECT, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onStart() {
						// Initiated the request
					}

					@Override
					public void onSuccess(String response) {
						// Successfully got a response
						jsonresp = response;
//						Log.d(TAG, "Response :" + jsonresp);
					}

					@Override
					public void onFailure(Throwable e, String response) {
						Log.d(TAG, "Response :Failed : " + response);
					}

					@Override
					public void onFinish() {
						// Completed the request (either success or failure)
						Log.d(TAG, "Finish");

					}
				});

	}

	public void inviteFriends(Activity activity,
			ArrayList<FacebookFrnd> friendsIds) {
		// Safe programming
		if (friendsIds == null || friendsIds.size() == 0)
			return;

		Bundle parameters = new Bundle();

		Vector<String>vfrndid=new Vector<String>();
		// Get the friend ids
//		String friendsIdsInFormat = "100000878484109, 100000427485536, 1400020277";
		String friendsIdsInFormat="";
		 for(int i=0; i<friendsIds.size(); i++){
			 if(friendsIds.get(i).getMystatus()==4){
//				 friendsIdsInFormat = friendsIdsInFormat + friendsIds.get(i).getId() +", ";
				 vfrndid.addElement(""+friendsIds.get(i).getId());
			 }
		 
		 }
//		 friendsIdsInFormat = friendsIdsInFormat +
//		 friendsIds.get(friendsIds.size()-1).getId();

		 for (int i = 0; i < vfrndid.size()-1; i++) {
			friendsIdsInFormat=friendsIdsInFormat+vfrndid.elementAt(i).toString()+", ";
		}
		 friendsIdsInFormat=friendsIdsInFormat+vfrndid.elementAt(vfrndid.size()).toString();
		parameters.putString("to", friendsIdsInFormat);
		parameters
				.putString("message",
						"I have an ApptarixTv app. It is to be fun on this app. Please check.");

		// Show dialog for invitation
		facebook.dialog(activity, "apprequests", parameters,
				new Facebook.DialogListener() {
					@Override
					public void onComplete(Bundle values) {
						// TODO Auto-generated method stub
						try {
							Toast.makeText(getApplicationContext(),
									"Invitation Send", Toast.LENGTH_LONG)
									.show();
							new ConnectWithApptarixUser().execute("");
						} catch (Exception e) {
							// TODO: handle exception
						}

						Intent intent = new Intent(getApplicationContext(),
								WorldWatchActivity.class);
						startActivity(intent);

					}

					@Override
					public void onFacebookError(FacebookError e) {
						try {
							Toast.makeText(getApplicationContext(),
									"Facebook Error :" + e.getMessage(),
									Toast.LENGTH_LONG).show();
						} catch (Exception e1) {
							// TODO: handle exception
						}
					}

					@Override
					public void onError(DialogError e) {
						try {
							Toast.makeText(getApplicationContext(),
									"Error :" + e.getMessage(),
									Toast.LENGTH_LONG).show();
						} catch (Exception e1) {
							// TODO: handle exception
						}

					}

					@Override
					public void onCancel() {
						try {
							Toast.makeText(getApplicationContext(),
									"Inviation Cancel..", Toast.LENGTH_LONG)
									.show();
						} catch (Exception e1) {
							// TODO: handle exception
						}

					}
				});

	}
	
	class ConnectWithApptarixUser extends AsyncTask<String, Void, String>{
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
				connectWithApptarixFrnd();
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		}
		
	}

	public void connectWithApptarixFrnd() {
		RequestParams params = new RequestParams();
		 params.put("apikey","ac2fdfd5fec83138415b9f98c82f0aac");    
		 params.put("fb_user_id",fb_id);
		 
		
	}
	

}
