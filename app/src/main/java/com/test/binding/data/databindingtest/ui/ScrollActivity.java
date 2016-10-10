package com.test.binding.data.databindingtest.ui;

import android.graphics.Point;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;

import com.test.binding.data.databindingtest.R;
import com.test.binding.data.databindingtest.adapter.ScrollAdapter;
import com.test.binding.data.databindingtest.custom.CarouselLinearLayoutManager;
import com.test.binding.data.databindingtest.custom.CarouselRecyclerView;
import com.test.binding.data.databindingtest.model.User;

import java.util.ArrayList;
import java.util.List;

public class ScrollActivity extends AppCompatActivity
        implements CarouselRecyclerView.CarouselScrollListener, ViewPager.OnPageChangeListener {

    /**
     * Number pages in the ViewPager.
     */
    private static final int NUM_PAGES = 4;

    private static final String TAG = "ScrollActivity";

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    /**
     * Width of the screen (It should be the width of the ViewPager though).
     */
    private int mWidth;

    /**
     * The last offset the listener received scaled to the child dimensions.
     */
    private int mLastPagerChildOffset;

    /**
     * Current state of the pager.
     */
    private int mPagerState;

    /**
     * The last pager offset the listener received.
     */
    private float mLastPagerPositionOffset;

    /**
     * The last known position of the pager.
     */
    private int mLastPagerPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        mWidth = size.x;

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.addOnPageChangeListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);
        ((CarouselRecyclerView) mRecyclerView).setScrollListener(this);
        mLayoutManager = new CarouselLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        List<User> list = createList();

        mAdapter = new ScrollAdapter(list, mWidth, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onCarouselScrolled(MotionEvent e) {
        mPager.dispatchTouchEvent(e);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        Log.d(TAG, "position: " + position + ", offset: " + positionOffsetPixels + ", current item: " + mPager.getCurrentItem());

        int widthPager = mPager.getWidth();

        // TODO Make number of items generic
        //int widthRecyclerViewItem = widthPager / 3;
        int widthRecyclerViewItem = getResources().getDimensionPixelSize(R.dimen.item_width);

        // No more scrolling => go to item
        if (positionOffsetPixels == 0) {
            mLastPagerChildOffset = 0;
            mLastPagerPosition = position;
            mLastPagerPositionOffset = positionOffset;
            return;
        }

        // Page has changed and offset restarted while dragging
        if (mPagerState == ViewPager.SCROLL_STATE_DRAGGING && position != mLastPagerPosition && positionOffset != mLastPagerPositionOffset) {
            if (position < mLastPagerPosition) {
                // While dragging to the left
                mLastPagerChildOffset = widthRecyclerViewItem;
            } else {
                // While dragging to the right
                mLastPagerChildOffset = 0;
            }
        }

        // If one page of the pager moves X pixels => we move one item from the RecyclerView the proportional pixels
        int childOffset = positionOffsetPixels * widthRecyclerViewItem / widthPager;
        int offset = childOffset - mLastPagerChildOffset;

        Log.d(TAG, "childOffset: " + offset);

        mRecyclerView.scrollBy(offset, 0);

        if (mPagerState == ViewPager.SCROLL_STATE_DRAGGING) {
            mLastPagerPosition = position;
            mLastPagerPositionOffset = positionOffset;
        }

        mLastPagerChildOffset = childOffset;
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.d(TAG, "state: " + state);
        mPagerState = state;
    }

    /**
     * Create some fake data to be displayed.
     */
    private List<User> createList() {

        List<User> list = new ArrayList<>();
        User user = new User("Juan", "Ortega");
        list.add(user);
        user = new User("Julio", "Lopez");
        list.add(user);
        user = new User("Mario", "Vaquerizo");
        list.add(user);
        user = new User("John", "Lennon");
        list.add(user);

        return list;
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new ScrollFragment();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
