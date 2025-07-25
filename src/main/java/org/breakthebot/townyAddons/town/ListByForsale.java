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

import com.palmergames.adventure.text.Component;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.adventure.text.format.NamedTextColor;
import com.palmergames.bukkit.towny.object.comparators.ComparatorType;
import com.palmergames.util.Pair;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ListByForsale implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        List<Pair<UUID, Component>> forSaleTowns = TownyAPI.getInstance().getTowns().stream()
                .filter(Town::isForSale)
                .sorted((a, b) -> Double.compare(b.getForSalePrice(), a.getForSalePrice()))
                .map(town -> {
                    UUID uuid = town.getUUID();
                    String townName = town.getName();
                    int price = (int) Math.ceil(town.getForSalePrice());

                    Component comp = Component.text()
                            .append(Component.text(townName).color(NamedTextColor.AQUA))
                            .append(Component.text(" - ").color(NamedTextColor.GRAY))
                            .append(Component.text(price + "g").color(NamedTextColor.AQUA))
                            .build();

                    return Pair.pair(uuid, comp);
                })
                .collect(Collectors.toList());
        if (forSaleTowns.isEmpty()) {
            TownyMessaging.sendErrorMsg(sender, "No towns forsale.");
            return false;
        }
        int townsPerPage = 10;
        int numPages = (int) Math.ceil((double) forSaleTowns.size() / townsPerPage);
        TownyMessaging.sendTownList(sender, forSaleTowns, ComparatorType.NAME, 1, numPages);
        return true;
    }
}
