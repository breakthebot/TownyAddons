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

package org.breakthebot.townyAddons.discordSRV;

import com.palmergames.bukkit.towny.event.*;
import com.palmergames.bukkit.towny.event.town.TownRuinedEvent;
import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import org.breakthebot.townyAddons.TownyAddons;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TownyMessageBroadcast implements Listener {

    public void SendMessage(String message) {

        String channelID = TownyAddons.getInstance().getConfiguration().discordSRVChannelID;
        if (channelID == null) {
            return;
        }
        TextChannel channel = DiscordSRV.getPlugin().getJda().getTextChannelById(channelID);

        if (channel != null) {
            channel.sendMessage(message).queue();
        }
    }

    @EventHandler
    public void onNewNation(NewNationEvent event) {
        String name = event.getNation().getName();
        SendMessage("[Towny] The nation of **" + name + "** was **founded** \uD83C\uDF89!");
    }

    @EventHandler
    public void onNationRename(RenameNationEvent event) {
        String oldName = event.getOldName();
        String newName = event.getNation().getName();
        SendMessage("[Towny] The nation of " + oldName + " was renamed to **" + newName + "**!");
    }

    @EventHandler
    public void onNewTown(NewTownEvent event) {
        String name = event.getTown().getName();
        SendMessage("[Towny] The town of **" + name + "** was **founded**");
    }

    @EventHandler
    public void onNationDelete(DeleteNationEvent event) {
        String name = event.getNationName();
        SendMessage("[Towny] The nation of **" + name + "** was **disbanded**!");
    }

    @EventHandler
    public void onTownDelete(DeleteTownEvent event) {
        String name = event.getTownName();
        SendMessage("[Towny] The town of **" + name + "** was **disbanded**!");
    }

    @EventHandler
    public void onTownRuin(TownRuinedEvent event) {
        String name = event.getTown().getName();
        SendMessage("[Towny] The town of **" + name + "** fell into **ruin**!");
    }
}
