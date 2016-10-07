package com.test.binding.data.databindingtest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.test.binding.data.databindingtest.R;
import com.test.binding.data.databindingtest.model.User;

import java.util.List;

public class ScrollAdapter extends RecyclerView.Adapter<ScrollAdapter.ViewHolder> {

    private Context mContext;

    private List<User> mDataset;

    private int mScreenWidth;

    public ScrollAdapter(List<User> data, int screenWidth, Context context) {
        mContext = context;
        mDataset = data;
        mScreenWidth = screenWidth;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public Button mButton;

        public ViewHolder(View v) {
            super(v);
            mButton = (Button) v.findViewById(R.id.name);
        }
    }

    @Override
    public ScrollAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, parent, false);

        // Change the width of the child to always have at least 3 displayed on screen
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) v.getLayoutParams();

        // TODO Make number of items generic
        params.width = mScreenWidth / 3;
        //params.width = mContext.getResources().getDimensionPixelSize(R.dimen.item_width);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (getItemViewType(position) == 0) {
            holder.mButton.setVisibility(View.INVISIBLE);
            holder.mButton.setOnClickListener(null);
        } else {
            holder.mButton.setVisibility(View.VISIBLE);
            holder.mButton.setText(mDataset.get(position - 1).getFirstName());
            holder.mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "HOLA", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == getItemCount() - 1) {
            return 0;
        }
        return 1;
    }

    @Override
    public int getItemCount() {
        return mDataset.size() + 2;
    }
}
