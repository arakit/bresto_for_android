package jp.crudefox.ricoh.bresto;

import jp.crudefox.tunacan.chikara.util.CFUtil;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Menu;
import android.view.View;

public class StartActivity extends Activity {

	/*		Auth: Chikara Funabashi
	 * 		Date: 2013/08/08
	 *
	 */

	/*		一番最初の画面 スプラッシュウィンドウ
	 * 		ちょっとかっこよくｗｗ
	 *		ログインしてたら、そのままホームへ
	 */




	private static int REQCODE_LOGIN_OR_REGSTER = 1001;
	//private static int REQCODE_SIGN_UP = 1002;


	private Handler mHandler;

	//private boolean mIsFirst = true;


	private static int S_MODE_NONE = 0;
	private static int S_MODE_START = 1;
	private static int S_MODE_LOGIN_OR_REGISTER = 2;
	//private static int S_MODE_LOGIN_SUCCESS = 3;



	int mMode = S_MODE_NONE;


	SplachView mSplachView;


	private final Runnable mStartRunnuable = new Runnable() {
		@Override
		public void run() {
			Intent intent  = new Intent(StartActivity.this, LoginSelectorActivity.class);
			mMode = S_MODE_LOGIN_OR_REGISTER;
			startActivityForResult(intent, REQCODE_LOGIN_OR_REGSTER);
		}
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		mHandler = new Handler();
		//mIsFirst = true;
		mMode = S_MODE_START;

//		mSplachView = new SplachView(this);

//		FrameLayout container = (FrameLayout) findViewById(R.id.start_back_container);
//		container.addView(mSplachView,
//				new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT));

	}



	@Override
	protected void onStart() {
		super.onStart();

		if(mMode==S_MODE_NONE){
			finish();
		}
	}

	@Override
	protected void onRestart() {
		// TODO 自動生成されたメソッド・スタブ
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO 自動生成されたメソッド・スタブ
		super.onResume();

		if(mMode==S_MODE_START){
			mHandler.postDelayed(mStartRunnuable, 1000);
			//mHandler.postDelayed(mDrawRunnable, 1);
		}
	}

	@Override
	protected void onPause() {
		// TODO 自動生成されたメソッド・スタブ
		super.onPause();
		mHandler.removeCallbacks(mStartRunnuable);
		mHandler.removeCallbacks(mDrawRunnable);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO 自動生成されたメソッド・スタブ
		super.onDestroy();
	}



















	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

//		if(requestCode==REQCODE_LOGIN){
//			if(resultCode==RESULT_OK){
//				LoginInfo lf = (LoginInfo) data.getSerializableExtra(Const.AK_LOGIN_INFO);
//
//				Intent intent = new Intent(StartActivity.this, BoardActivity.class);
//				intent.putExtra(Const.AK_LOGIN_INFO, lf);
//
//				mMode = S_MODE_NONE;
//				startActivity(intent);
//			}else{
//				finish();
//			}
//		}

		if(requestCode==REQCODE_LOGIN_OR_REGSTER){
//			if(resultCode==RESULT_OK){
//				LoginInfo lf = (LoginInfo) data.getSerializableExtra(Const.AK_LOGIN_INFO);
//
//				Intent intent = new Intent(StartActivity.this, BoardActivity.class);
//				intent.putExtra(Const.AK_LOGIN_INFO, lf);
//
//				mMode = S_MODE_NONE;
//				startActivity(intent);
//			}else{
//				finish();
//			}
			mMode = S_MODE_NONE;
			finish();
		}

	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.start, menu);
		return true;
	}



	private class SplachView extends View{

		boolean mmIsInitialized = false;

		final Point[] mmPts = new Point[8];

		Paint mmPaint;

		public SplachView(Context context) {
			super(context);
			mmPaint = new Paint();
			mmPaint.setStyle(Paint.Style.STROKE);
		}

		private void init(){

			int width = getWidth();
			int height = getHeight();

			for(int i=0;i<mmPts.length;i++){
				Point pt = mmPts[i] = new Point();
				pt.x = (int)( Math.random() * width );
				pt.y = (int)( Math.random() * height );
			}


		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);

			if(!mmIsInitialized){
				mmIsInitialized = true;
				init();
			}

			CFUtil.Log("onDraw----------------------------------");

			int width = getWidth();
			int height = getHeight();


			long time = SystemClock.uptimeMillis();

			canvas.drawARGB(255, 255,255,200);

			for(int i=0;i<mmPts.length;i++){
				Point pt = mmPts[i];


				mmPaint.setColor(Color.argb(200, 255, 200, 120));

				mmPaint.setStrokeWidth(30);

				double prog = time/2000.0f + i/(float)mmPts.length;
				prog = prog % 1.0;

				float rx = (float)( Math.cos((double)(2*Math.PI*prog))  );
				float ry = (float)( Math.sin((double)(2*Math.PI*prog))  );

				if(i%2==0) rx = -rx;

//				canvas.drawLine(
//						pt.x-rx*width, pt.y-ry*height,
//						pt.x+rx*width, pt.y+ry*height, mmPaint);

				canvas.drawCircle(pt.x+rx*100, pt.y+ry*100, 100, mmPaint);

			}


		}




	}


	Runnable mDrawRunnable = new Runnable() {
		@Override
		public void run() {
			mSplachView.postInvalidate();
			if(!isFinishing())
				mHandler.postDelayed(mDrawRunnable, 80);
		}
	};



}
