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

package org.breakthebot.townyAddons;

import org.bukkit.configuration.file.FileConfiguration;

public class config {
    // /n shout
    public final int maxShoutChars;
    public final int ShoutCooldownHours;
    public final int ShoutPrice;
    // /res set wiki
    public final String wikiURL;
    // /t | /n set discord
    public final String discordURL = "https://discord";
    // DiscordSRV Towny updates channel ID
    public final String discordSRVChannelID;
    // Towny Vault plot type cost
    public final int VaultCost;

    public config(TownyAddons plugin) {
        FileConfiguration config = plugin.getConfig();

        this.maxShoutChars = config.getInt("maxShoutChars", 128);
        this.ShoutCooldownHours = config.getInt("ShoutCooldownHours", 6);
        this.ShoutPrice = config.getInt("ShoutPrice", 128);
        this.wikiURL = config.getString("wikiURL", "https://wiki.earthmc.net/");
        this.discordSRVChannelID = config.getString("discordSRVChannelID", "969978366394511420");
        this.VaultCost = config.getInt("VaultCost", 0);
    }
}