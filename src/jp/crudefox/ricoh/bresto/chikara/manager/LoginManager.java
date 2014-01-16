package jp.crudefox.ricoh.bresto.chikara.manager;


import java.util.ArrayList;
import java.util.List;

import jp.crudefox.tunacan.chikara.util.CFUtil;
import jp.crudefox.tunacan.chikara.util.CFUtil.HttpResponseData;

import org.apache.http.NameValuePair;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class LoginManager {



	/*		Auth: Chikara Funabashi
	 * 		Date: 2013/07/06
	 *
	 */


	private static String LOGIN_URL = CFConst.SERVER + CFConst.LOGIN;
	private static String CREATE_ACCOUNT_URL = CFConst.SERVER + CFConst.CREATE_ACCOUNT;
	private static String PROJECT_LIST_URL = CFConst.SERVER + CFConst.PROJECT_LIST;
	private static String SELECT_PROJECT_URL = CFConst.SERVER + CFConst.SELECT_PROJECT;


//	private static String CONTRIBUTE_URL = CFConst.SERVER + CFConst.CONTRIBUTE;
//	private static String DEL_CONTRIBUTE_URL = CFConst.SERVER + CFConst.DEL_CONTRIBUTE;


	private Context mContext;

	public LoginManager(Context context){
		mContext = context;
	}


	public static final String DEL_OK = "OK";

	public static final String BOARD_OK = "OK";
	public static final String BOARD_ERR_OVER_TIME_TOKEN = "E04";

	public static final String CONTOBUTE_OK = "OK";

	public static final String SIGN_UP_OK = "OK";
	public static final String SIGN_UP_ERR_CONNECT = "Err_Connect";

	public static final String LOGIN_OK = "OK";
	public static final String LOGIN_ERR_CONNECT = "Err_Connect";
	public static final String LOGIN_ERR_DATA = "Err_Data";
	public static final String LOGIN_ERR_DB = "E01";
	public static final String LOGIN_ERR_SELECT = "E02";
	public static final String LOGIN_ERR_NOT_REG_ID = "E03";
	public static final String LOGIN_ERR_NOT_MATCH_PASS = "E04";


	//E01 データベースの接続不良
	//E02 select失敗
	//E03 ID未登録
	//E04 : パスワード不一位

	//ログインをします。
	public LoginInfo login(String id, String password,String[] outResult){

//		if(true){
//			return _login_mock(id, password, outResult);
//		}

		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("user_id", id));
		params.add(new BasicNameValuePair("user_password", password));

		log("00001");

//		String data = null;
//		if(CFUtil.LOCAL_DEBUG){
//			//data = CUtil.getRawText(mContext, R.raw.login_resp_sample);
//
//			try {
//				// Simulate network access.
//				Thread.sleep(2000);
//			} catch (InterruptedException e) {
//				if(outResult!=null) outResult[0] = LOGIN_ERR_CONNECT;
//				return null;
//			}
//
//		}else{
//
//		}

		log("login url = "+LOGIN_URL);

		HttpResponseData rd = null;
		//String data = null;

//		ArrayList<Cookie> cookie = new ArrayList<Cookie>();
//		cookie.add(new DefaultC);

		rd = CFUtil.postData(LOGIN_URL, params, null);
		
		log("login url2 = "+LOGIN_URL);

		if(rd==null || rd.data==null){
			if(outResult!=null) outResult[0] = LOGIN_ERR_CONNECT;
			return null;
		}

		log("00002");

		try {
			JSONObject json = new JSONObject(rd.data);

			String result = json.getString("result");
//			String sid = json.getString("sid");
//			String name = json.getString("user_name");

			if(!result.equalsIgnoreCase("OK")){
				if(outResult!=null) outResult[0] = LOGIN_ERR_NOT_MATCH_PASS;
				return null;
			}

			Cookie session_cookie = null;
		    for (int i = 0; i < rd.cookies.size(); i++) {
				Cookie c = rd.cookies.get(i);
				if( c.getName().equalsIgnoreCase("JSESSIONID") ){
					session_cookie = c;
					break;
				}
		    }

		    if(session_cookie==null){
				if(outResult!=null) outResult[0] = rd.data;
				return null;//LOGIN_RESULT_FAILED;
		    }

		    String sid = session_cookie.getValue();


//			log("00004");

//			if(!result.equalsIgnoreCase(LOGIN_OK)){
//				if(outResult!=null) outResult[0] = result;
//				return null;//LOGIN_RESULT_FAILED;
//			}
//			if(TextUtils.isEmpty(sid)){
//				if(outResult!=null) outResult[0] = LOGIN_ERR_DATA;
//				return null;//LOGIN_RESULT_FAILED;
//			}
//			if(TextUtils.isEmpty(name)){
//				if(outResult!=null) outResult[0] = LOGIN_ERR_DATA;
//				return null;//LOGIN_RESULT_FAILED;
//			}

//			log("00005");

			LoginInfo lf = new LoginInfo();
			lf.sid = new SessionID(sid);
			lf.id = id;
			lf.password = password;
			//lf.name = name;

			return lf;

		} catch (JSONException e) {
			e.printStackTrace();
			log("00003");
			if(outResult!=null) outResult[0] = LOGIN_ERR_DATA;
			return null;
		}
		 catch (Exception e) {
			e.printStackTrace();
			if(outResult!=null) outResult[0] = LOGIN_ERR_DATA;
			log("00003-b");
			return null;
		}

	}

	//ログインをし直します。
	public boolean retryLogin(LoginInfo lf,String[] outResult){

		LoginInfo new_lf = login(lf.id, lf.password,outResult);

		if(new_lf==null) return false;

		lf.id = new_lf.id;
		lf.password = new_lf.password;
		lf.name = new_lf.name;
		lf.sid = new_lf.sid;

		return true;
	}

	//アカウントを作成します
	public boolean createUser(String id, String password,String name, String[] outResult){

		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("user_id", id));
		params.add(new BasicNameValuePair("user_password", password));
		//params.add(new BasicNameValuePair("repass", password));
		params.add(new BasicNameValuePair("user_name", name));

		String data = null;
		if(CFUtil.LOCAL_DEBUG){
			//data = CUtil.getRawText(mContext, R.raw.sign_up_resp_sample);
			try {
				// Simulate network access.
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				if(outResult!=null) outResult[0] = SIGN_UP_ERR_CONNECT;
				return false;
			}
		}else{
			data = CFUtil.postData(CREATE_ACCOUNT_URL, params);
		}

		if(data==null) {
			if(outResult!=null) outResult[0] = SIGN_UP_ERR_CONNECT;
			return false;
		}

		try {
			JSONObject json = new JSONObject(data);

			String result = json.getString("result");
//			String id = json.getString("id");
//			String name = json.getString("name");
//			String pass = json.getString("pass");

//			boolean result = json.getBoolean("result");

			if(!result.equalsIgnoreCase(SIGN_UP_OK)){
				if(outResult!=null) outResult[0] = result;
				return false;
			}

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			if(outResult!=null) outResult[0] = SIGN_UP_ERR_CONNECT;
			return false;
		}


	}

	public static class ProjectInfo{
		public String project_id;
		public String project_name;
		public String publish_url;
		public String auhtor_id;
	}


	public List<ProjectInfo> getProjectList(LoginInfo l){

		if(!l.isLoggedIn()) return null;

		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		//params.add(new BasicNameValuePair("searchfriend", q));


		ArrayList<Cookie> cookie = new ArrayList<Cookie>();
		cookie.add(new BasicClientCookie("JSESSIONID", l.sid.sid));

		for(int i=0;i<cookie.size();i++){
			BasicClientCookie c = (BasicClientCookie) cookie.get(i);
			c.setDomain(CFConst.DOMAIN);
			c.setPath("/");
		}

		HttpResponseData rd;

		rd = CFUtil.postData( PROJECT_LIST_URL, params, cookie);

		if(rd == null || rd.data == null) return null;

		log(rd.data);

		try {
			JSONObject json = new JSONObject(rd.data);

			String result = json.getString("result");

			if(!result.equalsIgnoreCase("OK")) return null;

			JSONArray jdata = json.getJSONArray("data");

			//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			ArrayList<ProjectInfo> list = new ArrayList<LoginManager.ProjectInfo>();

			int  num = jdata.length();
			for(int i=0;i<num;i++){
				JSONObject jc = jdata.getJSONObject(i);

				ProjectInfo item = new ProjectInfo();
				item .project_id = jc.getString("project_id");
				item .project_name = jc.getString("project_name");
				item .auhtor_id = jc.getString("author_id");
				item .publish_url = jc.getString("project_id");

				list.add(item);
			}

			return list;

		} catch (JSONException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			//CFUtil.Log("member ", e);
			return null;
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			//CFUtil.Log("member ", e);
			return null;
		}

	}

	public boolean selectProject(LoginInfo l, String project_id){

		CFUtil.Log("select project = "+project_id);

		if(!l.isLoggedIn()) return false;

		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("project_id", project_id));


		ArrayList<Cookie> cookie = new ArrayList<Cookie>();
		cookie.add(new BasicClientCookie("JSESSIONID", l.sid.sid));

		for(int i=0;i<cookie.size();i++){
			BasicClientCookie c = (BasicClientCookie) cookie.get(i);
			c.setDomain(CFConst.DOMAIN);
			c.setPath("/");
		}

		HttpResponseData rd;

		rd = CFUtil.postData( SELECT_PROJECT_URL, params, cookie);

		if(rd == null || rd.data == null) return false;

		log(rd.data);

		try {
			JSONObject json = new JSONObject(rd.data);

			String result = json.getString("result");

			if(!result.equalsIgnoreCase("OK")) return false;

			return true;

		} catch (JSONException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return false;
		}

	}


//	//投稿をします
//	public int submitContribute(LoginInfo lf, String body){
//
//		if(TextUtils.isEmpty(body)){
//			return -1;
//		}
//
//		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
//
//		params.add(new BasicNameValuePair("token", lf.sid.sid));
//		params.add(new BasicNameValuePair("body", body));
//
//		String data = null;
//		if(CFUtil.LOCAL_DEBUG){
//			//data = CUtil.getRawText(mContext, R.raw.contribute_resp_sample);
//
//			try {
//				// Simulate network access.
//				Thread.sleep(2000);
//			} catch (InterruptedException e) {
//				return -1;
//			}
//
//		}else{
//			data = CFUtil.postData(CONTRIBUTE_URL, params);
//		}
//
//
//		try {
//			JSONObject json = new JSONObject(data);
//
//			String result = json.getString("result");
//			int res_post_id = json.getInt("post_id");
////			int res_acc_id = json.getInt("acc_id");
////			long res_post_time = json.getLong("post_time");
////			int res_body = json.getInt("body");
//			String res_token = json.getString("token");
//
//			if(!result.equalsIgnoreCase(CONTOBUTE_OK)){
//				return -1;
//			}
//
//			if(!TextUtils.isEmpty(res_token)){
//				lf.sid = new SessionID( res_token );
//				log("tokenがNuLLではないです。");
//			}else{
//				log("tokenがNuLLです。");
//			}
//
//			return res_post_id;
//
//		} catch (JSONException e) {
//			e.printStackTrace();
//			return -1;
//		}
//	}
//
//	//投稿を削除します
//	public boolean deleteContribute(LoginInfo lf, int id){
//
//		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
//
//		params.add(new BasicNameValuePair("token", lf.sid.sid));
//		params.add(new BasicNameValuePair("post", ""+id));
//
//		String data = null;
//		if(CFUtil.LOCAL_DEBUG){
//			//data = CUtil.getRawText(mContext, R.raw.del_cont_resp_sample);
//
//			try {
//				// Simulate network access.
//				Thread.sleep(2000);
//			} catch (InterruptedException e) {
//				return false;
//			}
//
//		}else{
//			data = CFUtil.postData(DEL_CONTRIBUTE_URL, params);
//		}
//
//
//		try {
//			JSONObject json = new JSONObject(data);
//
//			String result = json.getString("result");
//
//			if(!result.equalsIgnoreCase(DEL_OK)){
//				return false;
//			}
//
//			return true;
//
//		} catch (JSONException e) {
//			e.printStackTrace();
//			return false;
//		}
//	}

	//アカウントを作成します
	public boolean _createUser_mock(String id, String password,String name, String[] outResult){

		try {
			// Simulate network access.
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			if(outResult!=null) outResult[0] = SIGN_UP_ERR_CONNECT;
			return false;
		}

		return true;

	}



	public LoginInfo _login_mock(String id, String password,String[] outResult){

		try {
			// Simulate network access.
			Thread.sleep(100);
		} catch (InterruptedException e) {
			if(outResult!=null) outResult[0] = LOGIN_ERR_CONNECT;
			return null;
		}

		LoginInfo li = new LoginInfo();
		li.id = id;
		li.name = "モック"+id+"さん";
		li.password = password;
		li.sid = new SessionID("daskldjaskldjasdjska");

		return li;
	}



	public static void log(String str){
		android.util.Log.d("test", str);
	}





}
