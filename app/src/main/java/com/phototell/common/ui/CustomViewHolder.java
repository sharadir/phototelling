package com.phototell.common.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.phototell.ui.views.PhotoListItemCard;

/**
 * For supporting {@link com.phototell.common.ui.DataCustomRecyclerAdapter}
 * */
public class CustomViewHolder<D> extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private final ClickListener clickListener;

    private final CustomView customView;

    public CustomViewHolder(@NonNull CustomView customView, @NonNull CustomViewHolder.ClickListener clickListener) {
        super(customView.getView());
        this.customView = customView;
        this.clickListener = clickListener;
        if(itemView instanceof PhotoListItemCard){//TODO fix this (bug)
            ((PhotoListItemCard)itemView).photoCard.setOnClickListener(this);
        }
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    @NonNull
    public CustomView getCustomView() {
        return customView;
    }

    @Override
    public void onClick(View v) {
        clickListener.onItemClick(getAdapterPosition(), v);
    }

    @Override
    public boolean onLongClick(View v) {
        clickListener.onItemLongClick(getAdapterPosition(), v);
        return false;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }
}