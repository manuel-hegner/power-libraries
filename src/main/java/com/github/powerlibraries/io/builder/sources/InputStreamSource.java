package com.github.powerlibraries.io.builder.sources;

import java.io.IOException;
import java.io.InputStream;

/**
 * This class represents a source that is a an open InputStream. In contrast to other sources this Source is not reusable.
 * @author Manuel Hegner
 */
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
