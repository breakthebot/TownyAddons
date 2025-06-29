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
