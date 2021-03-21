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

public class SetStatCommand implements CommandExecutor
{
    public static final String ALIAS = "setStat";
    public static final String DESCRIPTION = "set a stat";

    private StatsPlugin plugin;

    public SetStatCommand(StatsPlugin plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
    {
        Stat stat = new Stat();
        stat.setName((String) args.getOne(Stat.ARGUMENT_NAME).orElseThrow(IllegalArgumentException::new));
        stat.setBase((String) args.getOne(Stat.ARGUMENT_BASE).orElseThrow(IllegalArgumentException::new));
        stat.setModifier((String) args.getOne(Stat.ARGUMENT_MODIFIER).orElseThrow(IllegalArgumentException::new));
        stat.setRecovery((String) args.getOne(Stat.ARGUMENT_RECOVERY).orElseThrow(IllegalArgumentException::new));
        stat.setAttributes(Arrays.asList(((String) args.getOne(Stat.ARGUMENT_ATTRIBUTES).orElse("")).split(" ")));

        stat.saveStat(plugin);

        src.sendMessage(Text.of("Created stat:\n" + stat.toString()));
        return CommandResult.success();
    }
}
