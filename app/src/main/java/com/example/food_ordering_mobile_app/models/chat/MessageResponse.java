package com.example.food_ordering_mobile_app.models.chat;
import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageResponse implements Serializable{
    private Chat chat;
    private List<Message> messages;
    @Override
    public String toString() {
        return "MessageResponse{" +
                "chat=" + chat +
                ", messages=" + messages +
                '}';
    }
}
