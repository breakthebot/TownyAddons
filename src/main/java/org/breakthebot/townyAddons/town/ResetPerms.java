package org.breakthebot.townyAddons.town;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ResetPerms implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            TownyMessaging.sendErrorMsg(sender, "Only players may use this command.");
            return false;
        }


        TownyAPI API = TownyAPI.getInstance();
        Resident res = API.getResident(player);
        assert res != null;
        if (!res.isMayor()) {
            TownyMessaging.sendErrorMsg(player, "You must be the mayor of a town.");
            return false;
        }

        Town town = res.getTownOrNull();
        assert town != null;
        TownyMessaging.sendMsg(town.getTrustedResidents().toString());
        Set<Resident> trustedList = new HashSet<>(town.getTrustedResidents());
        TownyMessaging.sendMsg(trustedList.toString());
        for (Resident trustedRes : trustedList) {
            town.removeTrustedResident(trustedRes);
        }




        Collection<TownBlock> allTownBlocks = town.getTownBlocks();

        for (TownBlock block : allTownBlocks) {
            block.evictOwnerFromTownBlock(false);
            block.setTrustedResidents(new HashSet<>());
            block.getPermissionOverrides().clear();
            block.setPermissions(town.getPermissions().toString());
            block.save();
        }
        town.save();

        TownyMessaging.sendMsg(player, "Successfully cleaned up town permissions!");
        TownyMessaging.sendMsg(player, "Untrusted " + trustedList.size() + " players.");
        TownyMessaging.sendMsg(player, "Reset " + allTownBlocks.size() + " chunks' permissions.");

        return false;
    }
}