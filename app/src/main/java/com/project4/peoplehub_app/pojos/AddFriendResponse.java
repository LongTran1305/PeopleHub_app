package com.project4.peoplehub_app.pojos;

public class AddFriendResponse {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AddFriendResponse(String message) {
        this.message = message;
    }
}
