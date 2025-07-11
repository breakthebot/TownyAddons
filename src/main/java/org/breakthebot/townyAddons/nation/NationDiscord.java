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

package org.breakthebot.townyAddons.nation;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.metadata.CustomDataField;
import com.palmergames.bukkit.towny.object.metadata.StringDataField;
import org.breakthebot.townyAddons.TownyAddons;
import org.breakthebot.townyAddons.config;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class NationDiscord implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            TownyMessaging.sendErrorMsg(sender, "Only players may use this command.");
            return false;
        }

        if (!player.hasPermission("towny.command.nation.set.discord")) {
            TownyMessaging.sendMsg(player, "You do not have permission to perform this command.");
        }

        TownyAPI API = TownyAPI.getInstance();
        Resident res = API.getResident(player);
        assert res != null;
        if (!res.hasNation()) {
            TownyMessaging.sendErrorMsg(player, "You must be part of a nation.");
            return false;
        }
        Nation nation = res.getNationOrNull();
        assert nation != null;
        config settings = TownyAddons.getInstance().getConfiguration();

        if (args.length == 0) {
            if (!nationHasDiscord(nation)) {
                TownyMessaging.sendErrorMsg(player, "You haven't linked a discord.");
                return false;
            }
            TownyMessaging.sendMsg(player, "Discord unlinked.");
            nationRemoveDiscord(nation);
            return true;
        }

        String discordURL = args[0];
        if (!discordURL.startsWith(settings.discordURL)) {
            TownyMessaging.sendErrorMsg(player, "Invalid discord invite");
            return false;
        }

        setNationDiscord(nation, discordURL);
        TownyMessaging.sendMsg(player, "Discord linked!");
        return true;
    }

    private static final String nationDiscord_KEY = "discord_url";

    public static void setNationDiscord(@NotNull Nation nation, String url) {
        if (nation.hasMeta(nationDiscord_KEY)) {
            nation.removeMetaData(nationDiscord_KEY);
        }
        StringDataField field = new StringDataField(nationDiscord_KEY, url);
        nation.addMetaData(field);
        nation.save();
    }

    public static String getNationDiscord(@NotNull Nation nation) {
        if (!nation.hasMeta(nationDiscord_KEY)) {
            return "";
        }

        CustomDataField<?> field = nation.getMetadata(nationDiscord_KEY);
        if (field instanceof StringDataField s) {
            return s.getValue();
        }
        return "";
    }

    public static boolean nationHasDiscord(@NotNull Nation nation) {
        return nation.hasMeta(nationDiscord_KEY);
    }

    public static void nationRemoveDiscord(@NotNull Nation nation) {
        if (!nation.hasMeta(nationDiscord_KEY)) {
            return;
        }
        nation.removeMetaData(nationDiscord_KEY);
        nation.save();
    }


}
