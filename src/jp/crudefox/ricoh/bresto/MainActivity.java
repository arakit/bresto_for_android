package jp.crudefox.ricoh.bresto;



import jp.crudefox.ricoh.bresto.fragment.ScreenFragment;
import jp.crudefox.tunacan.chikara.util.CFUtil;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends SherlockFragmentActivity {


	private AppManager mApp;

	private DrawerLayout mDrawerLayout;
	private View mLeftDrawer;
	private View mMainContent;
	private View mContent;

	private ScreenFragment mScreenFragment;

	private ActionBarDrawerToggle mDrawerListener;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		ActionBar actionBar = getSupportActionBar();
        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mApp = (AppManager) getApplication();

        //LoginManager lm = am.getLoginManager();

        //LoginInfo li = am.getLoginInfo();


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mMainContent = findViewById(R.id.main_content);
        mLeftDrawer = findViewById(R.id.left_drawer);
        mContent = findViewById(android.R.id.content);


        mDrawerListener = new MyDrawerListener(
        		MainActivity.this,
        		mDrawerLayout,
        		R.drawable.ic_launcher,
        		R.string.app_name,
        		R.string.app_name
        );

        mDrawerLayout.setDrawerListener(mDrawerListener);


        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);



        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft =  fm.beginTransaction();

        ScreenFragment f_sc = mScreenFragment = new ScreenFragment();

        ft.add(android.R.id.content, f_sc);

        ft.commit();



//		Tab tab;
//		Bundle args;



//		tab = actionBar.newTab();
//		tab.setText("スクリーン");
//		args = new Bundle();
//		args.putSerializable(Const.AK_LOGIN_INFO, li);
//		tab.setTabListener(new TabListener<ScreenFragment>(
//                this, "tag1", ScreenFragment.class, android.R.id.content, args));
//		actionBar.addTab(tab);


//		tab = actionBar.newTab();
//		tab.setText("貼り付け");
//		args = new Bundle();
//		args.putSerializable(Const.AK_LOGIN_INFO, li);
//		tab.setTabListener(new TabListener<ContributeFragment>(
//                this, "tag1", ContributeFragment.class, android.R.id.content, args));
//		actionBar.addTab(tab);

//		tab = actionBar.newTab();
//		tab.setText("キーワード");
//		args = new Bundle();
//		args.putSerializable(Const.AK_LOGIN_INFO, li);
//		tab.setTabListener(new TabListener<KeywordsFragment>(
//                this, "tag1", KeywordsFragment.class, android.R.id.content, args));
//		actionBar.addTab(tab);



//		tab = actionBar.newTab();
//		tab.setText("ヘルリン\nの部屋");
//		args = new Bundle();
//		args.putSerializable(Const.AK_LOGIN_INFO, li);
//		tab.setTabListener(new TabListener2<DummySectionFragment>(
//                this, "tag2", DummySectionFragment.class, android.R.id.content, args));
//		actionBar.addTab(tab);
//
//		tab = actionBar.newTab();
//		tab.setText("メンバ");
//		args = new Bundle();
//		args.putSerializable(Const.AK_LOGIN_INFO, li);
//		tab.setTabListener(new TabListener2<DummySectionFragment>(
//                this, "tag3", DummySectionFragment.class, android.R.id.content, args));
//		actionBar.addTab(tab);
//
//		tab = actionBar.newTab();
//		tab.setText("グラフ");
//		args = new Bundle();
//		args.putSerializable(Const.AK_LOGIN_INFO, li);
//		tab.setTabListener(new TabListener2<DummySectionFragment>(
//                this, "tag4", DummySectionFragment.class, android.R.id.content, args));
//		actionBar.addTab(tab);


//		tab = actionBar.newTab();
//		tab.setText("プロジェクタ一覧");
//		args = new Bundle();
//		args.putSerializable(Const.AK_LOGIN_INFO, li);
//		tab.setTabListener(new TabListener<ProjectorFragment>(
//                this, "tab_projector", ProjectorFragment.class, android.R.id.content, args));
//		actionBar.addTab(tab);


	}




	@Override
	protected void onDestroy() {
		super.onDestroy();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft =  fm.beginTransaction();

        if(mScreenFragment!=null) ft.remove(mScreenFragment);

        ft.commit();
	}




	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	private class MyDrawerListener extends ActionBarDrawerToggle{


		public MyDrawerListener(Activity activity, DrawerLayout drawerLayout, int drawerImageRes,
				int openDrawerContentDescRes, int closeDrawerContentDescRes) {
			super(activity, drawerLayout, drawerImageRes, openDrawerContentDescRes, closeDrawerContentDescRes);
		}

		@Override
		public void onDrawerClosed(View drawerView) {
			// TODO 自動生成されたメソッド・スタブ
			super.onDrawerClosed(drawerView);
			CFUtil.Log("onDrawerClosed");
		}

		@Override
		public void onDrawerOpened(View drawerView) {
			// TODO 自動生成されたメソッド・スタブ
			super.onDrawerOpened(drawerView);
			CFUtil.Log("onDrawerOpened");
		}

		@Override
		public void onDrawerSlide(View drawerView, float slideOffset) {
			// TODO 自動生成されたメソッド・スタブ
			super.onDrawerSlide(drawerView, slideOffset);
			CFUtil.Log("onDrawerSlide");
		}

		@Override
		public void onDrawerStateChanged(int newState) {
			// TODO 自動生成されたメソッド・スタブ
			super.onDrawerStateChanged(newState);
			CFUtil.Log("onDrawerStateChanged");
		}


	}




	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
	    super.onPostCreate(savedInstanceState);
	    mDrawerListener.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    mDrawerListener.onConfigurationChanged(newConfig);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

	    // ActionBarDrawerToggleにandroid.id.home(up ナビゲーション)を渡す。
//	    if (mDrawerListener.onOptionsItemSelected(item)) {
//	        return true;
//	    }
		CFUtil.Log("onOptionsItemSelected");

		if( item.getItemId() == android.R.id.home ){
			mDrawerLayout.openDrawer(mLeftDrawer);
			return true;
		}

	    return super.onOptionsItemSelected(item);
	}


}
