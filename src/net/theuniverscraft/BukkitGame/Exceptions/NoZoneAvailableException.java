package net.theuniverscraft.BukkitGame.Exceptions;

public class NoZoneAvailableException extends Exception {
	private static final long serialVersionUID = 1L;
	private String message;
	
	public NoZoneAvailableException(String msg) {
		message = msg;
	}
	
	public String getMessage() {
		return message;
	}
}
