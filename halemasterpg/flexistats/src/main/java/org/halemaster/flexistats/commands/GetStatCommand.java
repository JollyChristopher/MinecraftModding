package org.halemaster.flexistats.commands;

import org.halemaster.flexistats.Stat;
import org.halemaster.flexistats.StatsPlugin;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class GetStatCommand implements CommandExecutor
{
    public static final String ALIAS = "stat";
    public static final String DESCRIPTION = "view a stat";


    private StatsPlugin plugin;

    public GetStatCommand(StatsPlugin plugin)
    {
        this.plugin = plugin;
    }

    public void printStat(String name, CommandSource src)
    {
        Optional<Stat> stat = Stat.loadStat(plugin, name);
        if(stat.isPresent())
        {
            src.sendMessage(Text.of("\n" + stat.get().toString()));
        }
        else
        {
            src.sendMessage(Text.of(name + " does not exist!"));
        }
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
    {
        Optional<String> arg = args.getOne(Stat.ARGUMENT_NAME);
        if(arg.isPresent())
        {
            printStat(arg.get(), src);
        }
        else
        {
            if(!plugin.getStatsNode().getNode(Stat.ARGUMENT_STATS).getChildrenMap().isEmpty())
            {
                plugin.getStatsNode().getNode(Stat.ARGUMENT_STATS).getChildrenMap().values().stream()
                        .map(node -> (String) node.getKey())
                        .forEach(key -> printStat(key, src));
            }
            else
            {
                src.sendMessage(Text.of("There aren't any stats."));
            }
        }
        return CommandResult.success();
    }
}
