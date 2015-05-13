package org.github.power.io.builder.sources;

import java.io.IOException;
import java.io.InputStream;

public interface Source {
	public InputStream openStream() throws IOException;

	public default boolean hasName() {
		return false;
	}

	public default String getName() {
		throw new UnsupportedOperationException();
	}
}
