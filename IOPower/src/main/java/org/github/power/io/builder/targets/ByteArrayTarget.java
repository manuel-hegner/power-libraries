package org.github.power.io.builder.targets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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
