package com.github.powerlibraries.io.builder;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import com.github.powerlibraries.io.IOConfig;

@SuppressWarnings("unchecked")
public abstract class CharsetHolder <SELF extends CharsetHolder<SELF>> {
	private Charset charset=null;
	
	/**
	 * This method returns the the {@link Charset} that is used to create writers.
	 * It uses {@link IOConfig#getDefaultCharset} by default.
	 * @return the charset to be used
	 */
	public Charset getCharset() {
		return (charset==null)?IOConfig.getDefaultCharset():charset;
	}
	
	/**
	 * This method sets the charset that is used by all methods that end up writing Strings.
	 * @param charset the charset that should be used
	 * @return this builder
	 */
	public SELF withCharset(Charset charset) {
		if(charset==null)
			throw new NullPointerException("charset can not be null");
		this.setCharset(charset);
		return (SELF) this;
	}
	
	/**
	 * This method sets the charset that is used by all methods that end up writing Strings to UTF-8.
	 * @return this builder
	 */
	public SELF withUTF8() {
		this.setCharset(StandardCharsets.UTF_8);
		return (SELF) this;
	}
	
	/**
	 * This method sets the charset that is used by all methods that end up writing Strings to UTF-16.
	 * @return this builder
	 */
	public SELF withUTF16() {
		this.setCharset(StandardCharsets.UTF_16);
		return (SELF) this;
	}
	
	/**
	 * This method sets the charset that is used by all methods that end up writing Strings to UTF-16 big endian.
	 * @return this builder
	 */
	public SELF withUTF16BE() {
		this.setCharset(StandardCharsets.UTF_16BE);
		return (SELF) this;
	}
	
	/**
	 * This method sets the charset that is used by all methods that end up writing Strings to UTF-16 little endian.
	 * @return this builder
	 */
	public SELF withUTF16LE() {
		this.setCharset(StandardCharsets.UTF_16LE);
		return (SELF) this;
	}
	
	/**
	 * This method sets the charset that is used by all methods that end up writing Strings to ASCII.
	 * @return this builder
	 */
	public SELF withASCII() {
		this.setCharset(StandardCharsets.US_ASCII);
		return (SELF) this;
	}
	
	/**
	 * This method sets the charset that is used by all methods that end up writing Strings to ISO 8859 1.
	 * @return this builder
	 */
	public SELF withISO88591() {
		this.setCharset(StandardCharsets.ISO_8859_1);
		return (SELF) this;
	}
	
	/**
	 * This method sets the charset that is used by all methods that end up writing Strings.
	 * The given charsetName is resolved with {@link Charset#forName(String)}.
	 * @param charsetName the name of the charset that should be used
	 * @throws UnsupportedEncodingException If no support for the named charset is available 
	 * in this instance of the Java virtual machine
	 * @return this builder
	 */
	public SELF withCharset(String charsetName) throws UnsupportedEncodingException {
		this.setCharset(Charset.forName(charsetName));
		return (SELF) this;
	}

	protected void setCharset(Charset charset) {
		this.charset = charset;
	}
}
