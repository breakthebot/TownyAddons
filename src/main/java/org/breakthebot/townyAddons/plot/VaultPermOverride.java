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

package org.breakthebot.townyAddons.plot;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.event.actions.*;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class VaultPermOverride implements Listener {

    @EventHandler
    public void onBuild(TownyBuildEvent event) {
        checkAllowed(event);
    }
    @EventHandler
    public void onDestroy(TownyDestroyEvent event) { checkAllowed(event); }
    @EventHandler
    public void onSwitch(TownySwitchEvent event) { checkAllowed(event); }
    @EventHandler
    public void onItemuse(TownyItemuseEvent event) { checkAllowed(event); }

    public void checkAllowed(TownyActionEvent event) {
        Player player = event.getPlayer();

        TownBlock block = event.getTownBlock();
        if (block == null) { return; }
        Resident res = TownyAPI.getInstance().getResident(player);
        if (res == null) { return; }
        Town town = block.getTownOrNull();
        if (town == null) { return; }
        if (!"vault".equalsIgnoreCase(block.getTypeName())) { return; }
        boolean exempt = town.isMayor(res) || player.hasPermission("towny.admin") || player.hasPermission("towny.vault.perm.exempt");

        if (!exempt) {
            event.setCancelled(true);
            event.setCancelMessage("Players are not allowed permissions in a Vault plot.");
        }
    }
}
