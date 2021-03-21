package org.halemaster.flexistats;


import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;

import java.util.Optional;

public class PlayerEvents
{
    public static final String STAT_CURRENT = "current";
    public static final String STAT_MAX = "max";
    public static final String STAT_FORMULAS = "formulas";
    public static final String STAT_POINTS = "statPoints";
    public static final String MULLIGANS_USED = "mulligansUsed";
    public static final String FORMULA_STAT_POINTS = "_stat_points_";
    private StatsPlugin plugin;
    private RandomEvaluator evaluator = new RandomEvaluator();

    public PlayerEvents(StatsPlugin plugin)
    {
        this.plugin = plugin;
    }

    public int getStartingStat(String name)
    {
        Optional<Stat> stat = Stat.loadStat(plugin, name);
        if(stat.isPresent())
        {
            return evaluator.evaluate(stat.get().getBase()).intValue();
        }
        return 0;
    }

    @Listener
    public void onPlayerJoin(ClientConnectionEvent.Join playerJoinEvent)
    {
        Player player = playerJoinEvent.getTargetEntity();
        plugin.getStatsNode().getNode(Stat.ARGUMENT_STATS).getChildrenMap().values().stream()
                .map(node -> node.getKey().toString())
                .forEach(key -> {
                    if (!plugin.getPlayerStatsNode().getNode(player.getUniqueId().toString(),
                            Stat.ARGUMENT_STATS).getChildrenMap().containsKey(key))
                    {
                        ConfigurationNode statNode = plugin.getPlayerStatsNode().getNode(player.getUniqueId().toString(),
                                Stat.ARGUMENT_STATS, key);
                        int amount = getStartingStat(key);
                        statNode.getNode(STAT_CURRENT).setValue(amount);
                        statNode.getNode(STAT_MAX).setValue(amount);
                    }
                });
        plugin.savePlayers();
    }
}
