/**
 * 
 */
package com.rayzr522.rankupgrade;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.rayzr522.creativelynamedlib.utils.text.TextUtils;
import com.rayzr522.rankupgrade.data.RankData;

/**
 * @author Rayzr
 *
 */
public class SignListener implements Listener {

    private RankUpgrade plugin;

    public SignListener(RankUpgrade plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        if (!plugin.getConfig().getString("placeholder").equalsIgnoreCase(e.getLine(0))) {
            return;
        }

        if (!e.getPlayer().hasPermission("RankUpgrade.sign.create")) {
            e.getPlayer().sendMessage(plugin.tr("command.no-permission", "RankUpgrade.sign.create"));
            return;
        }

        String rank = clean(e.getLine(1));
        RankData data = plugin.getRankData(rank);
        if (data == null) {
            e.getPlayer().sendMessage(plugin.tr("rank.invalid", rank));
            return;
        }

        e.setLine(0, plugin.getDisplayText());
        e.setLine(1, plugin.trRaw("sign.rank-color") + rank);
        e.setLine(2, plugin.trRaw("sign.cost-prefix") + TextUtils.format(data.getCost()));
        e.setLine(3, plugin.trRaw("sign.rank-color") + data.getNext());
    }

    private String clean(String input) {
        return input.replace("\uF701", "");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (!(e.getClickedBlock().getState() instanceof Sign)) {
            return;
        }

        Sign sign = (Sign) e.getClickedBlock().getState();
        if (!plugin.getDisplayText().equals(sign.getLine(0))) {
            return;
        }

        Player player = e.getPlayer();

        String rank = TextUtils.stripColor(sign.getLine(1));
        RankData data = plugin.getRankData(rank);
        if (data == null) {
            player.sendMessage(plugin.tr("rank.invalid", rank));
            return;
        }

        UpgradeGUI.create(rank, data, player);
    }

}
