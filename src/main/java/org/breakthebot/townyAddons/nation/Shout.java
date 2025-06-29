package org.breakthebot.townyAddons.nation;

import org.breakthebot.townyAddons.TownyAddons;
import org.breakthebot.townyAddons.config;
import org.bukkit.command.CommandExecutor;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.palmergames.bukkit.towny.TownyMessaging;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class Shout implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            TownyMessaging.sendErrorMsg(sender, "Only players may use this command.");
            return false;
        }

        config settings = TownyAddons.getInstance().getConfiguration();
        TownyAPI API = TownyAPI.getInstance();
        Resident res = API.getResident(player);
        assert res != null;
        Nation nat = res.getNationOrNull();
        assert nat != null;


        if (!res.hasNation()){
            TownyMessaging.sendErrorMsg(sender, "You must be in a nation.");
            return false;
        }

        if (args.length < 1){
            TownyMessaging.sendErrorMsg(sender, "Usage: /n shout {message}");
            return false;
        }

        if (!player.hasPermission("towny.command.nation.shout")){
            TownyMessaging.sendErrorMsg(sender, "You do not have permission to perform this command.");
            return false;
        }

        int maxChars = settings.maxShoutChars;
        if (args[0].length() > maxChars){
            TownyMessaging.sendErrorMsg(sender, "Too many Characters! Maximum: " + maxChars);
            return false;
        }


        long current = System.currentTimeMillis();
        long last = getLastUsedNation(nat);
        long next = last + (settings.ShoutCooldownHours * 3600L * 1000);

        if (current < next) {
            long remaining = next - current;
            long fakePast = current - remaining;
            List<Long> timeLeft = parseTimestamp(fakePast);

            long days = timeLeft.getFirst();
            long hours = timeLeft.get(1);
            long minutes = timeLeft.getLast();

            String durationMsg = "";
            if (days > 0) durationMsg += days + " days ";
            if (hours > 0) durationMsg += hours + " hours ";
            if (minutes > 0) durationMsg += minutes + " minutes";

            TownyMessaging.sendErrorMsg(sender, "Your nation must wait " + durationMsg.trim() + " before using this command again.");
            return false;
        }


        double bal = nat.getAccount().getHoldingBalance();
        double owed = settings.ShoutPrice;
        if (bal < owed){
            TownyMessaging.sendErrorMsg(sender, "Your nation does not have enough gold for this action.");
            return false;
        }

        nat.getAccount().withdraw(owed, "Shouting at request of " + sender.getName());
        setLastUsedNation(nat, System.currentTimeMillis());
        TownyMessaging.sendGlobalMessage("&6The nation of &l" + nat + "&r&6 shouts: &r&f&o" + String.join(" ", args));
        return true;
    }


    private static final Map<UUID, Long> nationCooldowns = new HashMap<>();

    public static void setLastUsedNation(Nation nation, long timestamp) {
        nationCooldowns.put(nation.getUUID(), timestamp);
    }

    public static long getLastUsedNation(Nation nation) {
        return nationCooldowns.getOrDefault(nation.getUUID(), 0L);
    }

    public static List<Long> parseTimestamp(long timestamp){
        long current = Instant.now().toEpochMilli();
        long diff = current - timestamp;
        Duration duration = Duration.ofMillis(diff);
        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;
        List<Long> list = new ArrayList<>();
        list.add(days);
        list.add(hours);
        list.add(minutes);
        return list;
    }

}
