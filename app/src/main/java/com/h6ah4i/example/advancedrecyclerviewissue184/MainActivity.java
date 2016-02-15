package com.h6ah4i.example.advancedrecyclerviewissue184;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        Tracker tracker = application.getDefaultTracker();
        tracker.setScreenName("MainActivity");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());


        RecyclerView rv = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerViewDragDropManager dndmgr = new RecyclerViewDragDropManager();

        dndmgr.setInitiateOnLongPress(true);
        dndmgr.setInitiateOnMove(false);

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(dndmgr.createWrappedAdapter(new SampleAdapter()));

        dndmgr.attachRecyclerView(rv);
    }

    private static class SampleAdapter
            extends RecyclerView.Adapter<ViewHolder>
            implements DraggableItemAdapter<ViewHolder> {
        private List<Integer> mData;

        public SampleAdapter() {
            mData = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                mData.add(i);
            }

            setHasStableIds(true);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ((TextView) holder.itemView).setText(Integer.toString(mData.get(position)));
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        @Override
        public long getItemId(int position) {
            return mData.get(position);
        }

        @Override
        public boolean onCheckCanStartDrag(ViewHolder holder, int position, int x, int y) {
            return true;
        }

        @Override
        public ItemDraggableRange onGetItemDraggableRange(ViewHolder holder, int position) {
            return null;
        }

        @Override
        public void onMoveItem(int fromPosition, int toPosition) {
            Integer removed = mData.remove(fromPosition);
            mData.add(toPosition, removed);
            notifyItemMoved(fromPosition, toPosition);
        }
    }

    private static class ViewHolder extends AbstractDraggableItemViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
