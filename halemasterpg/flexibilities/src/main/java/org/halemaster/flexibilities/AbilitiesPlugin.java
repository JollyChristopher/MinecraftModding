package org.halemaster.flexibilities;

import com.google.inject.Inject;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Plugin(id = "flexibilities", name = "Flexibilities", version = "1.0")
public class AbilitiesPlugin
{
    public static final String STATS_CONFIG = "abilities.hocon";
    public static final String PLAYER_STATS_CONFIG = "players.hocon";

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path privateConfigDir;
    private ConfigurationLoader<CommentedConfigurationNode> abilitiesLoader;
    private ConfigurationNode abilitiesRootNode;
    private ConfigurationLoader<CommentedConfigurationNode> playerLoader;
    private ConfigurationNode playerRootNode;
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

    public ConfigurationNode getAbilitiesNode()
    {
        return abilitiesRootNode;
    }

    public ConfigurationNode getPlayerNode()
    {
        return playerRootNode;
    }

    public void saveAbilities()
    {
        try
        {
            abilitiesLoader.save(abilitiesRootNode);
        } catch(IOException e)
        {
            logger.error("Failed to save stats", e);
        }
    }

    public void savePlayers()
    {
        try
        {
            playerLoader.save(playerRootNode);
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
        abilitiesLoader = HoconConfigurationLoader.builder().setPath(statsConfig).build();
        try
        {
            abilitiesRootNode = abilitiesLoader.load();
        }
        catch (IOException e)
        {
            logger.warn("Could not load stats, loading default", e);
        }

        Path playerStatsConfig = privateConfigDir.resolve(new File(PLAYER_STATS_CONFIG).toPath());
        playerLoader = HoconConfigurationLoader.builder().setPath(playerStatsConfig).build();
        try
        {
            playerRootNode = playerLoader.load();
        }
        catch (IOException e)
        {
            logger.warn("Could not load stats, loading default", e);
        }

        //registerCommands();
        //game.getEventManager().registerListeners(this, new PlayerEvents(this));
    }

    @Listener
    public void onGameStopping(GameStoppingEvent event)
    {
        saveAbilities();
        savePlayers();
    }
}
