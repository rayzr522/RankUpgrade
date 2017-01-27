/**
 * 
 */
package com.rayzr522.rankupgrade.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.rayzr522.creativelynamedlib.utils.text.TextUtils;
import com.rayzr522.rankupgrade.RankUpgrade;
import com.rayzr522.rankupgrade.data.RankData;

/**
 * @author Rayzr
 *
 */
public class CommandRank implements CommandExecutor {

    private RankUpgrade plugin;

    /**
     * @param rankUpgrade
     */
    public CommandRank(RankUpgrade plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.tr("command.only-players"));
            return true;
        }

        Player player = (Player) sender;

        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            if (!player.hasPermission("RankUpgrade.admin")) {
                player.sendMessage(plugin.tr("command.no-permission", "RankUpgrade.admin"));
                return true;
            }
            plugin.loadConfig();
            player.sendMessage(plugin.tr("command.reloaded"));
            return true;
        }

        if (!player.hasPermission("RankUpgrade.view")) {
            sender.sendMessage(plugin.tr("command.no-permission", "RankUpgrade.view"));
            return true;
        }

        String current = plugin.getPermissions().getPrimaryGroup(player);
        RankData data = plugin.getRankData(current);

        if (data == null) {
            player.sendMessage(plugin.tr("command.rank.top", current));
        } else {
            player.sendMessage(plugin.tr("command.rank.next", current, data.getNext(), TextUtils.format(data.getCost())));
        }

        return true;
    }

}
