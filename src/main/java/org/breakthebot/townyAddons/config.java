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
        this.ShoutCooldownHours = config.getInt("ShoutCooldownHours", 21600);
        this.ShoutPrice = config.getInt("ShoutPrice", 128);
        this.wikiURL = config.getString("wikiURL", "https://wiki.earthmc.net/");
        this.discordSRVChannelID = config.getString("discordSRVChannelID", "969978366394511420");
        this.VaultCost = config.getInt("VaultCost", 0);
    }
}