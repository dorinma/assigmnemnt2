package bgu.spl.mics.example.messages;

import bgu.spl.mics.Broadcast;

public class ExampleBroadcast implements Broadcast<Number> {

    private String senderId;

    public ExampleBroadcast(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderId() {
        return senderId;
    }

}
