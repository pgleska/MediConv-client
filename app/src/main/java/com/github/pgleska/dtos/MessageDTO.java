package com.github.pgleska.dtos;

public class MessageDTO {
    private Integer id;
    private String timestamp;
    private Integer authorId;
    private Integer receiverId;
    private String authorName;
    private String receiverName;
    private String content;
    private String authorSecretKey;
    private String receiverSecretKey;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthorSecretKey() {
        return authorSecretKey;
    }

    public void setAuthorSecretKey(String authorSecretKey) {
        this.authorSecretKey = authorSecretKey;
    }

    public String getReceiverSecretKey() {
        return receiverSecretKey;
    }

    public void setReceiverSecretKey(String receiverSecretKey) {
        this.receiverSecretKey = receiverSecretKey;
    }
}
