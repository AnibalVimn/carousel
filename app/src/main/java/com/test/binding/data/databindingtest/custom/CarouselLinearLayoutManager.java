package com.test.binding.data.databindingtest.custom;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * Custom LayoutManager that will divide the screen into three columns when it comes to scaling its visible
 * items on screen. It will scale up and down the items as they enter or leave the
 * column in the middle correspondingly.
 */
public class CarouselLinearLayoutManager extends LinearLayoutManager {

    private static final String TAG = "LayoutManager";

    private static final float HIGH_SCALE = 1.5f;

    private static final float NORMAL_SCALE = 1.0f;

    private boolean mFirstTime = true;

    /**
     * One third of the total width of the layout.
     */
    private int mOneThirdWidth;

    /**
     * Two thirds of the total width of the layout.
     */
    private int mTwoThirdsWidth;

    public CarouselLinearLayoutManager(Context context, int orientation, boolean reverseLayout)    {
        super(context, orientation, reverseLayout);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);

        if (mFirstTime) {

            // The Layout will consist of three columns when it comes to scaling. The views will only
            //scale when they enter/leave the one in the middle.
            mOneThirdWidth = getWidth() / 3;
            mTwoThirdsWidth = mOneThirdWidth * 2;

            for (int i = findFirstVisibleItemPosition(); i <= findLastVisibleItemPosition(); i++) {
                View view = findViewByPosition(i);

                int left = view.getLeft();

                if (left <= mTwoThirdsWidth && left >= mOneThirdWidth) {
                    scaleView(view, mTwoThirdsWidth, mOneThirdWidth, left, NORMAL_SCALE, HIGH_SCALE);
                } else if (left >= 0 && left <= mOneThirdWidth) {
                    scaleView(view, mOneThirdWidth, 0, left, HIGH_SCALE, NORMAL_SCALE);
                }
            }

            mFirstTime = false;
        }
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {

        Log.d(TAG, "scroll by " + dx + ", onethird: " + mOneThirdWidth + ", twothirds: " + mTwoThirdsWidth);

        for (int i = findFirstVisibleItemPosition(); i <= findLastVisibleItemPosition(); i++) {
            View view = findViewByPosition(i);

            int left = view.getLeft();
            int right = view.getRight();

            if (dx > 0) {

                // Move to the left
                Log.d(TAG, "view position " +  i + ", left: " + view.getLeft());

                if (left <= mTwoThirdsWidth && left >= mOneThirdWidth && right >= mTwoThirdsWidth) {
                    // View scale up
                    scaleView(view, mTwoThirdsWidth, mTwoThirdsWidth - view.getWidth() , left, NORMAL_SCALE, HIGH_SCALE);

                } else if (left >= 0 && left <= mOneThirdWidth && right >= mOneThirdWidth) {
                    // View scale down
                    scaleView(view, mOneThirdWidth, mOneThirdWidth - view.getWidth(), left, HIGH_SCALE, NORMAL_SCALE);
                }

            } else {

                // Move to the right
                Log.d(TAG, "view position " +  i + ", right: " + view.getRight());

                if (right >= mOneThirdWidth && right <= mTwoThirdsWidth && left <= mOneThirdWidth) {
                    // View scale up
                    scaleView(view, mOneThirdWidth + view.getWidth(), mOneThirdWidth, right, HIGH_SCALE, NORMAL_SCALE);

                } else if (right >= mTwoThirdsWidth && right <= getWidth() && left <= mTwoThirdsWidth) {
                    // View scale down
                    scaleView(view, mTwoThirdsWidth + view.getWidth(), mTwoThirdsWidth, right, NORMAL_SCALE, HIGH_SCALE);
                }
            }
        }

        return super.scrollHorizontallyBy(dx, recycler, state);
    }

    // If you are a mathematician or physicist maybe you could come up with a better equation to calculate the scale ;)
    private float scaleView(View view, int maxDistante, int minDistance, int distance, float maxDistanceScale, float minDistanceScale) {
        float scale = (float) (minDistanceScale + (double) (((distance - minDistance) * (maxDistanceScale - minDistanceScale)) / (maxDistante - minDistance)));
        view.setScaleX(scale);
        view.setScaleY(scale);

        return scale;
    }
}
