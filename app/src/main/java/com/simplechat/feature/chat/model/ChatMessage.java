package com.simplechat.feature.chat.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Date;

public class ChatMessage implements Parcelable {

  public enum MessageType {
    SENT, RECEIVED
  }

  public static ChatMessage newInstanceForSentTextMessage(final String messageBody) {
    return new ChatMessage(messageBody, MessageType.SENT, new Date());
  }

  public static ChatMessage newInstanceForReceivedTextMessage(final String messageBody) {
    return new ChatMessage(messageBody, MessageType.RECEIVED, new Date());
  }

  private final String messageBody;
  private final MessageType messageType;
  private final Date date;

  private ChatMessage(final String messageBody, final MessageType messageType, Date date) {
    this.messageBody = messageBody;
    this.messageType = messageType;
    this.date = date;
  }

  private ChatMessage(Parcel in) {
    messageBody = in.readString();
    messageType = (MessageType) in.readSerializable();
    date = new Date(in.readLong());
  }

  public String getMessageBody() {
    return messageBody;
  }

  public MessageType getMessageType() {
    return messageType;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(messageBody);
    dest.writeSerializable(messageType);
    dest.writeLong(date.getTime());
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<ChatMessage> CREATOR = new Creator<ChatMessage>() {
    @Override
    public ChatMessage createFromParcel(Parcel in) {
      return new ChatMessage(in);
    }

    @Override
    public ChatMessage[] newArray(int size) {
      return new ChatMessage[size];
    }
  };
}
