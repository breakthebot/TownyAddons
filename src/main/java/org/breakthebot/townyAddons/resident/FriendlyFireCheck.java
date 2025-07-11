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

import com.palmergames.bukkit.towny.event.plot.toggle.PlotTogglePvpEvent;
import com.palmergames.bukkit.towny.object.Town;
import org.breakthebot.townyAddons.town.FriendlyFire;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class FriendlyFireCheck implements Listener {

    @EventHandler
    public void onPlotTogglePvP(PlotTogglePvpEvent event) {
        if (!event.getFutureState()) {
            return;
        }
        Town town = event.getTown();
        Player player = event.getPlayer();
        if (town.hasResident(player)) {
            if (player.hasPermission("towny.town.friendlyfirelimitation.bypass")) {
                return;
            }

            boolean allowed = FriendlyFire.FriendlyFireON(town);

            if (!allowed) {
                event.setCancelled(true);
                event.setCancelMessage("This town has friendly fire disabled. You may not toggle PvP in plots.");
            }
        }
    }
}
