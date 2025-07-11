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
import com.palmergames.bukkit.towny.event.TownAddResidentEvent;
import com.palmergames.bukkit.towny.event.TownRemoveResidentEvent;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.*;


public class TownLog implements CommandExecutor, Listener {

    private static final Map<UUID, List<ResidentAction>> recentResidentActivity = new HashMap<>();


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            TownyMessaging.sendErrorMsg(sender, "Only players may use this command.");
            return false;
        }

        if (!player.hasPermission("towny.town.log.view")) {
            TownyMessaging.sendErrorMsg(player, "You do not have permission to perform this command.");
            return false;
        }

        TownyAPI API = TownyAPI.getInstance();
        Resident res = API.getResident(player);
        assert res != null;
        if (!res.hasTown()) {
            TownyMessaging.sendErrorMsg(player, "You must be in a town.");
            return false;
        }
        Town town = res.getTownOrNull();
        assert town != null;
        if (!recentResidentActivity.containsKey(town.getUUID())) {
            TownyMessaging.sendErrorMsg(player, "No recent joins or leaves found for your town.");
            return false;
        }

        List<ResidentAction> recentList = recentResidentActivity.get(town.getUUID());

        String message = "&bRecent resident activity:";

        for (int i = recentList.size() - 1; i >= 0 && i >= recentList.size() - 5; i--) {
            ResidentAction action = recentList.get(i);
            Resident target = API.getResident(action.getResidentUUID());
            if (target == null) continue;

            String actionText = action.isJoin() ? "&ajoined" : "&4left";
            String timeAgo = formatRelativeTime(action.getTimestamp());

            message += "\n&b- &r" + target.getName() + " " + actionText + "&r the town &7(" + timeAgo + "&7)";
        }

        TownyMessaging.sendMsg(player, message);

        return true;
    }


    @EventHandler
    public void onTownJoin(TownAddResidentEvent event) {
        Town town = event.getTown();
        Resident res = event.getResident();

        recentResidentActivity
                .computeIfAbsent(town.getUUID(), k -> new ArrayList<>())
                .add(new ResidentAction(res.getUUID(), true));
    }

    @EventHandler
    public void onTownLeave(TownRemoveResidentEvent event) {
        Town town = event.getTown();
        Resident res = event.getResident();

        recentResidentActivity
                .computeIfAbsent(town.getUUID(), k -> new ArrayList<>())
                .add(new ResidentAction(res.getUUID(), false));
    }

    public static String formatRelativeTime(long timestamp) {
        long diffMillis = System.currentTimeMillis() - timestamp;

        long seconds = diffMillis / 1000;
        if (seconds < 60) return seconds + "s ago";

        double minutes = seconds / 60.0;
        if (minutes < 60) return String.format("%.1fm ago", minutes);

        double hours = minutes / 60.0;
        if (hours < 24) return String.format("%.1fh ago", hours);

        double days = hours / 24.0;
        return String.format("%.1fd ago", days);
    }

}
