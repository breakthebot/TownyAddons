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

import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Resident;

import java.util.ArrayList;
import java.util.List;

public class TrustList implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            TownyMessaging.sendErrorMsg(sender, "Only players may use this command.");
            return false;
        }
        TownyAPI API = TownyAPI.getInstance();
        Resident res = API.getResident(player);
        List<Town> TownList = API.getTowns();
        ArrayList<Town> TrustedIn = new ArrayList<>();
        for (Town town : TownList) {
            if (town.hasTrustedResident(res)) {
                TrustedIn.add(town);
            }
        }
        String list = TrustedIn.stream()
                .map(Town::getName)
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
        TownyMessaging.sendMsg(player, "You are trusted in the following towns: \n" + list);


        return true;
    }
}
