package jp.crudefox.tunacan.chikara.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ListView;

public class CFOverScrolledListView extends ListView{


	/*		Auth: Chikara Funabashi
	 * 		Date:
	 *
	 */


	public interface OnOverScrolledListener{
		public void onOverScrolled(
				int scrollX, int scrollY,
				boolean clampedX, boolean clampedY);
	}




	private final float mDensity;

	private int mMaxOverSclolledTopY;
	private int mMaxOverSclolledBottomY;

	private OnOverScrolledListener mOnOverScrolledListener;


	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public CFOverScrolledListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mDensity = context.getResources().getDisplayMetrics().density;

		mMaxOverSclolledTopY = mMaxOverSclolledBottomY = (int)( 150 * mDensity );

		if( CFUtil.isOk_SDK(9) ){
			setOverScrollMode(ListView.OVER_SCROLL_ALWAYS);
		}
	}



	@SuppressLint("NewApi")
	@Override
	protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
		// TODO 自動生成されたメソッド・スタブ
		super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);


		if(mOnOverScrolledListener!=null){
			mOnOverScrolledListener.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
		}
		
		//CFUtil.Log("onOverScrolled "+scrollY);

	}


	@SuppressLint("NewApi")
	@Override
	protected boolean overScrollBy(
			int deltaX, int deltaY,
			int scrollX, int scrollY,
			int scrollRangeX, int scrollRangeY,
			int maxOverScrollX, int maxOverScrollY,
			boolean isTouchEvent) {

		int py = scrollY + deltaY;
		
		if(py<0){
			maxOverScrollY = mMaxOverSclolledTopY;
		}else if(py>0){
			maxOverScrollY = mMaxOverSclolledBottomY;
		}else{
			//maxOverScrollY = mMaxOverSclolledTopY;
		}
		
		//CFUtil.Log("py "+scrollY);

		return super.overScrollBy(
				0, deltaY,
				0, scrollY,
				0, scrollRangeY,
				0, maxOverScrollY,
				isTouchEvent);
	}


	public void setOnOverScrolledListener(OnOverScrolledListener lis){
		mOnOverScrolledListener = lis;
	}



	public void setMaxOverScrolledY(int top,int bottom){
		mMaxOverSclolledTopY = top;
		mMaxOverSclolledBottomY = bottom;
	}
	public int getMaxOverScrolledTopY(){
		return mMaxOverSclolledTopY;
	}
	public int getMaxOverScrolledBottomY(){
		return mMaxOverSclolledTopY;
	}



//	@Override
//	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
//		super.onScrollChanged(l, t, oldl, oldt);
//		
//		CFUtil.Log("onScrollChanged " +t);
//	}





}
