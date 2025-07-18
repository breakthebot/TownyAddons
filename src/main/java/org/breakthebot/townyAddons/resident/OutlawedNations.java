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

package org.breakthebot.townyAddons.resident;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.metadata.StringDataField;
import com.palmergames.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class OutlawedNations implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            TownyMessaging.sendErrorMsg(sender, "Only players may use this command.");
            return false;
        }
        TownyAPI API = TownyAPI.getInstance();
        Resident res = API.getResident(player);
        assert res != null;
        List<Nation> outlawingNations = getNationsThatOutlawPlayer(player);
        if (outlawingNations.isEmpty()) {
            player.sendMessage(NamedTextColor.DARK_GREEN + "Outlawed in " + NamedTextColor.GREEN + "[" + 0 + "]:");
            return false;
        }

        String list = outlawingNations.stream()
                .map(Nation::getName)
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
        int count = outlawingNations.size();
        player.sendMessage(NamedTextColor.DARK_GREEN + "Outlawed in " + NamedTextColor.GREEN + "[" + count + "]:" + NamedTextColor.WHITE + " " + list);


        return true;
    }

    public List<Nation> getNationsThatOutlawPlayer(Player player) {
        if (player == null) return Collections.emptyList();

        UUID playerUUID = player.getUniqueId();
        List<Nation> outlawingNations = new ArrayList<>();

        for (Nation nation : TownyUniverse.getInstance().getNations()) {
            StringDataField sdf = (StringDataField) nation.getMetadata("townycommandaddons_nation_outlaws");
            if (sdf == null || sdf.getValue() == null || sdf.getValue().isEmpty()) continue;

            for (String uuidString : sdf.getValue().split(",")) {
                if (uuidString.trim().equalsIgnoreCase(playerUUID.toString())) {
                    outlawingNations.add(nation);
                    break;
                }
            }
        }

        return outlawingNations;
    }
}
