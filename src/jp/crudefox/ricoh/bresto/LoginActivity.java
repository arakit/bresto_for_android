package jp.crudefox.ricoh.bresto;

import jp.crudefox.ricoh.bresto.chikara.manager.LoginInfo;
import jp.crudefox.ricoh.bresto.chikara.manager.LoginManager;
import jp.crudefox.tunacan.chikara.util.CFUtil;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {

	/*		Auth: Chikara Funabashi
	 * 		Date: 2013/08/08
	 *
	 */

	/*		ログイン画面
	 * 		元の画面にログイン情報を戻す
	 *
	 */


	//private static int REQCODE_SIGN_UP = 1002;


	private AppManager mApp;

	private UserLoginTask mAuthTask = null;

	private LoginManager mLoginManager = null;

	// Values for email and password at the time of the login attempt.
	private String mId;
	private String mPassword;

	// UI references.
	private EditText mIdView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
	private Button mLoginBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		mApp = (AppManager) getApplication();

		// Set up the login form.
		//mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
		mId = "user0";
		mPassword = "0000";
		//mName = "ヽ(=´▽`=)ﾉ";

		mIdView = (EditText) findViewById(R.id.login_login_id);
		mPasswordView = (EditText) findViewById(R.id.logon_login_password);
		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_login_status_message);
		mLoginBtn = (Button) findViewById(R.id.login_login_btn);

		mIdView.setText(mId);
		mPasswordView.setText(mPassword);

		mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});



		mLoginBtn.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});

//		mSignUpBtn.setOnClickListener(
//				new View.OnClickListener() {
//					@Override
//					public void onClick(View view) {
//						Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
//						startActivityForResult(i, REQCODE_SIGN_UP);
//					}
//				});

	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		super.onCreateOptionsMenu(menu);
//		getMenuInflater().inflate(R.menu.login, menu);
//		return true;
//	}



	@Override
	public void onBackPressed() {
		// TODO 自動生成されたメソッド・スタブ
		super.onBackPressed();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
		if(mAuthTask!=null){
			mAuthTask.cancel(true);
		}
	}



	/*
	 *
	 * ID　　：　スペース、記号、全角、8文字以上　不可
	 * 氏名 ：　スペース、記号、20文字以上　　　　不可
	 * 	PW　 ：　スペース、記号、全角、6文字以下　不可
	 *
	 */

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {

		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mIdView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mId = mIdView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;


		// Check for a valid email address.
		if (TextUtils.isEmpty(mId)) {
			mIdView.setError(getString(R.string.error_field_required));
			focusView = mIdView;
			cancel = true;
		}
		else if( mId.length() > 8 ) {
			mIdView.setError("8文字以下にしてください");
			focusView = mIdView;
			cancel = true;
		}
		else if( !CFUtil.isEisuuzi(mId) ) {
			mIdView.setError("半角英数字のみです！");
			focusView = mIdView;
			cancel = true;
		}


		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if ( mPassword.length() < 4 ) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}
		else if( !CFUtil.isEisuuzi(mPassword) ) {
			mPasswordView.setError("半角英数字のみです！");
			focusView = mPasswordView;
			cancel = true;
		}





		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mLoginManager = new LoginManager(getApplicationContext());
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}




	public class UserLoginTask extends AsyncTask<Void, Void, LoginInfo> {

		String mmErr = null;

		@Override
		protected LoginInfo doInBackground(Void... params) {


			//Toast.makeText(LoginActivity.this, "バックグラウンド処理中です！", Toast.LENGTH_SHORT).show();

			//通信処理
			String[] result = new String[]{null};
			LoginInfo li = mLoginManager.login(mId, mPassword, result);
			//LoginInfo li = mLoginManager._login_mock(mId, mPassword, result);

			if( li==null && result[0]!=null ){
				mmErr = result[0];
			}


			return li;
		}

		@Override
		protected void onPostExecute(final LoginInfo lf) {
			mAuthTask = null;
			showProgress(false);

			if (lf!=null) {

				mApp.setLoginInfo(lf);

				Intent data = new Intent();
				data.putExtra(Const.AK_LOGIN_INFO, lf);
				setResult(RESULT_OK, data);
				finish();
			} else {

				if(mmErr.equals(LoginManager.LOGIN_ERR_CONNECT)){
					toast("通信エラー");
				}else{
					mPasswordView
							.setError(getString(R.string.error_incorrect_password));
				}
				mPasswordView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}

	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

//		if(requestCode==REQCODE_SIGN_UP){
//			if(resultCode==RESULT_OK){
////				LoginInfo lf = (LoginInfo) data.getSerializableExtra(Const.AK_LOGIN_INFO);
////
////				Intent intent = new Intent(StartActivity.this, BoardActivity.class);
////				intent.putExtra(Const.AK_LOGIN_INFO, lf);
////
////				mMode = S_MODE_NONE;
////				startActivity(intent);
//
//				setResult(RESULT_OK, data);
//				finish();
//			}else{
//				//finish();
//			}
//		}

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

}
