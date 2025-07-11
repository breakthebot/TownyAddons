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
import com.palmergames.bukkit.towny.object.metadata.BooleanDataField;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FriendlyFire implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            TownyMessaging.sendErrorMsg(sender, "Only players may use this command.");
            return false;
        }

        TownyAPI API = TownyAPI.getInstance();
        Resident res = API.getResident(player);
        assert res != null;
        if (!res.hasTown()) {
            TownyMessaging.sendErrorMsg(player, "You must be in a town.");
            return false;
        }
        if (!player.hasPermission("towny.command.town.toggle.friendlyfire")) {
            TownyMessaging.sendErrorMsg(player, "You do not have permission to perform this command.");
            return false;
        }

        Town town = res.getTownOrNull();
        assert town != null;
        boolean bool;
        if (args.length == 0) {
            bool = !FriendlyFireON(town);
            setFriendlyFire(town, bool);
            TownyMessaging.sendErrorMsg(player, "Successfully toggled Friendly Fire to " + bool);
        } else {
            if (args[0].equalsIgnoreCase("on")) {
                bool = true;
            } else if (args[0].equalsIgnoreCase("off")) {
                bool = false;
            } else {
                TownyMessaging.sendErrorMsg(player, "Usage: /t toggle friendlyfire on|off");
                return false;
            }
            setFriendlyFire(town, bool);
        }
        TownyMessaging.sendMsg(player, "Successfully toggled Friendly Fire to " + bool);
        return true;
    }

    private static final String townFriendlyFire_KEY = "friendly_fire";

    public static void setFriendlyFire(@NotNull Town town, boolean bool) {
        if (town.hasMeta(townFriendlyFire_KEY)) {
            town.removeMetaData(townFriendlyFire_KEY);
        }
        BooleanDataField field = new BooleanDataField(townFriendlyFire_KEY, bool);
        town.addMetaData(field);
        town.save();
    }

    public static boolean FriendlyFireON(@NotNull Town town) {
        if (!town.hasMeta(townFriendlyFire_KEY)) {
            return true;
        }

        CustomDataField<?> field = town.getMetadata(townFriendlyFire_KEY);
        if (field instanceof BooleanDataField s) {
            return s.getValue();
        }
        return false;
    }
}
