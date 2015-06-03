package com.github.power.io.helper.byteout;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * This interface should be used in all classes that extend a normal Writer or InputStream but rely upon
 * a {@link ByteArrayOutputStream} at the end of the chain. This interface implements default delegate methods
 * to all common {@link ByteArrayOutputStream} methods.
 * @author Manuel Hegner
 *
 */
public interface ByteArrayWrapper extends Closeable {
	
	/**
	 * This method returns the ByteArrayOutputStream wrapped by this object.
	 * @return the ByteArrayOutputStream
	 */
	public ByteArrayOutputStream getUnderlyingOutput();
	
	/**
     * Writes the complete contents of this byte array output stream to
     * the specified output stream argument, as if by calling the output
     * stream's write method using <code>out.write(buf, 0, count)</code>.
     * 
     * This method automatically closes this stream or writer. If you need to use
     * this method without closing the output, call <code>getUnderlyingOutput().writeTo(out)</code>
     *
     * @param      out   the output stream to which to write the data.
     * @exception  IOException  if an I/O error occurs.
     */
	public default void writeTo(OutputStream out) throws IOException {
		close();
		getUnderlyingOutput().writeTo(out);
	}

	/**
     * Resets the <code>count</code> field of this byte array output
     * stream to zero, so that all currently accumulated output in the
     * output stream is discarded. The output stream can be used again,
     * reusing the already allocated buffer space.
     * @see     java.io.ByteArrayInputStream#count
     */
	public default void resetByteBuffer() {
		getUnderlyingOutput().reset();
	}

	/**
     * Creates a newly allocated byte array. Its size is the current
     * size of this output stream and the valid contents of the buffer
     * have been copied into it.
     *
     * This method automatically closes this stream or writer. If you need to use
     * this method without closing the output, call <code>getUnderlyingOutput().toByteArray()</code>
     * @return  the current contents of this output stream, as a byte array.
     * @see     java.io.ByteArrayOutputStream#size()
     */
	public default byte[] toByteArray() {
		closeSilently();
		return getUnderlyingOutput().toByteArray();
	}

	/**
     * Returns the current size of the buffer.
     *
     * @return  the value of the <code>count</code> field, which is the number
     *          of valid bytes in this output stream.
     * @see     java.io.ByteArrayOutputStream#count
     */
	public default int bufferSize() {
		return getUnderlyingOutput().size();
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
     * This method automatically closes this stream or writer. If you need to use
     * this method without closing the output, call <code>getUnderlyingOutput().writeTo(out)</code>
     *
     * @param      charset  a supported {@link java.nio.charset.Charset charset}
     * @return     String decoded from the buffer's contents.
     */
	public default String toString(Charset charset) {
		closeSilently();
		return new String(getUnderlyingOutput().toByteArray(), charset);
	}
	
	/**
	 * Closes this Closable without throwing any exceptions. 
	 */
	public default void closeSilently() {
		try {
			close();
		} catch(Exception e) {}
	}
}
