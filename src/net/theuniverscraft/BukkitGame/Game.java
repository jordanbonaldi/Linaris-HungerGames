package net.theuniverscraft.BukkitGame;

import net.theuniverscraft.BukkitGame.Background.BGPlayers;
import net.theuniverscraft.BukkitGame.Config.WorldManager;
import net.theuniverscraft.BukkitGame.Config.ZoneGlassSave;
import net.theuniverscraft.BukkitGame.Config.ZonesSave;
import net.theuniverscraft.BukkitGame.Exceptions.UnknownZoneException;
import net.theuniverscraft.BukkitGame.Exceptions.ZoneHasentLocationException;
import net.theuniverscraft.BukkitGame.Listeners.BeforeGameBeginListener;
import net.theuniverscraft.BukkitGame.Listeners.GameListener;
import net.theuniverscraft.BukkitGame.Timers.AreaBreakedTimer;
import net.theuniverscraft.BukkitGame.Timers.BeforeGameBeginTimer;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Game {
	private GameStatut gameStat;
	private BukkitGame plugin;
	
	private BeforeGameBeginTimer beforeGameTimer;
	
	private static Game instance;
	public static Game getInstance() {
		if(instance == null) instance = new Game(BukkitGame.getInstance());
		return instance;
	}
	
	private Game(BukkitGame thePlugin) {
		plugin = thePlugin;
		gameStat = GameStatut.INIT_GAME;
	}
	
	public void setGameStatut(GameStatut newStatut) {
		if(gameStat == newStatut) return;
		
		gameStat = newStatut;
		if(gameStat == GameStatut.BEFORE_GAME) {
			plugin.unRegisterListeners();
			beforeGameTimer = new BeforeGameBeginTimer(plugin);
			
			Bukkit.getPluginManager().registerEvents(new BeforeGameBeginListener(), plugin);
			beforeGameTimer.start();
		}
		else if(gameStat == GameStatut.GAME) {
			BGPlayers.initPlayers();
			plugin.unRegisterListeners();
			
			for(Player player : BGPlayers.getPlayers()) {
				player.setHealth(20);
				player.setFoodLevel(20);
				player.setExp(0);
				player.setRemainingAir(20);
				
				player.playSound(player.getLocation(), Sound.GLASS, 1L, 1L);
				player.getInventory().clear();
				
                BukkitGame.getGameAPI().applyKit(player);
				
				try { player.teleport(ZonesSave.getInstance().getZone(player.getName()).getLocation()); }
				catch (ZoneHasentLocationException e) { e.printStackTrace(); }
				catch (UnknownZoneException e) { e.printStackTrace(); }
			}
			
			World w = WorldManager.getInstance().getHungerWorld();
			w.setDifficulty(Difficulty.HARD);
			w.setTime(0);
			
			plugin.setMotd("§cIn game");
			
			ZoneGlassSave.getInstance().breakGlass();
			
			Bukkit.getPluginManager().registerEvents(new GameListener(), plugin);
			// Bukkit.getPluginManager().registerEvents(worldListener, plugin);
			(new AreaBreakedTimer(plugin)).start();
			
			for(Player p : Bukkit.getOnlinePlayers()){
					p.sendMessage("§bLa partie commence§f, §6Puisse le sort vous être favorable !");

			}

		}
		else if(gameStat == GameStatut.END_GAME) {
		}
	}
	public GameStatut getStatut() { return gameStat; }
	
	public void startBeforeTimer() {
		beforeGameTimer.start();
	}
	public void shortBeforeTimer() {
		beforeGameTimer.setShortTime();
	}
}
