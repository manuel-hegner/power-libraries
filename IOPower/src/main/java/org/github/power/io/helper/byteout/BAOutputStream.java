package org.github.power.io.helper.byteout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BAOutputStream extends OutputStream implements ByteArrayWrapper {
	
	private OutputStream out;
	private ByteArrayOutputStream underlyingOutput;

	public BAOutputStream(OutputStream outputStream, ByteArrayOutputStream underlyingOutput) {
		this.out=outputStream;
		this.underlyingOutput=underlyingOutput;
	}
	
	/**
     * Converts the buffer's contents into a string decoding bytes using the
     * platform's default character set. The length of the new <tt>String</tt>
     * is a function of the character set, and hence may not be equal to the
     * size of the buffer.
     *
     * <p> This method always replaces malformed-input and unmappable-character
     * sequences with the default replacement string for the platform's
     * default character set. The {@linkplain java.nio.charset.CharsetDecoder}
     * class should be used when more control over the decoding process is
     * required.
     *
     * @return String decoded from the buffer's contents.
     */
	@Override
	public String toString() {
		return getUnderlyingOutput().toString();
	}

	@Override
	public ByteArrayOutputStream getUnderlyingOutput() {
		return underlyingOutput;
	}

	@Override
	public void write(int b) throws IOException {
		out.write(b);
	}
	
	@Override
	public void close() throws IOException {
		out.close();
	}
	
	@Override
	public void flush() throws IOException {
		out.flush();
	}
}
