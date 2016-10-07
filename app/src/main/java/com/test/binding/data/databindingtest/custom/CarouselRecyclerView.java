package com.test.binding.data.databindingtest.custom;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Custom RecyclerView that will be able to tell if the user is trying to click on one of its
 * children. If so, it will propagate the event to the child, if not, then it won't scroll, but
 * pass the event to its listener.
 */
public class CarouselRecyclerView extends RecyclerView {

    private static final String TAG = "CarouselRecyclerView";

    private int mTouchSlop;

    private boolean mIsScrolling;

    private float mDownX;

    private int mLastEventAction = 1;

    private CarouselScrollListener mListener;

    public CarouselRecyclerView(Context context) {
        super(context);
        init();
    }

    public CarouselRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CarouselRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init() {
        ViewConfiguration vc = ViewConfiguration.get(getContext());
        mTouchSlop = vc.getScaledTouchSlop();
    }

    public void setScrollListener(CarouselScrollListener listener) {
        mListener = listener;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {

        final int action = MotionEventCompat.getActionMasked(e);

        // Always handle the case of the touch gesture being complete.
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            // Release the scroll.
            mIsScrolling = false;

            return false; // Do not intercept touch event, let the child handle it
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mDownX = e.getRawX();
                mIsScrolling = false;

                return false;
            }
            case MotionEvent.ACTION_MOVE: {
                if (mIsScrolling) {
                    // We're currently scrolling, so yes, intercept the touch event!
                    return true;
                }

                // If the user has dragged her finger horizontally more than the touch slop, start the scroll
                final int xDiff = calculateDistanceX(e);

                if (Math.abs(xDiff) > mTouchSlop) {
                    // Start scrolling!
                    mIsScrolling = true;

                    return true;
                }
                break;
            }
        }

        // In general, we don't want to intercept touch events. They should be handled by the child view.
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        Log.d(TAG, "on touch action: " + e.getAction());

        int action = MotionEventCompat.getActionMasked(e);

        // If this is the first event we are gonna pass to the parent, we need to make sure it's a DOWN
        // event, so any other View that will handle it can start from this point until the end of the
        // movement.
        if (mLastEventAction != MotionEvent.ACTION_MOVE) {
            e.setAction(MotionEvent.ACTION_DOWN);
        }
        if (mListener != null) {
            mListener.onCarouselScrolled(e);
        }
        mLastEventAction = action;

        return true;
    }

    private int calculateDistanceX(MotionEvent ev) {
        return (int) (ev.getRawX() - mDownX);
    }

    public interface CarouselScrollListener {

        void onCarouselScrolled(MotionEvent e);
    }
}
