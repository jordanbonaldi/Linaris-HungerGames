package net.theuniverscraft.BukkitGame.Config;

import net.theuniverscraft.BukkitGame.BukkitGame;

import org.bukkit.configuration.file.FileConfiguration;

public class BukkitGameConfig {
	public Integer ZONE_NUMBER = 24;
	public Integer TIME_BEFORE_GAME_BEGIN = 5;
	public Integer AREA_LIMIT = 300;
	public Integer MIN_PEOPLE_FOR_BEGIN = 2;
	public Integer MAGIC_CHEST_INTERVAL = 4;
	
	public String HUNGER_WORLD_NAME = "worldhunger";
	
	private BukkitGame plugin;
	private FileConfiguration config;
	
	private static BukkitGameConfig instance = null;
	
	public static BukkitGameConfig getInstance() {
		if(instance == null) instance = new BukkitGameConfig(BukkitGame.getInstance());
		return instance;
	}
	
	private BukkitGameConfig(BukkitGame thePlugin) {
		plugin = thePlugin;
		config = plugin.getConfig();
		
		if(config.get("zone_number") != null) ZONE_NUMBER = config.getInt("zone_number");
		else config.set("zone_number", ZONE_NUMBER);
		
		if(config.get("time_before_game_begin") != null) TIME_BEFORE_GAME_BEGIN = config.getInt("time_before_game_begin");
		else config.set("time_before_game_begin", TIME_BEFORE_GAME_BEGIN);
		
		if(config.get("area_limit") != null) AREA_LIMIT = config.getInt("area_limit");
		else config.set("area_limit", AREA_LIMIT);
		
		if(config.get("min_people_for_begin") != null) MIN_PEOPLE_FOR_BEGIN = config.getInt("min_people_for_begin");
		else config.set("min_people_for_begin", MIN_PEOPLE_FOR_BEGIN);
		
		if(config.get("magic_chest_interval") != null) MAGIC_CHEST_INTERVAL = config.getInt("magic_chest_interval");
		else config.set("magic_chest_interval", MAGIC_CHEST_INTERVAL);
		
	
		
		// RECUPERATION DU MONDE ACTUEL
		
		if(config.contains("world_hunger")) HUNGER_WORLD_NAME = config.getString("world_hunger");
		else config.set("world_hunger", HUNGER_WORLD_NAME);
		
		plugin.saveConfig();
	}
}
