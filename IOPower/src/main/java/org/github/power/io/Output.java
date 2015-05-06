package org.github.power.io;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.github.power.io.helper.CompressorRegistry;

/**
 * This class is a helper that has a number of functions to simply and easily create the most common types of OutputStreams
 * and Writers.
 * @author Manuel Hegner
 */
public interface Output {
	//streaming methods
	/**
	 * Creates a new OutputStream to a compressed file. This method tries to wrap the stream
	 * in an appropriate compressor by using the file extension. To register new extensions
	 * use {@link CompressorRegistry}
	 * @param file the file to open a stream to
	 * @return a new Outputstream for the file
	 * @throws IOException if the compressing stream throws an IOException or if the file is not found
	 */
	@SuppressWarnings("resource")
	public static OutputStream compressedFile(File file) throws IOException {
		return CompressorRegistry.getInstance().wrap(file.getName(), new FileOutputStream(file));
	}
	
	/**
	 * Creates a new OutputStream to a file. 
	 * @param file the file to open a stream to
	 * @return a new Outputstream for the file
	 * @throws FileNotFoundException if the file exists but is a 
	 * directory rather than a regular file, does not exist but cannot be created, 
	 * or cannot be opened for any other reason
	 */
	public static FileOutputStream streamFile(File file) throws FileNotFoundException {
		return new FileOutputStream(file);
	}
	
	/**
	 * Creates a new ByteArrayOutputStream to write to a byte array. 
	 * @return a new ByteArrayOutputStream
	 */
	public static ByteArrayOutputStream bytes() {
		return new ByteArrayOutputStream();
	}
	
	/**
	 * Creates a new ByteArrayOutputStream to write to a byte array.
	 * @param bufferSize the initial buffer size
	 * @return a new ByteArrayOutputStream
	 */
	public static ByteArrayOutputStream bytes(int bufferSize) {
		return new ByteArrayOutputStream(bufferSize);
	}
}
