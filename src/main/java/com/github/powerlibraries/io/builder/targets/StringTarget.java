package com.github.powerlibraries.io.builder.targets;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import com.github.powerlibraries.io.helper.stringout.StringBuilderOutputStream;

/**
 * This class represents a string as the target.
 * @author Manuel Hegner
 *
 */
public class StringTarget implements Target {

	private Charset charset;
	private StringBuilderOutputStream lastStream;
	
	public StringTarget(Charset charset) {
		this.charset=charset;
	}
	
	@Override
	public OutputStream openStream() throws IOException {
		lastStream = new StringBuilderOutputStream(charset);
		return lastStream;
	}

	public StringBuilderOutputStream getLastStream() {
		return lastStream;
	}

}
