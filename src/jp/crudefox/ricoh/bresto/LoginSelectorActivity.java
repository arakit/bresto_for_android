package jp.crudefox.ricoh.bresto;

import jp.crudefox.ricoh.bresto.chikara.manager.LoginInfo;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockActivity;

/**
 * 		@author  Chikara Funabashi
 * 		@first_date 2013/08/08
 *
 */

public class LoginSelectorActivity extends SherlockActivity {






	private static int REQCODE_LOGIN = 1001;
	//private static int REQCODE_LOGIN = 1001;
	private static int REQCODE_SIGN_UP = 1002;
//
//
//	private Handler mHandler;
//
//	//private boolean mIsFirst = true;
//
//
//	private static int S_MODE_NONE = 0;
//	private static int S_MODE_START = 1;
//	private static int S_MODE_LOGIN_OR_REGISTER = 2;
//	//private static int S_MODE_LOGIN_SUCCESS = 3;
//
//
//	int mMode = S_MODE_NONE;


	private Button mLoginBtn;
	private Button mRegisterBtn;


	private AppManager mApp;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_selector);

		mLoginBtn = (Button) findViewById(R.id.login_selector_login_btn);
		mRegisterBtn = (Button) findViewById(R.id.login_selector_register_btn);

		mApp = (AppManager) getApplication();


		mLoginBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startLogin();
			}
		});
		mRegisterBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startSignUp();
			}
		});



		if( mApp.isLoggedIn() ){
			startHome(mApp.getLoginInfo());
		}

	}



	private void startLogin(){
		Intent intent  = new Intent(LoginSelectorActivity.this, LoginActivity.class);
		//mMode = S_MODE_LOGIN_OR_REGISTER;
		startActivityForResult(intent, REQCODE_LOGIN);
		//startActivity(intent);
	}

	private void startSignUp(){
//		Intent intent  = new Intent(LoginSelectorActivity.this, SignUpActivity.class);
//		//mMode = S_MODE_LOGIN_OR_REGISTER;
//		startActivityForResult(intent, REQCODE_SIGN_UP);
//		//startActivity(intent);
	}

	private void startHome(LoginInfo li){
		Intent intent = new Intent(LoginSelectorActivity.this, MainActivity.class);
		intent.putExtra(Const.AK_LOGIN_INFO, li);

		//mMode = S_MODE_NONE;
		startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if(requestCode==REQCODE_LOGIN){
			if(resultCode==RESULT_OK){
				LoginInfo li = (LoginInfo) data.getSerializableExtra(Const.AK_LOGIN_INFO);

				startHome(li);
			}else{
				finish();
			}
		}

		else if(requestCode==REQCODE_SIGN_UP){
			if(resultCode==RESULT_OK){
				LoginInfo li = (LoginInfo) data.getSerializableExtra(Const.AK_LOGIN_INFO);

				startHome(li);
			}else{
				finish();
			}
		}



	}




//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.login_selector, menu);
//		return true;
//	}



}
