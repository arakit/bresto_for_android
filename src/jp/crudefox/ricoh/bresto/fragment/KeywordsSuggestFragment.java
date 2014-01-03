package jp.crudefox.ricoh.bresto.fragment;



import java.util.ArrayList;
import java.util.List;

import jp.crudefox.ricoh.bresto.Const;
import jp.crudefox.ricoh.bresto.R;
import jp.crudefox.ricoh.bresto.R.anim;
import jp.crudefox.ricoh.bresto.R.id;
import jp.crudefox.ricoh.bresto.R.layout;
import jp.crudefox.ricoh.bresto.chikara.manager.KeywordManager;
import jp.crudefox.ricoh.bresto.chikara.manager.KeywordManager.Keyword;
import jp.crudefox.ricoh.bresto.chikara.manager.LoginInfo;
import jp.crudefox.tunacan.chikara.util.CFCardUIAdapter;
import jp.crudefox.tunacan.chikara.util.CFUtil;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;


/**
 * 		@Auth Chikara Funabashi
 * 		@Date 2013/08/10
 *
 */

/**
 *
 *
 */

public class KeywordsSuggestFragment extends SherlockFragment{



	private Context mContext;
	private View mRootView;

	private Handler mHandler = new Handler();



	private Button mContributeOKBtn;


//	private final ArrayList<Keyword> mSearchList = new ArrayList<Keyword>();
//	private AdapterBridge<Keyword> mSearchListBridge;
//	private Spinner mSearchListSpinner;


	//--------------------

//	private AdapterBridge<String> mSuggestSpinnerBridge;
//	private ArrayAdapter<String> mSuggestSpinnerAdapter;
//	private Spinner mSuggestSpinnerView;
//
//	private AdapterBridge<Integer> mSuggestTextBridge;
//	private AutoCompleteTextView mSuggestTextView;


//	private final ArrayList<Keyword> mKeywordList = new ArrayList<Keyword>();


	private SuggestListAdapter mAdapter;

	private GridView mGridView;

	private EditText mEditText;			//サジェストの入力bックス
	private Button mBtnOK;				//サジェスト確定ボタン
	private TextView mSrcKeywordTextView;	//親のキーボードを選択するビューです。


	//--------------------

	private Keyword mSrcKeyword;

	private LayoutInflater mLayoutInflater;				
	//private DateFormat mDateFormat;

	//private GetSuggestTask mGetSuggestTask;
	private GetSuggestTask mGetSuggestTask;
	private ConnectKeywordTask mConnectKeywordTask;


	private LoginInfo mLoginInfo;

	
	//キーワードマネージャ
	private KeywordManager mKeywordManager;



//	private HeallinView mHeallinView;

	//private TextView mCommentTextView;

	//private DeleteContributeTask mDelTask;


	public KeywordsSuggestFragment() {
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


		setContentView(R.layout.fragment_suggest);

		mGridView = (GridView) findViewById(R.id.suggest_gridView);
		mEditText = (EditText) findViewById(R.id.suggest_edittext);
		mBtnOK = (Button) findViewById(R.id.suggest_btn_send);
		mSrcKeywordTextView = (TextView) findViewById(R.id.suggest_text_srckeyword);


		mAdapter = new SuggestListAdapter(mContext);

		mGridView.setAdapter(mAdapter);



//		updateContoributeSpinner(new ArrayList<String>());


//		{
//			Spinner spinner = mSuggestSpinnerView;
//			ArrayAdapter<String> ada_sr = mSuggestSpinnerAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item);
//			ada_sr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//			spinner.setAdapter(ada_sr);
//
////			AdapterBridge<Integer> bri = mSuggestSpinnerBridge = new AdapterBridge<Integer>();
////
////			for(int i=0;i<3;i++){
////				bri.addItem("item"+i, i);
//////				int sr = MainActivity._SAMPLE_RATE_LIST[i];
//////				if(CFSoundRecorder.isSupportedSampleRateMono16bit(sr)){
//////					mSampleRateBridge.addItem(String.format("%1$,3d Hz", sr), sr);
//////				}
////			}
////			String[] items = bri.getItemTextArray();
////			for(int i=0;i<items.length;i++) ada_sr.add(items[i]);
////			spinner.setAdapter(ada_sr);
////			spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
////				public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
//////					AppUtil.Log("mSamplerateSpinner.onItemSelected");
//////					int sample_rate = mSampleRateBridge.getItem(position);
//////					mActivity.setSampleRate(sample_rate);
//////					CFSettings.setRecordingSampleRate(getActivity().getApplicationContext(), mActivity.getSampleRate());
////				}
////				public void onNothingSelected(AdapterView<?> parent) {
////				}
////			});
//		}


		mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String item = mAdapter.getItem(position);
				mEditText.setText(item);
			}
		});

		mBtnOK.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String keyword = mEditText.getText().toString();
				if(TextUtils.isEmpty(keyword)) return ;

				attemptConnectKeyword( mSrcKeyword, keyword);
			}
		});




		//mListView  = (CFOverScrolledListView) findViewById(R.id.member_frends_listView);


		//mCommentTextView = (TextView) findViewById(R.id.heallin_room_comment);


		//\ViewGroup heallin_container = (ViewGroup) findViewById(R.id.screen_container);


		//int wc = ViewGroup.LayoutParams.WRAP_CONTENT;
//		int mp = ViewGroup.LayoutParams.MATCH_PARENT;
//
//		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(mp, mp);
//		lp.gravity = Gravity.CENTER;
//		mHeallinView = new HeallinView(mContext, null);
//		mHeallinView.setLayoutParams(lp);
//
//
//		heallin_container.addView(mHeallinView);


		//mCommentTextView.setText("");


		Bundle bundle = getArguments();

		//Intent intent = getIntent();
		mLoginInfo = (LoginInfo) bundle.getSerializable(Const.AK_LOGIN_INFO);

		mSrcKeyword = (Keyword) bundle.getSerializable(Const.AK_KEYWORD);

//		if(CFUtil.isOk_SDK(9)){
//			mListView.setOverscrollHeader(
//					getResources().getDrawable(R.drawable.update_over_scrolled));
//		}


		//mListView.setAdapter(mAdapter);

		if(mLoginInfo!=null || mSrcKeyword == null){
			//CFUtil.Log("length = "+mTLManager.getItemLength());
//			if(mMemberManager.getItemLength()==0){
//				postAttemptGetBorad(250);
//			}
		}else{
			toast("ログインに失敗しています。");
			//finish();
		}

//		mListView.setOnOverScrolledListener(mOverScrolledListener);
//
//		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//				MItem mitem = mMemberManager.getItemByIndex(position);
//				if(mitem==null) return ;
//				showFrendsDialog(mitem);
//
//			}
//		});

		mSrcKeywordTextView.setText(mSrcKeyword.keyword);

		updateSuggestView(new ArrayList<String>());

		attemptGetSuggestList(mSrcKeyword.keyword);

		return mRootView;

	}

	private void addSuggest(String s){
		mAdapter.addItem(s, 0);
		mAdapter.notifyDataSetChanged();
	}

	private void updateSuggestView(List<String> list){


		mAdapter.clearItems();

		for(int i=0; i<list.size(); i++){
			String item = list.get(i);
			mAdapter.addItem(item, 0);
		}

		mAdapter.notifyDataSetChanged();


//		Spinner spinner = mSearchListSpinner;
//		ArrayAdapter<String> ada_sr = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item);
//		ada_sr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		final AdapterBridge<Keyword> bri = mSearchListBridge = new AdapterBridge<Keyword>();
//		final Keyword[] data_list = new Keyword[mSearchList.size()];
//
//		spinner.setOnItemSelectedListener(null);
//
//		for(int i=0;i<mSearchList.size();i++){
//			data_list[i] = mSearchList.get(i);
//			bri.addItem( mSearchList.get(i).keyword , mSearchList.get(i));
////			int sr = MainActivity._SAMPLE_RATE_LIST[i];
////			if(CFSoundRecorder.isSupportedSampleRateMono16bit(sr)){
////				mSampleRateBridge.addItem(String.format("%1$,3d Hz", sr), sr);
////			}
//		}
//		String[] items = bri.getItemTextArray();
//		for(int i=0;i<items.length;i++) ada_sr.add(items[i]);
//		spinner.setAdapter(ada_sr);
//		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
////				AppUtil.Log("mSamplerateSpinner.onItemSelected");
////				int sample_rate = mSampleRateBridge.getItem(position);
////				mActivity.setSampleRate(sample_rate);
////				CFSettings.setRecordingSampleRate(getActivity().getApplicationContext(), mActivity.getSampleRate());
//
//				Keyword sug = bri.getItem(position);
//				attemptGetSuggest(sug.keyword);
//			}
//			public void onNothingSelected(AdapterView<?> parent) {
//			}
//		});
	}





	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();

		mLayoutInflater = getLayoutInflater();
		//mDateFormat = DateFormat.getInstance();

		mKeywordManager = new KeywordManager(mContext, "p1");


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


//	private void postAttemptGetBorad(long delayed){
//		mHandler.postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				Activity activity = getActivity();
//				if(activity==null) return ;
//				if(activity.isFinishing()) return ;
//				attemptGetBorad();
//			}
//		}, delayed);
//	}

//	private void attemptGetSuggest(String str){
//		if(mGetSuggestTask!=null){
//			return ;
//		}
//
//		mGetSuggestTask = new GetSuggestTask();
//		mGetSuggestTask.execute(str);
//
//	}

	private void attemptGetSuggestList(String srckeywrod){

		if(mGetSuggestTask!=null){
			return ;
		}

		mGetSuggestTask = new GetSuggestTask();
		mGetSuggestTask.execute(srckeywrod);

	}


	private void attemptConnectKeyword(Keyword k1, String str){

		if(mConnectKeywordTask!=null){
			return ;
		}

		mConnectKeywordTask = new ConnectKeywordTask();
		mConnectKeywordTask.execute(k1, str);

	}








	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		//super.onCreateOptionsMenu(menu, inflater);

		MenuItem mi;
		int order = 1;

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
	}

	@Override
	public void onStop() {
		super.onStop();
		cancelTuusin();
	}

	private void cancelTuusin(){
		cancelGetSuggest();
		//cancelGetKeywordList();
		//cancelConnectKeywordTask();
	}

	private void cancelGetSuggest(){
		if(mGetSuggestTask!=null){
			mGetSuggestTask.cancel(true);
		}
	}
//	private void cancelGetKeywordList(){
//		if(mGetSuggestListTask!=null){
//			mGetSuggestListTask.cancel(true);
//		}
//	}
	private void cancelConnectKeywordTask(){
		if(mConnectKeywordTask!=null){
			mConnectKeywordTask.cancel(true);
		}
	}


	/**
	 * doBack, Progress, postExecute
	 */
	private class GetSuggestTask extends AsyncTask<String , Void, Boolean> {

		List<String> mmResult;

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO: attempt authentication against a network service.
			//postToast("更新中...");
			List<String> result = mKeywordManager.suggestKeywords(params[0]);
			mmResult = result;
			return result!=null;
		}

		@Override
		protected void onPostExecute(final Boolean success) {

			if(mGetSuggestTask==null) return ;
			mGetSuggestTask = null;
			//showProgress(false);

			if(success){
				updateSuggestView(mmResult);
			}
		}

		@Override
		protected void onCancelled() {
			mGetSuggestTask = null;
			//showProgress(false);
		}
	}

//	/**
//	 * doBack, Progress, postExecute
//	 */
//	private class GetSuggestListTask extends AsyncTask<Void , Void, Boolean> {
//
//		List<Keyword> mmResult;
//
//		@Override
//		protected Boolean doInBackground(Void... params) {
//			// TODO: attempt authentication against a network service.
//			//postToast("更新中...");
//			List<Keyword> result = mKeywordManager.getKeywords(mLoginInfo);
//			mmResult = result;
//			return result!=null;
//		}
//
//		@Override
//		protected void onPostExecute(final Boolean success) {
//
//			if(mGetSuggestListTask==null) return ;
//			mGetSuggestListTask = null;
//			//showProgress(false);
//
//			if(success){
//				postToast("取得しました。");
//
////				mSearchList.clear();
////				for(int i=0;i<mmResult.size();i++){
////					Keyword k = mmResult.get(i);
////					mSearchList.add(k);
////				}
////
////				updateSearch();
//
//				updateKeywrodsView(mmResult);
//			}else{
//				postToast("取得失敗しました。");
//			}
//		}
//
//		@Override
//		protected void onCancelled() {
//			mGetSuggestListTask = null;
//			//showProgress(false);
//		}
//	}

	/**
	 * doBack, Progress, postExecute
	 */
	private class ConnectKeywordTask extends AsyncTask<Object , Void, Boolean> {

		List<Keyword> mmResult;

		@Override
		protected Boolean doInBackground(Object... params) {
			// TODO: attempt authentication against a network service.
			//postToast("更新中...");
			List<Keyword> result = mKeywordManager.connectKeyword(mLoginInfo, (Keyword)params[0], (String)params[1]);
			mmResult = result;
			return result!=null;
		}

		@Override
		protected void onPostExecute(final Boolean success) {

			if(mConnectKeywordTask==null) return ;
			mConnectKeywordTask = null;
			//showProgress(false);

			if(success){
				postToast("成功");

				FragmentManager manager = getFragmentManager();
				manager.popBackStack();

//				manager.beginTransaction()
//					   .p
//					   .remove(KeywordsSuggestFragment.this)
////				       .addToBackStack("KeywordsSuggestFragment")
////				       .replace(android.R.id.content, f)
//				       .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
//				       .commit();

//				for(int i=1;i<mmResult.size();i++){
//					Keyword k = mmResult.get(i);
//					mSearchList.add(0, k);
//				}
//
//				updateSearch();


			}else{
				postToast("失敗");
			}
		}

		@Override
		protected void onCancelled() {
			mConnectKeywordTask = null;
			//showProgress(false);
		}
	}





//	private final BaseAdapter mAdapter = new BaseAdapter() {
//
//		public View getView(int position, View convertView, ViewGroup parent) {
//			View line = convertView;
//			if(line==null){
//				line =  mLayoutInflater.inflate(R.layout.member_frends_list_item, null);
//			}
//
//			MItem mitem = mMemberManager.getItemByIndex(position);
//
//			//ListData data = mListData.get(position);
//
//			TextView text_name = (TextView) line.findViewById(R.id.member_frends_list_name);
//			TextView text_id = (TextView) line.findViewById(R.id.member_frends_list_id);
//			ImageView icon_icon = (ImageView) line.findViewById(R.id.member_frends_list_icon);
//
//			text_name.setText(mitem.name);
//			text_id.setText(mitem.id);
//
//			icon_icon.setImageBitmap(mitem.icon);
//
//			return line;
//		}
//
//		public long getItemId(int position) {
//			return position;
//		}
//
//		public Object getItem(int position) {
//			return mMemberManager.getItemByIndex(position);
//		}
//
//		public int getCount() {
//			return mMemberManager.getItemLength();
//		}
//	};


	private class SuggestListAdapter extends CFCardUIAdapter<String>{

		public SuggestListAdapter(Context context) {
			super(context);

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			if(convertView==null){
				view = mLayoutInflater.inflate(R.layout.keyword_grid_item, null);
			}else{
				view = convertView;
			}

			String item = getItem(position);

			ImageView icon = (ImageView) view.findViewById(R.id.icon);
			TextView text1 = (TextView) view.findViewById(R.id.text_1);
			TextView text2 = (TextView) view.findViewById(R.id.text_2);

			text1.setText(""+item);
			text2.setText("");

			if( isCardMotion(position) ){
//				int rand = (int)(Math.random() * 1000) % 2;
				switch(position%2){
//				case 0: doCardMotion(view, R.anim.card_ui_motion_up); break;
//				case 1: doCardMotion(view, R.anim.card_ui_motion_down); break;
				case 0: doCardMotion(view, R.anim.card_ui_motion_from_right); break;
				case 1: doCardMotion(view, R.anim.card_ui_motion_from_left); break;
				}
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
