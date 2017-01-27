/**
 * 
 */
package com.rayzr522.rankupgrade;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.rayzr522.creativelynamedlib.gui.ClickEvent;
import com.rayzr522.creativelynamedlib.gui.Component;
import com.rayzr522.creativelynamedlib.gui.GUI;
import com.rayzr522.creativelynamedlib.utils.text.TextUtils;
import com.rayzr522.creativelynamedlib.utils.types.Point;
import com.rayzr522.rankupgrade.data.RankData;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

/**
 * @author Rayzr
 *
 */
public class UpgradeGUI {

    public static void create(String rank, RankData data, Player player) {
        RankUpgrade plugin = RankUpgrade.getInstance();

        Economy eco = plugin.getEconomy();
        Permission perm = plugin.getPermissions();

        if (eco.getBalance(player) < data.getCost()) {
            player.sendMessage(plugin.tr("rank.failed.not-enough-money"));
            return;
        } else if (perm.playerInGroup(player, data.getNext())) {
            player.sendMessage(plugin.tr("rank.failed.already-have", data.getNext()));
            return;
        } else if (!perm.playerInGroup(player, rank)) {
            player.sendMessage(plugin.tr("rank.failed.require-higher", rank, data.getNext()));
            return;
        }

        new GUI(3, plugin.trRaw("gui.title"))
                .add(Component.create()
                        .type(Material.STAINED_GLASS)
                        .colored(DyeColor.LIME)
                        .named(plugin.trRaw("gui.accept.name"))
                        .withLore(plugin.trRaw("gui.accept.lore"))
                        .ofSize(4, 1).onClick(e -> accept(rank, data, player, e)).build(),
                        Point.at(0, 1))
                .add(Component.create()
                        .type(Material.DIAMOND)
                        .named(plugin.trRaw("gui.description.name", rank, data.getNext(), TextUtils.format(data.getCost())))
                        .withLore(plugin.trRaw("gui.description.lore", rank, data.getNext(), TextUtils.format(data.getCost())))
                        .build(),
                        Point.at(4, 1))
                .add(Component.create()
                        .type(Material.STAINED_GLASS)
                        .colored(DyeColor.RED)
                        .named(plugin.trRaw("gui.cancel.name"))
                        .withLore(plugin.trRaw("gui.cancel.lore"))
                        .ofSize(4, 1).onClick(e -> {
                            player.sendMessage(plugin.tr("rank.cancelled"));
                        }).build(),
                        Point.at(5, 1))
                .open(player);
    }

    private static void accept(String rank, RankData data, Player player, ClickEvent e) {
        RankUpgrade plugin = RankUpgrade.getInstance();
        Economy eco = plugin.getEconomy();

        String command = String.format("groupmanager:manuadd %s %s", player.getName(), data.getNext());
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);

        eco.withdrawPlayer(player, data.getCost());
        player.sendMessage(plugin.tr("rank.success", rank, data.getNext(), TextUtils.format(data.getCost())));
    }

}
