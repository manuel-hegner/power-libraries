package org.github.power.io;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.github.power.io.helper.CompressorRegistry;

/**
 * This class is a helper that has a number of functions to simply and easily create the most common types of InputStreams
 * and Readers.
 * @author Manuel Hegner
 */
public interface Input {
	//streaming methods
	
	/**
	 * Creates a new InputStream to a compressed file. This method tries to wrap the stream
	 * in an appropriate decompressor by using the file extension. To register new extensions
	 * use {@link CompressorRegistry}
	 * @param file the file to open a stream to
	 * @return a new Inputstream for the file
	 * @throws IOException if the decompressing stream throws an IOException or if the file is not found
	 */
	@SuppressWarnings("resource")
	public static InputStream compressedFile(File file) throws IOException {
		return CompressorRegistry.getInstance().wrap(file.getName(), new FileInputStream(file));
	}
	
	/**
	 * Creates a new InputStream to a file.
	 * @param file the file to open a stream to
	 * @return a new Inputstream for the file
	 * @throws FileNotFoundException if the file does not exist
	 */
	public static FileInputStream file(File file) throws FileNotFoundException {
		return new FileInputStream(file);
	}
	
	/**
	 * Creates a new InputStream to a compressed resource. This method tries to wrap the stream
	 * in an appropriate decompressor by using the file extension. To register new extensions
	 * use {@link CompressorRegistry}
	 * @param resource the resource URL that you want to stream
	 * @return a new Inputstream for the resource
	 * @throws IOException if the decompressing stream throws an IOException
	 */
	public static InputStream compressedResource(URL resource) throws IOException {
		return CompressorRegistry.getInstance().wrap(resource.toString(), resource.openStream());
	}
	
	/**
	 * Creates a new InputStream to a resource.
	 * @param resource the resource URL that you want to stream
	 * @return a new Inputstream for the resource
	 * @throws IOException if the stream throws an IOException
	 */
	public static InputStream resource(URL resource) throws IOException {
		return resource.openStream();
	}
	
	/**
	 * Creates a new InputStream streaming the contents of the given byte array
	 * @param bytes the bytes to open a stream to
	 * @return a new Inputstream for the bytes
	 */
	public static ByteArrayInputStream bytes(byte[] bytes) {
		return new ByteArrayInputStream(bytes);
	}
}
