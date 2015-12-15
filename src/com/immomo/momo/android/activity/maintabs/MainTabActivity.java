package com.immomo.momo.android.activity.maintabs;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;

import com.immomo.momo.android.R;

@SuppressWarnings("deprecation")
public class MainTabActivity extends TabActivity {
	private TabHost mTabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maintabs);
		initViews();
		initTabs();
	}

	private void initViews() {
		mTabHost = getTabHost();
	}

	private void initTabs() {
		LayoutInflater inflater = LayoutInflater.from(MainTabActivity.this);

		View nearbyView = inflater.inflate(
				R.layout.common_bottombar_tab_nearby, null);
		TabHost.TabSpec nearbyTabSpec = mTabHost.newTabSpec(
				NearByActivity.class.getName()).setIndicator(nearbyView);
		nearbyTabSpec.setContent(new Intent(MainTabActivity.this,
				NearByActivity.class));
		mTabHost.addTab(nearbyTabSpec);

		View nearbyFeedsView = inflater.inflate(
				R.layout.common_bottombar_tab_site, null);
		TabHost.TabSpec nearbyFeedsTabSpec = mTabHost.newTabSpec(
				NearByFeedsActivity.class.getName()).setIndicator(
				nearbyFeedsView);
		nearbyFeedsTabSpec.setContent(new Intent(MainTabActivity.this,
				NearByFeedsActivity.class));
		mTabHost.addTab(nearbyFeedsTabSpec);

		View sessionListView = inflater.inflate(
				R.layout.common_bottombar_tab_chat, null);
		TabHost.TabSpec sessionListTabSpec = mTabHost.newTabSpec(
				SessionListActivity.class.getName()).setIndicator(
				sessionListView);
		sessionListTabSpec.setContent(new Intent(MainTabActivity.this,
				SessionListActivity.class));
		mTabHost.addTab(sessionListTabSpec);

		View contactView = inflater.inflate(
				R.layout.common_bottombar_tab_friend, null);
		TabHost.TabSpec contactTabSpec = mTabHost.newTabSpec(
				ContactTabsActivity.class.getName()).setIndicator(contactView);
		contactTabSpec.setContent(new Intent(MainTabActivity.this,
				ContactTabsActivity.class));
		mTabHost.addTab(contactTabSpec);

		View userSettingView = inflater.inflate(
				R.layout.common_bottombar_tab_profile, null);
		TabHost.TabSpec userSettingTabSpec = mTabHost.newTabSpec(
				UserSettingActivity.class.getName()).setIndicator(
				userSettingView);
		userSettingTabSpec.setContent(new Intent(MainTabActivity.this,
				UserSettingActivity.class));
		mTabHost.addTab(userSettingTabSpec);
		
	}
}
