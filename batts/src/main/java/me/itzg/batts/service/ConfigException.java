package me.itzg.batts.service;

public class ConfigException extends RuntimeException {
	private static final long serialVersionUID = -2708220594684404401L;

	public ConfigException() {
		super();
	}

	public ConfigException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConfigException(String message) {
		super(message);
	}

	public ConfigException(Throwable cause) {
		super(cause);
	}

}
