package net.theuniverscraft.BukkitGame.Config;

import java.io.File;
import java.io.IOException;

import net.theuniverscraft.BukkitGame.Utils.LocationZone;
import net.theuniverscraft.BukkitGame.Utils.Utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ZoneGlassSave {
	private File file;
	private FileConfiguration fileSave;
	
	public LocationZone locZone = null;
	
	private static ZoneGlassSave instance = null;
	public static ZoneGlassSave getInstance() {
		if(instance == null) instance = new ZoneGlassSave();
		return instance;
	}
	
	private ZoneGlassSave() {
		try {
			File dirs = new File("plugins/BukkitGame_TheUniversCraft/saves");
			dirs.mkdirs();
			
			file = new File("plugins/BukkitGame_TheUniversCraft/saves/zone_glass.yml");
			if(!file.exists()) file.createNewFile();
			fileSave = YamlConfiguration.loadConfiguration(file);
			
			if(fileSave.contains("loc1") && fileSave.contains("loc2")) {
				locZone = new LocationZone(Utils.getLocation(fileSave.getConfigurationSection("loc1")),
						Utils.getLocation(fileSave.getConfigurationSection("loc2")));
			}
		}
		catch(Exception e) {}
	}
	
	public boolean hasZone() {
		return locZone != null;
	}
	
	public void setLocations(Location loc1, Location loc2) {
		locZone = new LocationZone(loc1, loc2);
		
		String nameSection = "loc1";
		if(!fileSave.contains(nameSection)) fileSave.createSection(nameSection);
		Utils.setLocation(fileSave.getConfigurationSection(nameSection), loc1);
		
		nameSection = "loc2";
		if(!fileSave.contains(nameSection)) fileSave.createSection(nameSection);
		Utils.setLocation(fileSave.getConfigurationSection(nameSection), loc2);
		
		try { fileSave.save(file); }
		catch (IOException e) { e.printStackTrace(); }
	}
	
	public void breakGlass() {
		if(locZone != null) locZone.breakInto(Material.GLASS);
	}
}
