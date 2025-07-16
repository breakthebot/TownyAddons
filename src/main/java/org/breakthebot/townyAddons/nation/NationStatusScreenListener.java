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

package org.breakthebot.townyAddons.nation;


import com.palmergames.adventure.text.Component;
import com.palmergames.adventure.text.event.ClickEvent;
import com.palmergames.adventure.text.event.HoverEvent;
import com.palmergames.adventure.text.format.NamedTextColor;
import com.palmergames.bukkit.towny.event.statusscreen.NationStatusScreenEvent;
import com.palmergames.bukkit.towny.object.Nation;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


public class NationStatusScreenListener implements Listener {

    @EventHandler
    public void onNationStatusScreen(NationStatusScreenEvent event) {
        Nation nation = event.getNation();
        int nationBonus = getNationBonus(nation);
        int residents = nation.getNumResidents();
        int size = nation.getNumTownblocks();


        Component stats = Component.empty()
                .append(Component.text("[", NamedTextColor.GRAY))
                .append(Component.text("Stats", NamedTextColor.GREEN))
                .hoverEvent(HoverEvent.showText(Component.empty()
                        .append(Component.text("Residents: ", NamedTextColor.DARK_GREEN))
                        .append(Component.text(String.valueOf(residents), NamedTextColor.GREEN))
                        .append(Component.newline())
                        .append(Component.text("Bonus: ", NamedTextColor.DARK_GREEN))
                        .append(Component.text(String.valueOf(nationBonus), NamedTextColor.GREEN))
                        .append(Component.newline())
                        .append(Component.text("Size: ", NamedTextColor.DARK_GREEN))
                        .append(Component.text(String.valueOf(size), NamedTextColor.GREEN))
                ))
                .append(Component.text("]", NamedTextColor.GRAY));

        event.getStatusScreen().addComponentOf("Stats", stats);

        if (NationDiscord.nationHasDiscord(nation)) {
            String discordURL = NationDiscord.getNationDiscord(nation);

            Component component = Component.empty()
                    .append(Component.text("[", NamedTextColor.GRAY))
                    .append(Component.text("Discord", NamedTextColor.GREEN)
                            .hoverEvent(Component.text(discordURL, NamedTextColor.DARK_GREEN))
                            .clickEvent(ClickEvent.openUrl(discordURL)))
                    .append(Component.text("]", NamedTextColor.GRAY));

            event.getStatusScreen().addComponentOf("discord", component);
        }
    }

    private static int getNationBonus(Nation nation) {
        int numRes = nation.getNumResidents();
        int nationBonus;
        if (numRes < 20) {
            nationBonus = 0;
        } else if (numRes < 40) {
            nationBonus = 10;
        } else if (numRes < 60) {
            nationBonus = 30;
        } else if (numRes < 80) {
            nationBonus = 50;
        } else if (numRes < 120) {
            nationBonus = 60;
        } else if (numRes < 200) {
            nationBonus = 80;
        } else {
            nationBonus = 100;
        }
        return nationBonus;
    }
}
