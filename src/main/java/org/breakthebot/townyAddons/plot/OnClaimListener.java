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

import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.event.plot.changeowner.PlotClaimEvent;
import com.palmergames.bukkit.towny.event.plot.changeowner.PlotPreClaimEvent;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownBlock;
import org.breakthebot.townyAddons.town.PlotLimit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class OnClaimListener implements Listener {

//    @EventHandler
    public void prePlotClaim(PlotPreClaimEvent event) {
        TownBlock block = event.getTownBlock();
        Town town = block.getTownOrNull();
        Resident res = event.getNewResident();
        assert res != null;  // Any player claiming a plot is a registered Towny resident
        if (town == null) { return; }
        if (!PlotLimit.hasPlotLimit(town)) { return; }
        int limit = PlotLimit.getPlotLimit(town);
        if (!(limit > 0)) { return; }
        int claimed = 0;
        for (TownBlock chunk : res.getTownBlocks()) {
            if (chunk.getTownOrNull() != null && chunk.getTownOrNull().equals(town)) {
                claimed++;
            }
        }
        if (claimed >= limit) {
            TownyMessaging.sendErrorMsg(res, "You have claimed the maximum limit of plots allowed in this town.");
            event.setCancelled(true);
            // ERROR: "Successfully claimed" message is still sent.
            // Awaiting Towny Response https://github.com/TownyAdvanced/Towny/issues/7868
        }
    }

    @EventHandler
    public void onPlotClaim(PlotClaimEvent event) {
        Resident res = event.getNewResident();
        TownBlock block = event.getTownBlock();
        Town town = block.getTownOrNull();
        if (town == null) { return; }
        TownyMessaging.sendPrefixedTownMessage(town, res + " successfully bought plot " + block.getCoord());
    }
}
