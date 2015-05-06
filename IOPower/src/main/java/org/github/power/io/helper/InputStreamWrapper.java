package org.github.power.io.helper;

import java.io.IOException;
import java.io.InputStream;

/**This class is a simple functional interface used to wrap an InputStream with another. Instead of implementing this 
 * interface it is often enough to use a constructor, e.g. BufferedInputStream::new.
 * 
 * @author Manuel Hegner
 */
@FunctionalInterface
public interface InputStreamWrapper {
	/**
	 * This method should wrap the given InputStream in with another InputStream (e.g. decompression) and it.
	 * @param in the stream given
	 * @return a wrapped InputStream
	 * @throws IOException thrown by some InputStream constructos
	 */
	public InputStream wrap(InputStream in) throws IOException;
}
