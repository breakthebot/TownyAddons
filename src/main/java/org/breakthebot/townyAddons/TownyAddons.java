package org.breakthebot.townyAddons;

import com.palmergames.bukkit.towny.TownyCommandAddonAPI;
import org.breakthebot.townyAddons.discordSRV.TownyMessageBroadcast;
import org.breakthebot.townyAddons.nation.AnnounceNationRename;
import org.breakthebot.townyAddons.nation.NationDiscord;
import org.breakthebot.townyAddons.nation.NationStatusScreenListener;
import org.breakthebot.townyAddons.nation.Shout;
import org.breakthebot.townyAddons.plot.OnClaimListener;
import org.breakthebot.townyAddons.plot.Vault;
import org.breakthebot.townyAddons.resident.*;
import com.palmergames.bukkit.towny.object.AddonCommand;
import org.breakthebot.townyAddons.town.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

import static org.breakthebot.townyAddons.plot.Vault.registerVaultPlot;


public final class TownyAddons extends JavaPlugin {
    private static TownyAddons instance;
    private config configuration;



    @Override
    public void onEnable() {
        getLogger().info("TownyAddons has been enabled!");
        startup();

        saveDefaultConfig();
        configuration = new config(this);
    }

    @Override
    public void onLoad() {
        instance = this;
        registerVaultPlot();
    }

    @Override
    public void onDisable() {
        instance = null;
        getLogger().info("TownyAddons has been disabled.");
    }

    public static TownyAddons getInstance() {
        return instance;
    }

    @Override
    public @NotNull Logger getLogger() {
        return super.getLogger();
    }

    public config getConfiguration() { return this.configuration; }


    public void startup() {
        TownyCommandAddonAPI.CommandType resType = TownyCommandAddonAPI.CommandType.RESIDENT;
        TownyCommandAddonAPI.CommandType townType = TownyCommandAddonAPI.CommandType.TOWN;
        TownyCommandAddonAPI.CommandType nationType = TownyCommandAddonAPI.CommandType.NATION;

        command(new AddonCommand(resType, "trustlist", new TrustList()));
        command(new AddonCommand(resType, "onlinefriends", new OnlineFriends()));
        command(new AddonCommand(resType, "outlawednations", new OutlawedNations()));
        command(new AddonCommand(townType, "resetperms", new ResetPerms()));
        command(new AddonCommand(nationType, "shout", new Shout()));
        command(new AddonCommand(resType, "wiki", new Wiki()));
        command(new AddonCommand(townType, "discord", new TownDiscord()));
        command(new AddonCommand(nationType, "discord", new NationDiscord()));
//        command(new AddonCommand(TownyCommandAddonAPI.CommandType.TOWN_SET, "plotlimit", new PlotLimit()));
        command(new AddonCommand(TownyCommandAddonAPI.CommandType.TOWN_TOGGLE, "friendlyfire", new FriendlyFire()));
        command(new AddonCommand(townType, "log", new TownLog()));

        event(new OverclaimNotify(), this);
        event(new AnnounceNationRename(), this);
        event(new ClearInvites(), this);
        event(new NationStatusScreenListener(), this);
        event(new TownStatusScreenListener(), this);
        event(new ResidentStatusScreenListener(), this);
        event(new OnClaimListener(), this);
        event(new FriendlyFireCheck(), this);
        event(new TownyMessageBroadcast(), this);
        event(new TownLog(), this);
        event(new Vault(), this);
    }

    public void command(AddonCommand command) {
        TownyCommandAddonAPI.addSubCommand(command);
    }
    public void event(Listener listener, Plugin plugin) {
        getServer().getPluginManager().registerEvents(listener, plugin);
    }
}