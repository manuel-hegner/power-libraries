package org.github.power.io.helper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class ByteArrayWrappedOutputStream extends OutputStream {
	
	private OutputStream out;
	private ByteArrayOutputStream underlyingOutput;

	public ByteArrayWrappedOutputStream(OutputStream outputStream, ByteArrayOutputStream underlyingOutput) {
		this.out=outputStream;
		this.underlyingOutput=underlyingOutput;
	}
	
	/**
     * Writes the complete contents of this byte array output stream to
     * the specified output stream argument, as if by calling the output
     * stream's write method using <code>out.write(buf, 0, count)</code>.
     *
     * @param      out   the output stream to which to write the data.
     * @exception  IOException  if an I/O error occurs.
     */
	public void writeTo(OutputStream out) throws IOException {
		underlyingOutput.writeTo(out);
	}

	/**
     * Resets the <code>count</code> field of this byte array output
     * stream to zero, so that all currently accumulated output in the
     * output stream is discarded. The output stream can be used again,
     * reusing the already allocated buffer space.
     *
     * @see     java.io.ByteArrayInputStream#count
     */
	public void reset() {
		underlyingOutput.reset();
	}

	/**
     * Creates a newly allocated byte array. Its size is the current
     * size of this output stream and the valid contents of the buffer
     * have been copied into it.
     *
     * @return  the current contents of this output stream, as a byte array.
     * @see     java.io.ByteArrayOutputStream#size()
     */
	public byte[] toByteArray() {
		return underlyingOutput.toByteArray();
	}

	/**
     * Returns the current size of the buffer.
     *
     * @return  the value of the <code>count</code> field, which is the number
     *          of valid bytes in this output stream.
     * @see     java.io.ByteArrayOutputStream#count
     */
	public int size() {
		return underlyingOutput.size();
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
		return underlyingOutput.toString();
	}

	/**
     * Converts the buffer's contents into a string by decoding the bytes using
     * the {@link java.nio.charset.Charset charset}. The length of the new
     * <tt>String</tt> is a function of the charset, and hence may not be equal
     * to the length of the byte array.
     *
     * <p> This method always replaces malformed-input and unmappable-character
     * sequences with this charset's default replacement string. The {@link
     * java.nio.charset.CharsetDecoder} class should be used when more control
     * over the decoding process is required.
     *
     * @param      charset  a supported {@link java.nio.charset.Charset charset}
     * @return     String decoded from the buffer's contents.
     */
	public String toString(Charset charset) {
		return new String(underlyingOutput.toByteArray(), charset);
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
