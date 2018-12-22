package net.theuniverscraft.BukkitGame.Background;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BGPlayers {
	private static List<Player> m_players;
	
	public static void initPlayers() {
		m_players = new LinkedList<Player>(Arrays.asList(Bukkit.getOnlinePlayers()));
	}
	public static void deletePlayer(Player player) {
		for(int i = 0; i < m_players.size(); i++) {
			if(m_players.get(i).getName().equals(player.getName())) {
				m_players.remove(i);
				return;
			}
		}
	}
	public static void clear() {
		m_players.clear();
	}
	
	public static List<Player> getPlayers() {
		return m_players;
	}
}
