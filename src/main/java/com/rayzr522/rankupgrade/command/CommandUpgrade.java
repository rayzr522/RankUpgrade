/**
 * 
 */
package com.rayzr522.rankupgrade.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.rayzr522.rankupgrade.RankUpgrade;
import com.rayzr522.rankupgrade.UpgradeGUI;
import com.rayzr522.rankupgrade.data.RankData;

/**
 * @author Rayzr
 *
 */
public class CommandUpgrade implements CommandExecutor {

    private RankUpgrade plugin;

    /**
     * @param rankUpgrade
     */
    public CommandUpgrade(RankUpgrade plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.tr("command.only-players"));
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("RankUpgrade.upgrade")) {
            player.sendMessage(plugin.tr("command.no-permission", "RankUpgrade.upgrade"));
            return true;
        }

        String current = plugin.getPermissions().getPrimaryGroup(player);
        RankData data = plugin.getRankData(current);
        if (data == null) {
            player.sendMessage(plugin.tr("rank.failed.no-more", current));
            return true;
        }

        UpgradeGUI.create(current, data, player);

        return true;
    }

}
