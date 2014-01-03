package jp.crudefox.ricoh.bresto;


import jp.crudefox.ricoh.bresto.chikara.manager.LoginInfo;
import jp.crudefox.ricoh.bresto.chikara.manager.LoginManager;
import android.app.Application;


/**
*
* @author Chikara Funabashi.
*
*ログイン情報などの管理をするマネージャーを管理するアプリケーションコンテキスト
*
*/

public class AppManager extends Application{




	//ログイン情報
	private LoginInfo mLoginInfo;

	//ログインを管理する
	private LoginManager mLoginManger;

//
//	private MemberManager mMemberManager;
//
//	private ProfileManager mProfileManager;


	private String mSelectProjectorIp;


	@Override
	public void onCreate() {
		super.onCreate();
		//アクティビティ作成時に呼ばれる

		mLoginManger = new LoginManager(this);
//		mMemberManager = new MemberManager(this);
//		mProfileManager = new ProfileManager(this);

	}

	@Override
	public void onTerminate() {
		// TODO 自動生成されたメソッド・スタブ
		super.onTerminate();
	}

	public LoginInfo getLoginInfo(){
		return mLoginInfo;
	}

	public LoginManager getLoginManager(){
		return mLoginManger;
	}

//	public MemberManager getMemberManager(){
//		return mMemberManager;
//	}
//
//	public ProfileManager getProfileManager(){
//		return mProfileManager;
//	}




	public void setLoginInfo(LoginInfo info){
		mLoginInfo = info;
	}



	public boolean isLoggedIn(){
		if(mLoginInfo==null) return false;
		return mLoginInfo.isLoggedIn();
	}


	public void setSelectProjectorIp(String ip){
		mSelectProjectorIp = ip;
	}
	public String getSelectProjectorIp(){
		return mSelectProjectorIp;
	}


	public void clearLogin(){
		setLoginInfo(null);

//		mMemberManager.clear();
//		mProfileManager.clear();

	}





}
