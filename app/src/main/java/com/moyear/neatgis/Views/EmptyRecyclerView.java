package com.moyear.neatgis.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class EmptyRecyclerView extends RecyclerView {

    private View mEmptyView;

    private AdapterDataObserver emptyObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();

            Adapter<?> adapter = getAdapter();
            if (adapter != null && mEmptyView != null) {

                if (adapter.getItemCount() == 0) {
                    mEmptyView.setVisibility(VISIBLE);
                    EmptyRecyclerView.this.setVisibility(GONE);
                } else {
                    mEmptyView.setVisibility(GONE);
                    EmptyRecyclerView.this.setVisibility(VISIBLE);
                }
            }
        }
    };
    public EmptyRecyclerView(@NonNull Context context) {
        super(context);
    }

    public EmptyRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /****通过这个方法设置空布局***/
    public void setEmptyView(View view) {
        mEmptyView = view;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter != null) {
            //这里用了观察者模式，同时把这个观察者添加进去。
            adapter.registerAdapterDataObserver(emptyObserver);
        }
        if (emptyObserver != null) {
            emptyObserver.onChanged();
        }
    }

}
