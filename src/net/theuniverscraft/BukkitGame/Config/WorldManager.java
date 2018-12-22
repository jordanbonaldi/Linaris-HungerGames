package net.theuniverscraft.BukkitGame.Config;

import java.io.File;

import net.theuniverscraft.BukkitGame.Utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class WorldManager {
	private World baseWorld = null;
	private World hungerWorld = null;
	
	private static WorldManager instance = null;
	public static WorldManager getInstance() {
		if(instance == null) instance = new WorldManager();
		return instance;
	}
	
	private WorldManager() {
		baseWorld = Bukkit.getWorld("world");
		Utils.deleteDirectory(new File(baseWorld.getWorldFolder().getParentFile().getAbsolutePath()+"/"+
				BukkitGameConfig.getInstance().HUNGER_WORLD_NAME));
		
		Utils.emptyDir(new File(baseWorld.getWorldFolder().getAbsolutePath()+"/players"));
		Utils.copyDir(baseWorld.getWorldFolder(), baseWorld.getWorldFolder().getParent()+"/"+BukkitGameConfig.getInstance().HUNGER_WORLD_NAME, false);
		File uidFile = new File(baseWorld.getWorldFolder().getParent()+"/"+BukkitGameConfig.getInstance().HUNGER_WORLD_NAME+"/uid.dat");
		if(uidFile.exists()) uidFile.delete();
		
		hungerWorld = Bukkit.getWorld("world");
	}
	
	
	
	public World getHungerWorld() {
		return hungerWorld;
	}
}
