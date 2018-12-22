package net.theuniverscraft.BukkitGame.Listeners;

import java.util.HashMap;
import java.util.Iterator;

import net.theuniverscraft.BukkitGame.BukkitGame;
import net.theuniverscraft.BukkitGame.Game;
import net.theuniverscraft.BukkitGame.GameStatut;
import net.theuniverscraft.BukkitGame.Background.BGPlayers;
import net.theuniverscraft.BukkitGame.Config.BukkitGameConfig;
import net.theuniverscraft.BukkitGame.Timers.GameEndTimer;
import net.theuniverscraft.BukkitGame.Utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class GameListener implements Listener {
	private Integer tickToReinit = 0;
	private Boolean haveBossole = false;
	private Boolean endTimer = false;
	
	public GameListener() {}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(BGPlayers.getPlayers().size() <= 5 && BGPlayers.getPlayers().size() >= 2) {
			Player player = event.getPlayer();
			if(player.getItemInHand() == null) return;
			
			if(player.getItemInHand().getType() == Material.COMPASS) {
						player.sendMessage(("§aLa boussole pointe vers §e<player> §a!").replaceAll("<player>", getClosest(player).getName()));

				

			}
		
		}
	}
	
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if(BGPlayers.getPlayers().size() <= 5 && BGPlayers.getPlayers().size() >= 2) {
			tickToReinit++;
			if(tickToReinit < 5) return;
			tickToReinit = 0;
			
			for(Player p : BGPlayers.getPlayers()) {
				p.setCompassTarget(getClosest(p).getLocation());
			}
		}
	}
	
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event) {
        for(Player p : Bukkit.getOnlinePlayers()){
        		p.sendMessage("§cLa partie est en cours !");

        }
		event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "");
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Location loc = event.getPlayer().getLocation();
		Location spawn = loc.getWorld().getSpawnLocation();
		
		boolean depx = loc.getX()-spawn.getX() > BukkitGameConfig.getInstance().AREA_LIMIT ||
						loc.getX()-spawn.getX() < -BukkitGameConfig.getInstance().AREA_LIMIT;
		
		boolean depz = loc.getZ()-spawn.getZ() > BukkitGameConfig.getInstance().AREA_LIMIT ||
				loc.getZ()-spawn.getZ() < -BukkitGameConfig.getInstance().AREA_LIMIT;
		
		if(depx || depz)
			event.setCancelled(true);
		
		
	}
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Location loc = event.getPlayer().getLocation();
		Location spawn = loc.getWorld().getSpawnLocation();
		
		boolean depx = loc.getX()-spawn.getX() > BukkitGameConfig.getInstance().AREA_LIMIT ||
						loc.getX()-spawn.getX() < -BukkitGameConfig.getInstance().AREA_LIMIT;
		
		boolean depz = loc.getZ()-spawn.getZ() > BukkitGameConfig.getInstance().AREA_LIMIT ||
				loc.getZ()-spawn.getZ() < -BukkitGameConfig.getInstance().AREA_LIMIT;
		
		if(depx || depz)
			event.setCancelled(true);
		
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player dead = event.getEntity();
		dead.getWorld().strikeLightning(dead.getLocation().add(0, 10, 0));
		
		if(dead.getKiller() != null) {
			BukkitGame.getGameAPI().kill(dead.getKiller());
		}
		
	    final Player player = event.getEntity();
	            for(Player p : Bukkit.getOnlinePlayers()){
	            		p.sendMessage(" §e" + player.getName() + (Object)ChatColor.YELLOW + " " + (player.getKiller() == null ? "§7a succombé." : new StringBuilder("§7a été tué par §e").append((Object)player.getKiller().getName()).toString()));

	            }
	    event.setDeathMessage(null);
        	    dead.sendMessage("§cTu es mort, tu as donc perdu !");

        
	    Utils.tpToLobby(dead);
	
	
	
	
	
	}
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		BGPlayers.deletePlayer(event.getPlayer());
		Integer playerNb = BGPlayers.getPlayers().size();
        for(Player p : Bukkit.getOnlinePlayers()){
        		p.sendMessage(("§7Il reste §e<nb> joueurs §7en jeu !").replaceAll("<nb>", playerNb.toString()));

        }
		event.setQuitMessage(null);
		if(playerNb >= 1) playerQuitOrKick(event.getPlayer());
		else {
			Bukkit.getScheduler().scheduleSyncDelayedTask(BukkitGame.getInstance(), new Runnable() {
				@Override
				public void run() {
					Game.getInstance().setGameStatut(GameStatut.END_GAME);
				}
			}, 40L);
		}
	}
	
	@EventHandler
	public void onPlayerKick(final PlayerKickEvent event) {

		BGPlayers.deletePlayer(event.getPlayer());
		Integer playerNb = BGPlayers.getPlayers().size();
        for(Player p : Bukkit.getOnlinePlayers()){
        		p.sendMessage(("§7Il reste §e<nb> joueurs §7en jeu !").replaceAll("<nb>", playerNb.toString()));

        }
		event.setLeaveMessage(null);
		if(playerNb >= 1) playerQuitOrKick(event.getPlayer());
		else {
			Bukkit.getScheduler().scheduleSyncDelayedTask(BukkitGame.getInstance(), new Runnable() {
				@Override
				public void run() {
					Game.getInstance().setGameStatut(GameStatut.END_GAME);
				}
			}, 40L);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void playerQuitOrKick(Player p) {
		Integer playerNb = BGPlayers.getPlayers().size();
		if(playerNb <= 5 && !haveBossole) {
			for(Player player : BGPlayers.getPlayers()) {
				player.getInventory().addItem(new ItemStack(Material.COMPASS, 1));
			}
			haveBossole = true;
	        for(Player p1 : Bukkit.getOnlinePlayers()){
	        		p1.sendMessage("§bUtilise ta boussole pour localier l'ennemi le plus proche !");

	        }
		}
		
		if(playerNb >= 4 && !endTimer) {
			(new GameEndTimer()).start();
			endTimer = true;
		}
		if(playerNb == 1){
			win();
		}
		
			
		
	}
	
	public void win(){
		
				
		
		if(BGPlayers.getPlayers().size() == 1){
			
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(BukkitGame.getInstance(), new Runnable() {
				@Override
				public void run() {
					for(Player p : Bukkit.getOnlinePlayers()){
                        		p.sendMessage(ChatColor.GOLD + "Victoire du joueur §a" + p.getName() + " " + ChatColor.YELLOW + ChatColor.MAGIC + "|" + ChatColor.AQUA + ChatColor.MAGIC + "|" + ChatColor.GREEN + ChatColor.MAGIC + "|" + ChatColor.RED + ChatColor.MAGIC + "|" + ChatColor.LIGHT_PURPLE + ChatColor.MAGIC + "|" + ChatColor.YELLOW + ChatColor.MAGIC + "|" + ChatColor.AQUA + ChatColor.MAGIC + "|" + ChatColor.GREEN + ChatColor.MAGIC + "|" + ChatColor.RED + ChatColor.MAGIC + "|" + ChatColor.LIGHT_PURPLE + ChatColor.MAGIC + "|" + ChatColor.AQUA + ChatColor.BOLD + " Félicitations " + ChatColor.YELLOW + ChatColor.MAGIC + "|" + ChatColor.AQUA + ChatColor.MAGIC + "|" + ChatColor.GREEN + ChatColor.MAGIC + "|" + ChatColor.RED + ChatColor.MAGIC + "|" + ChatColor.LIGHT_PURPLE + ChatColor.MAGIC + "|" + ChatColor.YELLOW + ChatColor.MAGIC + "|" + ChatColor.AQUA + ChatColor.MAGIC + "|" + ChatColor.GREEN + ChatColor.MAGIC + "|" + ChatColor.RED + ChatColor.MAGIC + "|" + ChatColor.LIGHT_PURPLE + ChatColor.MAGIC + "|");

                        
						Utils.tpToLobby(p);
						BukkitGame.getGameAPI().winHG(p);
					}
				}
			}, 50L);
			Bukkit.getScheduler().scheduleSyncDelayedTask(BukkitGame.getInstance(), new Runnable() {
				@Override
				public void run() {
						
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
					
				}
			}, 100L);

		}
		
		
	}
	
	

	
    public Player getClosest(Player who) {
    	HashMap <String, Double> distances = new HashMap<String, Double>();
    	for (Player online : BGPlayers.getPlayers()) {
    		if(!online.getName().equals(who.getName()))
    			distances.put(online.getName(), who.getLocation().distanceSquared(online.getLocation()));
    	}
    	
    	String player = null;
    	Double lastDistance = null;
		Iterator<String> i = distances.keySet().iterator();
		while (i.hasNext()){
			String key = i.next();
			Double distance = distances.get(key);
			
			if(lastDistance == null || lastDistance > distance) {
				lastDistance = distance;
				player = key;
			}
		}
    	return Bukkit.getPlayer(player);
    }
    
    
    
    
    
}
