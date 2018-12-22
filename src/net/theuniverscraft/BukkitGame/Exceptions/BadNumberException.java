package net.theuniverscraft.BukkitGame.Exceptions;

public class BadNumberException extends Exception {
	private static final long serialVersionUID = 1L;
	private String message;
	
	public BadNumberException(String msg) {
		message = msg;
	}
	
	public String getMessage() {
		return message;
	}
}
