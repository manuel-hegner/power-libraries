package com.github.powerlibraries.io.functions;

import java.io.IOException;
import java.io.Writer;

/**This class is a simple functional interface used to wrap a Writer with another. Instead of implementing this 
 * interface it is often enough to use a constructor, e.g. BufferedWriter::new.
 * 
 * @author Manuel Hegner
 */
@FunctionalInterface
public interface WriterWrapper {
	
	/**
	 * This method should wrap the given Writer with another Writer.
	 * @param out the Writer given
	 * @return a wrapped Writer
	 * @throws IOException thrown by some OutputStream constructors
	 */
	public Writer wrap(Writer out) throws IOException;
}
