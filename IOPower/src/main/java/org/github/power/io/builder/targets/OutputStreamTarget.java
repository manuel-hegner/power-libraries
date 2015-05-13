package org.github.power.io.builder.targets;

import java.io.IOException;
import java.io.OutputStream;

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
