package org.breakthebot.townyAddons.nation;


import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.event.RenameNationEvent;
import com.palmergames.bukkit.towny.object.Nation;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


public class AnnounceNationRename implements Listener {

    @EventHandler
    public void onNationRename(RenameNationEvent event) {
        Nation nation = event.getNation();
        String oldName = event.getOldName();

        TownyMessaging.sendGlobalMessage(oldName + " was renamed to " + nation.getName());
    }
}
