package de.zorcic.entity;

import java.util.UUID;

public class NewGameEvent {

    private UUID gameId;

    public NewGameEvent(UUID gameId) {
        this.gameId = gameId;
    }

    public UUID gameId() {
        return gameId;
    }

}
