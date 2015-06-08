package com.github.powerlibraries.io.helper.stringout;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;

/**
 * This class is a simple extension of an {@link ByteArrayOutputStream} that also
 * stores a charset to simply create a string from its byte buffer
 * @author Manuel Hegner
 *
 */
public class StringBuilderOutputStream extends ByteArrayOutputStream {
	
	private Charset charset;

	public StringBuilderOutputStream(Charset charset) {
		this.charset=charset;
	}
	
	/**
	 * This method returns the string build by this chain.
	 * @return the string built by this output chain
	 */
	public String getResult() {
		closeSilently();
		return new String(this.toByteArray(), charset);
	}
	
	/**
	 * This method returns the string build by this chain.
	 * @param charset the charset that is used to build the string
	 * @return the string built by this output chain
	 */
	public String getResult(Charset charset) {
		closeSilently();
		return new String(this.toByteArray(), charset);
	}
	
	/**
	 * Closes this Closable without throwing any exceptions. 
	 */
	private void closeSilently() {
		try {
			close();
		} catch(Exception e) {}
	}

	/**
	 * Sets the charset of this stream that is used by the getResult methods.
	 * @param charset the {@link Charset}
	 */
	public void setCharset(Charset charset) {
		this.charset=charset;
	}
}
