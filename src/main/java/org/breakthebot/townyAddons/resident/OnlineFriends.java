package org.breakthebot.townyAddons.resident;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.object.Resident;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class OnlineFriends implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            TownyMessaging.sendErrorMsg(sender, "Only players may use this command.");
            return false;
        }
        TownyAPI API = TownyAPI.getInstance();
        Resident res = API.getResident(player);
        assert res != null;
        List<Resident> friends = res.getFriends();
        ArrayList<Resident> onlineFriends = new ArrayList<>();
        if (friends.isEmpty()) {
            TownyMessaging.sendErrorMsg(player, "You have no friends");
            return false;
        }
        for (Resident friend : friends) {
            if (friend.isOnline()) {
                onlineFriends.add(friend);
            }
        }
        String list = onlineFriends.stream()
                .map(Resident::getName)
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
        TownyMessaging.sendMsg(player, "These are your online friends: \n" + list);


        return false;
    }
}
