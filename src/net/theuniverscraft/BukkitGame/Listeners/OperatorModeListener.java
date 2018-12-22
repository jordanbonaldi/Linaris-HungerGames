package net.theuniverscraft.BukkitGame.Listeners;

import net.theuniverscraft.BukkitGame.Config.WorldManager;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class OperatorModeListener implements Listener {
	public OperatorModeListener() {
	}
	
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event) {
		if(!event.getPlayer().isOp())
			event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "");
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Location spawn = WorldManager.getInstance().getHungerWorld().getSpawnLocation();
		while(WorldManager.getInstance().getHungerWorld().getBlockAt(spawn).getType() != Material.AIR) spawn.add(0, 1, 0);
		event.getPlayer().teleport(spawn);
	}
}
