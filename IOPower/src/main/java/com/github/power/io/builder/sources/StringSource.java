package com.github.power.io.builder.sources;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * This class represents a source that is a String. It also needs the charset used to transform the String into a bytestream. 
 * @author Manuel Hegner
 */
public class StringSource implements Source {

	private String string;
	private Charset charset;

	public StringSource(String str, Charset charset) {
		this.string=str;
		this.charset=charset;
	}

	@Override
	public InputStream openStream() throws IOException {
		return new ByteArrayInputStream(string.getBytes(charset));
	}

}
