package org.breakthebot.townyAddons.town;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.event.town.TownUnclaimEvent;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OverclaimNotify implements Listener {

    private static final Map<UUID, String> townMessageQueue = new HashMap<>();
    private static final TownyAPI API = TownyAPI.getInstance();

    @EventHandler
    public void TownClaimEvent(TownUnclaimEvent event) {
        if (!event.isOverClaim()) {
            return;
        }
        TownBlock block = API.getTownBlock(event.getWorldCoord());
        if (block == null) {
            return;
        }
        Town town = block.getTownOrNull();
        if (town == null) {
            return;
        }
        if (town.getMayor().isOnline()) {
            TownyMessaging.sendPrefixedTownMessage(town, "Your town is being overclaimed!");
        } else {
            townMessageQueue.put(town.getUUID(), "Your town was overclaimed!");
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Resident res = API.getResident(event.getPlayer());
        assert res != null;
        if (!res.isMayor()) {
            return;
        }
        Town town = res.getTownOrNull();
        assert town != null;
        UUID uuid = town.getUUID();
        if (!townMessageQueue.containsKey(uuid)) {
            return;
        }

        String message = townMessageQueue.remove(uuid);
        TownyMessaging.sendPrefixedTownMessage(town, message);
    }
}
