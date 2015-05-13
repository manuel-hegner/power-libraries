package org.github.power.io.builder.sources;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamSource implements Source {

	private InputStream input;

	public InputStreamSource(InputStream input) {
		this.input=input;
	}
	
	@Override
	public InputStream openStream() throws IOException {
		return input;
	}

}
