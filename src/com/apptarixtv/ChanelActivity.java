package com.apptarixtv;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.apptarixtv.R;
import com.apptarixtv.constant.ApptarixConstant;
import com.apptarixtv.constant.HorizontalListView;
import com.apptarixtv.constant.HttpContn;
import com.apptarixtv.util.TVChannel;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ChanelActivity extends Activity {
	
	//Temporary randomly adding category 
	int Min=0,Max=4;
	public static String CATEGORY[]={"News","Sports","Music","Movie","Religional"};
//	ArrayList<TVChannel> alltvchannel;
	
	ArrayList<TVChannel> newsChannel;
	ArrayList<TVChannel> musicChannel;
	HorizontalListView newsChannellist;
	HorizontalListView musicChannellist;
	class GetAllChannel extends AsyncTask<Integer, Void, Integer>{
		String resp;
		ProgressDialog dialog=new ProgressDialog(ChanelActivity.this);
		
		@Override
		protected void onPreExecute() {
			dialog.show();
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			HttpContn conn=new HttpContn();
			Log.e("mohsin", "in background task..");
			resp=conn.coonectHttp(ApptarixConstant.ALL_CHANNEL_URL);
			parseResponse(resp);
		//	Log.e("mohsin", "Response= "+resp);
			return null;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			
			newsChannellist.setAdapter(mAdapter);
			
			dialog.cancel();
			super.onPostExecute(result);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.chanel_activity);
		newsChannellist=(HorizontalListView) findViewById(R.id.music_channel_list);
		new GetAllChannel().execute(0);

	}

	public void parseResponse(String resp) {
		try {
			newsChannel=new ArrayList<TVChannel>();
			musicChannel=new ArrayList<TVChannel>();
			JSONObject root=new JSONObject(resp);
			JSONObject chanobj=new JSONObject(root.getString("channellist"));
			JSONArray jchannel=new JSONArray(chanobj.getString("channel"));
			for (int i = 0; i < jchannel.length(); i++) {
				TVChannel channel=new TVChannel();
				channel.setChannelName(jchannel.getJSONObject(i).getString("name"));
				channel.setChannelCategory(CATEGORY[Min + (int)(Math.random() * ((Max - Min) + 1))]);
				newsChannel.add(channel);
//				Log.e("mohsin", "Channel "+i+" "+jchannel.getJSONObject(i).getString("name"));
//				Log.e("mohsin", "Category "+i+" "+CATEGORY[Min + (int)(Math.random() * ((Max - Min) + 1))]);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
BaseAdapter mAdapter=new BaseAdapter() {

	@Override
	public int getCount() {
		return newsChannel.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View v = convertView;
		if (v == null) {
			@SuppressWarnings("static-access")
			LayoutInflater vi = (LayoutInflater) getApplicationContext()
					.getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.channel_list_item, null);

		} else {
			v = convertView;
		}
		Log.e("GETVIEW", "Position "+position);
		//View retval = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem, null);
//		HashMap<String, String> map=array_list_programs.get(position);
		
		TVChannel chan=newsChannel.get(position);
		
		TextView channel_name_textview = (TextView) v.findViewById(R.id.channel_name);
		channel_name_textview.setText(chan.getChannelName());
//		TextView channel_program_name_textview = (TextView) v.findViewById(R.id.channel_program);
//		channel_program_name_textview.setText(map.get("program_name"));
//		TextView total_watching_txtvw=(TextView) v.findViewById(R.id.watching_total);
//		total_watching_txtvw.setText(map.get("program_watching"));
//		TextView total_comments_txtvw=(TextView) v.findViewById(R.id.comments_total);
//		total_comments_txtvw.setText(map.get("program_comments"));
		
		return v;
	}
	
};

}
