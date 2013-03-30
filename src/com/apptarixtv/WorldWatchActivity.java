package com.apptarixtv;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.apptarixtv.R;
import com.apptarixtv.HorzScrollWithListMenu.ClickListenerForScrolling;
import com.apptarixtv.HorzScrollWithListMenu.SizeCallbackForMenu;
import com.apptarixtv.constant.ApptarixConstant;
import com.apptarixtv.constant.HorizontalListView;
import com.apptarixtv.constant.HttpContn;
import com.apptarixtv.util.ImageLoader;
import com.apptarixtv.util.LazyAdapter;
import com.apptarixtv.util.MyHorizontalScrollView;
import com.apptarixtv.util.ProgramGrid;
import com.apptarixtv.util.ViewUtils;
import com.apptarixtv.util.MyHorizontalScrollView.SizeCallback;
import com.slidingmenu.lib.SlidingMenu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class WorldWatchActivity extends Activity{
	public static final String TAG = "WorldWatchActivity";
	HorizontalListView world_watch_list;
	// ArrayList<HashMap<String, String>> array_list_programs;
	ArrayList<ProgramGrid> programgrid;
	LazyAdapter adapter;
//	ToggleButton toglww;
	TextView txtchathead;
//	ProgressBar pgbarchat;
	 ArrayList<ProgramComment> progmcomnt;
	 ListView chatlist;
	 
	 EditText txtchat;
	 Button btnPost;
	
	ImageView imgBrowse,imgInvite;
	private SharedPreferences fbPrefs;
	String uname="";
	private Bitmap mbmp;
	Button btn_frnd_watch,btn_all_watch;
	SlidingMenu sldngmenu;
	
	View menu;
	View app;
	 MyHorizontalScrollView scrollView;
	boolean menuOut = false;
	Button btninvite;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		  LayoutInflater inflater = LayoutInflater.from(this);
	        scrollView = (MyHorizontalScrollView) inflater.inflate(R.layout.world_watch_main, null);
	        setContentView(scrollView);

	        menu = inflater.inflate(R.layout.invitefrnd_menu, null);
	        app = inflater.inflate(R.layout.world_watch_app, null);
	        
	        
	        
			 btn_frnd_watch=(Button) app.findViewById(R.id.btn_frnd);
			 btn_all_watch=(Button) app.findViewById(R.id.btn_all);
			txtchathead=(TextView) app.findViewById(R.id.textchatheading);
//			pgbarchat=(ProgressBar) app.findViewById(R.id.chatPgbar);
			txtchat=(EditText) app.findViewById(R.id.txtchat);
			btnPost=(Button) app.findViewById(R.id.btnPost);
			imgBrowse=(ImageView) app.findViewById(R.id.imgbrowse);
			imgInvite=(ImageView) app.findViewById(R.id.imginvite);
			btninvite=(Button) menu.findViewById(R.id.btninvitefrnds);
	        final View[] children = new View[] { menu, app };
	        
	        int scrollToViewIdx = 1;
	        scrollView.initViews(children, scrollToViewIdx, new SizeCallbackForMenu(imgInvite));

		 fbPrefs = this.getSharedPreferences("fbPrefs", MODE_PRIVATE);
		// array_list_programs=new ArrayList<HashMap<String,String>>();
//		toglww = (ToggleButton) findViewById(R.id.toggleworldwatch);
		 
		
		
		
		programgrid = new ArrayList<ProgramGrid>();
		progmcomnt=new ArrayList<ProgramComment>();
		chatlist=(ListView) findViewById(R.id.lstChat);
		uname=fbPrefs.getString("uname", "John");
		loadAllChat();
		
		
//		sldngmenu = new SlidingMenu(this);
//		sldngmenu.setMode(SlidingMenu.LEFT);
//		sldngmenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
//		sldngmenu.setShadowWidth(5);
//		sldngmenu.setFadeDegree(0.0f);
//		sldngmenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
////		menu.setBehindWidth(deviceWidth);
//		sldngmenu.setMenu(R.layout.check_frnd);

		btninvite.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				 Intent startIntent=new Intent(getApplicationContext(), CheckFrnd.class);
			        startActivity(startIntent);
			}
		});
		
		btn_all_watch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btn_all_watch.setBackgroundColor(R.drawable.tab_select);
				btn_frnd_watch.setBackgroundColor(R.drawable.tab_dselect);
				btn_all_watch.setTextColor(0xbc7801);
				btn_frnd_watch.setTextColor(0xffffff);
				loadAllChat();
				chatlist.setAdapter(chatadapter);
			}
		});
		
		btn_frnd_watch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btn_all_watch.setBackgroundColor(R.drawable.tab_dselect);
				btn_frnd_watch.setBackgroundColor(R.drawable.tab_select);
				btn_frnd_watch.setTextColor(0xbc7801);
				btn_all_watch.setTextColor(0xffffff);
				loadFrndChat();
				chatlist.setAdapter(chatadapter);
			}
		});
		
//		if(!toglww.isChecked()){
//			
//			toglww.setButtonDrawable(R.drawable.toggle_all);
//			toglww.setTextOn("Friends");
//			toglww.setChecked(toglww.isChecked());
//		}else{
//			
//			toglww.setButtonDrawable(R.drawable.toggle_frnd);
//			toglww.setTextOff("All");
//			toglww.setChecked(toglww.isChecked());
//		}
//		
		
		
		 String fondo = fbPrefs.getString("propic", "vacio");

	      if(!fondo.equals("vacio")){ 
	          byte[] b = Base64.decode(fondo, Base64.DEFAULT);
	          InputStream is = new ByteArrayInputStream(b);
	           mbmp = BitmapFactory.decodeStream(is);
	       
	      }else{
	    	  mbmp=null;
	      }
		
		// dumyDataTemp();
		new getProgrammForThisHour().execute("");
		
		
		btnPost.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(txtchat.length()>0){
					ProgramComment pc=new ProgramComment();
					pc.setComment(txtchat.getText().toString());
					pc.setUserName(uname);
					pc.setTime("");
					progmcomnt.add(pc);
//					if(progmcomnt.size()>10){
//						progmcomnt.remove(0);
//					}
					chatadapter.notifyDataSetChanged();
//					chatadapter.notifyDataSetInvalidated();
//					chatlist.inflate(context, resource, root)
					txtchat.setText("");
					
				}else{
					Toast.makeText(getApplicationContext(), "Please enter some text", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		
		imgBrowse.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 Intent startIntent=new Intent(getApplicationContext(), ChanelActivity.class);
			        startActivity(startIntent);
			}
		});
		
		imgInvite.setOnClickListener(new ClickListenerForScrolling(scrollView, menu));

	}
	

    /**
     * Helper for examples with a HSV that should be scrolled by a menu View's width.
     */
    static class ClickListenerForScrolling implements OnClickListener {
        HorizontalScrollView scrollView;
        View menu;
        /**
         * Menu must NOT be out/shown to start with.
         */
        boolean menuOut = false;

        public ClickListenerForScrolling(HorizontalScrollView scrollView, View menu) {
            super();
            this.scrollView = scrollView;
            this.menu = menu;
        }

        @Override
        public void onClick(View v) {
            Context context = menu.getContext();
//            String msg = "Slide " + new Date();
//            Toast.makeText(context, msg, 1000).show();
//            System.out.println(msg);

            int menuWidth = menu.getMeasuredWidth();

            // Ensure menu is visible
            menu.setVisibility(View.VISIBLE);

            if (!menuOut) {
                // Scroll to 0 to reveal menu
                int left = 0;
                scrollView.smoothScrollTo(left, 0);
            } else {
                // Scroll to menuWidth so menu isn't on screen.
                int left = menuWidth;
                scrollView.smoothScrollTo(left, 0);
            }
            menuOut = !menuOut;
        }
    }
	
    /**
     * Helper that remembers the width of the 'slide' button, so that the 'slide' button remains in view, even when the menu is
     * showing.
     */
    static class SizeCallbackForMenu implements SizeCallback {
        int btnWidth;
        View btnSlide;

        public SizeCallbackForMenu(View btnSlide) {
            super();
            this.btnSlide = btnSlide;
        }

        @Override
        public void onGlobalLayout() {
            btnWidth = btnSlide.getMeasuredWidth();
            System.out.println("btnWidth=" + btnWidth);
        }

        @Override
        public void getViewSize(int idx, int w, int h, int[] dims) {
            dims[0] = w;
            dims[1] = h;
            final int menuIdx = 0;
            if (idx == menuIdx) {
                dims[0] = w - btnWidth;
            }
        }
    }


	
	
	BaseAdapter chatadapter=new BaseAdapter(){
		

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return progmcomnt.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View vi = convertView;
			if (vi == null) {
				@SuppressWarnings("static-access")
				LayoutInflater v = (LayoutInflater) getApplicationContext()
						.getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);
				vi = v.inflate(R.layout.program_coment_row, null);

			} else {
				vi = convertView;
			}
			if(convertView==null){
//				vi=inflater.inflate(R.layout.program_coment_row, null);
				TextView txtuname=(TextView) vi.findViewById(R.id.txtUname);
				txtuname.setText(progmcomnt.get(position).getUserName());
				TextView txtcommnet=(TextView) vi.findViewById(R.id.txtComment);
				txtcommnet.setText(progmcomnt.get(position).getComment());
				TextView txtctime=(TextView) vi.findViewById(R.id.txtctime);
				txtctime.setText(progmcomnt.get(position).getTime());
				ImageView imgprof=(ImageView) vi.findViewById(R.id.imgProfile);
				if(mbmp!=null){
					imgprof.setImageBitmap(mbmp);
				}
				
				
			}
			return vi;
		}
		
	};
	class getProgrammForThisHour extends AsyncTask<String, Void, String> {

		private String resp;
		ProgressDialog dialog = new ProgressDialog(WorldWatchActivity.this);
		private String fromTime;
		private String toTime;

		@Override
		protected void onPreExecute() {
			dialog.setMessage("Loading Program..");
			dialog.show();
			Date now = new Date();
			Calendar c = Calendar.getInstance();
			fromTime = new SimpleDateFormat("yyyyMMddHHmm").format(now);
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			month = month + 1;
			int day = c.get(Calendar.DAY_OF_MONTH);
			int minut = c.get(Calendar.MINUTE);

			if (hour < 23) {
				hour = hour + 1;
			} else {
				hour = 0;
				day=day+1;
			}
			toTime = "" + appendZero("" + year) + appendZero("" + month)
					+ appendZero("" + day) + appendZero("" + hour) + ""
					+ appendZero("" + minut);
			Log.e(TAG, "From Time : " + fromTime);
			Log.e(TAG, "To Time   : " + toTime);
		}

		private String appendZero(String time) {

			if (time.length() < 2) {
				time = "0" + time;
			}

			return time;
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
//			HttpContn conn = new HttpContn();
//			Log.e("mohsin", "in background task..");
//			resp = conn.coonectHttp(ApptarixConstant.PROGRAMM_SCHEDULE_GRID
//					+ "fromdatetime=" + fromTime + "&todatetime=" + toTime
//					+ "&context=applicationName=apptarix");
//			Log.e(TAG, "URL= " + ApptarixConstant.PROGRAMM_SCHEDULE_GRID
//					+ "fromdatetime=" + fromTime + "&todatetime=" + toTime
//					+ "&context=applicationName=apptarix");
			resp=ReadFromfile("schedule.json", getApplicationContext());
			parseResponse(resp);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			dialog.cancel();
			adapter = new LazyAdapter(WorldWatchActivity.this);
			world_watch_list = (HorizontalListView) findViewById(R.id.world_watch_list);
			world_watch_list.setAdapter(adapter);
			loadAllChat();
//			addTogleButton();
			
//			new LoadChatForProgram().execute("");
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

			return programgrid.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View vi = convertView;
			if (convertView == null)
				vi = inflater.inflate(R.layout.listitem, null);

			TextView chaneltext = (TextView) vi.findViewById(R.id.channel_name);
			TextView channel_program_name_textview = (TextView) vi
					.findViewById(R.id.channel_program);
			channel_program_name_textview.setText(programgrid.get(position)
					.getProgramName());
			ImageView image = (ImageView) vi.findViewById(R.id.program_img);
			chaneltext.setText(programgrid.get(position).getChannelName());
			imageLoader.DisplayImage(programgrid.get(position).getPrgmImgUrl(),
					image);
			return vi;
		}
		
	}

	public void parseResponse(String resp) {

		try {
			JSONObject root = new JSONObject(resp);
			JSONObject SchdlGrid = new JSONObject(
					root.getString("Schedule"));
			JSONArray channelarray = new JSONArray(
					SchdlGrid.getString("channel"));
			// String[] chid=new String[channelarray.length()];
			// String[] dispname=new String[channelarray.length()];
			// String[] chanelImg =new String[channelarray.length()];

			for (int i = 0; i < channelarray.length(); i++) {
				// pg.setChannelName(channelarray.getJSONObject(i).getString("channellogourl"));
				JSONArray programarray = new JSONArray(channelarray
						.getJSONObject(i).getString("programme"));
				for (int j = 0; j < programarray.length(); j++) {
					ProgramGrid pg = new ProgramGrid();
					pg.setChannelName(channelarray.getJSONObject(i).getString(
							"display-name"));
					pg.setProgramName(programarray.getJSONObject(j).getString(
							"title"));
					Log.e(TAG, "Programm name="
							+ programarray.getJSONObject(j).getString("title"));
					pg.setStartTime(programarray.getJSONObject(j).getString(
							"start"));
					pg.setStopTime(programarray.getJSONObject(j).getString(
							"stop"));//
					pg.setPrgmImgUrl(programarray.getJSONObject(j).getString(
							"programmeurl"));
					Log.e(TAG,
							"Programm Url="
									+ programarray.getJSONObject(j).getString(
											"programmeurl"));
					pg.setPrgDesc(programarray.getJSONObject(j).getString(
							"desc"));
					programgrid.add(pg);
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String ReadFromfile(String fileName, Context context) {
	    StringBuilder ReturnString = new StringBuilder();
	    InputStream fIn = null;
	    InputStreamReader isr = null;
	    BufferedReader input = null;
	    try {
	    	Log.e("file path: ", fileName);
	        fIn = context.getResources().getAssets()
	                .open("schedule.json", context.MODE_WORLD_READABLE);
	        isr = new InputStreamReader(fIn);
	        input = new BufferedReader(isr);
	        String line = "";
	        while ((line = input.readLine()) != null) {
	            ReturnString.append(line);
	        }
	    } catch (Exception e) {
	        e.getMessage();
	    } finally {
	        try {
	            if (isr != null)
	                isr.close();
	            if (fIn != null)
	                fIn.close();
	            if (input != null)
	                input.close();
	        } catch (Exception e2) {
	            e2.getMessage();
	        }
	    }
	    return ReturnString.toString();
	}
	

	protected void loadAllChat() {
		ProgramComment pc=new ProgramComment();
		pc.setComment("Hi this is very nice programm");
		pc.setUserName(uname);
		pc.setTime("");
		progmcomnt.add(pc);
		ProgramComment pc1=new ProgramComment();
		pc1.setComment("I like this programm very much");
		pc1.setUserName(uname);
		pc1.setTime("");
		progmcomnt.add(pc1);
		
		
		
		
	}

	protected void loadFrndChat() {
		
		
		ProgramComment pc=new ProgramComment();
		pc.setComment("Hi hows this Program");
		pc.setUserName(uname);
		pc.setTime("");
		progmcomnt.add(pc);
		ProgramComment pc1=new ProgramComment();
		pc1.setComment("I don't like this");
		pc1.setUserName(uname);
		pc1.setTime("");
		progmcomnt.add(pc1);
		
	}
	
	/*
	class LoadChatForProgram extends AsyncTask<String, Void, String>{
		
		
		@Override
		protected void onPreExecute() {
			pgbarchat.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			pgbarchat.setVisibility(View.INVISIBLE);
			super.onPostExecute(result);
			
		}
		
	}
	*/
	
	
	

}
