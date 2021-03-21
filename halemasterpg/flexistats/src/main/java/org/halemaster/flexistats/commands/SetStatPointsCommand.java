package org.halemaster.flexistats.commands;

import org.halemaster.flexistats.StatsPlugin;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

public class SetStatPointsCommand implements CommandExecutor
{
    public static final String ALIAS = "setStatPoints";
    public static final String DESCRIPTION = "set starting stat points";
    public static final String ARGUMENT_BASE = "base";
    public static final String STAT_POINT_NAME = "statPoints";


    private StatsPlugin plugin;

    public SetStatPointsCommand(StatsPlugin plugin)
    {
        this.plugin = plugin;
    }


    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
    {
        String base = (String) args.getOne(ARGUMENT_BASE).orElseThrow(IllegalArgumentException::new);
        plugin.getStatsNode().getNode(STAT_POINT_NAME,ARGUMENT_BASE).setValue(base);
        plugin.saveStats();

        src.sendMessage(Text.of("Set Starting Stat Points to: " + base));
        return CommandResult.success();
    }
}
