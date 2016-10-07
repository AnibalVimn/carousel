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

            // TODO Make number of items generic
            mOneThirdWidth = getWidth() / 3;
            mTwoThirdsWidth = mOneThirdWidth * 2;

            for (int i = findFirstVisibleItemPosition(); i <= findLastVisibleItemPosition(); i++) {
                View view = findViewByPosition(i);
                if (view.getLeft() <= mTwoThirdsWidth && view.getLeft() >= mOneThirdWidth) {
                    scaleView(view, mTwoThirdsWidth, mOneThirdWidth, view.getLeft(), NORMAL_SCALE, HIGH_SCALE);
                } else if (view.getLeft() >= 0 && view.getLeft() <= mOneThirdWidth) {
                    scaleView(view, mOneThirdWidth, 0, view.getLeft(), HIGH_SCALE, NORMAL_SCALE);
                }
            }

            mFirstTime = false;
        }
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {

        Log.d(TAG, "scroll by " + dx + ", first visible item: " + findFirstVisibleItemPosition()
                + ", last visible item: " + findLastVisibleItemPosition());

        for (int i = findFirstVisibleItemPosition(); i <= findLastVisibleItemPosition(); i++) {
            View view = findViewByPosition(i);

            if (dx > 0) {

                Log.d(TAG, "view position " +  i + ", left: " + view.getLeft());

                // Move to the left
                int left = view.getLeft();

                if (left <= mTwoThirdsWidth && left >= mOneThirdWidth) {
                    // View scale up
                    scaleView(view, mTwoThirdsWidth, mOneThirdWidth, left, NORMAL_SCALE, HIGH_SCALE);

                } else if (left >= 0 && left <= mOneThirdWidth) {
                    // View scale down
                    scaleView(view, mOneThirdWidth, 0, left, HIGH_SCALE, NORMAL_SCALE);
                }
            } else {

                Log.d(TAG, "view position " +  i + ", right: " + view.getRight());

                // Move to the right
                int right = view.getRight();

                if (right >= mOneThirdWidth && right <= mTwoThirdsWidth) {
                    // View scale up
                    scaleView(view, mTwoThirdsWidth, mOneThirdWidth, right, HIGH_SCALE, NORMAL_SCALE);

                } else if (right >= mTwoThirdsWidth && right <= getWidth()) {
                    // View scale down
                    scaleView(view, getWidth(), mTwoThirdsWidth, right, NORMAL_SCALE, HIGH_SCALE);
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
