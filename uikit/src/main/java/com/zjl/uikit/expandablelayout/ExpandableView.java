package com.zjl.uikit.expandablelayout;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by zjl on 2018/8/20.
 */
public class ExpandableView extends LinearLayout {
    private final static String TAG = "ExpandableView";

    private RecyclerView mRecyclerView;
    private Adapter mAdapter;


    public ExpandableView(Context context) {
        super(context);
        init(context);
    }

    public ExpandableView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ExpandableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);

        mRecyclerView = new RecyclerView(context);
        mRecyclerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        // don't handle scrolling
        mRecyclerView.setNestedScrollingEnabled(false);

        mRecyclerView.setOverScrollMode(OVER_SCROLL_NEVER);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context) {
            @Override
            public boolean canScrollVertically() {
                // don't handle scrolling
                return false;
            }
        };

        // 使height的wrap_content生效
        layoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(layoutManager);

        addView(mRecyclerView);
    }


    public void setAdapter(final Adapter adapter) {
        if (adapter == null) {
            Log.e(TAG, "exception: adapter is null");
            return;
        }

        if (!adapter.tooFew()) {
            final ToggleHandlerViewHolder viewHolder = adapter.createToggleHandlerViewHolder();
            if (viewHolder != null && viewHolder.mItemView != null) {
                View handlerView = viewHolder.mItemView;
                handlerView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean result = adapter.toggle();
                        if (result) {
                            viewHolder.onToggle(adapter.state.get());
                        }
                    }
                });
                addView(handlerView);
            }
        }

        mAdapter = adapter;
        mRecyclerView.setAdapter(adapter);
    }

//    private void toggle() {
//        if (mAdapter != null && !mAdapter.tooFew()) {
//            boolean result = mAdapter.toggle();
//            if (result) {
//
//            }
//        }
//    }


    public abstract static class Adapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
        private AtomicBoolean state = new AtomicBoolean(false);// false:collapse, true:expand

        public Adapter() {
            if (tooFew()) {
                this.state.set(true);
            }
        }

        @Override
        public final int getItemCount() {
            boolean expand = state.get();
            if (expand) {
                return getTotalCount();
            } else {
                return Math.min(getTotalCount(), getInitialCount());
            }
        }

        public abstract int getTotalCount();

        public abstract int getInitialCount();

        public abstract ToggleHandlerViewHolder createToggleHandlerViewHolder();

        private boolean tooFew() {
            return getTotalCount() <= getInitialCount();
        }

        private boolean toggle() {
            boolean rst = false;

            if (!tooFew()) {
                boolean current = state.get();
                rst = state.compareAndSet(current, !current);

                Log.i(TAG, "Adapter.toggle result=" + rst);

                if (rst) {
                    notifyDataSetChanged();
                }
            }

            return rst;
        }
    }

    public static abstract class ToggleHandlerViewHolder {
        public View mItemView;

        public ToggleHandlerViewHolder(View itemView) {
            mItemView = itemView;
        }

        public abstract void onToggle(boolean isExpand);
    }


}
