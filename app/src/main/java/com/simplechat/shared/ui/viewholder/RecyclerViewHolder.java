package com.simplechat.shared.ui.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import butterknife.ButterKnife;

public abstract class RecyclerViewHolder<T> extends RecyclerView.ViewHolder {

  protected final Context context;

  public RecyclerViewHolder(final ViewGroup container, @LayoutRes final int layoutResId) {
    super(LayoutInflater.from(container.getContext()).inflate(layoutResId, container, false));
    this.context = itemView.getContext();
    ButterKnife.bind(this, itemView);
  }

  public abstract void bindData(final T item, final int position);
}
