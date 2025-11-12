package com.uniconnect.app.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUtil {
    private static DatabaseReference databaseReference;
    private static FirebaseDatabase firebaseDatabase;

    public static DatabaseReference getDatabaseReference() {
        if (databaseReference == null) {
            if (firebaseDatabase == null) {
                firebaseDatabase = FirebaseDatabase.getInstance();
                // Enable offline persistence
                firebaseDatabase.setPersistenceEnabled(true);
            }
            databaseReference = firebaseDatabase.getReference();
        }
        return databaseReference;
    }

    public static DatabaseReference getUsersRef() {
        return getDatabaseReference().child("users");
    }

    public static DatabaseReference getGroupChatRef(String groupId) {
        return getDatabaseReference().child("group_chats").child(groupId);
    }

    public static DatabaseReference getMessagesRef(String groupId) {
        return getGroupChatRef(groupId).child("messages");
    }

    public static String formatTimestamp(long timestamp) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault());
        return sdf.format(new java.util.Date(timestamp));
    }
}