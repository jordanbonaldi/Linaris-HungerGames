package net.theuniverscraft.BukkitGame.Timers;

import java.util.HashMap;

import net.theuniverscraft.BukkitGame.BukkitGame;
import net.theuniverscraft.BukkitGame.Config.BukkitGameConfig;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class AreaBreakedTimer {
	private Integer timerId;
	private BukkitGame plugin;
	private HashMap<String, Location> lastLoc = new HashMap<String, Location>();
	
	public AreaBreakedTimer(BukkitGame instance) {
		plugin = instance;
	}
	
	public void start() {
		timerId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {
				for(Player player : Bukkit.getOnlinePlayers()) {
					
					Location loc = player.getLocation();
					Location spawn = loc.getWorld().getSpawnLocation();
					
					boolean depx = loc.getX()-spawn.getX() > BukkitGameConfig.getInstance().AREA_LIMIT ||
									loc.getX()-spawn.getX() < -BukkitGameConfig.getInstance().AREA_LIMIT;
					
					boolean depz = loc.getZ()-spawn.getZ() > BukkitGameConfig.getInstance().AREA_LIMIT ||
							loc.getZ()-spawn.getZ() < -BukkitGameConfig.getInstance().AREA_LIMIT;
					
					if(depx || depz) {
						if(!lastLoc.containsKey(player.getName())) lastLoc.put(player.getName(), spawn);
						player.teleport(lastLoc.get(player.getName()));
								player.sendMessage("§4Vous ne pouvez pas dépasser cette limite !");

						

						player.playSound(player.getLocation(), Sound.FIZZ, 1L, 1L);
					}
					else {
						if(lastLoc.containsKey(player.getName())) lastLoc.remove(player.getName());
						lastLoc.put(player.getName(), loc);
					}
				}
			}
		}, 0, 15); // tout les 0.5 secondes
	}
	
	public void cancel() {
		Bukkit.getScheduler().cancelTask(timerId);
	}
}
