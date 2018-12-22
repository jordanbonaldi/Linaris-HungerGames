package net.theuniverscraft.BukkitGame.Exceptions;

public class ZoneHasentLocationException extends Exception {
	private static final long serialVersionUID = 1L;
	private String message;
	
	public ZoneHasentLocationException(String msg) {
		message = msg;
	}
	
	public String getMessage() {
		return message;
	}
}
