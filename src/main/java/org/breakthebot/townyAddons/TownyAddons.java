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

import com.palmergames.bukkit.towny.TownyCommandAddonAPI;
import org.breakthebot.townyAddons.discordSRV.TownyMessageBroadcast;
import org.breakthebot.townyAddons.nation.AnnounceNationRename;
import org.breakthebot.townyAddons.nation.NationDiscord;
import org.breakthebot.townyAddons.nation.NationStatusScreenListener;
import org.breakthebot.townyAddons.nation.Shout;
import org.breakthebot.townyAddons.plot.OnClaimListener;
import org.breakthebot.townyAddons.plot.VaultPermOverride;
import org.breakthebot.townyAddons.plot.VaultPlotRegister;
import org.breakthebot.townyAddons.resident.*;
import com.palmergames.bukkit.towny.object.AddonCommand;
import org.breakthebot.townyAddons.town.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;


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
        VaultPlotRegister.registerVaultPlot();
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
        command(new AddonCommand(townType, "log", new TownLog()));
        command(new AddonCommand(nationType, "shout", new Shout()));
        command(new AddonCommand(TownyCommandAddonAPI.CommandType.RESIDENT_SET, "wiki", new Wiki()));
        command(new AddonCommand(TownyCommandAddonAPI.CommandType.TOWN_SET, "discord", new TownDiscord()));
//        command(new AddonCommand(TownyCommandAddonAPI.CommandType.TOWN_SET, "plotlimit", new PlotLimit()));
        command(new AddonCommand(TownyCommandAddonAPI.CommandType.TOWN_TOGGLE, "friendlyfire", new FriendlyFire()));
        command(new AddonCommand(TownyCommandAddonAPI.CommandType.NATION_SET, "discord", new NationDiscord()));
        command(new AddonCommand(TownyCommandAddonAPI.CommandType.TOWNYADMIN, "shield", new TAOverclaimShield()));
        command(new AddonCommand(TownyCommandAddonAPI.CommandType.TOWN_LIST_BY, "forsale", new ListByForsale()));

        event(new OverclaimNotify());
        event(new AnnounceNationRename());
        event(new ClearInvites());
        event(new NationStatusScreenListener());
        event(new TownStatusScreenListener());
        event(new ResidentStatusScreenListener());
        event(new OnClaimListener());
        event(new FriendlyFireCheck());
        event(new TownyMessageBroadcast());
        event(new TownLog());
        event(new VaultPermOverride());
    }

    public void command(AddonCommand command) {
        TownyCommandAddonAPI.addSubCommand(command);
    }

    public void event(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }
}