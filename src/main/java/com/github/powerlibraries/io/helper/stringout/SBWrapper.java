package com.github.powerlibraries.io.helper.stringout;

import java.io.Closeable;
import java.nio.charset.Charset;

/**
 * This interface should be used in all classes that extend a normal Writer or InputStream but rely upon
 * a {@link StringBuilderOutputStream} at the end of the chain. This interface implements default delegate methods
 * to all common {@link StringBuilderOutputStream} methods.
 * @author Manuel Hegner
 *
 */
public interface SBWrapper extends Closeable {
	
	/**
	 * This method returns the ByteArrayOutputStream wrapped by this object.
	 * @return the ByteArrayOutputStream
	 */
	public StringBuilderOutputStream getUnderlyingOutput();
	
	/**
	 * This method returns the string build by this chain.
	 * @return the string built by this output chain
	 */
	public default String getResult() {
		return getUnderlyingOutput().getResult();
	}
	
	/**
	 * This method returns the string build by this chain.
	 * @param charset the charset that is used to build the string
	 * @return the string built by this output chain
	 */
	public default String getResult(Charset charset) {
		return getUnderlyingOutput().getResult(charset);
	}
}
