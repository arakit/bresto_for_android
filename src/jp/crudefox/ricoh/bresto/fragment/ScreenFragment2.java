package jp.crudefox.ricoh.bresto.fragment;

import jp.crudefox.ricoh.bresto.Const;
import jp.crudefox.ricoh.bresto.GraphicsThread;
import jp.crudefox.ricoh.bresto.R;
import jp.crudefox.ricoh.bresto.chikara.manager.LoginInfo;
import jp.crudefox.tunacan.chikara.util.CFUtil;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

/**
 * 		@author Chikara Funabashi
 * 		@date 2013/08/10
 *
 */

public class ScreenFragment2 extends SherlockFragment{



	private Context mContext;
	private View mRootView;

	private Handler mHandler = new Handler();


	private LayoutInflater mLayoutInflater;
	//private DateFormat mDateFormat;

	private GetMemberTask mGetMemberTask;

	private LoginInfo mLoginInfo;



	private DrawView mHeallinView;

	private View mBtn1;
	private View mBtn2;

	//private TextView mCommentTextView;

	//private DeleteContributeTask mDelTask;


	public ScreenFragment2() {
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


		setContentView(R.layout.fragment_screen);

		//mListView  = (CFOverScrolledListView) findViewById(R.id.member_frends_listView);


		//mCommentTextView = (TextView) findViewById(R.id.heallin_room_comment);

		mBtn1 = findViewById(R.id.btn_1);
		mBtn2 = findViewById(R.id.btn_2);


		ViewGroup heallin_container = (ViewGroup) findViewById(R.id.screen_container);


		//int wc = ViewGroup.LayoutParams.WRAP_CONTENT;
		int mp = ViewGroup.LayoutParams.MATCH_PARENT;

		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(mp, mp);
		lp.gravity = Gravity.CENTER;
		mHeallinView = new DrawView(mContext, null);
		mHeallinView.setLayoutParams(lp);


		heallin_container.addView(mHeallinView);


		mBtn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startGraphics();
				mHandler.postDelayed(new Runnable() {

					@Override
					public void run() {
						Bitmap bmp = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
						Canvas cv = new Canvas(bmp);

						TextPaint tp = new TextPaint();
						tp.setTextSize(20);
						tp.setColor(Color.argb(255, 0,0,0));

						cv.drawARGB(255, 255, 255, 255);
						cv.drawText("見えてくれ！！", 0, 100, tp);

						CFUtil.Log("updateImageをします。");
						mGraTh.updateImage(bmp);
						bmp.recycle();


					}
				}, 1000);
			}
		});
		mBtn2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				stopGraphics();
			}
		});


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

		return mRootView;

	}



	private GraphicsThread mGraTh;


	private void startGraphics(){
		if( mGraTh !=null ) return ;

		GraphicsThread t = mGraTh = new GraphicsThread(200, 200, "");
		t.start();

	}
	private void stopGraphics(){
		GraphicsThread t = mGraTh;
		if(mGraTh == null) return ;
		t.cancel();
		mGraTh = null;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();

		mLayoutInflater = getLayoutInflater();
		//mDateFormat = DateFormat.getInstance();


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


	private void postAttemptGetBorad(long delayed){
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				Activity activity = getActivity();
				if(activity==null) return ;
				if(activity.isFinishing()) return ;
				attemptGetBorad();
			}
		}, delayed);
	}

	private void attemptGetBorad(){
		if(mGetMemberTask!=null){
			return ;
		}

		mGetMemberTask = new GetMemberTask();
		mGetMemberTask.execute((Void)null);

	}





	private class DrawView extends View{

		public static final int FRAME_TIME = 50;
		public static final float FRAME_SEC_F = FRAME_TIME / 1000.0f;

		Handler mmHandler = new Handler();

		Bitmap mmChara;
		float mDesnsity;

		boolean mmIsStarted;
		boolean mmIsInitialized = false;


		public DrawView(Context context, AttributeSet attrs) {
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

//			canvas.drawCircle(cx, cy, 200, null);

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


		final Runnable mmDrawRunnable = new Runnable() {

			@Override
			public void run() {
				if(!mmIsStarted) return ;
				DrawView.this.tickHeallin();
				DrawView.this.invalidate();
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
		if(mGetMemberTask!=null){
			mGetMemberTask.cancel(true);
		}
		if(mHeallinView!=null){
			mHeallinView.stopHeallin();
		}
	}



	@Override
	public void onPause() {
		super.onPause();
		if(mHeallinView!=null){
			mHeallinView.stopHeallin();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if(mHeallinView!=null){
			mHeallinView.startHeallin();
		}
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
		if(mGetMemberTask!=null){
			mGetMemberTask.cancel(true);
		}

		stopGraphics();
	}




	/**
	 * doBack, Progress, postExecute
	 */
	private class GetMemberTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			postToast("更新中...");

			boolean result = true;//mMemberManager._update_mock(mLoginInfo);

			return result;

		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mGetMemberTask = null;
			//showProgress(false);

//			if(success){
//				toast("メンバー情報を更新しました。");
//				updateListView();
//			}else{
//				toast("メンバー情報を更新出来ませんでした\n(；´Д｀)");
//			}

		}

		@Override
		protected void onCancelled() {
			mGetMemberTask = null;
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
