package org.github.power.io.builder.targets;

import java.io.IOException;
import java.io.OutputStream;

public interface Target {
	public OutputStream openStream() throws IOException;

	public default boolean hasName() {
		return false;
	}

	public default String getName() {
		throw new UnsupportedOperationException();
	}
}
