package com.github.powerlibraries.io.builder.sources;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class represents a source that is a byte array.
 * @author Manuel Hegner
 */
public class ByteArraySource implements Source {

	private byte[] bytes;
	private int offset;
	private int length;

	public ByteArraySource(byte[] bytes) {
		this(bytes, 0, bytes.length);
	}
	
	public ByteArraySource(byte[] bytes, int offset, int length) {
		this.bytes=bytes;
		this.offset=offset;
		this.length=length;
	}
	
	@Override
	public InputStream openStream() throws IOException {
		return new ByteArrayInputStream(bytes, offset, length);
	}

}
