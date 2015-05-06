package org.github.power.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;

import org.github.power.io.helper.CompressorRegistry;

public interface Read {
	//reading versions
	/**
	 * Creates a new BufferedReader to a compressed file. This method tries to wrap the stream
	 * in an appropriate decompressor by using the file extension. To register new extensions
	 * use {@link CompressorRegistry}
	 * @param file the file to open a stream to
	 * @return a new BufferedReader for the file
	 * @throws IOException if the decompressing stream throws an IOException or if the file is not found
	 */
	public static BufferedReader compressedFile(File file) throws IOException {
		return new BufferedReader(new InputStreamReader(Input.compressedFile(file)));
	}
	
	/**
	 * Creates a new BufferedReader to a file.
	 * @param file the file to open a stream to
	 * @return a new BufferedReader for the file
	 * @throws FileNotFoundException if the file does not exist
	 */
	public static BufferedReader file(File file) throws FileNotFoundException {
		return new BufferedReader(new InputStreamReader(Input.file(file)));
	}
	
	/**
	 * Creates a new BufferedReader to a compressed resource. This method tries to wrap the stream
	 * in an appropriate decompressor by using the file extension. To register new extensions
	 * use {@link CompressorRegistry}
	 * @param resource the resource to open a stream to
	 * @return a new BufferedReader for the resource
	 * @throws IOException if the decompressing stream throws an IOException
	 */
	public static BufferedReader compressedResource(URL resource) throws IOException {
		return new BufferedReader(new InputStreamReader(Input.compressedResource(resource)));
	}
	
	/**
	 * Creates a new BufferedReader to a resource.
	 * @param resource the resource to open a stream to
	 * @return a new BufferedReader for the resource
	 * @throws IOException if the stream throws an IOException
	 */
	public static BufferedReader resource(URL resource) throws IOException {
		return new BufferedReader(new InputStreamReader(Input.resource(resource)));
	}
	
	/**
	 * Creates a new BufferedReader to a compressed resource. This method tries to wrap the stream
	 * in an appropriate decompressor by using the file extension. To register new extensions
	 * use {@link CompressorRegistry}
	 * @param resource the resource to open a stream to
	 * @param charset the charset to use to decode the resource
	 * @return a new BufferedReader for the resource
	 * @throws IOException if the decompressing stream throws an IOException
	 */
	public static BufferedReader compressedResource(URL resource, Charset charset) throws IOException {
		return new BufferedReader(new InputStreamReader(Input.compressedResource(resource), charset));
	}
	
	/**
	 * Creates a new BufferedReader to a resource.
	 * @param resource the resource to open a stream to
	 * @param charset the charset to use to decode the resource
	 * @return a new BufferedReader for the resource
	 * @throws IOException if the stream throws an IOException
	 */
	public static BufferedReader resource(URL resource, Charset charset) throws IOException {
		return new BufferedReader(new InputStreamReader(Input.resource(resource), charset));
	}
	
	/**
	 * Creates a new BufferedReader for the given byte array.
	 * @param bytes the bytes that are used as a source for this reader
	 * @return a new BufferedReader for the bytes
	 */
	public static BufferedReader bytes(byte[] bytes) {
		return new BufferedReader(new InputStreamReader(Input.bytes(bytes)));
	}
	
	/**
	 * Creates a new Reader from the given String.
	 * @param str the string to transform into a reader
	 * @return a new StringReader
	 */
	public static StringReader string(String str) {
		return new StringReader(str);
	}
	
	//reading with charset
	/**
	 * Creates a new BufferedReader to a compressed file. This method tries to wrap the stream
	 * in an appropriate decompressor by using the file extension. To register new extensions
	 * use {@link CompressorRegistry}
	 * @param file the file to open a stream to
	 * @param charset the charset to use to decode the file
	 * @return a new BufferedReader for the file
	 * @throws IOException if the decompressing stream throws an IOException or if the file is not found
	 */
	public static BufferedReader compressedFile(File file, Charset charset) throws IOException {
		return new BufferedReader(new InputStreamReader(Input.compressedFile(file), charset));
	}
	
	/**
	 * Creates a new BufferedReader to a file.
	 * @param file the file to open a stream to
	 * @param charset the charset to use to decode the file
	 * @return a new BufferedReader for the file
	 * @throws FileNotFoundException if the file does not exist
	 */
	public static BufferedReader file(File file, Charset charset) throws FileNotFoundException {
		return new BufferedReader(new InputStreamReader(Input.file(file), charset));
	}
	
	/**
	 * Creates a new BufferedReader for the given byte array.
	 * @param bytes the bytes that are used as a source for this reader
	 * @param charset the charset to use to decode the bytes
	 * @return a new BufferedReader for the bytes
	 */
	public static BufferedReader bytes(byte[] bytes, Charset charset) {
		return new BufferedReader(new InputStreamReader(Input.bytes(bytes), charset));
	}
}
