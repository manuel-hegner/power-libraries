package com.github.powerlibraries.io;

import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

public final class IOConfig {
	/**
	 * There is no reason to create an instance.
	 */
	private IOConfig() {}
	
	
	private static Charset DEFAULT_CHARSET=Charset.defaultCharset();
	
	/**
	 * This method returns the default charset that is used by all In and OutBuilders.
	 * By default this method returns the platform default charset returned by
	 * {@link Charset#defaultCharset()}.
	 * @return the default charset that is used by IOPower classes.
	 */
	public static Charset getDefaultCharset() {
		return DEFAULT_CHARSET;
	}
	
	/**
	 * This method sets the default {@link Charset} that is used by all In and OutBuilders.
	 * By default this is the platform default charset returned by
	 * {@link Charset#defaultCharset()}.
	 * @param the {@link Charset} that should be used by default
	 */
	public static void setDefaultCharset(Charset charset) {
		if(charset==null)
			throw new NullPointerException("charset can not be null");
		DEFAULT_CHARSET=charset;
	}
	
	/**
	 * This method sets the default {@link Charset} that is used by all In and OutBuilders
	 * by calling {@link Charset#forName(String)} on the given name.
	 * By default this is the platform default charset returned by
	 * {@link Charset#defaultCharset()}.
	 * @param the name of a {@link Charset} that should be used by default
	 * @throws If no support for the named charset is available in this instance of the 
	 * Java virtual machine
	 */
	public static void setDefaultCharset(String charsetName) throws UnsupportedCharsetException{
		DEFAULT_CHARSET=Charset.forName(charsetName);
	}
}
