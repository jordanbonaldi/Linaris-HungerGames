package net.theuniverscraft.BukkitGame.Timers;

import net.theuniverscraft.BukkitGame.BukkitGame;
import net.theuniverscraft.BukkitGame.Game;
import net.theuniverscraft.BukkitGame.GameStatut;
import net.theuniverscraft.BukkitGame.Config.BukkitGameConfig;
import net.theuniverscraft.BukkitGame.Config.WorldManager;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class BeforeGameBeginTimer implements Listener{
	private Integer timerId;
	private Integer timerIdpvp;
	private Integer timeToStart;
	private Integer timeToStartpvp;// en seconde
	private Integer timeToReloadTime; // en seconde
	private BukkitGame plugin;
	
	public BeforeGameBeginTimer(BukkitGame instance) {
		plugin = instance;
		timeToReloadTime = 5;
	}
	
	public void start() {
		timeToStart = BukkitGameConfig.getInstance().TIME_BEFORE_GAME_BEGIN*60+1;
		timerId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {
				timeToReloadTime--;
				if(timeToReloadTime <= 0) {
					timeToReloadTime = 5;
					World w = WorldManager.getInstance().getHungerWorld();
					w.setTime(0);
				}
				
				timeToStart--;
				if(timeToStart <= 0) {
					if(Bukkit.getOnlinePlayers().length < BukkitGameConfig.getInstance().MIN_PEOPLE_FOR_BEGIN) {
						timeToStart = BukkitGameConfig.getInstance().TIME_BEFORE_GAME_BEGIN*60+1;
						for(Player p : Bukkit.getOnlinePlayers()){
								p.sendMessage("§4Pas assez de joueurs connectés !");

						}
					}
					else {
						Game.getInstance().setGameStatut(GameStatut.GAME);
						cancel();
					}
				}
				else if((timeToStart % 60D) == 0) {

					for(Player p : Bukkit.getOnlinePlayers()){
							Integer minutes = timeToStart / 60;
							String unite = "minutes";
							if(minutes <= 1) unite = "minute";
							p.sendMessage(("§bLe jeu commence dans §6<x> <unite> §b!")
									.replaceAll("<x>", minutes.toString()).replaceAll("<unite>", unite));

					}
					Integer minutes = timeToStart / 60;
					plugin.setMotd(("§a<m>m")
							.replaceAll("<m>", minutes.toString()));
				}
				else if((timeToStart % 30D) == 0) {
					Integer minutes = timeToStart / 60;
					Integer secondes = timeToStart - minutes*60;
					for(Player p : Bukkit.getOnlinePlayers()){
							String unite = "minutes";
							if(minutes <= 1) unite = "minute";
							p.sendMessage(("§bLe jeu commence dans §6<m> <unite_m> §bet §6<s> <unite_s> §b!")
									.replaceAll("<m>", minutes.toString()).replaceAll("<unite_m>", unite)
									.replaceAll("<s>", secondes.toString()).replaceAll("<unite_s>", "secondes"));
		
					}
				
					
					plugin.setMotd(("§a<m>m<s>s")
							.replaceAll("<m>", minutes.toString()).replaceAll("<s>", secondes.toString()));
				}
				else if(timeToStart <= 15) {

					for(Player p : Bukkit.getOnlinePlayers()){
							String unite = "secondes";
							if(timeToStart <= 1) unite = "seconde";
							p.sendMessage(("§bLe jeu commence dans §6<x> <unite> §b!")
									.replaceAll("<x>", timeToStart.toString()).replaceAll("<unite>", unite));

					}

				}
			}
		}, 20, 20); // toutes les secondes
	}
	
	public void setShortTime() {
		if(timeToStart > 16) timeToStart = 16;
	}
	
	public void cancel() {
		Bukkit.getScheduler().cancelTask(timerId);
	}
	

	
}
