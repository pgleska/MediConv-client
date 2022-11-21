package com.github.pgleska.dtos;

import java.time.LocalDateTime;

public class MessageDTO {
    private Integer id;
    private LocalDateTime timestamp;
    private Integer authorId;
    private Integer receiverId;
    private String content;
    private String sharedKeyEncryptedWithAuthorPKey;
    private String sharedKeyEncryptedWithReceiverPKey;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSharedKeyEncryptedWithAuthorPKey() {
        return sharedKeyEncryptedWithAuthorPKey;
    }

    public void setSharedKeyEncryptedWithAuthorPKey(String sharedKeyEncryptedWithAuthorPKey) {
        this.sharedKeyEncryptedWithAuthorPKey = sharedKeyEncryptedWithAuthorPKey;
    }

    public String getSharedKeyEncryptedWithReceiverPKey() {
        return sharedKeyEncryptedWithReceiverPKey;
    }

    public void setSharedKeyEncryptedWithReceiverPKey(String sharedKeyEncryptedWithReceiverPKey) {
        this.sharedKeyEncryptedWithReceiverPKey = sharedKeyEncryptedWithReceiverPKey;
    }
}
