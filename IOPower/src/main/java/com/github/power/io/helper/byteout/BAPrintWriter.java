package com.github.power.io.helper.byteout;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * This class represents a normal Writer that wraps a ByteArrayOutputStream. It is a simple
 * helper class that allows you to access the ByteArrayOutputStream specific methods of the underlying
 * ByteArrayOutputStream. Remember to close this writer before accessing the underlying byte array.
 * @author Manuel Hegner
 *
 */
public class BAPrintWriter extends PrintWriter implements ByteArrayWrapper {

	private ByteArrayOutputStream underlyingOutput;

	/**
	 * The constructor of the BufferedByteWriter
	 * @param writer the writer directly underlying this BufferedWriter
	 * @param underlyingOutput the ByteArrayOutputStream that is at the end of this writer/stream chain
	 */
	public BAPrintWriter(Writer writer, ByteArrayOutputStream underlyingOutput) {
		super(writer);
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
}
