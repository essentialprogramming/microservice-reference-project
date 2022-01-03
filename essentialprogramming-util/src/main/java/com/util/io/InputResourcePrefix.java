package com.util.io;

public enum InputResourcePrefix {

	/** Pseudo URL prefix for loading from the class path: "classpath:" */
	CLASSPATH_URL_PREFIX("classpath:");

	private final String prefix;

	InputResourcePrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getPrefix() {
		return prefix;
	}
}
