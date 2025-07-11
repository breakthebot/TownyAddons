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

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.TownBlockOwner;
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
        Resident target = null;
        String confirm;
        if (args.length == 0) {
            TownyMessaging.sendMsg(player, "&bUsing /t resetperms will reset all of the following; town trusted, plot owners, plot trusted, plot perm overrides.");
            TownyMessaging.sendMsg(player, "&bSpecifying a resident will only reset permissions for the resident.");
            TownyMessaging.sendErrorMsg(player, "Usage: /t resetperms {resident} confirm");
            return false;
        } else if (args.length == 1) {
            confirm = args[0];
        } else if (args.length == 2){
            target = TownyAPI.getInstance().getResident(args[0]);
            if (target == null) {
                TownyMessaging.sendErrorMsg(player, "Invalid resident chosen.");
                return false;
            }
            confirm = args[1];
        } else {
            TownyMessaging.sendErrorMsg(player, "Usage: /t resetperms {resident} confirm");
            return false;
        }
        if (!confirm.equalsIgnoreCase("confirm")) {
            TownyMessaging.sendErrorMsg(player, "You must confirm this action! Use 'confirm' in your last argument.");
            return false;
        }


        Town town = res.getTownOrNull();
        assert town != null;
        Collection<TownBlock> allTownBlocks = town.getTownBlocks();

        if (target == null) {
            Set<Resident> trustedList = new HashSet<>(town.getTrustedResidents());
            for (Resident trustedRes : trustedList) {
                town.removeTrustedResident(trustedRes);
            }

            for (TownBlock block : allTownBlocks) {
                block.evictOwnerFromTownBlock(false);
                block.setTrustedResidents(new HashSet<>());
                block.getPermissionOverrides().clear();
                block.setPermissions(town.getPermissions().toString());
                block.save();
            }
            town.save();
            TownyMessaging.sendMsg(player, "Successfully reset town permissions!");
        }
        else {
            town.removeTrustedResident(target);
            for (TownBlock block : allTownBlocks) {
                TownBlockOwner owner = block.getTownBlockOwner();
                if (owner instanceof Resident && block.getTownBlockOwner().getName().equalsIgnoreCase(target.getName())) {
                    block.evictOwnerFromTownBlock(false);
                }
                if (block.hasTrustedResident(target)) { block.removeTrustedResident(target); }
                block.getPermissionOverrides().remove(target);

                block.save();
            }
            town.save();
            TownyMessaging.sendMsg(player, "Successfully reset permission overrides & trust for player " + target.getName());
        }
        return true;
    }
}