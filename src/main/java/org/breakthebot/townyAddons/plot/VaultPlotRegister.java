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

package org.breakthebot.townyAddons.plot;

import com.palmergames.bukkit.towny.event.TownBlockTypeRegisterEvent;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.*;
import org.breakthebot.townyAddons.TownyAddons;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.logging.Logger;

public class VaultPlotRegister implements Listener {
    private static Logger getLogger() {
        return TownyAddons.getInstance().getLogger();
    }

    public static void registerVaultPlot() {

        if (TownBlockTypeHandler.exists("Vault")) {
            return;
        }

        TownBlockType VAULT = new TownBlockType("Vault", new TownBlockData() {
            @Override
            public String getMapKey() {
                return "V";
            }
            @Override
            public double getCost() {
                return TownyAddons.getInstance().getConfiguration().VaultCost;
            }
        });
        try {
            TownBlockTypeHandler.registerType(VAULT);
            getLogger().info("Vault plot type registered successfully.");
        } catch (TownyException e) {
            getLogger().severe("An error occurred while registering Vault plot: " + e);
        }
    }

    @EventHandler
    public void onTownyLoadTownBlockTypes(TownBlockTypeRegisterEvent event) {
        registerVaultPlot();
    }
}
