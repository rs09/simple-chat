package com.simplechat.feature.chat.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.simplechat.R;
import com.simplechat.feature.chat.model.ChatMessage;
import com.simplechat.feature.chat.ui.adapter.ChatAdapter;
import com.simplechat.feature.chat.viewmodel.ChatViewModel;
import io.reactivex.disposables.CompositeDisposable;

public class ChatActivity extends AppCompatActivity {

  @BindView(R.id.messages_view) RecyclerView messagesRecyclerView;
  @BindView(R.id.message_input) EditText messageEditText;
  @BindView(R.id.send_btn) View sendButton;

  private ChatViewModel chatViewModel;
  private ChatAdapter adapter;
  private CompositeDisposable disposable = new CompositeDisposable();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chat);

    ButterKnife.bind(this);

    initViewModel(savedInstanceState);
    setupRecyclerViewAndAdapter();
    setupListeners();
  }

  @Override
  protected void onResume() {
    super.onResume();
    disposable.add(chatViewModel.getChatMessageSubject().subscribe(this::addMessage));
  }

  @Override
  protected void onPause() {
    super.onPause();
    disposable.clear();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    chatViewModel.saveInstanceState(outState);
  }

  private void initViewModel(Bundle savedInstanceState) {
    chatViewModel = new ChatViewModel(savedInstanceState);
  }

  private void setupRecyclerViewAndAdapter() {
    adapter = new ChatAdapter(this, chatViewModel.getChatMessages());

    messagesRecyclerView.setAdapter(adapter);
    messagesRecyclerView.setHasFixedSize(true);
    messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    /*
    Setting to null to have more control over the animation of new messages. The animation logic
    is in the adapter.
     */
    messagesRecyclerView.setItemAnimator(null);
  }

  private void addMessage(final ChatMessage message) {
    adapter.addMessage(message);
    messagesRecyclerView.getLayoutManager().scrollToPosition(adapter.getItemCount() - 1);
  }

  private void setupListeners() {
    sendButton.setOnClickListener(v -> {
      final String messageText = messageEditText.getText().toString();
      final ChatMessage message = ChatMessage.newInstanceForSentTextMessage(messageText);
      messageEditText.setText("");
      chatViewModel.sendMessage(message);
      addMessage(message);
    });

    messageEditText.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
      }

      @Override
      public void afterTextChanged(Editable s) {
        sendButton.setVisibility(TextUtils.isEmpty(s) ? View.GONE : View.VISIBLE);
      }
    });
  }
}
