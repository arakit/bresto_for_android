package jp.crudefox.ricoh.bresto.fragment;

import java.net.URI;
import java.net.URISyntaxException;

import jp.crudefox.ricoh.bresto.AppManager;
import jp.crudefox.ricoh.bresto.GraphicsThread;
import jp.crudefox.ricoh.bresto.R;
import jp.crudefox.ricoh.bresto.chikara.manager.CFConst;
import jp.crudefox.ricoh.bresto.chikara.manager.LoginInfo;
import jp.crudefox.tunacan.chikara.util.CFUtil;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Picture;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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

@SuppressLint("SetJavaScriptEnabled")
public class ScreenFragment extends SherlockFragment{



	private Context mContext;
	private View mRootView;

	private Handler mHandler = new Handler();

	private AppManager mApp;
	private LoginInfo mLoginInfo;

	private LayoutInflater mLayoutInflater;
	//private DateFormat mDateFormat;

	private GetMemberTask mGetMemberTask;

	private ScreenThread mScreenThread;






	private DrawView mHeallinView;
	private WebView mWebView;


	private View mBtn1;
	private View mBtn2;

	//private TextView mCommentTextView;

	//private DeleteContributeTask mDelTask;

	private Bitmap mBmp;


	private WebSocketClient mClient;





	public ScreenFragment() {
		super();
		setHasOptionsMenu(true);
	}





	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}


	@SuppressLint({ "NewApi", "JavascriptInterface" })
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
		mWebView = (WebView) findViewById(R.id.webView);


		//ViewGroup heallin_container = (ViewGroup) findViewById(R.id.screen_container);


		//int wc = ViewGroup.LayoutParams.WRAP_CONTENT;
		int mp = ViewGroup.LayoutParams.MATCH_PARENT;

		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(mp, mp);
		lp.gravity = Gravity.CENTER;
		mHeallinView = new DrawView(mContext, null);
		mHeallinView.setLayoutParams(lp);


		//heallin_container.addView(mHeallinView);


		mBtn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				startGraphics();
				startScreen();

				mHandler.postDelayed(new Runnable() {

					@Override
					public void run() {
//						Bitmap bmp = mBmp;
//						Canvas cv = new Canvas(bmp);
//
//						TextPaint tp = new TextPaint();
//						tp.setTextSize(19);
//						tp.setColor(Color.argb(255, 0,0,0));
//
//						//Picture pic = mWebView.capturePicture();
//
//						cv.drawARGB(255, 255, 255, 255);
//
//						//mWebView.draw(cv);
//
//						{
//							int srcw = mWebView.getWidth();
//							float scale = (float)bmp.getWidth() / (float)srcw;
//							cv.save();
//							cv.scale(scale, scale);
//							//pic.draw(cv);
//							//cv.drawBitmap(, src, dst, null);
//							mWebView.draw(cv);
//
//							cv.restore();
//						}
//
//						cv.save();
//
//						long time = System.currentTimeMillis();
//						int timem = 10*1000;
//						int degree = ((int)((time%timem)/(double)(timem) * 360));
//
//						cv.rotate(degree, bmp.getWidth()/2, bmp.getHeight()/2);
//						cv.drawText("映し出せ！！", 40, 100, tp);
//						cv.drawText("Androidより愛を込めて。", 40, 150, tp);
//
//						cv.restore();
//
//
//						CFUtil.Log("updateImageをします。");
//						mGraTh.updateImage(bmp);
//						//bmp.recycle();


					}
				}, 0);
			}
		});
		mBtn2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				stopScreen();
				stopGraphics();
			}
		});


		//mCommentTextView.setText("");


//		Bundle bundle = getArguments();


		mLoginInfo = mApp.getLoginInfo();

		//Intent intent = getIntent();
//		mLoginInfo = (LoginInfo) bundle.getSerializable(Const.AK_LOGIN_INFO);

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










		WebSettings settings =mWebView.getSettings();

		settings.setUserAgentString("bresto");

		settings.setJavaScriptEnabled(true);

		mWebView.setWebViewClient(new MyWebViewClient());
		mWebView.setWebChromeClient(new MyWebVieChrome());

		//mWebView.addJavascriptInterface(new WebSocketFactory(mWebView), "WebSocketFactory");

		//mWebView.loadUrl("https://google.com");

		//mWebView.loadUrl("http://www.websocket.org/echo.html");
		//mWebView.loadUrl("http://bresto.cloudapp.net:8080/BreStoServer0/");

		return mRootView;

	}



	public void startWebView(){

		LoginInfo li = mApp.getLoginInfo();
		if(li==null) return ;

		CookieSyncManager.createInstance(getApplicationContext());
        CookieSyncManager.getInstance().startSync();
        CookieManager.getInstance().setAcceptCookie(true);
        CookieManager.getInstance().removeExpiredCookie();

//		CookieStore cookieStr = httpClient.getCookieStore();
//        cookieStr.clearExpired(new java.util.Date());
//        List<Cookie> cookies = cookieStr.getCookies();
        CookieManager cookieManager = CookieManager.getInstance();
//        for (Cookie cookie : cookies) {
//            String cookieString = cookie.getName() + "=" + cookie.getValue();
//            Log.d(TAG, "cookieString="+cookieString);
//            cookieManager.setCookie("nicovideo.jp", cookieString);
//            CookieSyncManager.getInstance().sync();
//            System.out.println(cookie.getValue());
//            System.out.println(cookie.getDomain());
//            System.out.println("HOSI3");
//        }

        String cookieString = "JSESSIONID=" + li.getToken().getSessionID();
        cookieManager.setCookie(CFConst.DOMAIN, cookieString);
        CookieSyncManager.getInstance().sync();


		//mWebView.loadUrl("http://192.168.1.122/bresto/");
        mWebView.loadUrl(CFConst.SERVER+CFConst.ROOTDIR+"play.html");
	}


	private void initWebSocket(){

		 URI uri = null;
		try {
			uri = new URI("ws://192.168.1.117:8080/BreStoServer0/api/socket_node_edge");
		} catch (URISyntaxException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

        mClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake handshake) {
                //Log.d(TAG, "onOpen");
	           	 CFUtil.Log("onOpen");
	           	 Runnable r = new Runnable() {
					@Override
					public void run() {
			           	 String script = "javascript: fakeWebSocket.open();";
			           	 mWebView.loadUrl(script);
					}
				};
				mHandler.post(r);
            }

            @Override
            public void onMessage(final String message) {
           	 CFUtil.Log("onMessage "+message);
                //Log.d(TAG, "onMessage");
                //Log.d(TAG, "Message:" + message);
//                mHandler.post(new Runnable() {    // ----2
//                    @Override
//                    public void run() {
//                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
//                    }
//                });

	           	 Runnable r = new Runnable() {
					@Override
					public void run() {
			           	 String script = "javascript: var str = '"+ message +"'; fakeWebSocket.message(str);";
			           	 mWebView.loadUrl(script);
					}
				};
				mHandler.post(r);
            }

            @Override
            public void onError(Exception ex) {
                //Log.d(TAG, "onError");
                ex.printStackTrace();

                Runnable r = new Runnable() {
					@Override
					public void run() {
		              	 String script = "javascript: fakeWebSocket.error();";
		               	 mWebView.loadUrl(script);
					}
				};
 				mHandler.post(r);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
	                //Log.d(TAG, "onClose");
	           	 CFUtil.Log("onClose");

	           	 Runnable r = new Runnable() {
					@Override
					public void run() {
			           	 String script = "javascript: fakeWebSocket.close();";
			           	 mWebView.loadUrl(script);
					}
				};
				mHandler.post(r);
            }
        };

        mClient.connect();


	}


	private void destroyWebSocket(){

		if(mClient!=null){
			mClient.close();
		}

	}


	private class MyWebViewClient extends WebViewClient {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
//	        view.loadUrl(url);
//	        return true;
	    	return false;
	    }
	}
	private class MyWebVieChrome extends WebChromeClient{
		@Override
	    public boolean onConsoleMessage(ConsoleMessage cm){
	        CFUtil.Log(  cm.message() + "--From line " + cm.lineNumber() + " of " + cm.sourceId() );
	        return true;
	    }

		@Override
		@Deprecated
		public void onConsoleMessage(String message, int lineNumber, String sourceID) {
			// TODO 自動生成されたメソッド・スタブ
			super.onConsoleMessage(message, lineNumber, sourceID);
	        //CFUtil.Log(  cm.message() + "--From line " + cm.lineNumber() + " of " + cm.sourceId() );
		}

	}


	private GraphicsThread mGraTh;


	private void startGraphics(){
		if( mGraTh !=null ) return ;

		String ip = mApp.getSelectProjectorIp();
		if(TextUtils.isEmpty(ip)) return ;

		GraphicsThread t = mGraTh = new GraphicsThread(mBmp.getWidth(), mBmp.getHeight(), ip);
		t.start();

	}
	private void stopGraphics(){
		GraphicsThread t = mGraTh;
		if(t == null) return ;
		t.cancel();
		mGraTh = null;
	}

	private void startScreen(){
		if( mScreenThread !=null ) return ;

		ScreenThread t = mScreenThread = new ScreenThread();
		t.start();

		initWebSocket();
	}
	private void stopScreen(){
		ScreenThread t = mScreenThread;
		if(t == null) return ;
		t.cancel();
		mScreenThread = null;

		destroyWebSocket();
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();

		mLayoutInflater = getLayoutInflater();
		//mDateFormat = DateFormat.getInstance();
		mApp = (AppManager) getActivity().getApplication();

		mBmp = Bitmap.createBitmap(960, 960, Bitmap.Config.ARGB_4444);

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



	private class ScreenThread extends Thread {

		boolean mmIsCanceld = false;

		@Override
		public void run() {
			super.run();

			int roop_count = 0;
			while(!mmIsCanceld){

				//System.out.println("suggest roop_count = "+roop_count);

				Runnable r = new Runnable() {
					@Override
					public void run() {

						draw_label : {

						Bitmap bmp = mBmp;
						Canvas cv = new Canvas(bmp);

						TextPaint tp = new TextPaint();
						tp.setTextSize(19);
						tp.setColor(Color.argb(255, 0,0,0));


						cv.drawARGB(255, 255, 255, 255);

						//mWebView.draw(cv);

						{
							Picture pic = mWebView.capturePicture();
							int srcw = pic.getWidth();
							//int srcw = mWebView.getWidth();
							float scale = (float)bmp.getWidth() / (float)srcw;
							cv.save();
							cv.scale(scale, scale);
							pic.draw(cv);
							//cv.drawBitmap(, src, dst, null);
							//mWebView.ondraw(cv);
							//pic.draw(cv);

							cv.restore();
						}

//						cv.save();
	//
//						long time = System.currentTimeMillis();
//						int timem = 10*1000;
//						int degree = ((int)((time%timem)/(double)(timem) * 360));
	//
//						cv.rotate(degree, bmp.getWidth()/2, bmp.getHeight()/2);
//						cv.drawText("映し出せ！！", 40, 100, tp);
//						cv.drawText("Androidより愛を込めて。", 40, 150, tp);
	//
//						cv.restore();


						//CFUtil.Log("updateImageをします。");
						if(mGraTh!=null){
							mGraTh.updateImage(bmp);
						}
						//bmp.recycle();

					}


					}
				};

				mHandler.post(r);


				//sleep
				if(!Thread.interrupted()){
					try {
						sleep(200);
					} catch (Exception e) {
						//System.out.println("sleep interrput");
					}
				}

				roop_count++;

			}

			//System.out.println("end suggest thread.");

		}

		public void go(){
			if( !ScreenThread.this.isInterrupted() ){
				ScreenThread.this.interrupt();
			}
		}


		public void cancel(){
			if(mmIsCanceld) return ;
			System.out.println("cancel start");
			mmIsCanceld = true;
			ScreenThread.this.interrupt();
			try {
				ScreenThread.this.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("cancel end");
		}


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


		Runnable mmDrawRunnable = new Runnable() {

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

		mBmp.recycle();
		mBmp = null;
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

		stopScreen();
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
