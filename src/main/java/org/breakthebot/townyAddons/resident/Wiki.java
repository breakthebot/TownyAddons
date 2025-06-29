package org.breakthebot.townyAddons.resident;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.metadata.CustomDataField;
import com.palmergames.bukkit.towny.object.metadata.StringDataField;
import org.breakthebot.townyAddons.TownyAddons;
import org.breakthebot.townyAddons.config;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
public class Wiki implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            TownyMessaging.sendErrorMsg(sender, "Only players may use this command.");
            return false;
        }

        if (!player.hasPermission("towny.command.resident.set.wiki")) {
            TownyMessaging.sendMsg(player, "You do not have permission to perform this command.");
        }

        TownyAPI API = TownyAPI.getInstance();
        Resident res = API.getResident(player);
        config settings = TownyAddons.getInstance().getConfiguration();
        assert res != null;

        if (args.length == 0) {
            if (!residentHasWiki(res)) {
                TownyMessaging.sendErrorMsg(player, "You haven't linked a wiki page.");
                return false;
            }
            TownyMessaging.sendMsg(player, "Resident wiki page unlinked.");
            residentRemoveWiki(res);
            return true;
        }

        String wikiURL = args[0];
        if (!(settings.wikiURL == null)) {
            if (!wikiURL.startsWith(settings.wikiURL)) {
                TownyMessaging.sendErrorMsg(player, "Invalid wiki page. You can only link pages from " + settings.wikiURL);
                return false;
            }
        }
        setResidentWiki(res, wikiURL);
        TownyMessaging.sendMsg(player, "Wiki page linked!");
        return true;
    }
    
    
    private static final String residentWiki_KEY = "wiki_url";


    public static void setResidentWiki(@NotNull Resident res, String url) {
        if (res.hasMeta(residentWiki_KEY)) {
            res.removeMetaData(residentWiki_KEY);
        }
        StringDataField field = new StringDataField(residentWiki_KEY, url);
        res.addMetaData(field);
        res.save();
    }

    public static String getResidentWiki(@NotNull Resident res) {
        if (!res.hasMeta(residentWiki_KEY)) {
            return "";
        }

        CustomDataField<?> field = res.getMetadata(residentWiki_KEY);
        if (field instanceof StringDataField s) {
            return s.getValue();
        }
        return "";
    }

    public static boolean residentHasWiki(@NotNull Resident res) {
        return res.hasMeta(residentWiki_KEY);
    }

    public static void residentRemoveWiki(@NotNull Resident res) {
        if (!res.hasMeta(residentWiki_KEY)) {
            return;
        }
        res.removeMetaData(residentWiki_KEY);
        res.save();
    }
}
