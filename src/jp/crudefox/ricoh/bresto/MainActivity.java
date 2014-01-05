package jp.crudefox.ricoh.bresto;



import java.util.List;

import jp.crudefox.ricoh.bresto.chikara.manager.LoginInfo;
import jp.crudefox.ricoh.bresto.chikara.manager.LoginManager;
import jp.crudefox.ricoh.bresto.chikara.manager.LoginManager.ProjectInfo;
import jp.crudefox.ricoh.bresto.fragment.ProjectorFragment;
import jp.crudefox.ricoh.bresto.fragment.ScreenFragment;
import jp.crudefox.tunacan.chikara.util.AdapterBridge;
import jp.crudefox.tunacan.chikara.util.CFUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.AsyncTask;
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
	private ProjectorFragment mProjectorFragment;

	private ActionBarDrawerToggle mDrawerListener;




	private GetProjectListTask mGetProjectListTask;
	private SelectProjectTask mSelectProjectTask;





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

        mScreenFragment = new ScreenFragment();
        mProjectorFragment = new ProjectorFragment();



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






	@Override
	protected void onStart() {
		// TODO 自動生成されたメソッド・スタブ
		super.onStart();


        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft =  fm.beginTransaction();

        //ft.add(android.R.id.content, mScreenFragment);
        ft.replace(R.id.fragment_content, mScreenFragment);
        ft.replace(R.id.fragment_drawer_content, mProjectorFragment);

        ft.commit();

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
			mDrawerLayout.openDrawer(mLeftDrawer);
			return true;
		}
		else if( id == Const.MENU_ID_PROJECT_SELECT ){
			attemptGetProjectList();
			return true;
		}
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




	private void showProjectSelectDlg(List<ProjectInfo> pro_list){


		AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);

		final AdapterBridge<ProjectInfo> bri = new AdapterBridge<ProjectInfo>();

		for(int i=0;i<pro_list.size();i++){
			ProjectInfo info = pro_list.get(i);
			bri.addItem(""+info.project_name, info);
		}

		String[] items = bri.getItemTextArray();

		ab.setItems(items , new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				ProjectInfo pf = bri.getItem(which);

				attemptSelectProjectTask(pf.project_id);
			}
		});


		ab.create().show();

	}


	private void cancelTuusin(){
		cancelGetProjectListTask();
		cancelSelectProjectTask();
	}



	//aaaa
	private void attemptGetProjectList(){
		if(mGetProjectListTask!=null){
			return ;
		}

		mGetProjectListTask = new GetProjectListTask();
		mGetProjectListTask.execute((Void)null);

	}
	private void cancelGetProjectListTask(){
		if(mGetProjectListTask==null) return ;

		mGetProjectListTask.cancel(true);

	}

	//aaaa
	private void attemptSelectProjectTask(String project_id){
		if(mSelectProjectTask!=null){
			return ;
		}

		mSelectProjectTask = new SelectProjectTask();
		mSelectProjectTask.execute(project_id);

	}
	private void cancelSelectProjectTask(){
		if(mSelectProjectTask==null) return ;

		mSelectProjectTask.cancel(true);

	}



	/**
	 * doBack, Progress, postExecute
	 */
	private class GetProjectListTask extends AsyncTask<Void , Void, Boolean> {

		List<ProjectInfo> mmResult;

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.
			//postToast("更新中...");
			LoginManager lm = mApp.getLoginManager();
			LoginInfo li = mApp.getLoginInfo();
			List<ProjectInfo> result = lm.getProjectList(li);
			mmResult = result;

			return result!=null;
		}

		@Override
		protected void onPostExecute(final Boolean success) {

			//showProgress(false);

			if(success){

				CFUtil.Log("プロジェクトリスト更新しました。");

//				postToast("更新しましした。");

//				mSearchList.clear();
//				for(int i=0;i<mmResult.size();i++){
//					Keyword k = mmResult.get(i);
//					mSearchList.add(k);
//				}
//
//				updateSearch();

//				updateProjectorView(mmResult);

				showProjectSelectDlg(mmResult);

			}else{

				CFUtil.Log("プロジェクトリスト更新できませんでした。");


				//postToast("プロジェクト一覧を取得できませんでした。");
			}

			mGetProjectListTask = null;
		}

		@Override
		protected void onCancelled() {
			mGetProjectListTask = null;
			//showProgress(false);
		}
	}

	/**
	 * doBack, Progress, postExecute
	 */
	private class SelectProjectTask extends AsyncTask<String , Void, Boolean> {

		String mProId;
		boolean mResult;

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO: attempt authentication against a network service.
			//postToast("更新中...");
			mProId = params[0];
			LoginManager lm = mApp.getLoginManager();
			LoginInfo li = mApp.getLoginInfo();
			mResult  = lm.selectProject(li, mProId);

			return mResult;
		}

		@Override
		protected void onPostExecute(final Boolean success) {

			//showProgress(false);

			if(success){

				CFUtil.Log("プロジェクトを選択しました。");

//				postToast("更新しましした。");


				mScreenFragment.startWebView();

			}else{

				CFUtil.Log("プロジェクト選択に失敗しました。");

				//postToast("プロジェクト一覧を取得できませんでした。");
			}

			mSelectProjectTask = null;
		}

		@Override
		protected void onCancelled() {
			mSelectProjectTask = null;
			//showProgress(false);
		}
	}



}
