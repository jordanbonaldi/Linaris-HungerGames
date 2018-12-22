package net.theuniverscraft.BukkitGame.Listeners;

import java.util.HashMap;

import net.theuniverscraft.BukkitGame.BukkitGame;
import net.theuniverscraft.BukkitGame.Game;
import net.theuniverscraft.BukkitGame.GameStatut;
import net.theuniverscraft.BukkitGame.Config.BukkitGameConfig;
import net.theuniverscraft.BukkitGame.Config.ZonesSave;
import net.theuniverscraft.BukkitGame.Exceptions.NoZoneAvailableException;
import net.theuniverscraft.BukkitGame.Exceptions.UnknownZoneException;
import net.theuniverscraft.BukkitGame.Exceptions.ZoneHasentLocationException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.skelerex.LinarisKits.api.LinarisKitsAPI;

public class BeforeGameBeginListener implements Listener {
	private HashMap<String, Location> lastLoc = new HashMap<String, Location>();
	
	public BeforeGameBeginListener() {
		ZonesSave.getInstance().setAllPlayerZone();
	}
	
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event) {
		try {
			ZonesSave.getInstance().setPlayerZone(event.getPlayer().getName());
		} catch (NoZoneAvailableException e) {
			if(!event.getPlayer().hasPermission("game.staff")){
				event.disallow(PlayerLoginEvent.Result.KICK_OTHER, e.getMessage());
	        	}else{
	        		event.getPlayer().setFlying(true);
	        		event.getPlayer().setFlySpeed(2);
					PotionEffect inv = PotionEffectType.INVISIBILITY.createEffect(10000000, 1000000000);
			        event.getPlayer().addPotionEffect(inv, true);
	        	}
		}
	}
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		
		Player player = event.getPlayer();
		
		PlayerInventory inv = player.getInventory();
		
		if(Game.getInstance().getStatut() == GameStatut.BEFORE_GAME){
			
			ItemStack is_kit = new ItemStack(Material.NETHER_STAR);
			
			ItemMeta is_kit_meta = is_kit.getItemMeta();
			is_kit_meta.setDisplayName(LinarisKitsAPI.ITEM_CHOOSE_KIT);
			is_kit.setItemMeta(is_kit_meta);
			
			inv.setItem(4, is_kit);
			
			player.updateInventory();
			
		}
		
		
		try {
			event.getPlayer().teleport(ZonesSave.getInstance().getZone(event.getPlayer().getName()).getLocation());
		}
		catch (UnknownZoneException e) { e.printStackTrace(); }
		catch (ZoneHasentLocationException e) { e.printStackTrace(); }
        for(Player p : Bukkit.getOnlinePlayers()){
        		if(player.getName().equals("Neferett")){
    	    		p.sendMessage("§f§o[§c§oFondateur§f§o] §b§o" + event.getPlayer().getName() + "§7§o a rejoint le jeu !");
        		}else if(player.hasPermission("game.megavip")) {
    	    		p.sendMessage("§f§o[§a§oMegaVip§f§o] §a§o" + event.getPlayer().getName() + "§7§o a rejoint le jeu !");
        		}else if(player.hasPermission("game.vip")){
    	    		p.sendMessage("§f§o[§e§oVip§f§o] §e§o" + event.getPlayer().getName() + "§7§o a rejoint le jeu !");
        		}else if(player.hasPermission("game.modo")){
    	    		p.sendMessage("§f§o[§6§oModo§f§o] §6§o" + event.getPlayer().getName() + "§7§o a rejoint le jeu !");
        		}else if(player.hasPermission("game.admin")){
    	    		p.sendMessage("§f§o[§c§oAdmin§f§o] §c§o" + event.getPlayer().getName() + "§7§o a rejoint le jeu !");
        		}else if(player.hasPermission("game.vipelite")) { 
    	    		p.sendMessage("§f§o[§b§oVIPElite§f§o] §b§o" + event.getPlayer().getName() + "§7§o a rejoint le jeu !");
        		}else if(player.hasPermission("game.yt")) { 
    	    		p.sendMessage("§f§o[§c§oYouTuber§f§o] §b§o" + event.getPlayer().getName() + "§7§o a rejoint le jeu !");
        		}else{	
    	    		p.sendMessage("§6§o" + event.getPlayer().getName() + "§7§o a rejoint le jeu !");
        		}
        }
        event.setJoinMessage(null);
		if(Bukkit.getOnlinePlayers().length >= BukkitGameConfig.getInstance().ZONE_NUMBER) {
			Game.getInstance().shortBeforeTimer();
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		ZonesSave.getInstance().removePlayerZone(event.getPlayer().getName());
        for(Player p : Bukkit.getOnlinePlayers()){
	    		if(player.getName().equals("Neferett")){
		    		p.sendMessage("§f§o[§c§oFondateur§f§o] §b§o" + event.getPlayer().getName() + "§7§o a quitté le jeu !");
	    		}else if(player.hasPermission("game.megavip")) {
		    		p.sendMessage("§f§o[§a§oMegaVip§f§o] §a§o" + event.getPlayer().getName() + "§7§o a quitté le jeu !");
	    		}else if(player.hasPermission("game.vip")){
		    		p.sendMessage("§f§o[§e§oVip§f§o] §e§o" + event.getPlayer().getName() + "§7§o a quitté le jeu !");
	    		}else if(player.hasPermission("game.modo")){
		    		p.sendMessage("§f§o[§6§oModo§f§o] §6§o" + event.getPlayer().getName() + "§7§o a quitté le jeu !");
	    		}else if(player.hasPermission("game.admin")){
		    		p.sendMessage("§f§o[§c§oAdmin§f§o] §c§o" + event.getPlayer().getName() + "§7§o a quitté le jeu !");
	    		}else if(player.hasPermission("game.vipelite")) { 
		    		p.sendMessage("§f§o[§b§oVIPElite§f§o] §b§o" + event.getPlayer().getName() + "§7§o a quitté le jeu !");
	    		}else if(player.hasPermission("game.yt")) { 
    	    		p.sendMessage("§f§o[§c§oYouTuber§f§o] §b§o" + event.getPlayer().getName() + "§7§o a quitté le jeu !");
        		}else{	
		    		p.sendMessage("§6" + event.getPlayer().getName() + "§7§o a a quitté le jeu !");
	    		}

        }
        event.setQuitMessage(null);
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		try {
			Player player = event.getPlayer();
			if(!lastLoc.containsKey(player.getName())) {
				lastLoc.put(player.getName(), player.getLocation());
				return;
			}
			Location now = player.getLocation();
			Location last = lastLoc.get(player.getName());
			if(now.getX() != last.getX() || now.getY() != last.getY() || now.getZ() != last.getZ()) {
				Location zoneLoc = ZonesSave.getInstance().getZone(event.getPlayer().getName()).getLocation();
				player.teleport(zoneLoc);
				lastLoc.remove(player.getName());
				lastLoc.put(player.getName(), zoneLoc);
			}
		}
		catch (ZoneHasentLocationException e) {	e.printStackTrace(); }
		catch (UnknownZoneException e) { e.printStackTrace(); }
		
	}
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		event.setCancelled(true);
	}
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		event.setCancelled(true);
	}
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		event.setCancelled(true);
	}
	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		event.setCancelled(true);
	}
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		event.setCancelled(true);
	}
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event) {
		event.setCancelled(true);
	}
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		event.setCancelled(true);
	}
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		try {
			Location zoneLoc = ZonesSave.getInstance().getZone(event.getPlayer().getName()).getLocation();
			event.setRespawnLocation(zoneLoc);
		}
		catch (ZoneHasentLocationException e) {	e.printStackTrace(); }
		catch (UnknownZoneException e) { e.printStackTrace(); }
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) { // CHOIX DE LA TEAM
		Player player = event.getPlayer();
		ItemStack inHand = player.getItemInHand();
		if(inHand == null) return;		
		
		if(Game.getInstance().getStatut() == GameStatut.BEFORE_GAME) {
			if(inHand.getType() == Material.NETHER_STAR) { // Choix du kit
				player.openInventory(BukkitGame.getGameAPI().getKitInventory(player));
			}
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void InventoryClick(InventoryClickEvent event){
		if(Game.getInstance().getStatut() == GameStatut.BEFORE_GAME){
	      BukkitGame.getGameAPI().onInventoryClick(event);
		}
	}
}
