package com.github.power.io.builder.targets;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This class represents an OutputStream as the target. This makes the builder non-reusable.
 * @author Manuel Hegner
 *
 */
public class OutputStreamTarget implements Target {

	private OutputStream output;

	public OutputStreamTarget(OutputStream input) {
		this.output=input;
	}
	
	@Override
	public OutputStream openStream() throws IOException {
		return output;
	}

}
