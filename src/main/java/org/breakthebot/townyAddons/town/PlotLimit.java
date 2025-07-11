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
import com.palmergames.bukkit.towny.object.metadata.IntegerDataField;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlotLimit implements CommandExecutor {

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
        if (!player.hasPermission("towny.command.town.set.plotlimit")) {
            TownyMessaging.sendErrorMsg(player, "You do not have permission to perform this command.");
            return false;
        }

        Town town = res.getTownOrNull();
        assert town != null;

        if (args.length == 0) {
            removePlotLimit(town);
            TownyMessaging.sendMsg(player, "Successfully removed plot limit");
            return true;
        }
        int limit;
        try {
            limit = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            TownyMessaging.sendErrorMsg(player, "Invalid integer provided.");
            return false;
        }
        setPlotLimit(town, limit);
        TownyMessaging.sendMsg(player, "Successfully set plot limit at " + limit);
        return true;
    }

    private static final String townLimit_KEY = "plotLimit";

    public static void setPlotLimit(@NotNull Town town, int limit) {
        if (town.hasMeta(townLimit_KEY)) {
            town.removeMetaData(townLimit_KEY);
        }
        IntegerDataField field = new IntegerDataField(townLimit_KEY, limit);
        town.addMetaData(field);
        town.save();
    }

    public static int getPlotLimit(@NotNull Town town) {
        if (!town.hasMeta(townLimit_KEY)) {
            return 0;
        }

        CustomDataField<?> field = town.getMetadata(townLimit_KEY);
        if (field instanceof IntegerDataField s) {
            return s.getValue();
        }
        return 0;
    }

    public static boolean hasPlotLimit(@NotNull Town town) {
        return town.hasMeta(townLimit_KEY);
    }

    public static void removePlotLimit(@NotNull Town town) {
        if (!town.hasMeta(townLimit_KEY)) {
            return;
        }
        town.removeMetaData(townLimit_KEY);
        town.save();
    }
}
