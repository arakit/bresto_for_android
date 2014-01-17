package jp.crudefox.ricoh.bresto;



import jp.crudefox.ricoh.bresto.chikara.manager.LoginManager.ProjectInfo;
import jp.crudefox.ricoh.bresto.fragment.BreStoListFragment;
import jp.crudefox.ricoh.bresto.fragment.ProjectorFragment;
import jp.crudefox.ricoh.bresto.fragment.ScreenNativeFragment;
import jp.crudefox.tunacan.chikara.util.CFCardUIAdapter;
import jp.crudefox.tunacan.chikara.util.CFUtil;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends SherlockFragmentActivity {


	private AppManager mApp;

	private DrawerLayout mDrawerLayout;
	private ViewGroup mLeftDrawer;
	private ViewGroup mMainContent;
	//private View mContent;

	private View mLeftDrawerContent;


	private ScreenNativeFragment mScreenFragment;
	private ProjectorFragment mProjectorFragment;
	private BreStoListFragment mBrestolistFragment;


	private ActionBarDrawerToggle mDrawerListener;



//	private GetProjectListTask mGetProjectListTask;
//	private SelectProjectTask mSelectProjectTask;


	private ListView mListView;

	private TextView mMapTextView;
	private TextView mProjectorTextView;


	private MyListdapter mAdapter;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		ActionBar actionBar = getSupportActionBar();
        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mApp = (AppManager) getApplication();

        mAdapter = new MyListdapter(this);
        //LoginManager lm = am.getLoginManager();

        //LoginInfo li = am.getLoginInfo();


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mMainContent = (ViewGroup) findViewById(R.id.main_content);
        mLeftDrawer = (ViewGroup) findViewById(R.id.left_drawer);
        //mContent = findViewById(android.R.id.content);

        mLeftDrawerContent = getLayoutInflater().inflate(R.layout.layout_drawer, null);
        mListView = (ListView) mLeftDrawerContent.findViewById(R.id.drawer_listView);
        mMapTextView = (TextView) mLeftDrawerContent.findViewById(R.id.text_map);
        mProjectorTextView = (TextView) mLeftDrawerContent.findViewById(R.id.text_projector);

        mLeftDrawer.addView(mLeftDrawerContent,
        		new ViewGroup.LayoutParams(
        				ViewGroup.LayoutParams.WRAP_CONTENT,
        				ViewGroup.LayoutParams.MATCH_PARENT
        				)
        		);


        mDrawerListener = new MyDrawerListener(
        		MainActivity.this,
        		mDrawerLayout,
        		R.drawable.ic_drawer,
        		R.string.app_name,
        		R.string.app_name
        );

        mDrawerLayout.setDrawerListener(mDrawerListener);

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        mScreenFragment = new ScreenNativeFragment();
        mProjectorFragment = new ProjectorFragment();
        mBrestolistFragment = new BreStoListFragment();


		//リストビューを選択したときフラグメントでサジェスト用のフラグメントに遷移する。
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ListItem item = mAdapter.getItem(position);
				if( item.onClick !=null ){
					item.onClick.run();
				}
			}
		});

		{//マインドマップ

			ListItem li = new ListItem();
			li.title = "Play!!";
			li.onClick = new Runnable() {
				@Override
				public void run() {
					setFragment(mScreenFragment);
					closeDrawer();
				}
			};
			mAdapter.addItem(li, 0);
		}

		{//マインドマップ

			ListItem li = new ListItem();
			li.title = "ぶれすと図一覧";
			li.onClick = new Runnable() {
				@Override
				public void run() {
					setFragment(mBrestolistFragment);
					closeDrawer();
				}
			};
			mAdapter.addItem(li, 0);
		}

		{//プロジェクタ選択

			ListItem li = new ListItem();
			li.title = "プロジェクタ選択";
			li.onClick = new Runnable() {
				@Override
				public void run() {
					setFragment(mProjectorFragment);
					closeDrawer();
				}
			};
			mAdapter.addItem(li, 0);
		}



		mListView.setAdapter(mAdapter);


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

	}




	private void setFragment(Fragment f){

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft =  fm.beginTransaction();

        //ft.add(android.R.id.content, mScreenFragment);
        ft.replace(R.id.main_content_fragment, f);
        //ft.replace(R.id.fragment_drawer_content, mProjectorFragment);

        ft.commit();
	}


	private void updateDrawerInfo(){

		String ip = mApp.getSelectProjectorIp();
		ProjectInfo p = mApp.getSelectMap();

		mProjectorTextView.setText(ip!=null ? ip : "プロジェクタ No Select");
		mMapTextView.setText(p!=null ? p.project_name : "マップ No Select. ");

	}

	@Override
	protected void onStart() {
		// TODO 自動生成されたメソッド・スタブ
		super.onStart();


		setFragment(mScreenFragment);

		updateDrawerInfo();

	}





	@Override
	protected void onStop() {
		// TODO 自動生成されたメソッド・スタブ
		super.onStop();

//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction ft =  fm.beginTransaction();

//        if(mScreenFragment!=null) ft.remove(mScreenFragment);
//        if(mProjectorFragment!=null) ft.remove(mProjectorFragment);

//        ft.commit();

		cancelTuusin();

	}



	public void selectIp(){
		setFragment(mScreenFragment);
		updateDrawerInfo();
	}
	public void selectMap(){
		setFragment(mScreenFragment);
		updateDrawerInfo();
	}


//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getSupportMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}


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

		int id = item.getItemId();

		if( id == android.R.id.home ){
			final View d = mLeftDrawer;
			if(mDrawerLayout.isDrawerOpen(d)){
				mDrawerLayout.closeDrawer(d);
			}else{
				mDrawerLayout.openDrawer(d);
			}
			return true;
		}
//		else if( id == Const.MENU_ID_PROJECT_SELECT ){
//			attemptGetProjectList();
//			return true;
//		}
		else if( id == Const.MENU_ID_LOGOUT ){
			mApp.setLoginInfo(null);
			finish();
			return true;
		}

	    return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		return super.onCreateOptionsMenu(menu);

		MenuItem mi;
		//SubMenu sm;
		int order = 1;

//		SubMenu sm_etc;

		mi = menu.add(Menu.NONE, Const.MENU_ID_PROJECT_SELECT,order++,"ぶれすと図");
		mi.setIcon(android.R.drawable.ic_menu_gallery);
		mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

//		sm_etc = menu.addSubMenu(1100,Menu.NONE,order++,"…");
//		mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);


//		mi = sm_etc.add(Menu.NONE,MENU_ID_PROFIELE, order++,"プロフィール");
//		mi = sm_etc.add(Menu.NONE,MENU_ID_ABOUT, order++,"About");


//		mi = menu.add(Menu.NONE, Const.MENU_ID_PROFILE, order++,"プロフィール");
//		mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

		mi = menu.add(Menu.NONE, Const.MENU_ID_LOGOUT,order++,"ログアウト");
		mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

		mi = menu.add(Menu.NONE, Const.MENU_ID_SETTINGS,order++,"設定");
		mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);


		mi = menu.add(Menu.NONE, Const.MENU_ID_ABOUT,order++,"About");
		mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);


		return true;

	}





	public void closeDrawer(){
		mDrawerLayout.closeDrawer(mLeftDrawer);
	}




//	private void showProjectSelectDlg(List<ProjectInfo> pro_list){
//
//
//		AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
//
//		final AdapterBridge<ProjectInfo> bri = new AdapterBridge<ProjectInfo>();
//
//		for(int i=0;i<pro_list.size();i++){
//			ProjectInfo info = pro_list.get(i);
//			bri.addItem(""+info.project_name, info);
//		}
//
//		String[] items = bri.getItemTextArray();
//
//		ab.setItems(items , new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				ProjectInfo pf = bri.getItem(which);
//
//				attemptSelectProjectTask(pf.project_id);
//			}
//		});
//
//
//		ab.create().show();
//
//	}


	private void cancelTuusin(){
//		cancelGetProjectListTask();
//		cancelSelectProjectTask();
	}






	private static class ListItem{
		String title = null;
		Runnable onClick = null;
	}

	private class MyListdapter extends CFCardUIAdapter<ListItem>{

		public MyListdapter(Context context) {
			super(context);

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			if(convertView==null){
				view = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, null);
			}else{
				view = convertView;
			}

			ListItem item = getItem(position);

			//ImageView icon = (ImageView) view.findViewById(R.id.icon);
			TextView text1 = (TextView) view.findViewById(android.R.id.text1);
			//TextView text2 = (TextView) view.findViewById(R.id.text_2);

			text1.setText( item.title!=null ? item.title : "" );
			//text2.setText( "---" );

//			if( isCardMotion(position) ){
//				doCardMotion(view, R.anim.card_ui_motion_from_right);
//				setCardMotion(position, false);
//			}

			return view;
		}

	}

}
