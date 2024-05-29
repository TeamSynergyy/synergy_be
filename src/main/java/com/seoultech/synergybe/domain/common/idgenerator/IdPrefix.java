package com.seoultech.synergybe.domain.common.idgenerator;

import lombok.Getter;

@Getter
public enum IdPrefix {
    USER("user"),
    APPLY("apply"),
    COMMENT("comment"),
    FOLLOW("follow"),
    NOTICE("notice"),
    POST("post"),
    POST_LIKE("post_like"),
    PROJECT("project"),
    PROJECT_LIKE("project_like"),
    PROJECT_USER("project_user"),
    RATE("rate"),
    SCHEDULE("schedule"),
    PUBLIC_LIBRARY("public_library"),
    SMALL_LIBRARY("small_library"),
    CHAT_ROOM("chat_room"),
    TICKET("ticket"),
    TICKET_USER("ticket_user");


    private final String value;

    IdPrefix(String value) {
        this.value = value;
    }
}
