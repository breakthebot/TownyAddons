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
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.metadata.BooleanDataField;
import com.palmergames.bukkit.towny.object.metadata.LongDataField;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

public class TAOverclaimShield implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!sender.hasPermission("towny.command.townyadmin.set.shield")) {
            TownyMessaging.sendErrorMsg(sender, "You do not have permission to perform this command.");
            return false;
        }
        TownyAPI API = TownyAPI.getInstance();
        Town town = API.getTown(args[0]);
        if (town == null) {
            TownyMessaging.sendErrorMsg(sender, "Invalid town specified.");
            return false;
        }

        String newStatus;
        if (args.length > 1) {
            newStatus = args[1];
        } else {
            newStatus = "";
        }

        if (newStatus.equalsIgnoreCase("on")) {
            if (hasOverclaimShield(town)) {
                TownyMessaging.sendErrorMsg(sender, "Town already has shield.");
                return true;
            }
            setOverclaimShield(town, true);
            setToggledShieldOnAt(town, Instant.now().getEpochSecond());
            TownyMessaging.sendMsg(sender, "Overclaim shield turned on.");
        } else if (newStatus.equalsIgnoreCase("off")) {
            if (!hasOverclaimShield(town)) {
                TownyMessaging.sendErrorMsg(sender, "Town does not have shield.");
                return true;
            }
            setOverclaimShield(town, false);
            TownyMessaging.sendMsg(sender, "Overclaim shield turned off.");
        } else if (newStatus.equalsIgnoreCase("")) {
            boolean opposite = !hasOverclaimShield(town);
            setOverclaimShield(town, opposite);
            TownyMessaging.sendMsg(sender, "Overclaim shield status toggled to " + opposite);
        } else {
            TownyMessaging.sendErrorMsg(sender, "Usage: /ta toggle <town> <on|off>");
            return false;
        }
        return true;
    }

    /*
    The following part is from
    https://github.com/jwkerr/OverclaimShield/blob/master/src/main/java/net/earthmc/overclaimshield/manager/TownMetadataManager.java
    It should already exist in lynchpin / the overclaim module on EMC.
     */

    private static final String hasShield = "os_hasShield";
    private static final String toggledShieldOnAt = "os_toggledShieldOnAt";

    public static Boolean hasOverclaimShield(Town town) {
        BooleanDataField bdf = (BooleanDataField) town.getMetadata(hasShield);
        if (bdf == null)
            return false;

        return bdf.getValue();
    }

    public static void setOverclaimShield(Town town, boolean value) {
        if (!town.hasMeta(hasShield))
            town.addMetaData(new BooleanDataField(hasShield, null));

        BooleanDataField bdf = (BooleanDataField) town.getMetadata(hasShield);
        if (bdf == null)
            return;

        bdf.setValue(value);
        town.addMetaData(bdf);
    }

    public static void setToggledShieldOnAt(Town town, Long value) {
        if (!town.hasMeta(toggledShieldOnAt))
            town.addMetaData(new LongDataField(toggledShieldOnAt, null));

        LongDataField ldf = (LongDataField) town.getMetadata(toggledShieldOnAt);
        if (ldf == null)
            return;

        ldf.setValue(value);
        town.addMetaData(ldf);
    }
}
