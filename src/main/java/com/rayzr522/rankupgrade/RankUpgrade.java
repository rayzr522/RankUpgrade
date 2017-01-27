package com.rayzr522.rankupgrade;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.rayzr522.creativelynamedlib.utils.text.TextUtils;
import com.rayzr522.rankupgrade.command.CommandRank;
import com.rayzr522.rankupgrade.command.CommandUpgrade;
import com.rayzr522.rankupgrade.data.Messages;
import com.rayzr522.rankupgrade.data.RankData;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

/**
 * @author Rayzr522
 */
public class RankUpgrade extends JavaPlugin {

    private static RankUpgrade instance;

    private Map<String, RankData> ranks = new HashMap<>();
    private Messages messages;

    private Economy economy;
    private Permission permission;

    @Override
    public void onEnable() {
        instance = this;

        if (!setupEconomy()) {
            getLogger().severe("Failed to load economy handler! Please make sure you have Vault properly installed, along with some form of economy plugin.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        if (!setupPermission()) {
            getLogger().severe("Failed to load permissions handler! Please make sure you have Vault properly installed, along with GroupManager.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        loadConfig();

        Bukkit.getPluginManager().registerEvents(new SignListener(this), this);

        getCommand("rank").setExecutor(new CommandRank(this));
        getCommand("upgrade").setExecutor(new CommandUpgrade(this));
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public void loadConfig() {
        getConfig("config.yml");
        reloadConfig();

        ranks.clear();

        YamlConfiguration ranksConfig = getConfig("ranks.yml");
        for (String key : ranksConfig.getKeys(false)) {
            ranks.put(key, RankData.load(ranksConfig.getConfigurationSection(key)));
        }

        messages = new Messages();
        messages.load(getConfig("messages.yml"));
    }

    public YamlConfiguration getConfig(String path) {
        if (!getFile(path).exists() && getResource(path) != null) {
            saveResource(path, false);
        }
        return YamlConfiguration.loadConfiguration(getFile(path));
    }

    public File getFile(String path) {
        return new File(getDataFolder(), path.replace('/', File.pathSeparatorChar));
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }

    private boolean setupPermission() {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }

        return (permission != null);
    }

    /**
     * @param rank The name of the rank-data to get
     */
    public RankData getRankData(String rank) {
        return ranks.get(rank);
    }

    public String getPlaceholder() {
        return getConfig().getString("placeholder");
    }

    public String getDisplayText() {
        return TextUtils.colorize(getConfig().getString("display-text"));
    }

    public String tr(String key, Object... objects) {
        return messages.tr(key, objects);
    }
    
    public String trRaw(String key, Object... objects) {
        return messages.trRaw(key, objects);
    }

    public static RankUpgrade getInstance() {
        return instance;
    }

    public Economy getEconomy() {
        return economy;
    }

    public Permission getPermissions() {
        return permission;
    }

}
