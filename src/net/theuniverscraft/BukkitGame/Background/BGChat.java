package net.theuniverscraft.BukkitGame.Background;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

public class BGChat {
	private static ConsoleCommandSender console = null;
	
	public static void sendConsoleMessage(String msg) {
		if(console == null) console = Bukkit.getConsoleSender();
		console.sendMessage(msg);
	}
	
	public static void sendAll(String msg) {
		Bukkit.broadcastMessage(msg);
	}
}
