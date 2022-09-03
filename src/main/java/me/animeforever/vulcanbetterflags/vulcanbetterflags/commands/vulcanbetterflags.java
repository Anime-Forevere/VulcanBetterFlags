package me.animeforever.vulcanbetterflags.vulcanbetterflags.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import static me.animeforever.vulcanbetterflags.vulcanbetterflags.VulcanbetterFlags.plugin;

public class vulcanbetterflags implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("")) {
            sender.sendMessage("§cYou do not have permission to use this command");
            return false;
        }
        plugin.reloadConfig();
        sender.sendMessage("§aConfig reloaded!");
        return true;
    }
}
