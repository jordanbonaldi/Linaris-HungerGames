package net.theuniverscraft.BukkitGame.Timers;

import net.theuniverscraft.BukkitGame.BukkitGame;
import net.theuniverscraft.BukkitGame.Game;
import net.theuniverscraft.BukkitGame.GameStatut;
import net.theuniverscraft.BukkitGame.Background.BGPlayers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class GameEndTimer {
	private Integer timerId;
	private Long m_timer; // en seconde
	
	public GameEndTimer() {
		m_timer = 600L;
	}
	
	public void start() {
		timerId = Bukkit.getScheduler().scheduleSyncRepeatingTask(BukkitGame.getInstance(), new Runnable() {
			@Override
			public void run() {
				if(m_timer <= 0) { // Fini
					for(Player p : Bukkit.getOnlinePlayers()) {
							p.kickPlayer("§c Le temps imparti est écoulé !");
					}
					BGPlayers.clear();
					Game.getInstance().setGameStatut(GameStatut.END_GAME);
				}
				else if((m_timer % 60D) == 0) { // Minutes plaines
					Long x = m_timer / 60L;
					
					for(Player p : Bukkit.getOnlinePlayers()) {
							String unite = x <= 1 ? "minute" : "minutes";
							p.sendMessage(("§6La partie se termine dans §b<x> <unite> §6!").replaceAll("<x>", x.toString())
							.replaceAll("<unite>", unite));

					}
				}
				else if((m_timer % 60D) == 30) { // Minutes et demi
					Long m = (m_timer - 30) / 60L;
					
					Long s = m_timer - m*60;
					
					if(m == 0) {
						for(Player p : Bukkit.getOnlinePlayers()) {
								String unite_s = s <= 1 ? "seconde" : "secondes";
								p.sendMessage(("§6La partie se termine dans §b<x> <unite> §6!").replaceAll("<x>", s.toString())
								.replaceAll("<unite>", unite_s));


						}
					}
				}
				else if(m_timer <= 15 && m_timer > 0) { // Moins de 15 secondes
					Long x = m_timer;
					for(Player p : Bukkit.getOnlinePlayers()) {
							String unite = x == 1 ? "seconde" : "secondes";
							p.sendMessage(("§6La partie se termine dans §b<x> <unite> §6!").replaceAll("<x>", x.toString())
							.replaceAll("<unite>", unite));
							

					}
				}
				
				m_timer--;
			}
		}, 20, 20); // toutes les secondes
	}
	
	public void cancel() {
		Bukkit.getScheduler().cancelTask(timerId);
	}
}
