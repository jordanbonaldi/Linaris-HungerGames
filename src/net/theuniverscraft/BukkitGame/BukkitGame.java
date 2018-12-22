package net.theuniverscraft.BukkitGame;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import net.theuniverscraft.BukkitGame.Background.BGChat;
import net.theuniverscraft.BukkitGame.Commands.CommandHungerGame;
import net.theuniverscraft.BukkitGame.Config.BukkitGameConfig;
import net.theuniverscraft.BukkitGame.Config.WorldManager;
import net.theuniverscraft.BukkitGame.Config.ZoneGlassSave;
import net.theuniverscraft.BukkitGame.Config.ZonesSave;
import net.theuniverscraft.BukkitGame.Listeners.MotdSetterListener;
import net.theuniverscraft.BukkitGame.Listeners.OperatorModeListener;
import net.theuniverscraft.BukkitGame.Utils.ChestsUtils;
import net.theuniverscraft.BukkitGame.Utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.skelerex.LinarisKits.api.GameAPI;
import com.skelerex.LinarisKits.api.GameType;

public class BukkitGame extends JavaPlugin {
	private MotdSetterListener motdSetter;
	private static BukkitGame instance;
	
	private boolean m_winnerFind = false;
	
	private GameAPI m_gameAPI = new GameAPI(GameType.HUNGER_GAMES);
	public static GameAPI getGameAPI() { return instance.m_gameAPI; }
	
	public static BukkitGame getInstance() {
		return instance;
	}
	
	public void onEnable()
	{
		instance = this;
		ChestsUtils.fillAllChests();
		BukkitGameConfig.getInstance();
		WorldManager.getInstance();
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		Game.getInstance();
		ZonesSave.getInstance();
		ZoneGlassSave.getInstance();
		
		motdSetter = new MotdSetterListener(("§a<m>m")
				.replaceAll("<m>", BukkitGameConfig.getInstance().TIME_BEFORE_GAME_BEGIN.toString()));
		
		List<String> aliases = new LinkedList<String>();
		
		aliases.add("hg");
		this.getCommand("hungergame").setAliases(aliases);
		this.getCommand("hungergame").setExecutor(new CommandHungerGame());
		
		getServer().getPluginManager().registerEvents(motdSetter, this);
		if(!ZonesSave.getInstance().allZoneIsDefined()) {
			getServer().getPluginManager().registerEvents(new OperatorModeListener(), this);
			motdSetter.setMotd("§cOnly ADMINS");
		}
		else {
			Game.getInstance().setGameStatut(GameStatut.BEFORE_GAME);
		}
		
		try {
			File file = new File("plugins/BukkitGame_TheUniversCraft/saves/spawn.yml");
			if(!file.exists()) file.createNewFile();
			FileConfiguration fileSave = YamlConfiguration.loadConfiguration(file);
			
			Location spawn = Bukkit.getWorld("world").getSpawnLocation();
			if(fileSave.contains("spawn")) {
				spawn = Utils.getLocation(fileSave.getConfigurationSection("spawn"));
			}
			Bukkit.getWorld("world").setSpawnLocation(spawn.getBlockX(), spawn.getBlockY(), spawn.getBlockZ());
		}
		catch(Exception e) { e.printStackTrace(); }
		
		BGChat.sendConsoleMessage(ChatColor.WHITE+"["+ChatColor.GOLD+"BukkitGame"+ChatColor.WHITE+" TheUniversCraft] Enabled");
	}
	
	
	public void setMotd(String motd) {
		motdSetter.setMotd(motd);
	}
	
	public void unRegisterListeners() {
		HandlerList.unregisterAll(this);
		getServer().getPluginManager().registerEvents(motdSetter, this);
	}
	
	
	
	
}
