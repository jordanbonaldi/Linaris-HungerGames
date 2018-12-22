package net.theuniverscraft.BukkitGame.Config;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import net.theuniverscraft.BukkitGame.Exceptions.NoZoneAvailableException;
import net.theuniverscraft.BukkitGame.Exceptions.UnknownZoneException;
import net.theuniverscraft.BukkitGame.Exceptions.ZoneHasentLocationException;
import net.theuniverscraft.BukkitGame.Exceptions.ZoneHasentPlayerException;
import net.theuniverscraft.BukkitGame.Utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class ZonesSave {
	private File file;
	private FileConfiguration fileSave;
	
	private List<Zone> zones = new LinkedList<Zone>();
	
	private static ZonesSave instance = null;
	public static ZonesSave getInstance() {
		if(instance == null) instance = new ZonesSave();
		return instance;
	}
	
	private ZonesSave() {
		try {
			File dirs = new File("plugins/BukkitGame_TheUniversCraft/saves");
			dirs.mkdirs();
			
			file = new File("plugins/BukkitGame_TheUniversCraft/saves/zones.yml");
			if(!file.exists()) file.createNewFile();
			fileSave = YamlConfiguration.loadConfiguration(file);
			
			for(int i = 0; i < BukkitGameConfig.getInstance().ZONE_NUMBER; i++) {
				Location loc = Utils.getLocation(fileSave.getConfigurationSection("zones.zone"+i));
				if(loc != null) zones.add(new Zone(i, loc));
				else zones.add(new Zone(i));
			}
			
			fileSave.save(file);
		}
		catch(Exception e) {}
	}
	
	public Zone getZone(Integer idZone) throws UnknownZoneException {
		for(int i = 0; i < BukkitGameConfig.getInstance().ZONE_NUMBER; i++) {
			if(zones.get(i).getId() == idZone) {
				return zones.get(i);
			}
		}
		throw new UnknownZoneException("§cCette emplacement n'existe pas !");
	}
	
	public Zone getZone(String player) throws UnknownZoneException {
		for(int i = 0; i < BukkitGameConfig.getInstance().ZONE_NUMBER; i++) {
			try {
				if(zones.get(i).getPlayer().equalsIgnoreCase(player)) {
					return zones.get(i);
				}
			}
			catch (ZoneHasentPlayerException e) {}
		}
		
		throw new UnknownZoneException("§cCette emplacement n'existe pas !");
	}
	
	public void setPlayerZone(String player) throws NoZoneAvailableException {
		for(int i = 0; i < BukkitGameConfig.getInstance().ZONE_NUMBER; i++) {
			if(zones.get(i).hasLocation() && !zones.get(i).hasPlayer()) {
				zones.get(i).setPlayer(player);
				return;
			}
		}
				throw new NoZoneAvailableException("§cLa partie est pleine !");

		

	}
	public void removePlayerZone(String player) {
		for(int i = 0; i < BukkitGameConfig.getInstance().ZONE_NUMBER; i++) {
			try {
				if(zones.get(i).hasPlayer()) {
					if(zones.get(i).getPlayer().equalsIgnoreCase(player)) {
						zones.get(i).setPlayer(null);
					}
				}
			}
			catch (ZoneHasentPlayerException e) { e.printStackTrace(); }
		}
	}
	
	public void setAllPlayerZone() {
		String player = null;
		Player[] players = Bukkit.getOnlinePlayers();
		for(Player objPlayer : players) {
			player = objPlayer.getName();
			
			boolean find = false;
			for(int i = 0; i < BukkitGameConfig.getInstance().ZONE_NUMBER && !find; i++) {
				if(zones.get(i).hasLocation() && !zones.get(i).hasPlayer() && !playerHasZone(player)) {
					zones.get(i).setPlayer(player);
					find = true;
				}
			}
		}
		
	}
	
	public boolean playerHasZone(String player) {
		for(int i = 0; i < BukkitGameConfig.getInstance().ZONE_NUMBER; i++) {
			if(zones.get(i).hasPlayer()) {
				try { if(zones.get(i).getPlayer().equalsIgnoreCase(player)) return true; } 
				catch (ZoneHasentPlayerException e) {}
			}
		}
		return false;
	}
	
	public void setZone(Zone zone) throws ZoneHasentLocationException {
		for(int i = 0; i < BukkitGameConfig.getInstance().ZONE_NUMBER; i++) {
			if(zones.get(i).getId() == zone.getId()) {
				// La zone existe deja
				zones.remove(i);
				break;
			}
		}
		zones.add(zone);
		
		String nameSection = "zones.zone"+zone.getId();
		if(!fileSave.contains(nameSection)) fileSave.createSection(nameSection);
		Utils.setLocation(fileSave.getConfigurationSection(nameSection), zone.getLocation());
		
		try { fileSave.save(file); }
		catch (IOException e) { e.printStackTrace(); }
	}
	
	public Boolean allZoneIsDefined() {
		for(int i = 0; i < BukkitGameConfig.getInstance().ZONE_NUMBER; i++) {
			if(!zones.get(i).hasLocation()) {
				return false;
			}
		}
		return true;
	}
	
	public class Zone {
		private Integer m_id;
		private Location m_location;
		private String m_player;
		
		private Zone(Integer id, Location location) {
			m_id = id;
			m_location = location;
			m_player = null;
		}
		private Zone(Integer id) {
			m_id = id;
			m_location = null;
			m_player = null;
		}
		
		public void setPlayer(String player) {
			m_player = player;
		}
		public boolean hasPlayer() {
			if(m_player == null) return false;
			return true;
		}
		public String getPlayer() throws ZoneHasentPlayerException {
			if(m_player != null) return m_player;
			throw new ZoneHasentPlayerException("§cCette emplacement n'a pas de joueur !");
		}
		
		public void setLocation(Location location) {
			m_location = location;
		}
		public boolean hasLocation() {
			if(m_location == null) return false;
			return true;
		}
		public Location getLocation() throws ZoneHasentLocationException {
			if(m_location != null) return m_location;
			throw new ZoneHasentLocationException("§cCette emplacement n'a pas de location !");
		}
		
		public Integer getId() { return m_id; }
	}
}
