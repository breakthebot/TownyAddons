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

import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.event.TownBlockTypeRegisterEvent;
import com.palmergames.bukkit.towny.event.plot.PlayerChangePlotTypeEvent;
import com.palmergames.bukkit.towny.event.town.TownTrustAddEvent;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.*;
import com.palmergames.bukkit.towny.utils.PermissionGUIUtil;
import org.breakthebot.townyAddons.TownyAddons;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class Vault implements Listener {
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

    private final PermissionGUIUtil.SetPermissionType[] negatedPermissions = new PermissionGUIUtil.SetPermissionType[] {
            PermissionGUIUtil.SetPermissionType.NEGATED,
            PermissionGUIUtil.SetPermissionType.NEGATED,
            PermissionGUIUtil.SetPermissionType.NEGATED,
            PermissionGUIUtil.SetPermissionType.NEGATED
    };

    @EventHandler
    public void onResidentTrusted(TownTrustAddEvent event) {
        Resident newResident = event.getTrustedResident();
        if (!(newResident instanceof Player)) { return; }
        Player player = newResident.getPlayer();

        assert player != null;
        if (player.hasPermission("towny.command.townyadmin.town.trust")) { return; }

        Town town = event.getTown();

        PermissionData denyAll = new PermissionData(negatedPermissions, "VaultSecurity");

        for (TownBlock townBlock : town.getTownBlocks()) {

            if (townBlock.getType() == null || !"vault".equalsIgnoreCase(townBlock.getType().getName())) {
                continue;
            }

            Map<Resident, PermissionData> overrides = new HashMap<>(townBlock.getPermissionOverrides());
            overrides.put(newResident, denyAll);

            townBlock.setPermissionOverrides(overrides);

            try {
                townBlock.save();
            } catch (Exception e) {
                getLogger().severe("Error while saving vault townblock at " + townBlock.getWorldCoord() + "\n" + e);
            }
        }
    }

    @EventHandler
    public void onTownyLoadTownBlockTypes(TownBlockTypeRegisterEvent event) {
        registerVaultPlot();
    }

    @EventHandler
    public void onChangePlotType(PlayerChangePlotTypeEvent event) {
        TownBlockType newType = event.getNewType();
        if (newType.getName().equalsIgnoreCase("vault")) {
            TownBlock vaultBlock = event.getTownBlock();
            PermissionData denyAll = new PermissionData(negatedPermissions, "VaultSecurity");
            Town town = vaultBlock.getTownOrNull();
            if (town == null) { return ; }
            Set<Resident> trustedList = town.getTrustedResidents();
            Map<Resident, PermissionData> overrides = new HashMap<>(vaultBlock.getPermissionOverrides());
            for (Resident trusted : trustedList) {
                overrides.put(trusted, denyAll);
            }
            vaultBlock.setPermissionOverrides(overrides);

            try {
                vaultBlock.save();
            } catch (Exception e) {
                TownyMessaging.sendErrorMsg(event.getPlayer(), "Unexpected error occurred. Please report immediately");
                getLogger().severe("Error while saving vault townblock at " + vaultBlock.getWorldCoord() + "\n" + e);
            }
        }
    }
}
