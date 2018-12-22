package net.theuniverscraft.BukkitGame.Utils;

import java.io.File;
import java.io.FileOutputStream;

import net.theuniverscraft.BukkitGame.BukkitGame;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class Utils {
	public static Location getLocation(ConfigurationSection section) {
		try {			
			Location location = new Location(Bukkit.getWorld(section.getString("world")),
					section.getDouble("x"), section.getDouble("y"), section.getDouble("z"),
					(float) section.getDouble("yaw"), (float) section.getDouble("pitch"));
			return location;
		}
		catch(Exception e) {}
		
		return null;
	}
	
	public static void setLocation(ConfigurationSection section, Location loc) {		
		try {
			section.set("world", loc.getWorld().getName());
			section.set("x", loc.getX());
			section.set("y", loc.getY());
			section.set("z", loc.getZ());
			section.set("yaw", loc.getYaw());
			section.set("pitch", loc.getPitch());
		}
		catch(Exception e) { e.printStackTrace(); }
	}
	
	public static Boolean locBlockEqual(Location loc1, Location loc2) {
		return loc1.getWorld().getName().equalsIgnoreCase(loc2.getWorld().getName()) && loc1.getBlockX() == loc2.getBlockX()
				&& loc1.getBlockY() == loc2.getBlockY() && loc1.getBlockZ() == loc2.getBlockZ();
	}
	
	public static Integer itemCount(Inventory inv) {
		Integer count = 0;
		for(int i = 0; i < inv.getSize(); i++) {
			if(inv.getItem(i) != null) count += inv.getItem(i).getAmount();
		}
		return count;
	}
	
	public static void emptyDir(File dir) {
		if(!dir.exists() || !dir.isDirectory()) return;
		
		File[] files = dir.listFiles();
		for(File file : files) {
			if(file.isDirectory()) {
				Utils.deleteDirectory(file);
			}
			else {
				file.delete();
			}
		}
	}
	
	public static void deleteDirectory(File path) {
		if(path.exists()) { 
			for(File file : path.listFiles()) { 
				if(file.isDirectory()) { 
					deleteDirectory(file); 
				} 
				else { 
					file.delete(); 
				} 
			} 
		}
	}
	
	public static void copyDir(File dir, String path, Boolean copyParent) {
		if(!dir.isDirectory()) return;
		if(copyParent) {
			path = path+"/"+dir.getName();
			File copyDir = new File(path);
			copyDir.mkdirs();
		}
		
		File[] files = dir.listFiles();
		
		for(File file : files) {
			if(file.isDirectory()) {
				Utils.copyDir(file, path, true);
			}
			else {
				Utils.copyFile(file, new File(path+"/"+file.getName()));
			}
		}
	}
	
	public static void copyFile(File source, File dest) {
		java.io.FileInputStream sourceFile = null;
		try{
			// Declaration et ouverture des flux
			sourceFile = new java.io.FileInputStream(source);
			java.io.FileOutputStream destinationFile = null;
	 
			if(!dest.exists()) dest.createNewFile();
			destinationFile = new FileOutputStream(dest);
	 
			// Lecture par segment de 0.5Mo 
			byte buffer[] = new byte[512 * 1024];
			
			int nbLecture;
			while ((nbLecture = sourceFile.read(buffer)) != -1){
				destinationFile.write(buffer, 0, nbLecture);
			}
			destinationFile.close();
			sourceFile.close();
		}
		catch (Exception e) { e.printStackTrace(); }
	}

    public static void tpToLobby(Player player) {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("Connect");
			out.writeUTF("Hub1");
			player.sendPluginMessage(BukkitGame.getInstance(), "BungeeCord", out.toByteArray());
		
	}

}
