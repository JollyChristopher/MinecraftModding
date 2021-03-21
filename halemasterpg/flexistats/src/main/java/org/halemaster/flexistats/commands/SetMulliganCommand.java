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

public class SetMulliganCommand implements CommandExecutor
{
    public static final String ALIAS = "setMulligan";
    public static final String DESCRIPTION = "set max mulligans";
    public static final String ARGUMENT_AMOUNT = "amount";
    public static final String MULLIGAN_NAME = "mulligan";


    private StatsPlugin plugin;

    public SetMulliganCommand(StatsPlugin plugin)
    {
        this.plugin = plugin;
    }


    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
    {
        int amount = Integer.parseInt((String) args.getOne(ARGUMENT_AMOUNT).orElseThrow(IllegalArgumentException::new));
        plugin.getStatsNode().getNode(MULLIGAN_NAME,ARGUMENT_AMOUNT).setValue(amount);
        plugin.saveStats();

        src.sendMessage(Text.of("Set Mulligans to: " + amount));
        return CommandResult.success();
    }
}
