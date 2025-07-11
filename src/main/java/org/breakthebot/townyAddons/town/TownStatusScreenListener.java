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

import com.palmergames.adventure.text.Component;
import com.palmergames.adventure.text.event.ClickEvent;
import com.palmergames.adventure.text.format.NamedTextColor;
import com.palmergames.bukkit.towny.event.statusscreen.TownStatusScreenEvent;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.metadata.CustomDataField;
import com.palmergames.bukkit.towny.object.metadata.BooleanDataField;
import com.palmergames.bukkit.towny.object.metadata.IntegerDataField;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TownStatusScreenListener implements Listener {

    @EventHandler
    public void onTownStatusScreen(TownStatusScreenEvent event) {
        Town town = event.getTown();
        String OutsidersCanSpawn_KEY = "bspawn_canoutsidersspawn";
        if (town.hasMeta(OutsidersCanSpawn_KEY)) {
                CustomDataField<?> field = town.getMetadata(OutsidersCanSpawn_KEY);
            boolean OutsidersCanSpawn;
            if (field instanceof BooleanDataField s) {
                OutsidersCanSpawn = s.getValue();
            } else {
                return;
            }
            if (!OutsidersCanSpawn) {
                return;
            }

            Component component = Component.empty()
                    .append(Component.text("[", NamedTextColor.GRAY))
                    .append(Component.text("Outsider Spawn", NamedTextColor.GREEN)
                            .append(Component.text("]", NamedTextColor.GRAY))
                            .hoverEvent(Component.text("Town has purchased outsidercanspawn\nAllowing outsiders to teleport.", NamedTextColor.DARK_GREEN)));


            event.getStatusScreen().addComponentOf("OutsidersCanSpawn", component);
        }
        if (TownDiscord.townHasDiscord(town)) {
            String discordURL = TownDiscord.getTownDiscord(town);

            Component component = Component.empty()
                    .append(Component.text("[", NamedTextColor.GRAY))
                    .append(Component.text("Discord", NamedTextColor.GREEN)
                            .hoverEvent(Component.text(discordURL, NamedTextColor.DARK_GREEN))
                            .clickEvent(ClickEvent.openUrl(discordURL)))
                    .append(Component.text("]", NamedTextColor.GRAY));

            event.getStatusScreen().addComponentOf("discord", component);
        }

        String overclaimsRemaining_KEY = "os_overclaimsRemainingToday";

        if (town.hasMeta(overclaimsRemaining_KEY)) {
            CustomDataField<?> field = town.getMetadata(overclaimsRemaining_KEY);
            int overclaimsRemaining;
            if (field instanceof IntegerDataField s) {
                overclaimsRemaining = s.getValue();
            } else {
                return;
            }

            Component component = Component.empty()
                    .append(Component.text("[", NamedTextColor.GRAY))
                    .append(Component.text("Overclaims", NamedTextColor.GREEN)
                            .append(Component.text("]", NamedTextColor.GRAY))
                            .hoverEvent(Component.text("town has " + overclaimsRemaining + " overclaims remaining today.", NamedTextColor.DARK_GREEN)));


            event.getStatusScreen().addComponentOf("overclaims_remaining", component);
        }
    }
}
