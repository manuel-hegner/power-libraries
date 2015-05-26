package org.github.power.io;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

import org.github.power.io.builder.InBuilder;
import org.github.power.io.builder.OutBuilder;
import org.github.power.io.builder.sources.ByteArraySource;
import org.github.power.io.builder.sources.FileSource;
import org.github.power.io.builder.sources.InputStreamSource;
import org.github.power.io.builder.sources.Source;
import org.github.power.io.builder.sources.StringSource;
import org.github.power.io.builder.sources.URLSource;

public interface In {
	
	/**
	 * This creates an input of any kind from a {@link File}. The returned {@link InBuilder} can be used
	 * to specifiy which kind of Reader or InputStream should be created and allows you to specify further how
	 * the input chain is build.
	 * @param file the path to read from
	 * @return an {@link InBuilder} used to specify which kind of input should be created
	 */
	public static InBuilder file(String file) {
		if(file==null)
			throw new NullPointerException("The given file was null");
		return file(new File(file));
	}
	
	/**
	 * This creates an input of any kind from a {@link File}. The returned {@link InBuilder} can be used
	 * to specifiy which kind of Reader or InputStream should be created and allows you to specify further how
	 * the input chain is build.
	 * @param file the file to read from
	 * @return an {@link InBuilder} used to specify which kind of input should be created
	 */
	public static InBuilder file(File file) {
		if(file==null)
			throw new NullPointerException("The given file was null");
		return new InBuilder(new FileSource(file));
	}

	/**
	 * This creates an input of any kind from an {@link URL}. The returned {@link InBuilder} can be used
	 * to specifiy which kind of Reader or InputStream should be created and allows you to specify further how
	 * the input chain is build.
	 * @param resource the resource URL to read from
	 * @return an {@link InBuilder} used to specify which kind of input should be created
	 */
	public static InBuilder resource(URL resource) {
		if(resource==null)
			throw new NullPointerException("The given resource was null");
		return new InBuilder(new URLSource(resource));
	}
	
	/**
	 * This creates an input of any kind from an {@link URL}. The returned {@link InBuilder} can be used
	 * to specifiy which kind of Reader or InputStream should be created and allows you to specify further how
	 * the input chain is build.
	 * This call is the same as calling:
	 * <pre>
	 * In.resource(referenceClass.getResource(resourceName));
	 * </pre>
	 * @param referenceClass the class used to resolve the resourceName
	 * @param resourceName the name of resource to load (see {@link Class#getResource})
	 * @return an {@link InBuilder} used to specify which kind of input should be created
	 */
	public static InBuilder resource(Class<?> referenceClass, String resourceName) {
		return resource(referenceClass.getResource(resourceName));
	}
	
	/**
	 * This creates an input of any kind from an {@link URL}. The returned {@link InBuilder} can be used
	 * to specifiy which kind of Reader or InputStream should be created and allows you to specify further how
	 * the input chain is build.
	 * This call is the same as calling:
	 * <pre>
	 * In.resource(In.class.getResource(resourceName));
	 * </pre>
	 * @param resourceName the name of resource to load (see {@link Class#getResource})
	 * @return an {@link InBuilder} used to specify which kind of input should be created
	 */
	public static InBuilder resource(String resourceName) {
		return resource(In.class.getResource(resourceName));
	}
	
	/**
	 * This creates an input of any kind from an {@link InputStream}. The returned {@link InBuilder} can be used
	 * to specifiy which kind of Reader or InputStream should be created and allows you to specify further how
	 * the input chain is build.
	 * Be aware that if you use this method you can only build one Input with the Builder. Otherwise the given
	 * stream will be used more than once.
	 * @param inputStream the {@link InputStream} to read from
	 * @return an {@link InBuilder} used to specify which kind of input should be created
	 */
	public static InBuilder stream(InputStream inputStream) {
		if(inputStream==null)
			throw new NullPointerException("The given inputstream was null");
		return new InBuilder(new InputStreamSource(inputStream));
	}
	
	/**
	 * This creates an input of any kind from a {@link String}. The returned {@link InBuilder} can be used
	 * to specifiy which kind of Reader or InputStream should be created and allows you to specify further how
	 * the input chain is build.
	 * This method uses the default {@link Charset}
	 * @param str the {@link String} to read from
	 * @return an {@link InBuilder} used to specify which kind of input should be created
	 */
	public static InBuilder string(String str) {
		if(str==null)
			throw new NullPointerException("The given string was null");
		return new InBuilder(new StringSource(str, Charset.defaultCharset()));
	}
	
	/**
	 * This creates an input of any kind from a {@link String}. The returned {@link InBuilder} can be used
	 * to specifiy which kind of Reader or InputStream should be created and allows you to specify further how
	 * the input chain is build.
	 * @param str the {@link String} to read from
	 * @param charset the {@link Charset} that should be used to decode the string into bytes
	 * @return an {@link InBuilder} used to specify which kind of input should be created
	 */
	public static InBuilder string(String str, Charset charset) {
		if(str==null)
			throw new NullPointerException("The given string was null");
		return new InBuilder(new StringSource(str, charset));
	}
	
	/**
	 * This creates an input of any kind from a {@link Source}. The returned {@link InBuilder} can be used
	 * to specifiy which kind of Reader or InputStream should be created and allows you to specify further how
	 * the input chain is build.
	 * This method can be used with custom implementations of the {@link Source} interface.
	 * @param source the {@link Source} to read from
	 * @return an {@link InBuilder} used to specify which kind of input should be created
	 */
	public static InBuilder source(Source source) {
		if(source==null)
			throw new NullPointerException("The given source was null");
		return new InBuilder(source);
	}
	
	/**
	 * This creates an input of any kind from an array of bytes. The returned {@link InBuilder} can be used
	 * to specifiy which kind of Reader or InputStream should be created and allows you to specify further how
	 * the input chain is build.
	 * @param bytes the byte array to read from
	 * @return an {@link InBuilder} used to specify which kind of input should be created
	 */
	public static InBuilder bytes(byte[] bytes) {
		if(bytes==null)
			throw new NullPointerException("The given bytes were null");
		return new InBuilder(new ByteArraySource(bytes));
	}
	
	/**
	 * This creates an input of any kind from an array of bytes. The returned {@link InBuilder} can be used
	 * to specifiy which kind of Reader or InputStream should be created and allows you to specify further how
	 * the input chain is build.
	 * @param bytes the byte array to read from
	 * @param offset the offset in the buffer of the first byte to read
	 * @param length the maximum number of bytes to read from the buffer.
	 * @return an {@link InBuilder} used to specify which kind of input should be created
	 */
	public static InBuilder bytes(byte[] bytes, int offset, int length) {
		if(bytes==null)
			throw new NullPointerException("The given bytes were null");
		return new InBuilder(new ByteArraySource(bytes, offset, length));
	}
}
