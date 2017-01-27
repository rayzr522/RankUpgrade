/**
 * 
 */
package com.rayzr522.rankupgrade.data;

import org.bukkit.configuration.ConfigurationSection;

/**
 * @author Rayzr
 *
 */
public class RankData {

    private String next = "";
    private double cost = 0L;

    private RankData() {
        // Used by .load
    }

    /**
     * @return The next rank
     */
    public String getNext() {
        return next;
    }

    /**
     * @return The cost to upgrade
     */
    public double getCost() {
        return cost;
    }

    public static RankData load(ConfigurationSection config) {
        RankData data = new RankData();
        data.next = config.getString("next");
        data.cost = config.getDouble("cost");
        return data;
    }

}
