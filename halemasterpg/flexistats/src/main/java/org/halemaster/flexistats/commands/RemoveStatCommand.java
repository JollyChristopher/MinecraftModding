package org.halemaster.flexistats.commands;

import org.halemaster.flexistats.Stat;
import org.halemaster.flexistats.StatsPlugin;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import java.util.Arrays;

public class RemoveStatCommand implements CommandExecutor
{
    public static final String ALIAS = "removeStat";
    public static final String DESCRIPTION = "remove a stat";


    private StatsPlugin plugin;

    public RemoveStatCommand(StatsPlugin plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
    {
        Stat stat = new Stat();
        stat.setName((String) args.getOne(Stat.ARGUMENT_NAME).orElseThrow(IllegalArgumentException::new));

        if(plugin.getStatsNode().getNode(Stat.ARGUMENT_STATS, stat.getName()).getValue() != null)
        {
            stat.deleteStat(plugin);
            src.sendMessage(Text.of("Removed stat: " + stat.getName()));
        }
        else
        {
            src.sendMessage(Text.of("Stat doesn't exist: " + stat.getName()));
        }
        return CommandResult.success();
    }
}
