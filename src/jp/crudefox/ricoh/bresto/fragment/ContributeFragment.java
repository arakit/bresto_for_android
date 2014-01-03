package jp.crudefox.ricoh.bresto.fragment;



import java.util.ArrayList;
import java.util.List;

import jp.crudefox.ricoh.bresto.Const;
import jp.crudefox.ricoh.bresto.R;
import jp.crudefox.ricoh.bresto.R.drawable;
import jp.crudefox.ricoh.bresto.R.id;
import jp.crudefox.ricoh.bresto.R.layout;
import jp.crudefox.ricoh.bresto.chikara.manager.KeywordManager;
import jp.crudefox.ricoh.bresto.chikara.manager.KeywordManager.Keyword;
import jp.crudefox.ricoh.bresto.chikara.manager.LoginInfo;
import jp.crudefox.tunacan.chikara.util.AdapterBridge;
import jp.crudefox.tunacan.chikara.util.CFUtil;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
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
 *このクラスは使用しない
 *
 */

public class ContributeFragment extends SherlockFragment{







	private Context mContext;
	private View mRootView;

	private Handler mHandler = new Handler();



	private Button mContributeOKBtn;


	private final ArrayList<Keyword> mSearchList = new ArrayList<Keyword>();
	private AdapterBridge<Keyword> mSearchListBridge;
	private Spinner mSearchListSpinner;


	//--------------------

	private AdapterBridge<String> mSuggestSpinnerBridge;
	private ArrayAdapter<String> mSuggestSpinnerAdapter;
	private Spinner mSuggestSpinnerView;

	private AdapterBridge<Integer> mSuggestTextBridge;
	private AutoCompleteTextView mSuggestTextView;



	//--------------------


	private LayoutInflater mLayoutInflater;
	//private DateFormat mDateFormat;

	private GetSuggestTask mGetSuggestTask;
	private GetKeywordListTask mGetKeywordListTask;
	private ConnectKeywordTask mConnectKeywordTask;


	private LoginInfo mLoginInfo;

	private KeywordManager mKeywordManager;



//	private HeallinView mHeallinView;

	//private TextView mCommentTextView;

	//private DeleteContributeTask mDelTask;


	public ContributeFragment() {
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


		setContentView(R.layout.fragment_contribute);

		mSearchListSpinner =(Spinner) findViewById(R.id.contribute_spinner_search);
		mSuggestSpinnerView = (Spinner) findViewById(R.id.contribute_spinner_input);
		mSuggestTextView = (AutoCompleteTextView) findViewById(R.id.contribute_text_input);

		mContributeOKBtn = (Button) findViewById(R.id.contribute_btn_send);





		updateSearch();

		updateContoributeSpinner(new ArrayList<String>());


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


		{
			final String[] COUNTRIES = new String[] {
			      "Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra",
			      "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina",
			      "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan",
			      "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium"};

			ArrayAdapter<String> ada_sr = new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line);
			//ada_sr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			AdapterBridge<Integer> bri = mSuggestTextBridge = new AdapterBridge<Integer>();
			for(int i=0;i<COUNTRIES.length;i++){
				bri.addItem(COUNTRIES[i], i);
			}

			String[] items = bri.getItemTextArray();
			for(int i=0;i<items.length;i++) ada_sr.add(items[i]);
			mSuggestTextView.setAdapter( ada_sr );
			mSuggestTextView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {

				}
				public void onNothingSelected(AdapterView<?> parent) {
				}
			});

			mSuggestTextView.setCompletionHint("どれですか？");
			mSuggestTextView.setThreshold(1);

		}

		mSuggestTextView.setVisibility(View.GONE);

		mContributeOKBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				int position1 = mSearchListSpinner.getSelectedItemPosition();
				Keyword k1  = mSearchListBridge.getItem(position1);

				int position2 = mSuggestSpinnerView.getSelectedItemPosition();
				String k2_s = mSuggestSpinnerBridge.getItem(position2);

				if( k2_s == null || k1 == null ) return ;

				attemptConnectKeyword(k1, k2_s);
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

//		if(CFUtil.isOk_SDK(9)){
//			mListView.setOverscrollHeader(
//					getResources().getDrawable(R.drawable.update_over_scrolled));
//		}


		//mListView.setAdapter(mAdapter);

		if(mLoginInfo!=null){
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

		attemptGetKeywordList();

		return mRootView;

	}


	private void updateSearch(){

		Spinner spinner = mSearchListSpinner;
		ArrayAdapter<String> ada_sr = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item);
		ada_sr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		final AdapterBridge<Keyword> bri = mSearchListBridge = new AdapterBridge<Keyword>();
		final Keyword[] data_list = new Keyword[mSearchList.size()];

		spinner.setOnItemSelectedListener(null);

		for(int i=0;i<mSearchList.size();i++){
			data_list[i] = mSearchList.get(i);
			bri.addItem( mSearchList.get(i).keyword , mSearchList.get(i));
//			int sr = MainActivity._SAMPLE_RATE_LIST[i];
//			if(CFSoundRecorder.isSupportedSampleRateMono16bit(sr)){
//				mSampleRateBridge.addItem(String.format("%1$,3d Hz", sr), sr);
//			}
		}
		String[] items = bri.getItemTextArray();
		for(int i=0;i<items.length;i++) ada_sr.add(items[i]);
		spinner.setAdapter(ada_sr);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
//				AppUtil.Log("mSamplerateSpinner.onItemSelected");
//				int sample_rate = mSampleRateBridge.getItem(position);
//				mActivity.setSampleRate(sample_rate);
//				CFSettings.setRecordingSampleRate(getActivity().getApplicationContext(), mActivity.getSampleRate());

				Keyword sug = bri.getItem(position);
				attemptGetSuggest(sug.keyword);
			}
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}

	private void updateContoributeSpinner(final List<String> suggest){

		//if(suggest==null) suggest = new ArrayList<String>();

		{
			Spinner spinner = mSuggestSpinnerView;
			ArrayAdapter<String> ada_sr = mSuggestSpinnerAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item);
			ada_sr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			final AdapterBridge<String> bri = mSuggestSpinnerBridge = new AdapterBridge<String>();
			final String[] sug_list = new String[suggest.size()+1];

			spinner.setOnItemSelectedListener(null);

			bri.addItem("さぁ、どれにしちゃう？", null);

			for(int i=0;i<suggest.size();i++){
				sug_list[i] = suggest.get(i);
				bri.addItem(suggest.get(i), suggest.get(i));
			}
			String[] items = bri.getItemTextArray();
			for(int i=0;i<items.length;i++) ada_sr.add(items[i]);
			spinner.setAdapter(ada_sr);
			spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
//					AppUtil.Log("mSamplerateSpinner.onItemSelected");
//					int sample_rate = mSampleRateBridge.getItem(position);
//					mActivity.setSampleRate(sample_rate);
//					CFSettings.setRecordingSampleRate(getActivity().getApplicationContext(), mActivity.getSampleRate());

					String sug = bri.getItem(position);
					if(sug==null) return ;

					//コネクト
//					String sug = sug_list[index];
//
//					mSearchList.add(0, sug);
//					updateSearch();
				}
				public void onNothingSelected(AdapterView<?> parent) {
				}
			});

			spinner.setSelection(0);

		}
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

	private void attemptGetSuggest(String str){
		if(mGetSuggestTask!=null){
			return ;
		}

		mGetSuggestTask = new GetSuggestTask();
		mGetSuggestTask.execute(str);

	}

	private void attemptGetKeywordList(){
		if(mGetKeywordListTask!=null){
			return ;
		}

		mGetKeywordListTask = new GetKeywordListTask();
		mGetKeywordListTask.execute((Void)null);

	}


	private void attemptConnectKeyword(Keyword k1, String str){

		if(mConnectKeywordTask!=null){
			return ;
		}

		mConnectKeywordTask = new ConnectKeywordTask();
		mConnectKeywordTask.execute(k1, str);

	}





	private class HeallinView extends View{

		public static final int FRAME_TIME = 50;
		public static final float FRAME_SEC_F = FRAME_TIME / 1000.0f;

		Handler mmHandler = new Handler();

		Bitmap mmChara;
		float mDesnsity;

		boolean mmIsStarted;
		boolean mmIsInitialized = false;


		public HeallinView(Context context, AttributeSet attrs) {
			super(context, attrs);

			Resources res = getResources();
			mDesnsity = res.getDisplayMetrics().density;

			mmChara = ((BitmapDrawable) res.getDrawable(R.drawable.chara_01)).getBitmap();


		}

		public void initHeallin(){
			int width = getWidth();
			int height = getHeight();


		}

		public void startHeallin(){
			if(mmIsStarted) return ;
			mmIsStarted = true;

			mmHandler.postDelayed(mmDrawRunnable, FRAME_TIME);
		}

		public void stopHeallin(){
			if(!mmIsStarted) return ;
			mmIsStarted = false;

			mmHandler.removeCallbacks(mmDrawRunnable);
		}

		private void tickHeallin(){
			if(!mmIsInitialized) return ;

			int width = getWidth();





		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);

			if(!mmIsInitialized){
				mmIsInitialized = true;
				initHeallin();
			}

			int width = getWidth();
			int height = getHeight();
			int cx = width/2;
			int cy = height/2;
			float sd = mDesnsity;

//			Bitmap ch = mmChara;
//			Rect s_rc = new Rect(0,0,ch.getWidth(),ch.getHeight());
//			Rect d_rc = new Rect(0,0,(int)(200*sd),(int)(200*sd));
//
//			long time = SystemClock.uptimeMillis();
//
//			float bb;
//			if(mmWalk>=750){
//				bb = -1.0f + (mmWalk-750)/250.0f;
//
//			}else if(mmWalk>=500){
//				bb = - (mmWalk-500)/250.0f;
//
//			}else if(mmWalk>=250){
//				bb = 1.0f - (mmWalk-250)/250.0f;
//
//			}else{
//				bb = (mmWalk-0)/250.0f;
//
//			}

			canvas.drawARGB(255, 128,128,255);

			canvas.drawCircle(cx, cy, 200, null);

			//float aa = mm;//( (time%3000)/3000.0f ) ;
//
//			canvas.save();
//			canvas.translate(mmHealPt.x-s_rc.width()/2, mmHealPt.y-s_rc.height()/2);
//			//canvas.translate( cx-s_rc.centerX(), cy-s_rc.centerY() );
//			canvas.rotate((bb)*10, s_rc.centerX(), s_rc.centerY());
//
//			canvas.drawBitmap(mmChara, s_rc, d_rc, null);
//
//			canvas.restore();

		}


		Runnable mmDrawRunnable = new Runnable() {

			@Override
			public void run() {
				if(!mmIsStarted) return ;
				HeallinView.this.tickHeallin();
				HeallinView.this.invalidate();
				mmHandler.postDelayed(mmDrawRunnable, FRAME_TIME);
			}
		};

		@Override
		public boolean onTouchEvent(MotionEvent event) {

			int action = event.getActionMasked();

			if(action == MotionEvent.ACTION_DOWN){
				//mCommentTextView.setText("ダイエットするリン！");
			}

			return super.onTouchEvent(event);
		}



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
		cancelGetSuggest();
//		if(mHeallinView!=null){
//			mHeallinView.stopHeallin();
//		}
	}



	@Override
	public void onPause() {
		super.onPause();
//		if(mHeallinView!=null){
//			mHeallinView.stopHeallin();
//		}
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
		cancelGetKeywordList();
		cancelConnectKeywordTask();
	}

	private void cancelGetSuggest(){
		if(mGetSuggestTask!=null){
			mGetSuggestTask.cancel(true);
		}
	}
	private void cancelGetKeywordList(){
		if(mGetSuggestTask!=null){
			mGetSuggestTask.cancel(true);
		}
	}
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
				updateContoributeSpinner(mmResult);
			}
		}

		@Override
		protected void onCancelled() {
			mGetSuggestTask = null;
			//showProgress(false);
		}
	}

	/**
	 * doBack, Progress, postExecute
	 */
	private class GetKeywordListTask extends AsyncTask<Void , Void, Boolean> {

		List<Keyword> mmResult;

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.
			//postToast("更新中...");
			List<Keyword> result = mKeywordManager.getKeywords(mLoginInfo);
			mmResult = result;
			return result!=null;
		}

		@Override
		protected void onPostExecute(final Boolean success) {

			if(mGetKeywordListTask==null) return ;
			mGetKeywordListTask = null;
			//showProgress(false);

			if(success){
				postToast("取得しました。");

				mSearchList.clear();
				for(int i=0;i<mmResult.size();i++){
					Keyword k = mmResult.get(i);
					mSearchList.add(k);
				}

				updateSearch();
			}else{
				postToast("取得失敗しました。");
			}
		}

		@Override
		protected void onCancelled() {
			mGetKeywordListTask = null;
			//showProgress(false);
		}
	}

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

				for(int i=1;i<mmResult.size();i++){
					Keyword k = mmResult.get(i);
					mSearchList.add(0, k);
				}

				updateSearch();
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
