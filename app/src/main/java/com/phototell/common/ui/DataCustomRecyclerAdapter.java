package com.phototell.common.ui;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.phototell.ui.views.ViewsFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Single adapter for adapting all CustomView object.
 * Note: data binding is done on the view itself using CustomView.bind(D data) interface
 * */
public class DataCustomRecyclerAdapter extends RecyclerView.Adapter<CustomViewHolder> {

    private final List<Object> items;
    private final List<Class> dataTypeIndex;
    private final Map<Class, Class> viewsHash;//key: data type class, value: custom view class.
    private final CustomViewHolder.ClickListener clickListener;

    @Nullable
    View.OnLongClickListener longClickListener;
    private CustomViewHolder.ClickListener onItemClickListener;

    public DataCustomRecyclerAdapter(@NonNull Map<Class, Class> viewsHash,
                                     @NonNull CustomViewHolder.ClickListener clickListener) {
        this.viewsHash = viewsHash;
        this.clickListener = clickListener;
        dataTypeIndex = new ArrayList<>(viewsHash.keySet());
        items = new ArrayList();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Class dataClass = dataTypeIndex.get(viewType);
        CustomView customView = ViewsFactory.getInstance().getCustomView(viewsHash.get(dataClass), viewGroup.getContext());
        CustomViewHolder vh = customView != null ? new CustomViewHolder(customView, clickListener) : null;
        return vh;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder vh, int position) {
        vh.getCustomView().bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        //make sure the adapter knows to look for all our items, and footers
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = items.get(position);
        return dataTypeIndex.indexOf(item.getClass());
    }

    public void removeItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    public void setItems(@NonNull List<?> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public List<Object> getItems() {
        return this.items;
    }

    public Object getItem(int position) {
        if (position < 0 || position >= items.size()) {
            return null;
        }
        return items.get(position);
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }
}