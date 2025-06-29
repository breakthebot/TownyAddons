package org.breakthebot.townyAddons.resident;

import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Resident;

import java.util.ArrayList;
import java.util.List;

public class TrustList implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            TownyMessaging.sendErrorMsg(sender, "Only players may use this command.");
            return false;
        }
        TownyAPI API = TownyAPI.getInstance();
        Resident res = API.getResident(player);
        List<Town> TownList = API.getTowns();
        ArrayList<Town> TrustedIn = new ArrayList<>();
        for (Town town : TownList) {
            if (town.hasTrustedResident(res)) {
                TrustedIn.add(town);
            }
        }
        String list = TrustedIn.stream()
                .map(Town::getName)
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
        TownyMessaging.sendMsg(player, "You are trusted in the following towns: \n" + list);


        return true;
    }
}
