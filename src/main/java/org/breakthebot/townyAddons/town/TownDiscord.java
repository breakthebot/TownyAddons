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

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.metadata.CustomDataField;
import com.palmergames.bukkit.towny.object.metadata.StringDataField;
import org.breakthebot.townyAddons.TownyAddons;
import org.breakthebot.townyAddons.config;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TownDiscord implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            TownyMessaging.sendErrorMsg(sender, "Only players may use this command.");
            return false;
        }

        if (!player.hasPermission("towny.command.town.set.discord")) {
            TownyMessaging.sendMsg(player, "You do not have permission to perform this command.");
            return false;
        }

        TownyAPI API = TownyAPI.getInstance();
        Resident res = API.getResident(player);
        assert res != null; // Any player issuing a command is a registered Towny resident
        if (!res.hasTown()) {
            TownyMessaging.sendErrorMsg(player, "You must be part of a town.");
            return false;
        }
        Town town = res.getTownOrNull();
        assert town != null; // if res.hasTown() is false it returns before reaching here
        config settings = TownyAddons.getInstance().getConfiguration();

        if (args.length == 0) {
            if (!townHasDiscord(town)) {
                TownyMessaging.sendErrorMsg(player, "You haven't linked a discord.");
                return false;
            }
            TownyMessaging.sendMsg(player, "Discord unlinked.");
            townRemoveDiscord(town);
            return true;
        }

        String discordURL = args[0];
        if (!discordURL.startsWith(settings.discordURL)) {
            TownyMessaging.sendErrorMsg(player, "Invalid discord invite");
            return false;
        }

        setTownDiscord(town, discordURL);
        TownyMessaging.sendMsg(player, "Discord linked!");
        return true;
    }

    private static final String townDiscord_KEY = "discord_url";


    
    public static void setTownDiscord(@NotNull Town town, String url) {
        if (town.hasMeta(townDiscord_KEY)) {
            town.removeMetaData(townDiscord_KEY);
        }
        StringDataField field = new StringDataField(townDiscord_KEY, url);
        town.addMetaData(field);
        town.save();
    }

    public static String getTownDiscord(@NotNull Town town) {
        if (!town.hasMeta(townDiscord_KEY)) {
            return "";
        }

        CustomDataField<?> field = town.getMetadata(townDiscord_KEY);
        if (field instanceof StringDataField s) {
            return s.getValue();
        }
        return "";
    }

    public static boolean townHasDiscord(@NotNull Town town) {
        return town.hasMeta(townDiscord_KEY);
    }

    public static void townRemoveDiscord(@NotNull Town town) {
        if (!town.hasMeta(townDiscord_KEY)) {
            return;
        }
        town.removeMetaData(townDiscord_KEY);
        town.save();
    }
}
