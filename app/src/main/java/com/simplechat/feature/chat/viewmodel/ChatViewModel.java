package com.simplechat.feature.chat.viewmodel;

import android.os.Bundle;
import com.simplechat.feature.chat.model.ChatMessage;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ChatViewModel {

  private static final String EXTRA_CHAT_MESSAGES = "chat_messages";

  private final Subject<ChatMessage> chatMessageSubject;
  private final List<ChatMessage> chatMessages;

  public ChatViewModel(final Bundle savedInstanceState) {
    chatMessageSubject = PublishSubject.create();
    chatMessages = savedInstanceState == null ? new ArrayList<>()
        : savedInstanceState.getParcelableArrayList(EXTRA_CHAT_MESSAGES);
  }

  public void sendMessage(final ChatMessage sentMessage) {
    /*
    Sending back a received message with the same text with a delay of 1 second.
     */
    final ChatMessage receivedMessage =
        ChatMessage.newInstanceForReceivedTextMessage(sentMessage.getMessageBody());

    Observable.just(receivedMessage)
        .delay(1, TimeUnit.SECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(chatMessageSubject::onNext);

    chatMessages.add(sentMessage);
    chatMessages.add(receivedMessage);
  }

  public void saveInstanceState(final Bundle outState) {
    outState.putParcelableArrayList(EXTRA_CHAT_MESSAGES, new ArrayList<>(chatMessages));
  }

  public Subject<ChatMessage> getChatMessageSubject() {
    return chatMessageSubject;
  }

  public List<ChatMessage> getChatMessages() {
    return chatMessages;
  }
}
