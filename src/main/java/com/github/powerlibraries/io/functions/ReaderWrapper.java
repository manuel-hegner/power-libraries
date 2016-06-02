package com.github.powerlibraries.io.functions;

import java.io.IOException;
import java.io.Reader;

/**This class is a simple functional interface used to wrap a Reader with another. Instead of implementing this 
 * interface it is often enough to use a constructor, e.g. BufferedReader::new.
 * 
 * @author Manuel Hegner
 */
@FunctionalInterface
public interface ReaderWrapper {
	
	/**
	 * This method should wrap the given Reader with another Reader.
	 * @param out the Reader given
	 * @return a wrapped Reader
	 * @throws IOException thrown by some OutputStream constructors
	 */
	public Reader wrap(Reader out) throws IOException;
}
