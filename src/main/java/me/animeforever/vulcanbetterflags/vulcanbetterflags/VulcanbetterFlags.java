package me.animeforever.vulcanbetterflags.vulcanbetterflags;

import com.google.common.base.Charsets;
import me.animeforever.vulcanbetterflags.vulcanbetterflags.commands.vulcanbetterflags;
import me.animeforever.vulcanbetterflags.vulcanbetterflags.events.flagEvent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public final class VulcanbetterFlags extends JavaPlugin {

    public static Plugin plugin;
    public static YamlConfiguration vulcConfig;

    @Override
    public void onEnable() {
        // Load config
        saveDefaultConfig();
        reloadConfig();
        plugin = this;

        // Check API
        if(!new File("plugins/Vulcan/config.yml").exists()) {
            getLogger().severe("Vulcan not found! Please download it and make sure folder Vulcan exists");
            getServer().getPluginManager().disablePlugin(this);
        }

        YamlConfiguration vulcanConfig = new YamlConfiguration();
        try {
            FileInputStream stream = new FileInputStream(new File("plugins/Vulcan/config.yml"));
            vulcanConfig.load(new InputStreamReader(stream, Charsets.UTF_8));
            vulcConfig = vulcanConfig;
            Boolean enabled = vulcanConfig.getBoolean("settings.enable-api");
            if(!enabled) {
                getLogger().info("Vulcan API was disabled, enabling it now");
                vulcanConfig.set("settings.enable-api", true);
                vulcanConfig.save(new File("plugins/Vulcan/config.yml"));
                Plugin plugin = Bukkit.getPluginManager().getPlugin("Vulcan");
                Bukkit.getPluginManager().disablePlugin(plugin);
                Bukkit.getPluginManager().enablePlugin(plugin);
            }
        } catch (Exception e) {
            getLogger().severe("There was an error loading vulcan's config file to check if api is enabled. Plugin will assume it is enabled");
        }

        // Register commands
        getCommand("vulcanbetterflags").setExecutor(new vulcanbetterflags());

        // Register events
        getServer().getPluginManager().registerEvents(new flagEvent(), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("VulcanbetterFlags has been disabled!");
    }
}
