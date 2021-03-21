package org.halemaster.flexistats;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Stat
{
    public static final String ARGUMENT_NAME = "name";
    public static final String ARGUMENT_BASE = "base";
    public static final String ARGUMENT_MODIFIER = "modifier";
    public static final String ARGUMENT_RECOVERY = "recovery";
    public static final String ARGUMENT_ATTRIBUTES = "attributes";
    public static final String ARGUMENT_STATS = "stats";

    private String name;
    private String base;
    private String modifier;
    private String recovery;
    private List<String> attributes;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getBase()
    {
        return base;
    }

    public void setBase(String base)
    {
        this.base = base;
    }

    public String getModifier()
    {
        return modifier;
    }

    public void setModifier(String modifier)
    {
        this.modifier = modifier;
    }

    public String getRecovery()
    {
        return recovery;
    }

    public void setRecovery(String recovery)
    {
        this.recovery = recovery;
    }

    public Optional<List<String>> getAttributes()
    {
        return Optional.of(attributes);
    }

    public void setAttributes(List<String> attributes)
    {
        this.attributes = attributes;
    }

    public void saveStat(StatsPlugin plugin)
    {
        plugin.getStatsNode().getNode(ARGUMENT_STATS, getName(), ARGUMENT_BASE).setValue(getBase());
        plugin.getStatsNode().getNode(ARGUMENT_STATS, getName(), ARGUMENT_MODIFIER).setValue(getModifier());
        plugin.getStatsNode().getNode(ARGUMENT_STATS, getName(), ARGUMENT_RECOVERY).setValue(getRecovery());
        if(getAttributes().isPresent())
        {
            plugin.getStatsNode().getNode(ARGUMENT_STATS, getName(), ARGUMENT_ATTRIBUTES).setValue(getAttributes().get());
        }
        plugin.saveStats();
    }

    public void deleteStat(StatsPlugin plugin)
    {
        plugin.getStatsNode().getNode(ARGUMENT_STATS).removeChild(getName());
        plugin.saveStats();
    }

    public static Optional<Stat> loadStat(StatsPlugin plugin, String name)
    {
        if(plugin.getStatsNode().getNode(ARGUMENT_STATS, name).getValue() != null)
        {
            Stat stat = new Stat();
            stat.setName(name);
            stat.setBase(plugin.getStatsNode().getNode(ARGUMENT_STATS, name, ARGUMENT_BASE).getString());
            stat.setModifier(plugin.getStatsNode().getNode(ARGUMENT_STATS, name, ARGUMENT_MODIFIER).getString());
            stat.setRecovery(plugin.getStatsNode().getNode(ARGUMENT_STATS, name, ARGUMENT_RECOVERY).getString());
            stat.setAttributes((List<String>)plugin.getStatsNode().getNode(ARGUMENT_STATS, name, ARGUMENT_ATTRIBUTES).getValue());
            return Optional.of(stat);
        }
        return Optional.empty();
    }

    @Override
    public String toString()
    {
        return
                name + "{" +
                        "\n\tbase=" + base +
                        "\n\tmodifier=" + modifier +
                        "\n\trecovery=" + recovery +
                        "\n\tattributes [" +
                            getAttributes().orElse(new ArrayList<>()).stream().map(att -> "\n\t\t" + att)
                                    .collect(Collectors.toList()) +
                        "\n\t]\n}";
    }
}
