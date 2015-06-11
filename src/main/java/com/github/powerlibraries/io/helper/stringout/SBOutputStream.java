package com.github.powerlibraries.io.helper.stringout;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This class extends a {@link OutputStream} with the ability to directly access an underlying 
 * {@link ByteArrayOutputStream} and to return a resulting string.
 * 
 * @see DataOutputStream
 * @see SBWrapper
 * @author Manuel Hegner
 */
public class SBOutputStream extends OutputStream implements SBWrapper {
	
	private OutputStream out;
	private StringBuilderOutputStream underlyingOutput;

	public SBOutputStream(OutputStream outputStream, StringBuilderOutputStream underlyingOutput) {
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
	public StringBuilderOutputStream getUnderlyingOutput() {
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
