package com.github.power.io.builder.targets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This class represents a ByteArrayOutputStream as the final element in the chain.
 * @author Manuel Hegner
 */
public class ByteArrayTarget implements Target {

	private ByteArrayOutputStream lastStream;

	public ByteArrayOutputStream getLastStream() {
		return lastStream;
	}

	@Override
	public OutputStream openStream() throws IOException {
		this.lastStream=new ByteArrayOutputStream();
		return lastStream;
	}

}
