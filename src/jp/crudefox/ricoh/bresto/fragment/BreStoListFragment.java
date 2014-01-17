package jp.crudefox.ricoh.bresto.fragment;



import java.util.List;

import jp.crudefox.ricoh.bresto.AppManager;
import jp.crudefox.ricoh.bresto.MainActivity;
import jp.crudefox.ricoh.bresto.R;
import jp.crudefox.ricoh.bresto.chikara.manager.LoginInfo;
import jp.crudefox.ricoh.bresto.chikara.manager.LoginManager;
import jp.crudefox.ricoh.bresto.chikara.manager.LoginManager.ProjectInfo;
import jp.crudefox.tunacan.chikara.util.AdapterBridge;
import jp.crudefox.tunacan.chikara.util.CFCardUIAdapter;
import jp.crudefox.tunacan.chikara.util.CFUtil;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;


/**
 * 		@Auth Chikara Funabashi
 * 		@Date 2013/08/10
 *
 */

/**
 *
 *
 */

public class BreStoListFragment extends SherlockFragment{






	private Context mContext;
	private View mRootView;

	private Handler mHandler = new Handler();


	private GetProjectListTask mGetProjectListTask;
	private SelectProjectTask mSelectProjectTask;


	private MapListAdapter mAdapter;

	private PullToRefreshListView mPullToRefreshListView;
	private ListView mListView;



	private AppManager mApp;

	private LoginInfo mLoginInfo;

	//--------------------

	private boolean mIsFirst = true;

	private LayoutInflater mLayoutInflater;






	public BreStoListFragment() {
		super();
		setHasOptionsMenu(true);
	}





	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}


	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//super.onCreateView(inflater, container, savedInstanceState);

		CFUtil.Log("onCreateView "+this);


		setContentView(R.layout.fragment_brestolist);

		mAdapter = new MapListAdapter(mContext);

		//mListView  = (CFOverScrolledListView) findViewById(R.id.member_frends_listView);
		mPullToRefreshListView  = (PullToRefreshListView) findViewById(R.id.listView);
		ListView actualListView = mListView = mPullToRefreshListView.getRefreshableView();


		mPullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				ProjectInfo item = mAdapter.getItem(position-1);

				//attemptSelectProjectTask(item.project_id);
				showMapDlg(item);

			}
		});

		//mListView.setOverscrollHeaderFooter(R.drawable.update_over_scrolled, 0);
//		mListView.setOverscrollHeaderEx(getResources().getDrawable(R.drawable.update_over_scrolled));
//		mListView.setOverscrollFooterEx(null);
//		mListView.setOnPullToRefreshListener(mPullToRefreshListener);


		mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);

		// Set a listener to be invoked when the list should be refreshed.
		mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				// Do work to refresh the list here.
				//new GetDataTask().execute();
				attemptGetProjectList();
			}
		});

		// Add an end-of-list listener
		mPullToRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				//Toast.makeText(PullToRefreshListActivity.this, "End of List!", Toast.LENGTH_SHORT).show();
			}
		});

		// Need to use the Actual ListView when registering for Context Menu
		registerForContextMenu(actualListView);


		/**
		 * Add Sound Event Listener
		 */
		SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(getActivity());
//		soundListener.addSoundEvent(State.PULL_TO_REFRESH, R.raw.pull_event);
//		soundListener.addSoundEvent(State.RESET, R.raw.reset_sound);
//		soundListener.addSoundEvent(State.REFRESHING, R.raw.refreshing_sound);
//		soundListener.addSoundEvent(State.PULL_TO_REFRESH, R.raw.pyo1);
//		soundListener.addSoundEvent(State.RESET, R.raw.reset_sound);
//		soundListener.addSoundEvent(State.REFRESHING, R.raw.cat5);
		mPullToRefreshListView.setOnPullEventListener(soundListener);

		// You can also just use setListAdapter(mAdapter) or
		// mPullRefreshListView.setAdapter(mAdapter)
		//actualListView.setAdapter(mCAdapter);

		mPullToRefreshListView.setAdapter(mAdapter);


		//updateListView();


//
//		mOkBtn.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				String text = mEditText.getText().toString();
//				if(TextUtils.isEmpty(text)){
//					toast("IP Adreessを入力してください！");
//					return ;
//				}
//				mApp.setSelectProjectorIp(text);
//
//				Activity act = getActivity();
//				if(act instanceof MainActivity){
//					MainActivity ma = (MainActivity)act;
//					ma.closeDrawer();
//				}
//
//			}
//		});


		mLoginInfo = mApp.getLoginInfo();


		if(mLoginInfo!=null){

		}else{
			toast("ログインに失敗しています。");
			//finish();
		}


		return mRootView;

	}


	private void showMapDlg(final ProjectInfo item){

		AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());

		final AdapterBridge<Integer> bri = new AdapterBridge<Integer>();

		bri.addItem("開く", 0);
		bri.addItem("削除", 1);
		bri.addItem("キャンセル", 2);

		ab.setItems(bri.getItemTextArray(), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				int id = bri.getItem(which);

				if(id==0){
					attemptSelectProjectTask(item);
				}
				else if(id==1){
					toast("削除できません。");
				}
				else if(id==2){

				}
			}
		});

		ab.create().show();

	}


	@Override
	public void onDetach() {
		super.onDetach();

//		FragmentManager manager = getFragmentManager();
//		manager.beginTransaction()
//			   .remove(manager.findFragmentByTag("KeywordsSuggestFragment"))
////		       .addToBackStack("KeywordsSuggestFragment")
////		       .replace(android.R.id.content, f)
//		       .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
//		       .commit();

	}







	//ビューの更新
	private void updateMapListView(List<ProjectInfo> list){

		mAdapter.clearItems();


		for(int i=0; i<list.size(); i++){
			ProjectInfo item = list.get(i);
			mAdapter.addItem(item, 0);
		}

		mAdapter.notifyDataSetChanged();



	}





	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();

		mLayoutInflater = getLayoutInflater();
		//mDateFormat = DateFormat.getInstance();

		//キーワードの通信を管理するマネージャ
		//プロジェクトはp1を直打ちしています。

		//mKeywordManager = new KeywordManager(mContext, "p1");

		mApp = (AppManager) getActivity().getApplication();



//		{
//			final String[] COUNTRIES = new String[] {
//					  "りんご", "みかん",
//				      "Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra",
//				      "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina",
//				      "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan",
//				      "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium"};
//			for(int i=0;i<COUNTRIES.length;i++){ mSearchList.add(COUNTRIES[i]); }
//		}

	}


	private View findViewById(int id){
		return mRootView.findViewById(id);
	}
	private LayoutInflater getLayoutInflater(){
		return getActivity().getLayoutInflater();
	}
	private void setContentView(int id){
		mRootView = getLayoutInflater().inflate(id, null);
	}
	private Context getApplicationContext(){
		return mContext.getApplicationContext();
	}

	private void finish(){
		Activity act = getActivity();
		if(act!=null){
			act.finish();
		}else{

		}
	}

	private void cancelTuusin(){
		cancelGetProjectListTask();
		cancelSelectProjectTask();
	}









	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		//super.onCreateOptionsMenu(menu, inflater);

//		MenuItem mi;
//		int order = 1;

//		mi = menu.add(Menu.NONE,MENU_ID_UPDATE,order++,"更新");
//		mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		super.onCreateOptionsMenu(menu, inflater);
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
//		return super.onOptionsItemSelected(item);

//		int id = item.getItemId();

//		if(id==MENU_ID_UPDATE){
//			attemptGetBorad();
//			return true;
//		}

		return false;
	}




//	@Override
//	public void onBackPressed() {
//		// TODO 自動生成されたメソッド・スタブ
//		super.onBackPressed();
//	}




	@Override
	public void onDestroy() {
		// TODO 自動生成されたメソッド・スタブ
		super.onDestroy();
		cancelTuusin();
	}



	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
//		if(mHeallinView!=null){
//			mHeallinView.startHeallin();
//		}
	}

	@Override
	public void onStart() {
		super.onStart();

		if(mIsFirst){
			mIsFirst = false;

			//attemptGetProjectorList();

		}else{
			
			//updateMapListView(list);
			
		}

		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				mPullToRefreshListView.setRefreshing(true);
			}
		},250);

	}

	@Override
	public void onStop() {
		super.onStop();
		cancelTuusin();
	}




//	/**
//	 * doBack, Progress, postExecute
//	 */
//	private class GetSuggestTask extends AsyncTask<String , Void, Boolean> {
//
//		List<String> mmResult;
//
//		@Override
//		protected Boolean doInBackground(String... params) {
//			// TODO: attempt authentication against a network service.
//			//postToast("更新中...");
//			List<String> result = mKeywordManager.suggestKeywords(params[0]);
//			mmResult = result;
//			return result!=null;
//		}
//
//		@Override
//		protected void onPostExecute(final Boolean success) {
//
//			if(mGetSuggestTask==null) return ;
//			mGetSuggestTask = null;
//			//showProgress(false);
//
//			if(success){
//				updateContoributeSpinner(mmResult);
//			}
//		}
//
//		@Override
//		protected void onCancelled() {
//			mGetSuggestTask = null;
//			//showProgress(false);
//		}
//	}



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
	private void attemptSelectProjectTask(ProjectInfo p){
		if(mSelectProjectTask!=null){
			return ;
		}

		mSelectProjectTask = new SelectProjectTask();
		mSelectProjectTask.execute(p);

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
			
			mPullToRefreshListView.onRefreshComplete();

			if(success){

				CFUtil.Log("プロジェクトリスト更新しました。");

				updateMapListView(mmResult);

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
	private class SelectProjectTask extends AsyncTask<ProjectInfo , Void, Boolean> {

		ProjectInfo mPro;
		boolean mResult;

		@Override
		protected Boolean doInBackground(ProjectInfo... params) {
			// TODO: attempt authentication against a network service.
			//postToast("更新中...");
			mPro = params[0];
			LoginManager lm = mApp.getLoginManager();
			LoginInfo li = mApp.getLoginInfo();
			mResult  = lm.selectProject(li, mPro.project_id);

			return mResult;
		}

		@Override
		protected void onPostExecute(final Boolean success) {

			//showProgress(false);

			if(success){

				CFUtil.Log("プロジェクトを選択しました。");

//				postToast("更新しましした。");

				mApp.setSelectMap(mPro);

				if(getActivity() instanceof MainActivity){
					MainActivity ma = (MainActivity)getActivity();
					ma.selectMap();
					ma.closeDrawer();
				}
				//mScreenFragment.startWebView();

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




	private class MapListAdapter extends CFCardUIAdapter<ProjectInfo>{

		public MapListAdapter(Context context) {
			super(context);

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			if(convertView==null){
				view = mLayoutInflater.inflate(R.layout.brestolist_grid_item, null);
			}else{
				view = convertView;
			}

			ProjectInfo item = getItem(position);

			ImageView icon = (ImageView) view.findViewById(R.id.icon);
			TextView text1 = (TextView) view.findViewById(R.id.text_1);
			TextView text2 = (TextView) view.findViewById(R.id.text_2);

			text1.setText(""+item.project_name);
			text2.setText( ""+item.project_id );

			if( isCardMotion(position) ){
//				int rand = (int)(Math.random() * 1000) % 4;
//				switch(rand){
//				case 0: doCardMotion(view, R.anim.card_ui_motion_up); break;
//				case 1: doCardMotion(view, R.anim.card_ui_motion_down); break;
//				case 2: doCardMotion(view, R.anim.card_ui_motion_from_right); break;
//				case 3: doCardMotion(view, R.anim.card_ui_motion_from_left); break;
//				}
				doCardMotion(view, R.anim.card_ui_motion_from_right);
				setCardMotion(position, false);
			}

			return view;
		}

	}














	private Toast mToast;
	private void toast(String str){
		if(mToast==null){
			mToast = Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT);
		}else{
			mToast.setText(str);
		}
		mToast.show();
	}
	private void postToast(final String str){
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				toast(str);
			}
		});
	}


}
