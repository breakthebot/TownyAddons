package org.breakthebot.townyAddons.resident;

import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.event.TownAddResidentEvent;
import com.palmergames.bukkit.towny.invites.Invite;
import com.palmergames.bukkit.towny.object.Resident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import java.util.List;

public class ClearInvites implements Listener {

    @EventHandler
    public void onTownJoin(TownAddResidentEvent event) {
        Resident res = event.getResident();
        List<Invite> inviteList = res.getReceivedInvites();

        for (Invite inv : inviteList) {
            res.deleteReceivedInvite(inv);
        }
        TownyMessaging.sendMsg(res.getPlayer(), "All your pending invites have been removed.");
    }
}
