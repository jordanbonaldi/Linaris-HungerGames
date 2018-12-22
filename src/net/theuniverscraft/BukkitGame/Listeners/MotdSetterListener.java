package net.theuniverscraft.BukkitGame.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.server.ServerListPingEvent;

public class MotdSetterListener implements Listener {
	private String m_motd;
	
	public MotdSetterListener(String motd) {
		m_motd = motd;
	}
	
	public void setMotd(String motd) { m_motd = motd; }
	
	@EventHandler
	public void onServerListPing(ServerListPingEvent event) {
		event.setMotd(m_motd);
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		if(player.getName().equals("Neferett")){
		event.setFormat(" §f§l[§c§lFondateur§f§l] §b§l" + player.getName()+"§f: §c"+ event.getMessage());
		}else if(player.hasPermission("game.megavip")) {
			event.setFormat(" §f[§aMegaVip§f] §a" + player.getName()+"§f: "+ event.getMessage());
		}else if(player.hasPermission("game.vip")){
			event.setFormat(" §f[§eVip§f] §e" + player.getName()+"§f: "+ event.getMessage());
			
		}else if(player.hasPermission("game.modo")){
			event.setFormat(" §f[§6Modo§f] §6" + player.getName()+"§f: §c"+ event.getMessage());
			
		}else if(player.hasPermission("game.admin")){
			event.setFormat(" §f[§cAdmin§f] §c" + player.getName()+"§f: §c"+ event.getMessage());
		}
			else if(player.hasPermission("game.vipelite")) {
				event.setFormat(" §f[§bVipElite§f] §b" + player.getName()+"§f: " + event.getMessage());
			
		}else{
			event.setFormat(" §7" + player.getName()+"§f: "+ event.getMessage());
			
			
		}
		
}
}
