package jp.crudefox.ricoh.bresto.chikara.manager;

import java.io.Serializable;

public class SessionID implements Serializable{


	/*		Auth: Chikara Funabashi
	 * 		Date: 2013/07/06
	 *
	 */


	final String sid;



	SessionID(String sid) {
		this.sid = sid;
	}

	public String getSessionID(){
		return this.sid;
	}

}
