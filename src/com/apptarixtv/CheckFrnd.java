package com.apptarixtv;


import java.util.ArrayList;
import java.util.HashMap;

import com.apptarixtv.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import android.widget.ToggleButton;

public class CheckFrnd extends Activity{
	
	ListView frnd_list_view;
	ArrayList<HashMap<String, String>>fendlist;
	TextView txtfbresult;
	String fbtxt="We have found 5 of you facebook friends on APPTARIX TV." +
			"  Connect with them and invite others.";
	Button btnNext,btnSkip;
	int []imgprof={R.drawable.ashish,R.drawable.amit,R.drawable.pravin,R.drawable.sajid,R.drawable.ware,
			R.drawable.shree,R.drawable.mohan,R.drawable.himanshu,R.drawable.rohit,R.drawable.kunal};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.check_frnd);
		txtfbresult=(TextView) findViewById(R.id.txtfbreport);
		txtfbresult.setText(fbtxt);
		btnNext=(Button) findViewById(R.id.btnnext);
		btnSkip=(Button) findViewById(R.id.btnskip);
		fendlist=new ArrayList<HashMap<String, String>>();
		dumyDataTemp();
		frnd_list_view=(ListView) findViewById(R.id.lst_frnd);
		frnd_list_view.setAdapter(frndAdapter);
		
		
//		new GetFrndList().execute();
		
		btnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			Intent intent=new Intent(getApplicationContext(),WorldWatchActivity.class);
			startActivity(intent);
			}
		});
		
		btnSkip.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(getApplicationContext(),WorldWatchActivity.class);
				startActivity(intent);
			}
		});
		
	}
	
//	class
	
	private void dumyDataTemp() {
		
		HashMap<String, String> hash_map=new HashMap<String, String>();
		hash_map.put("first_name", "Ashish");
		hash_map.put("last_name", "Rane");
		hash_map.put("fb_id", "100");
		hash_map.put("isfrnd", "0");
		
		fendlist.add(hash_map);
		
		hash_map=new HashMap<String, String>();
		hash_map.put("first_name", "Amarish");
		hash_map.put("last_name", "Nirant");
		hash_map.put("fb_id", "200");
		hash_map.put("isfrnd", "1");
		
		fendlist.add(hash_map);
		
		hash_map=new HashMap<String, String>();
		hash_map.put("first_name", "Ankush");
		hash_map.put("last_name", "Kumar");
		hash_map.put("fb_id", "300");
		hash_map.put("isfrnd", "0");
		
		fendlist.add(hash_map);
		
		hash_map=new HashMap<String, String>();
		hash_map.put("first_name", "Bhushan");
		hash_map.put("last_name", "Reddy");
		hash_map.put("fb_id", "400");
		hash_map.put("isfrnd", "0");
		
		fendlist.add(hash_map);
		
		hash_map=new HashMap<String, String>();
		hash_map.put("first_name", "Biswas");
		hash_map.put("last_name", "Raj");
		hash_map.put("fb_id", "500");
		hash_map.put("isfrnd", "1");
		
		fendlist.add(hash_map);
		
		hash_map=new HashMap<String, String>();
		hash_map.put("first_name", "Chandu");
		hash_map.put("last_name", "");
		hash_map.put("fb_id", "600");
		hash_map.put("isfrnd", "0");
		
		fendlist.add(hash_map);
		hash_map=new HashMap<String, String>();
		hash_map.put("first_name", "Chandrashekar");
		hash_map.put("last_name", "");
		hash_map.put("fb_id", "700");
		hash_map.put("isfrnd", "1");
		
		fendlist.add(hash_map);
		hash_map=new HashMap<String, String>();
		hash_map.put("first_name", "Hetal");
		hash_map.put("last_name", "Shah");
		hash_map.put("fb_id", "800");
		hash_map.put("isfrnd", "1");
		fendlist.add(hash_map);
		
		
		
		hash_map=new HashMap<String, String>();
		hash_map.put("first_name", "Himanshu");
		hash_map.put("last_name", "Sinha");
		hash_map.put("fb_id", "900");
		hash_map.put("isfrnd", "1");
		fendlist.add(hash_map);
		
	
		
		hash_map=new HashMap<String, String>();
		hash_map.put("first_name", "Kunal");
		hash_map.put("last_name", "Sharma");
		hash_map.put("fb_id", "1000");
		hash_map.put("isfrnd", "0");
		fendlist.add(hash_map);
		
		
	}
	
	private BaseAdapter frndAdapter=new BaseAdapter() {
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				@SuppressWarnings("static-access")
				LayoutInflater vi = (LayoutInflater) getApplicationContext()
						.getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.frnd_list_row, null);

			} else {
				v = convertView;
			}
			Log.e("GETVIEW", "Position "+position);
			//View retval = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem, null);
			final HashMap<String, String> map=fendlist.get(position);
			TextView frnd_name_textview = (TextView) v.findViewById(R.id.txtfrndname);
			frnd_name_textview.setText(map.get("first_name")+" "+map.get("last_name"));
			ImageView profimg=(ImageView)v.findViewById(R.id.imgfrnd);
			profimg.setImageResource(imgprof[position]);
//			profimg.setBackgroundResource(imgprof[position]);
			 ToggleButton btnfbreq=(ToggleButton) v.findViewById(R.id.btninvite);
			
			
			if(map.get("isfrnd")==("0")){
				
				btnfbreq.setTextOn("Invite  Don't");
				btnfbreq.setTextOff(" Invite  Don't");
				btnfbreq.setChecked(btnfbreq.isChecked());
				Log.e("", "in 0");
				
			}else{
				
				btnfbreq.setTextOn("Connect Disconnect");
				btnfbreq.setTextOff("Connect Disconnect");
				btnfbreq.setChecked(btnfbreq.isChecked());
				Log.e("", "in 1");
			}
			

			
			return v;
		}
		
		@Override
		public long getItemId(int position) {
			
			return 0;
		}
		
		@Override
		public Object getItem(int position) {
			return null;
		}
		
		@Override
		public int getCount() {
			return fendlist.size();
		}
	};
	

}
