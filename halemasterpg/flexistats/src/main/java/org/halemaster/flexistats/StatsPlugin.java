package org.halemaster.flexistats;


import com.google.inject.Inject;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.halemaster.flexistats.commands.*;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Plugin(id = "flexistats", name = "Flexistats", version = "1.0")
public class StatsPlugin
{
    public static final String STATS_CONFIG = "stats.hocon";
    public static final String PLAYER_STATS_CONFIG = "players.hocon";

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path privateConfigDir;
    private ConfigurationLoader<CommentedConfigurationNode> statsLoader;
    private ConfigurationNode statsRootNode;
    private ConfigurationLoader<CommentedConfigurationNode> playerStatsLoader;
    private ConfigurationNode playerStatsRootNode;
    @Inject
    private Game game;
    @Inject
    private Logger logger;

    public void setPrivateConfigDir(Path path)
    {
        this.privateConfigDir = path;
    }

    public void setGame(Game game)
    {
        this.game = game;
    }

    public Game getGame()
    {
        return game;
    }

    public void setLogger(Logger logger)
    {
        this.logger = logger;
    }

    public Logger getLogger()
    {
        return logger;
    }

    public ConfigurationNode getStatsNode()
    {
        return statsRootNode;
    }

    public ConfigurationNode getPlayerStatsNode()
    {
        return playerStatsRootNode;
    }

    public void saveStats()
    {
        try
        {
            statsLoader.save(statsRootNode);
        } catch(IOException e)
        {
            logger.error("Failed to save stats", e);
        }
    }

    public void savePlayers()
    {
        try
        {
            playerStatsLoader.save(playerStatsRootNode);
        } catch(IOException e)
        {
            logger.error("Failed to save stats", e);
        }
    }

    @Listener
    public void onGameInitialization(GameInitializationEvent event)
    {
        new File(privateConfigDir.toUri()).mkdirs();
        Path statsConfig = privateConfigDir.resolve(new File(STATS_CONFIG).toPath());
        statsLoader = HoconConfigurationLoader.builder().setPath(statsConfig).build();
        try
        {
            statsRootNode = statsLoader.load();
        }
        catch (IOException e)
        {
            logger.warn("Could not load stats, loading default", e);
        }

        Path playerStatsConfig = privateConfigDir.resolve(new File(PLAYER_STATS_CONFIG).toPath());
        playerStatsLoader = HoconConfigurationLoader.builder().setPath(playerStatsConfig).build();
        try
        {
            playerStatsRootNode = playerStatsLoader.load();
        }
        catch (IOException e)
        {
            logger.warn("Could not load stats, loading default", e);
        }

        registerCommands();
        game.getEventManager().registerListeners(this, new PlayerEvents(this));
    }

    @Listener
    public void onGameStopping(GameStoppingEvent event)
    {
        saveStats();
        savePlayers();
    }

    /*******************************************************************************************************************
     * Commands
     ******************************************************************************************************************/
    private CommandSpec setStatsCommand = CommandSpec.builder()
            .description(Text.of(SetStatCommand.DESCRIPTION))
            .permission("flexistats.command." + SetStatCommand.ALIAS)
            .arguments(
                    GenericArguments.onlyOne(GenericArguments.string(Text.of(Stat.ARGUMENT_NAME))),
                    GenericArguments.onlyOne(GenericArguments.string(Text.of(Stat.ARGUMENT_BASE))),
                    GenericArguments.onlyOne(GenericArguments.string(Text.of(Stat.ARGUMENT_MODIFIER))),
                    GenericArguments.onlyOne(GenericArguments.string(Text.of(Stat.ARGUMENT_RECOVERY))),
                    GenericArguments.optional(GenericArguments.remainingJoinedStrings(Text.of(Stat.ARGUMENT_ATTRIBUTES))))
            .executor(new SetStatCommand(this))
            .build();
    private CommandSpec getStatsCommand = CommandSpec.builder()
            .description(Text.of(GetStatCommand.DESCRIPTION))
            .permission("flexistats.command." + GetStatCommand.ALIAS)
            .arguments(
                    GenericArguments.optional(GenericArguments.string(Text.of(Stat.ARGUMENT_NAME))))
            .executor(new GetStatCommand(this))
            .build();
    private CommandSpec removeStatsCommand = CommandSpec.builder()
        .description(Text.of(RemoveStatCommand.DESCRIPTION))
        .permission("flexistats.command." + RemoveStatCommand.ALIAS)
        .arguments(
                GenericArguments.onlyOne(GenericArguments.string(Text.of(Stat.ARGUMENT_NAME))))
        .executor(new RemoveStatCommand(this))
        .build();
    private CommandSpec setMulliganCommand = CommandSpec.builder()
            .description(Text.of(SetMulliganCommand.DESCRIPTION))
            .permission("flexistats.command." + SetMulliganCommand.ALIAS)
            .arguments(
                    GenericArguments.onlyOne(GenericArguments.string(Text.of(SetMulliganCommand.ARGUMENT_AMOUNT))))
            .executor(new SetMulliganCommand(this))
            .build();
    private CommandSpec setStatPointsCommand = CommandSpec.builder()
            .description(Text.of(SetStatPointsCommand.DESCRIPTION))
            .permission("flexistats.command." + SetStatPointsCommand.ALIAS)
            .arguments(
                    GenericArguments.onlyOne(GenericArguments.string(Text.of(SetStatPointsCommand.ARGUMENT_BASE))))
            .executor(new SetStatPointsCommand(this))
            .build();

    private void registerCommands()
    {
        game.getCommandManager().register(this, setStatsCommand, SetStatCommand.ALIAS);
        game.getCommandManager().register(this, getStatsCommand, GetStatCommand.ALIAS);
        game.getCommandManager().register(this, removeStatsCommand, RemoveStatCommand.ALIAS);
        game.getCommandManager().register(this, setMulliganCommand, SetMulliganCommand.ALIAS);
        game.getCommandManager().register(this, setStatPointsCommand, SetStatPointsCommand.ALIAS);
    }
}
