package jp.crudefox.ricoh.bresto.chikara.manager;

import java.io.Serializable;

public class LoginInfo implements Serializable{

	/*		Auth: Chikara Funabashi
	 * 		Date: 2013/07/06
	 *
	 */
	
	/*		ログイン情報
	 * 		
	 *
	 */	


	SessionID sid;

	String id;
	String name;
	String password;


	LoginInfo() {
		//this.token = token;
	}


	public SessionID getToken(){
		return this.sid;
	}

	public boolean isLoggedIn(){
		return sid!= null;
	}

	public boolean isMyId(String id){
		if(id==null) return false;
		if(this.id==null) return false;
		return this.id.equals(id);
	}





	public String getId(){
		return id;
	}
	public String getPassword(){
		return password;
	}
	public String getName(){
		return name;
	}
//	public String getLoginToken(){
//		return mLoginToken;
//	}
//	public boolean isLogin(){
//		return mLoginToken!=null;
//	}

}
