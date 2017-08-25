package com.simplechat.feature.chat.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import butterknife.BindView;
import com.simplechat.R;
import com.simplechat.feature.chat.model.ChatMessage;
import com.simplechat.shared.ui.viewholder.RecyclerViewHolder;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerViewHolder<ChatMessage>> {

  private enum ViewType {
    SENT_TEXT, RECEIVED_TEXT
  }

  private final Context context;
  private final List<ChatMessage> chatMessages;
  private int lastAnimatedPosition;

  public ChatAdapter(final Context context, final List<ChatMessage> chatMessages) {
    this.context = context;
    this.chatMessages = chatMessages;
    lastAnimatedPosition = -1;
  }

  @Override
  public RecyclerViewHolder<ChatMessage> onCreateViewHolder(final ViewGroup parent,
      final int viewTypeOrdinal) {
    final ViewType viewType = ViewType.values()[viewTypeOrdinal];
    switch (viewType) {
      case SENT_TEXT:
        return TextMessageViewHolder.newInstanceForSentMessage(parent);

      case RECEIVED_TEXT:
        return TextMessageViewHolder.newInstanceForReceivedMessage(parent);

      default:
        throw new IllegalStateException("Unhandled view type: " + viewType);
    }
  }

  @Override
  public void onBindViewHolder(final RecyclerViewHolder<ChatMessage> holder, final int position) {
    holder.bindData(getItem(position), position);
    animateItem(holder, position);
  }

  @Override
  public int getItemCount() {
    return chatMessages.size();
  }

  @Override
  public int getItemViewType(int position) {
    final ChatMessage message = getItem(position);
    switch (message.getMessageType()) {
      case SENT:
        return ViewType.SENT_TEXT.ordinal();

      case RECEIVED:
        return ViewType.RECEIVED_TEXT.ordinal();

      default:
        throw new IllegalStateException("Unhandled message type: " + message.getMessageType());
    }
  }

  @Override
  public void onViewDetachedFromWindow(RecyclerViewHolder<ChatMessage> holder) {
    holder.itemView.clearAnimation();
  }

  public void addMessage(final ChatMessage message) {
    chatMessages.add(message);
    notifyItemInserted(chatMessages.size() - 1);
  }

  private void animateItem(RecyclerViewHolder<ChatMessage> holder, int position) {
    /*
    To ensure that we only animate the newer items, we're keeping a field to track the last
    animated position. We only animate items that have a position greater than the last animated
    one.
     */
    if (position <= lastAnimatedPosition) {
      return;
    }

    final ViewType viewType = ViewType.values()[getItemViewType(position)];
    final int animationResId;
    switch (viewType) {
      case SENT_TEXT:
        animationResId = R.anim.fade_in_scale_left;
        break;
      case RECEIVED_TEXT:
        animationResId = R.anim.fade_in_scale_right;
        break;
      default:
        animationResId = 0;
    }

    //noinspection ConstantConditions
    if (animationResId != 0) {
      holder.itemView.startAnimation(AnimationUtils.loadAnimation(context, animationResId));
      lastAnimatedPosition = position;
    }
  }

  private ChatMessage getItem(final int position) {
    return chatMessages.get(position);
  }

  static class TextMessageViewHolder extends RecyclerViewHolder<ChatMessage> {

    static TextMessageViewHolder newInstanceForSentMessage(final ViewGroup container) {
      return new TextMessageViewHolder(container, R.layout.sent_text_message);
    }

    static TextMessageViewHolder newInstanceForReceivedMessage(final ViewGroup container) {
      return new TextMessageViewHolder(container, R.layout.received_text_message);
    }

    @BindView(R.id.message_text) TextView messageTextView;

    private TextMessageViewHolder(ViewGroup container, int layoutResId) {
      super(container, layoutResId);
    }

    @Override
    public void bindData(ChatMessage item, int position) {
      messageTextView.setText(item.getMessageBody());
    }
  }
}
