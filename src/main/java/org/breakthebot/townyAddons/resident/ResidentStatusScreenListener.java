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
