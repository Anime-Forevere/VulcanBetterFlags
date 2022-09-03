package me.animeforever.vulcanbetterflags.vulcanbetterflags.events;

import me.frep.vulcan.api.event.VulcanFlagEvent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static me.animeforever.vulcanbetterflags.vulcanbetterflags.VulcanbetterFlags.plugin;
import static me.animeforever.vulcanbetterflags.vulcanbetterflags.VulcanbetterFlags.vulcConfig;

public class flagEvent implements Listener {
    @EventHandler
    public void onFlagEvent(VulcanFlagEvent event) {
        // Don't mind this dirty code
        Boolean severityEnabled = plugin.getConfig().getBoolean("severity.enabled");
        String severityColor = "";
        if(severityEnabled) {
            Set<String> keys = plugin.getConfig().getKeys(true);
            List<Integer> percentages = new ArrayList<>();
            for(String key : keys) {
                if(key.startsWith("severity.colors.")) {
                    String[] split = key.split("\\.");
                    int percentage = Integer.parseInt(split[2]);
                    percentages.add(percentage);
                }
            }
            double yes = event.getCheck().getVl() + 1;
            double oneperc = yes / event.getCheck().getMaxVl();
            double toFind =  oneperc * 100;
            int closest = 0;
            for(int i : percentages) {
                if(Math.abs(i - toFind) < Math.abs(closest - toFind)) {
                    closest = i;
                }
            }
            severityColor = plugin.getConfig().getString("severity.colors." + closest);
        }

        String text = plugin.getConfig().getString("alert")
                .replace("{prefix}", vulcConfig.getString("prefix") + "&r")
                .replace("{player}", event.getPlayer().getName())
                .replace("{check}", event.getCheck().getDisplayName()).replace("{type}", Character.toString(event.getCheck().getType()).toUpperCase())
                .replace("{vl}", Integer.toString(event.getCheck().getVl() + 1))
                .replace("{maxvl}", Integer.toString(event.getCheck().getMaxVl()))
                .replace("{severity}", severityColor)
                .replace("&", "§");

        String clickCommand = plugin.getConfig().getString("click-command");
        TextComponent message = new TextComponent(text);
        // TO-DO: Add hover and click events
        Bukkit.broadcast(text, "vulcan.betteralerts");
    }
}
