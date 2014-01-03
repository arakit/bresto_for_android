package jp.crudefox.tunacan.chikara.util;

import java.util.Arrays;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.OverScroller;

public class CFCellsView extends AdapterView<ListAdapter>{

	public static final int WC = LayoutParams.WRAP_CONTENT;
	public static final int MP = LayoutParams.MATCH_PARENT;

	public static class LayoutParams extends ViewGroup.LayoutParams{

		int viewType;


//		public LayoutParams(Context arg0, AttributeSet arg1) {
//			super(arg0, arg1);
//			ViewGroup.LayoutParams lp = new
//		}

		public LayoutParams(int width, int height) {
			super(width, height);
		}

//		public LayoutParams(android.view.ViewGroup.LayoutParams arg0) {
//			super(arg0);
//		}



	}


//	public static interface CellsAdapter{
//		public View getView(View convertView,int x, int y);
//		public int getCountX();
//		public int getCountY();
//	}


//	private CellsAdapter mAdapter;

	private ListAdapter mAdapter;



	private float mDensity;

	private boolean mNeedLocateCell = false;

	private final HashMap<Integer, View> mCells = new HashMap<Integer, View>();
	private final HashMap<View, Integer> mCellsPos = new HashMap<View, Integer>();

	private int mCellsCountX = 1;
	private int mCellsCountY = 0;

	private int[] mCellsWidth;
	private int[] mCellsHeight;

	private int[] mFixCellsWidth;


	public CFCellsView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mDensity = getResources().getDisplayMetrics().density;

		mFixCellsWidth = new int[mCellsCountX];
		Arrays.fill(mFixCellsWidth, -1);

		initScrollView();
	}

    private void initOrResetVelocityTracker() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        } else {
            mVelocityTracker.clear();
        }
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }


    @SuppressLint("NewApi")
	private void initScrollView() {
        mScroller = new OverScroller(getContext());
        setFocusable(true);
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        setWillNotDraw(false);
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        mOverscrollDistance = configuration.getScaledOverscrollDistance();
        mOverflingDistance = configuration.getScaledOverflingDistance();
    }


//    @Override
//    public boolean onGenericMotionEvent(MotionEvent event) {
//        if ((event.getSource() & InputDevice.SOURCE_CLASS_POINTER) != 0) {
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_SCROLL: {
//                    if (!mIsBeingDragged) {
//                        final float vscroll = event.getAxisValue(MotionEvent.AXIS_VSCROLL);
//                        if (vscroll != 0) {
//                            final int delta = (int) (vscroll * getVerticalScrollFactor());
//                            final int range = getScrollRange();
//                            int oldScrollY = mScrollY;
//                            int newScrollY = oldScrollY - delta;
//                            if (newScrollY < 0) {
//                                newScrollY = 0;
//                            } else if (newScrollY > range) {
//                                newScrollY = range;
//                            }
//                            if (newScrollY != oldScrollY) {
//                                super.scrollTo(mScrollX, newScrollY);
//                                return true;
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        return super.onGenericMotionEvent(event);
//    }
//


	boolean mIsDown = false;
	PointF mDownPt = new PointF();
	PointF mDownedScrollPt = new PointF();
	VelocityTracker mVelocityTracker;

	int mTouchSlop;
	int mMaximumVelocity = 500;
	int mMinimumVelocity;
	int mOverscrollDistance;
	int mOverflingDistance;

	OverScroller mScroller;


	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

		//return super.onInterceptTouchEvent(ev);


		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//return super.onTouchEvent(event);

		//int height = getHeight();

        initVelocityTrackerIfNotExists();
        mVelocityTracker.addMovement(event);

		int action = event.getActionMasked();

		if(action == MotionEvent.ACTION_DOWN){
			if(!mIsDown){
				mIsDown =true;
				mDownPt.x = event.getX();
				mDownPt.y = event.getY();
				mDownedScrollPt.x = getScrollX();
				mDownedScrollPt.y = getScrollY();
				//initOrResetVelocityTracker();
				//mVelocityTracker.addMovement(event);

                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }

			}
		}
		else if(action == MotionEvent.ACTION_MOVE){
			if(mIsDown){
				float x = event.getX();
				float y = event.getY();
				float sx = -(x - mDownPt.x) + mDownedScrollPt.x;
				float sy = -(y - mDownPt.y) + mDownedScrollPt.y;
				scrollTo((int) sx, (int) sy);
//                initVelocityTrackerIfNotExists();
//                mVelocityTracker.addMovement(event);
			}
		}
		else if(action == MotionEvent.ACTION_UP){
			if(mIsDown){

                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int initialVelocity = (int) velocityTracker.getYVelocity(event.getPointerId(0));
                int initialVelocitx = (int) velocityTracker.getXVelocity(event.getPointerId(0));
                fling(-initialVelocitx, -initialVelocity);

				recycleVelocityTracker();
				mIsDown =false;
			}
		}
		else if(action == MotionEvent.ACTION_CANCEL){
			if(mIsDown){
                if (mScroller.springBack(getScrollX(), getScrollY(), 0, getScrollRangeX(), 0, getScrollRangeY())) {
                    //postInvalidateOnAnimation();
                    postInvalidate();
                }
				recycleVelocityTracker();
				mIsDown = false;
			}
		}


		return true;
	}


    /**
     * Fling the scroll view
     *
     * @param velocityY The initial velocity in the Y direction. Positive
     *                  numbers mean that the finger/cursor is moving down the screen,
     *                  which means we want to scroll towards the top.
     */
//    @SuppressLint("NewApi")
	public void fling(int velocityX,int velocityY) {

    	CFUtil.Log("fling "+velocityY+", "+velocityY);

        if (getChildCount() > 0) {
            int height = getHeight() - getPaddingBottom() - getPaddingTop();
            int width = getWidth() - getPaddingLeft() - getPaddingRight();
//            int right = getChildAt(getChildCount()-1).getWidth();
//            int bottom = getChildAt(getChildCount()-1).getHeight();

            //mScroller.springBack(getScrollX(), getScrollY(), 0, width, 0, getScrollRange());
//            mScroller.startScroll(getScrollX(), getScrollY(), velocityX, velocityY, 1000);
            mScroller.fling(getScrollX(), getScrollY(), velocityX, velocityY,
//            		0, Math.max(0, right - width),
//            		0, Math.max(0, bottom - height),
            		0, Math.max(0, getScrollRangeX()),
            		0, Math.max(0, getScrollRangeY()),
                    width/2, height/2);

//            if (mFlingStrictSpan == null) {
//                mFlingStrictSpan = StrictMode.enterCriticalSpan("ScrollView-fling");
//            }

//            postInvalidateOnAnimation();
            postInvalidate();
        }
    }

//    @Override
//    protected void onOverScrolled(int scrollX, int scrollY,
//            boolean clampedX, boolean clampedY) {
//        // Treat animating scrolls differently; see #computeScroll() for why.
//        if (!mScroller.isFinished()) {
//        	CFUtil.Log("onOverScrolled "+scrollX+","+scrollY);
////        	scrollTo(scrollX, scrollY);
////            mScrollX = scrollX;
////            mScrollY = scrollY;
////            invalidateParentIfNeeded();
////            if (clampedY) {
////                mScroller.springBack(mScrollX, mScrollY, 0, 0, 0, getScrollRange());
////            }
//        } else {
//            super.scrollTo(scrollX, scrollY);
//        }
//        awakenScrollBars();
//    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            // This is called at drawing time by ViewGroup.  We don't want to
            // re-show the scrollbars at this point, which scrollTo will do,
            // so we replicate most of scrollTo here.
            //
            //         It's a little odd to call onScrollChanged from inside the drawing.
            //
            //         It is, except when you remember that computeScroll() is used to
            //         animate scrolling. So unless we want to defer the onScrollChanged()
            //         until the end of the animated scrolling, we don't really have a
            //         choice here.
            //
            //         I agree.  The alternative, which I think would be worse, is to post
            //         something and tell the subclasses later.  This is bad because there
            //         will be a window where mScrollX/Y is different from what the app
            //         thinks it is.
            //
            int oldX = getScrollX();//mScrollX;
            int oldY = getScrollY();//mScrollY;
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();

            CFUtil.Log("sx="+x+", sy="+y);

            if (oldX != x || oldY != y) {
                final int rangeY = getScrollRangeY();
                //final int overscrollMode = getOverScrollMode();
                //final boolean canOverscroll = overscrollMode == OVER_SCROLL_ALWAYS ||
                //        (overscrollMode == OVER_SCROLL_IF_CONTENT_SCROLLS && range > 0);

//                overScrollBy(x - oldX, y - oldY, oldX, oldY, 0, range,
//                        0, mOverflingDistance, false);
                scrollTo(x, y);
                onScrollChanged(getScrollX(), getScrollY(), oldX, oldY);

//                if (canOverscroll) {
//                    if (y < 0 && oldY >= 0) {
//                        mEdgeGlowTop.onAbsorb((int) mScroller.getCurrVelocity());
//                    } else if (y > range && oldY <= range) {
//                        mEdgeGlowBottom.onAbsorb((int) mScroller.getCurrVelocity());
//                    }
//                }
            }

            postInvalidate();
//            if (!awakenScrollBars()) {
//                // Keep on drawing until the animation has finished.
//                postInvalidateOnAnimation();
//            }

        }
//        else {
//            if (mFlingStrictSpan != null) {
//                mFlingStrictSpan.finish();
//                mFlingStrictSpan = null;
//            }
//        }
    }

    private int getScrollRangeY() {
        int scrollRange = 0;
//        if (getChildCount() > 0 ) {
        if ( mCellsCountY>0 && mCellsHeight!=null ) {
//            View child = getChildAt(getChildCount()-1);
            scrollRange = Math.max(0,
            		getCellsYInPixels(mCellsCountY-1)+mCellsHeight[mCellsCountY-1] 
            				- (getHeight() - getPaddingBottom() - getPaddingTop() ) );
//                    child.getBottom() - (getHeight() - getPaddingBottom() - getPaddingTop()));
        }
        return scrollRange;
    }

    private int getScrollRangeX() {
        int scrollRange = 0;
//        if (getChildCount() > 0) {
        if ( mCellsCountX>0 && mCellsWidth!=null ) {
//            View child = getChildAt(getChildCount()-1);
            scrollRange = Math.max(0,
            		getCellsXInPixels(mCellsCountX-1)+mCellsWidth[mCellsCountX-1] 
            				- (getWidth() - getPaddingRight() - getPaddingLeft()) );
//                    child.getRight() - (getWidth() - getPaddingRight() - getPaddingLeft()));
        }
        return scrollRange;
    }

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);




	}


	public void setCellsWidth(int col,int width){
		mFixCellsWidth[col] = width;
	}

	public void setCellCount(int x,int y){
		mCellsCountX = x;
		mCellsCountY = y;
		mFixCellsWidth = new int[mCellsCountX];
		Arrays.fill(mFixCellsWidth, -1);
		updateDataset();
	}

	private int getCellCountX(){
//		if(mAdapter==null) return 0;
//		return mAdapter.getCountX();
		return mCellsCountX;
	}
	private int getCellCountY(){
//		if(mAdapter==null) return 0;
//		return mAdapter.getCountY();
		return mCellsCountY;
	}



	private View getViewFromAdapter(int x,int y){
		if(mAdapter==null) return null;

		int count_x = mCellsCountX;//mAdapter.getCountX();
		int count_y = mCellsCountY;//mAdapter.getCountY();

		if(x<0 || x>=count_x) return null;
		if(y<0 || y>=count_y) return null;

		int index = calcIndex2(x, y);
		//View v = mAdapter.getView(null, x, y);
		View v = index<mAdapter.getCount() ? mAdapter.getView(index, null, this) : null;

		return v;
	}

	private int getViewTypeFromAdapter(int index){
		if(mAdapter==null) return 0;

		int type = index<mAdapter.getCount() ? mAdapter.getItemViewType(index) : 0 ;
		return type;
	}


	private CFCellsView.LayoutParams makeLayoutParams(int type){
		LayoutParams lp = new LayoutParams( MP, MP);
		lp.viewType = type;
		return lp;
	}


	private void locateViews(){

		mCellsPos.clear();
		mCells.clear();

		int count_y = mCellsCountY;
		int count_x = mCellsCountX;

    	for(int j=0;j<count_y;j++){
	    	for(int i=0;i<count_x;i++){
	    		View child = getViewFromAdapter(i, j);
	    		//int type = getViewTypeFromAdapter(calcIndex2(i, j));
	    		if(child==null) continue;

	    		mCells.put(calcIndex2(i, j), child);
	    		mCellsPos.put(child, calcIndex2(i, j));
	    	}
    	}

    	if( mCellsWidth == null || mCellsWidth.length!= mCellsCountX ){
    		mCellsWidth = new int[mCellsCountX];
    	}
    	if( mCellsHeight == null ||  mCellsHeight.length!= mCellsCountY ){
    		mCellsHeight = new int[mCellsCountY];
    	}

    	Arrays.fill(mCellsWidth, 0);
    	Arrays.fill(mCellsHeight, 0);

	}

	public void updateDataset(){
		mNeedLocateCell = true;
		requestLayout();
	}


    /**
     * {@inheritDoc}
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

    	if(mNeedLocateCell){
    		locateViews();
    		mNeedLocateCell = false;
    	}


    	//int measure_mode_w = MeasureSpec.getMode(widthMeasureSpec);
    	//int measure_mode_h = MeasureSpec.getMode(heightMeasureSpec);
    	int measure_w = MeasureSpec.getSize(widthMeasureSpec);
    	int measure_h = MeasureSpec.getSize(heightMeasureSpec);

    	setMeasuredDimension(measure_w, measure_h);

    	int count_x = getCellCountX();
    	int count_y = getCellCountY();

    	//addView
    	for(int j=0;j<count_y;j++){
	    	for(int i=0;i<count_x;i++){
	    		int index = calcIndex2(i, j);
	    		View child = mCells.get(index);
	    		int type = getViewTypeFromAdapter(index);
	    		if(child==null) continue;
	    		if( indexOfChild(child)==-1 ){
	    			LayoutParams lp = makeLayoutParams(type);
	    			child.setLayoutParams(lp);
	    		}
	    	}
    	}

    	//
    	for(int j=0;j<count_y;j++){
	    	for(int i=0;i<count_x;i++){
	    		View child = mCells.get(calcIndex2(i, j));

	    		if(child==null) continue;

//	    		LayoutParams lp = child.getLayoutParams();
//	    		if( lp == null ) lp = new LayoutParams( WC, WC );
//	    		child.setLayoutParams(lp);

	    		int childWidthMeasureSpec;
	    		int childHeightMeasureSpec;

	    		if( mFixCellsWidth[i] != -1 ){
		    		childWidthMeasureSpec = MeasureSpec.makeMeasureSpec( mFixCellsWidth[i] ,
		    				MeasureSpec.EXACTLY);
	    		}else{
		    		childWidthMeasureSpec = MeasureSpec.makeMeasureSpec( measure_w ,
		    				MeasureSpec.UNSPECIFIED);
	    		}
	    		childHeightMeasureSpec = MeasureSpec.makeMeasureSpec( (int)(150*mDensity) ,
	    				MeasureSpec.UNSPECIFIED);

	    		child.measure(childWidthMeasureSpec, childHeightMeasureSpec);

	    		if( mFixCellsWidth[i] == -1 ){
		    		if( child.getMeasuredWidth() > mCellsWidth[i] ){
		    			mCellsWidth[i] = child.getMeasuredWidth();
		    		}
	    		}else{
	    			mCellsWidth[i] = mFixCellsWidth[i];
	    		}
	    		if( child.getMeasuredHeight() > mCellsHeight[j] ){
	    			mCellsHeight[j] = child.getMeasuredHeight();
	    		}

	    	}
    	}

    	for(int j=0;j<count_y;j++){
	    	for(int i=0;i<count_x;i++){
	    		View child = mCells.get(calcIndex2(i, j));

	    		if(child==null) continue;

	    		int childWidthMeasureSpec;
	    		int childHeightMeasureSpec;

	    		childWidthMeasureSpec = MeasureSpec.makeMeasureSpec( mCellsWidth[i] ,
	    				MeasureSpec.EXACTLY);
	    		childHeightMeasureSpec = MeasureSpec.makeMeasureSpec( mCellsHeight[j] ,
	    				MeasureSpec.EXACTLY);

	    		child.measure(childWidthMeasureSpec, childHeightMeasureSpec);

	    	}
    	}


//        int count = getChildCount();
//
//        for (int i = 0; i < count; i++) {
//            final View child = getChildAt(i);
//            if (child.getVisibility() != GONE) {
//                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
//                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
//                maxWidth = Math.max(maxWidth,
//                        child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
//                maxHeight = Math.max(maxHeight,
//                        child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
//                childState = combineMeasuredStates(childState, child.getMeasuredState());
//                if (measureMatchParentChildren) {
//                    if (lp.width == LayoutParams.MATCH_PARENT ||
//                            lp.height == LayoutParams.MATCH_PARENT) {
//                        mMatchParentChildren.add(child);
//                    }
//                }
//            }
//        }
//
//
//
//        final boolean measureMatchParentChildren =
//                MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY ||
//                MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY;
//
//
//        //mMatchParentChildren.clear();
//
//        int maxHeight = 0;
//        int maxWidth = 0;
//        int childState = 0;
//
//        for (int i = 0; i < count; i++) {
//            final View child = getChildAt(i);
//            if (mMeasureAllChildren || child.getVisibility() != GONE) {
//                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
//                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
//                maxWidth = Math.max(maxWidth,
//                        child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
//                maxHeight = Math.max(maxHeight,
//                        child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
//                childState = combineMeasuredStates(childState, child.getMeasuredState());
//                if (measureMatchParentChildren) {
//                    if (lp.width == LayoutParams.MATCH_PARENT ||
//                            lp.height == LayoutParams.MATCH_PARENT) {
//                        mMatchParentChildren.add(child);
//                    }
//                }
//            }
//        }
//
//        // Account for padding too
//        maxWidth += getPaddingLeftWithForeground() + getPaddingRightWithForeground();
//        maxHeight += getPaddingTopWithForeground() + getPaddingBottomWithForeground();
//
//        // Check against our minimum height and width
//        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
//        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());
//
//        // Check against our foreground's minimum height and width
//        final Drawable drawable = getForeground();
//        if (drawable != null) {
//            maxHeight = Math.max(maxHeight, drawable.getMinimumHeight());
//            maxWidth = Math.max(maxWidth, drawable.getMinimumWidth());
//        }
//
//        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
//                resolveSizeAndState(maxHeight, heightMeasureSpec,
//                        childState << MEASURED_HEIGHT_STATE_SHIFT));
//
//        count = mMatchParentChildren.size();
//        if (count > 1) {
//            for (int i = 0; i < count; i++) {
//                final View child = mMatchParentChildren.get(i);
//
//                final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
//                int childWidthMeasureSpec;
//                int childHeightMeasureSpec;
//
//                if (lp.width == LayoutParams.MATCH_PARENT) {
//                    childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth() -
//                            getPaddingLeftWithForeground() - getPaddingRightWithForeground() -
//                            lp.leftMargin - lp.rightMargin,
//                            MeasureSpec.EXACTLY);
//                } else {
//                    childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,
//                            getPaddingLeftWithForeground() + getPaddingRightWithForeground() +
//                            lp.leftMargin + lp.rightMargin,
//                            lp.width);
//                }
//
//                if (lp.height == LayoutParams.MATCH_PARENT) {
//                    childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight() -
//                            getPaddingTopWithForeground() - getPaddingBottomWithForeground() -
//                            lp.topMargin - lp.bottomMargin,
//                            MeasureSpec.EXACTLY);
//                } else {
//                    childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec,
//                            getPaddingTopWithForeground() + getPaddingBottomWithForeground() +
//                            lp.topMargin + lp.bottomMargin,
//                            lp.height);
//                }
//
//                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
//            }
//        }
    }

//    public static final long calcIndex(int x, int y){
//    	return y * (long)Integer.MAX_VALUE + x;
//    }
//    public static final int toXFrom(long index){
//    	return (int)( index % Integer.MAX_VALUE );
//    }
//    public static final int toYFrom(long index){
//    	return (int)( index / Integer.MAX_VALUE );
//    }
    public final int calcIndex2(int x, int y){
    	return y * mCellsCountX+ x;
    }
    public final int toXFrom2(int index){
    	return (int)( index % mCellsCountX );
    }
    public final int toYFrom2(int index){
    	return (int)( index / mCellsCountX );
    }

    public int getCellsYInPixels(int y){
    	int sum = getPaddingTop();
    	for(int i=0;i<y;i++){
    		sum += mCellsHeight[i];
    	}
    	return sum;
    }
    public int getCellsXInPixels(int x){
    	int sum = getPaddingLeft();
    	for(int i=0;i<x;i++){
    		sum += mCellsWidth[i];
    	}
    	return sum;
    }




    //@Override
	protected void layoutChildren() {
		//super.layoutChildren();
		
		CFUtil.Log("layoutChildren");

        final int count = mCellsCountX * mCellsCountY;
        
        removeAllViewsInLayout();

        for(int i=0;i<count;i++){
        	View child = mCells.get(i);
        	
        	if(child==null) continue;
        	
        	addViewInLayout(child, -1, child.getLayoutParams());

        	//if(!mCellsPos.containsKey(child)) continue;
        	
        	int index = mCellsPos.get(child);
        	int x = toXFrom2(index);
        	int y = toYFrom2(index);

        	int xp = getCellsXInPixels(x);
        	int yp = getCellsYInPixels(y);

        	child.layout(xp, yp,
        			xp+child.getMeasuredWidth(),
        			yp+child.getMeasuredHeight());
        }

	}



	/**
     * {@inheritDoc}
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

    	super.onLayout(changed, left, top, right, bottom);

    	CFUtil.Log("onLayout");

    	layoutChildren();

    	//scrollTo(getScrollX(), getScrollY());


//        final int parentLeft = getPaddingLeftWithForeground();
//        final int parentRight = right - left - getPaddingRightWithForeground();
//
//        final int parentTop = getPaddingTopWithForeground();
//        final int parentBottom = bottom - top - getPaddingBottomWithForeground();
//
//        mForegroundBoundsChanged = true;
//
//        for (int i = 0; i < count; i++) {
//            final View child = getChildAt(i);
//            if (child.getVisibility() != GONE) {
//                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
//
//                final int width = child.getMeasuredWidth();
//                final int height = child.getMeasuredHeight();
//
//                int childLeft;
//                int childTop;
//
//                int gravity = lp.gravity;
//                if (gravity == -1) {
//                    gravity = DEFAULT_CHILD_GRAVITY;
//                }
//
//                final int layoutDirection = getLayoutDirection();
//                final int absoluteGravity = Gravity.getAbsoluteGravity(gravity, layoutDirection);
//                final int verticalGravity = gravity & Gravity.VERTICAL_GRAVITY_MASK;
//
//                switch (absoluteGravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
//                    case Gravity.LEFT:
//                        childLeft = parentLeft + lp.leftMargin;
//                        break;
//                    case Gravity.CENTER_HORIZONTAL:
//                        childLeft = parentLeft + (parentRight - parentLeft - width) / 2 +
//                        lp.leftMargin - lp.rightMargin;
//                        break;
//                    case Gravity.RIGHT:
//                        childLeft = parentRight - width - lp.rightMargin;
//                        break;
//                    default:
//                        childLeft = parentLeft + lp.leftMargin;
//                }
//
//                switch (verticalGravity) {
//                    case Gravity.TOP:
//                        childTop = parentTop + lp.topMargin;
//                        break;
//                    case Gravity.CENTER_VERTICAL:
//                        childTop = parentTop + (parentBottom - parentTop - height) / 2 +
//                        lp.topMargin - lp.bottomMargin;
//                        break;
//                    case Gravity.BOTTOM:
//                        childTop = parentBottom - height - lp.bottomMargin;
//                        break;
//                    default:
//                        childTop = parentTop + lp.topMargin;
//                }
//
//                child.layout(childLeft, childTop, childLeft + width, childTop + height);
//            }
//        }
    }




//	public void setAdapter(CellsAdapter adapter){
//		if(mAdapter == adapter) return ;
//
//		mNeedLocateCell = true;
//
//		if(mAdapter!=null){
//			//アダプター片づけ処理
//			mAdapter = null;
//			mCells.clear();
//			mCellsPos.clear();
//		}
//
//		mAdapter = adapter;
//
//		if(mAdapter!=null){
//			//アダプター初期化k処理
//		}
//
//	}



    @Override
	public void setAdapter(ListAdapter adapter){
		if(mAdapter == adapter) return ;

		mNeedLocateCell = true;

		if(mAdapter!=null){
			//アダプター片づけ処理
			mAdapter = null;
			mCells.clear();
			mCellsPos.clear();
		}

		mAdapter = adapter;

		if(mAdapter!=null){
			//アダプター初期化k処理
		}
		updateDataset();

	}

	@Override
	public ListAdapter getAdapter() {
		return mAdapter;
	}


	@Override
	public View getSelectedView() {
		return null;
	}


	@Override
	public void setSelection(int position) {
		// TODO 自動生成されたメソッド・スタブ

	}



}
