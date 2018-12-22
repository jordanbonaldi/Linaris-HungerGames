package net.theuniverscraft.BukkitGame.Utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class LocationZone {
	private World world;
	private Location m_pos1;
	private Location m_pos2;
	
	public LocationZone(Location pos1, Location pos2) {
		world = pos1.getWorld();
		m_pos1 = new Location(world, pos1.getX() <= pos2.getX() ? pos1.getX() : pos2.getX(),
				pos1.getY() <= pos2.getY() ? pos1.getY() : pos2.getY(),
				pos1.getZ() <= pos2.getZ() ? pos1.getZ() : pos2.getZ());
		
		m_pos2 = new Location(world, pos1.getX() <= pos2.getX() ? pos2.getX() : pos1.getX(),
				pos1.getY() <= pos2.getY() ? pos2.getY() : pos1.getY(),
				pos1.getZ() <= pos2.getZ() ? pos2.getZ() : pos1.getZ());
	}
	
	public void breakInto(Material m) {
		int lenthX = m_pos2.getBlockX()-m_pos1.getBlockX()+1;
		int lenthY = m_pos2.getBlockY()-m_pos1.getBlockY()+1;
		int lenthZ = m_pos2.getBlockZ()-m_pos1.getBlockZ()+1;
		
		for(int x = 0; x < lenthX; x++) {
			for(int y = 0; y < lenthY; y++) {
				for(int z = 0; z < lenthZ; z++) {
					Block block = world.getBlockAt(m_pos1.clone().add(x,y,z));
					if(block.getType() == m) {
						block.setType(Material.AIR);
						// Game.getInstance().worldListener.addBlock(m, block);
					}
				}
			}
		}
	}
}
