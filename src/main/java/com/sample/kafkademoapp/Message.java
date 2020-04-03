package com.sample.kafkademoapp;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Message {
    private int id;
    private String content;

    public Message(@JsonProperty("id") int id,
                   @JsonProperty("content") String content) {
        this.id = id;
        this.content = content;
    }

}
