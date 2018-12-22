package net.theuniverscraft.BukkitGame.Exceptions;

public class UnknownZoneException extends Exception {
	private static final long serialVersionUID = 1L;
	private String message;
	
	public UnknownZoneException(String msg) {
		message = msg;
	}
	
	public String getMessage() {
		return message;
	}
}
