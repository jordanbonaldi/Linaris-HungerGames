package net.theuniverscraft.BukkitGame.Exceptions;

public class ZoneHasentPlayerException extends Exception {
	private static final long serialVersionUID = 1L;
	private String message;
	
	public ZoneHasentPlayerException(String msg) {
		message = msg;
	}
	
	public String getMessage() {
		return message;
	}
}
