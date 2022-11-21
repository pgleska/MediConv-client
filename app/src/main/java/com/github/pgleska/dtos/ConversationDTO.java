package com.github.pgleska.dtos;

public class ConversationDTO {
    private UserDTO recipient;
    private String lastMessageEncrypted;
    private String plainLastMessage;
    private String date;

    public UserDTO getRecipient() {
        return recipient;
    }

    public void setRecipient(UserDTO recipient) {
        this.recipient = recipient;
    }

    public String getLastMessageEncrypted() {
        return lastMessageEncrypted;
    }

    public void setLastMessageEncrypted(String lastMessageEncrypted) {
        this.lastMessageEncrypted = lastMessageEncrypted;
    }

    public String getPlainLastMessage(String key) {
        plainLastMessage = decrypt(getLastMessageEncrypted(), key);

        return plainLastMessage;
    }

    public void setPlainLastMessage(String plainLastMessage) {
        this.plainLastMessage = plainLastMessage;
    }

    private String decrypt(String msg, String key) {
        return msg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
