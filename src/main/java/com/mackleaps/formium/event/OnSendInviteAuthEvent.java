package com.mackleaps.formium.event;

import com.mackleaps.formium.model.auth.User;
import org.springframework.context.ApplicationEvent;

public class OnSendInviteAuthEvent extends ApplicationEvent {

    private User user;

    public OnSendInviteAuthEvent(String email) {
        super(email);
//        this.user = new User(email);
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
