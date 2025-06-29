package org.breakthebot.townyAddons.nation;


import com.palmergames.adventure.text.Component;
import com.palmergames.adventure.text.event.ClickEvent;
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

        Component nationBonusComponent = Component.empty()
                .append(Component.text("[", NamedTextColor.GRAY))
                .append(Component.text("Nation Bonus", NamedTextColor.GREEN))
                .append(Component.text("]", NamedTextColor.GRAY))
                .hoverEvent(Component.text("This nation has a nation bonus of " + nationBonus, NamedTextColor.DARK_GREEN));

        event.getStatusScreen().addComponentOf("nationbonus", nationBonusComponent);

        Component resCountComponent = Component.empty()
                .append(Component.text("[", NamedTextColor.GRAY))
                .append(Component.text("Nation Bonus", NamedTextColor.GREEN))
                .append(Component.text("]", NamedTextColor.GRAY))
                .hoverEvent(Component.text("This nation has  " + residents + " residents", NamedTextColor.DARK_GREEN));

        event.getStatusScreen().addComponentOf("nationResidents", resCountComponent);


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
