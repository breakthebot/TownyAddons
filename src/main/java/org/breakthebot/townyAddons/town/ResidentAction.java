package org.breakthebot.townyAddons.town;

import java.util.UUID;

public class ResidentAction {
    private final UUID residentUUID;
    private final boolean joined;
    private final long timestamp; // store as epoch millis

    public ResidentAction(UUID residentUUID, boolean joined) {
        this.residentUUID = residentUUID;
        this.joined = joined;
        this.timestamp = System.currentTimeMillis();
    }

    public UUID getResidentUUID() {
        return residentUUID;
    }

    public boolean isJoin() {
        return joined;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
