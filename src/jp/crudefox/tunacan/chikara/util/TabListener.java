package jp.crudefox.tunacan.chikara.util;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;


public class TabListener<T extends Fragment> implements ActionBar.TabListener {


	/*		Auth: Chikara Funabashi
	 * 		Date: 2013/08/09
	 *
	 */

	/*		コピペして来ました。そして改造しました。
	 * 		ごめんなさい。URL忘れました。
	 *
	 */		


	private Fragment mFragment;
    private final SherlockFragmentActivity mActivity;
    private final String mTag;
    private final Class<T> mClass;
    private int mContainerId;
    private Bundle mArgs;

	    /**
	     * コンストラクタ
	     * @param activity
	     * @param tag
	     * @param clz
	     */
	    public TabListener(SherlockFragmentActivity activity, String tag, Class<T> clz, int container_id,Bundle args) {
	        mActivity = activity;
	        mTag = tag;
	        mClass = clz;
	        mContainerId = container_id;
	        //FragmentManagerからFragmentを探す。  2012/12/11 追記
	        mFragment = mActivity.getSupportFragmentManager().findFragmentByTag(mTag);
	        mArgs = args;
	        if(mFragment!=null && mArgs!=null && !mFragment.isDetached()) mFragment.setArguments(mArgs);
	    }

	    /**
	     * @brief 　タブが選択されたときの処理
	     */
	    public void onTabSelected(Tab tab, FragmentTransaction ft) {
	        //ftはnullではないが使用するとNullPointExceptionで落ちる
	        if (mFragment == null) {
	            mFragment = Fragment.instantiate(mActivity, mClass.getName());
	            if(mFragment!=null && mArgs!=null) mFragment.setArguments(mArgs);
	            FragmentManager fm = mActivity.getSupportFragmentManager();
	            fm.beginTransaction().add(mContainerId, mFragment, mTag).commit();
	        } else {
	            //detachされていないときだけattachするよう変更   2012/12/11　変更
	            //FragmentManager fm = mActivity.getSupportFragmentManager();
	            //fm.beginTransaction().attach(mFragment).commit();
	            if (mFragment.isDetached()) {
	                FragmentManager fm = mActivity.getSupportFragmentManager();
	                fm.beginTransaction().attach(mFragment).commit();
	             }
	        }
	    }
	    /**
	     * @brief 　タブの選択が解除されたときの処理
	     */
	    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	        //ftはnullではないが使用するとNullPointExceptionで落ちる
	        if (mFragment != null) {
	            FragmentManager fm = mActivity.getSupportFragmentManager();
	            fm.beginTransaction().detach(mFragment).commit();
	       }
	    }
	    /**
	     * @brief　タブが2度目以降に選択されたときの処理
	     */
	    public void onTabReselected(Tab tab, FragmentTransaction ft) {
	    }
}