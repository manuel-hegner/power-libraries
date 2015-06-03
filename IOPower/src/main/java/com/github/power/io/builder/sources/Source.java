package com.github.power.io.builder.sources;

import java.io.IOException;
import java.io.InputStream;

/**
 * This class represents a source. A source is a simple wrapper of some kind that is able to open an InputStream. It 
 * may als provide some kind of name that is useful to identify the file content. 
 * @author Manuel Hegner
 */
public interface Source {
	
	/**
	 * This method opens the stream to the source represented by this object. It should be calleable more than once, but
	 * this is not always guaranteed (e.g. InputStreamSource).
	 * @return an InputStream
	 * @throws IOException if opening the InputStream throws an exception
	 */
	public InputStream openStream() throws IOException;

	/**
	 * @return true if this source is able to return a name of any kind
	 */
	public default boolean hasName() {
		return false;
	}

	/**
	 * @return the name of the source if there is any. By default this method throws an {@link UnsupportedOperationException}.
	 */
	public default String getName() {
		throw new UnsupportedOperationException();
	}
}
