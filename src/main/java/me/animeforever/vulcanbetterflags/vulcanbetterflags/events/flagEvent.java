package me.animeforever.vulcanbetterflags.vulcanbetterflags.events;

import me.frep.vulcan.api.event.VulcanFlagEvent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.lang.reflect.InvocationTargetException;
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

        Player player = event.getPlayer();
        String hoverText = plugin.getConfig().getStringList("hover-message").stream().map(Object::toString).collect(Collectors.joining("\n"))
                // .replace("{ping}", getPing(player) + " ms")
                .replace("{description}", event.getCheck().getDescription())
                .replace("{info}", event.getInfo())
                .replace("{player}", event.getPlayer().getName())
                .replace("&", "§");
        TextComponent message = new TextComponent(text);

        // Hover event to show text
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent(hoverText)}));
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, plugin.getConfig().getString("click-command").replace("{player}", event.getPlayer().getName())));

        Bukkit.getOnlinePlayers().forEach(p -> {
            if(p.hasPermission("vulcan.betteralerts")) {
                p.spigot().sendMessage(message);
            }
        });
    }
}
