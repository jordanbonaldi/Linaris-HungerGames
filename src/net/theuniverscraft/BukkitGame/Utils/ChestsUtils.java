
package net.theuniverscraft.BukkitGame.Utils;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ChestsUtils {
	

	
	
	public static void fillAllChests(){
		for(Chunk chunk : Bukkit.getWorld("world").getLoadedChunks()){
		for(BlockState entities : chunk.getTileEntities()){
		if(entities instanceof Chest){
		Inventory inv = ((Chest) entities).getInventory();
		fillChests(inv);
		}
		}
		}
		}
	
	public static void fillChests(Inventory inv){
		inv.clear();
		Random itemnum = new Random();
		int items = 3+itemnum.nextInt(1);
		for(int i = 1; i < items+1; i++){
		Random slotnum = new Random();
		Random itemrand = new Random();
		int item = 1+itemrand.nextInt(20);
		int slot = slotnum.nextInt(inv.getSize());
		
		if(item == 1){
		inv.setItem(slot, new ItemStack(Material.APPLE, 2));
		}else if(item == 2){
			inv.setItem(slot, new ItemStack(Material.COOKED_BEEF, 2));
		}else if(item == 3){
			inv.setItem(slot, new ItemStack(Material.LEATHER_HELMET, 1));
		}else if(item == 4){
			inv.setItem(slot, new ItemStack(Material.LEATHER_CHESTPLATE, 1));
		}else if(item == 5){
			inv.setItem(slot, new ItemStack(Material.LEATHER_LEGGINGS, 1));
		}else if(item == 6){
			inv.setItem(slot, new ItemStack(Material.LEATHER_BOOTS, 1));
		}else if(item == 7){
			inv.setItem(slot, new ItemStack(Material.STONE_AXE, 1));
		}else if(item == 8){
			inv.setItem(slot, new ItemStack(Material.STONE_SWORD, 1));
		}else if(item == 9){
			inv.setItem(slot, new ItemStack(Material.IRON_SWORD, 1));
		}else if(item == 10){
			inv.setItem(slot, new ItemStack(Material.GOLD_SWORD, 1));
		}else if(item ==11){
			inv.setItem(slot, new ItemStack(Material.IRON_CHESTPLATE, 1));
		}else if(item == 12){
			inv.setItem(slot, new ItemStack(Material.IRON_LEGGINGS, 1));
		}else if(item == 13){
			inv.setItem(slot, new ItemStack(Material.STICK, 1));
		}else if(item == 14){
			inv.setItem(slot, new ItemStack(Material.STONE_PICKAXE, 1));
		}else if(item == 15){
			inv.setItem(slot, new ItemStack(Material.IRON_PICKAXE, 1));
		}else if(item == 16){
			inv.setItem(slot, new ItemStack(Material.GOLD_AXE, 1));
		}else if(item == 17){
			inv.setItem(slot, new ItemStack(Material.BREAD, 1));
		}else if(item == 18){
			inv.setItem(slot, new ItemStack(Material.GOLD_BOOTS, 1));
		}else if(item == 19){
			inv.setItem(slot, new ItemStack(Material.BOW, 1));
		}else if(item == 20){
			inv.setItem(slot, new ItemStack(Material.ARROW, 3));
		}else if(item == 21){
			inv.setItem(slot, new ItemStack(Material.COAL, 1));
		}
		}
		}
}