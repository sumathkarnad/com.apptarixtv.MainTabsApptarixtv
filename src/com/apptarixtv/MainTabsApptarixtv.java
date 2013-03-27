package com.apptarixtv;

import com.apptarixtv.R;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;


@SuppressWarnings("deprecation")
public class MainTabsApptarixtv  extends TabActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.launge_tabs);
		TabHost tabHost = getTabHost();

		tabHost.addTab(tabHost
				.newTabSpec("tab1")
				.setIndicator("",//Friend's Best
						getResources().getDrawable(R.drawable.friendswatch1))
				.setContent(new Intent(this, WorldWatchActivity.class)));

		tabHost.addTab(tabHost
				.newTabSpec("tab2")
				.setIndicator("",//World Watch
						getResources().getDrawable(R.drawable.worldwatch1))
				.setContent(new Intent(this, WorldWatchActivity.class)));

		tabHost.addTab(tabHost
				.newTabSpec("tab3")
				.setIndicator("",//Favorites
						getResources().getDrawable(R.drawable.favourites1))
				.setContent(new Intent(this, HomeLogin.class)));
		tabHost.addTab(tabHost
				.newTabSpec("tab4")
				.setIndicator("",//Settings
						getResources().getDrawable(R.drawable.setting1))
				.setContent(new Intent(this, HomeLogin.class)));
		tabHost.setCurrentTab(0);
	}
}