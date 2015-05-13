package org.github.power.io.builder.sources;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

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
