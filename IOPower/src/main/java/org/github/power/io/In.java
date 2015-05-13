package org.github.power.io;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

import org.github.power.io.builder.InBuilder;
import org.github.power.io.builder.sources.ByteArraySource;
import org.github.power.io.builder.sources.FileSource;
import org.github.power.io.builder.sources.InputStreamSource;
import org.github.power.io.builder.sources.Source;
import org.github.power.io.builder.sources.StringSource;
import org.github.power.io.builder.sources.URLSource;

public interface In {
	
	public static InBuilder file(String file) {
		if(file==null)
			throw new NullPointerException("The given file was null");
		return file(new File(file));
	}
	
	public static InBuilder file(File file) {
		if(file==null)
			throw new NullPointerException("The given file was null");
		return new InBuilder(new FileSource(file));
	}

	public static InBuilder resource(URL resource) {
		if(resource==null)
			throw new NullPointerException("The given resource was null");
		return new InBuilder(new URLSource(resource));
	}
	
	public static InBuilder resource(Class<?> referenceClass, String resourceName) {
		return resource(referenceClass.getResource(resourceName));
	}
	
	public static InBuilder resource(String resourceName) {
		return resource(In.class.getResource(resourceName));
	}
	
	public static InBuilder stream(InputStream inputStream) {
		if(inputStream==null)
			throw new NullPointerException("The given inputstream was null");
		return new InBuilder(new InputStreamSource(inputStream));
	}
	
	public static InBuilder string(String str) {
		if(str==null)
			throw new NullPointerException("The given string was null");
		return new InBuilder(new StringSource(str, Charset.defaultCharset()));
	}
	
	public static InBuilder string(String str, Charset charset) {
		if(str==null)
			throw new NullPointerException("The given string was null");
		return new InBuilder(new StringSource(str, charset));
	}
	
	public static InBuilder source(Source source) {
		if(source==null)
			throw new NullPointerException("The given source was null");
		return new InBuilder(source);
	}
	
	public static InBuilder bytes(byte[] bytes) {
		if(bytes==null)
			throw new NullPointerException("The given bytes were null");
		return new InBuilder(new ByteArraySource(bytes));
	}
	
	public static InBuilder bytes(byte[] bytes, int offset, int length) {
		if(bytes==null)
			throw new NullPointerException("The given bytes were null");
		return new InBuilder(new ByteArraySource(bytes, offset, length));
	}
}
