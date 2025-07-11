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

package org.breakthebot.townyAddons.resident;


import com.palmergames.adventure.text.Component;
import com.palmergames.adventure.text.event.ClickEvent;
import com.palmergames.adventure.text.format.NamedTextColor;
import com.palmergames.bukkit.towny.event.statusscreen.ResidentStatusScreenEvent;
import com.palmergames.bukkit.towny.object.Resident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ResidentStatusScreenListener implements Listener {


    @EventHandler
    public void onResidentStatusScreen(ResidentStatusScreenEvent event) {
        Resident res = event.getResident();
        if (Wiki.residentHasWiki(res)) {
            String wikiURL = Wiki.getResidentWiki(res);

            Component component = Component.empty()
                    .append(Component.text("[", NamedTextColor.GRAY))
                    .append(Component.text("Wiki", NamedTextColor.GREEN)
                            .hoverEvent(Component.text(wikiURL, NamedTextColor.DARK_GREEN))
                            .clickEvent(ClickEvent.openUrl(wikiURL)))
                    .append(Component.text("]", NamedTextColor.GRAY));

            event.getStatusScreen().addComponentOf("wiki", component);
        }
    }
}
