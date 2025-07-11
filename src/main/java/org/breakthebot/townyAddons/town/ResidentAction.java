/*
 * This file is part of TownyAddons.
 *
 * TownyAddons is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TownyAddons is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with TownyAddons. If not, see <https://www.gnu.org/licenses/>.
 */

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
