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

    public CarouselLinearLayoutManager(Context context, int orientation, boolean reverseLayout)    {
        super(context, orientation, reverseLayout);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);

        if (mFirstTime) {

            scrollHorizontallyBy(0, recycler, state);

            mFirstTime = false;
        }
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {

        Log.d(TAG, "scroll by " + dx);

        // One third of the total width of the layout.
        int oneThirdWidth;

        // Two thirds of the total width of the layout.
        int twoThirdsWidth;

        for (int i = findFirstVisibleItemPosition(); i <= findLastVisibleItemPosition(); i++) {
            View view = findViewByPosition(i);

            // The Layout will consist of three columns when it comes to scaling. The views will only
            //scale when they enter/leave the one in the middle.
            oneThirdWidth = (getWidth() - view.getWidth()) / 2;
            twoThirdsWidth = oneThirdWidth + view.getWidth();

            int left = view.getLeft();

            if (left <= twoThirdsWidth && left >= oneThirdWidth) {
                scaleView(view, twoThirdsWidth, oneThirdWidth, left, NORMAL_SCALE, HIGH_SCALE);
            } else if (left >= 0 && left <= oneThirdWidth) {
                scaleView(view, oneThirdWidth, 0, left, HIGH_SCALE, NORMAL_SCALE);
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
