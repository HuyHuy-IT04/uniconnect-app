package com.example.demobtl.GiangVien.Chat;

public class Group {
    public String groupId;
    public String name;
    public String ownerId;
    public long createdAt;
    public int memberCount;

    public Group() {}

    public Group(String groupId, String name, String ownerId, long createdAt, int memberCount) {
        this.groupId = groupId;
        this.name = name;
        this.ownerId = ownerId;
        this.createdAt = createdAt;
        this.memberCount = memberCount;
    }
}
