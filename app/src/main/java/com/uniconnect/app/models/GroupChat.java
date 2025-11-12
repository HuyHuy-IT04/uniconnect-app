package com.uniconnect.app.models;

public class GroupChat {
    private String groupId;
    private String groupName;
    private long lastMessageTime;

    public GroupChat() {
        // Required for Firebase
    }

    public GroupChat(String groupId, String groupName, long lastMessageTime) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.lastMessageTime = lastMessageTime;
    }

    public String getGroupId() { return groupId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public long getLastMessageTime() { return lastMessageTime; }
    public void setLastMessageTime(long lastMessageTime) { this.lastMessageTime = lastMessageTime; }
}