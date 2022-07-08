package ru.yandex.practicum.filmorate.model.referencebook;

import lombok.Data;

@Data
public class FriendStatus {
    private final int statusId;
    private String statusName;

    public FriendStatus (int statusId) {
        this.statusId = statusId;
    }
}
