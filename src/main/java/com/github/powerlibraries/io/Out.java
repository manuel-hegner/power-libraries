package com.github.powerlibraries.io;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.io.Writer;
import java.io.ObjectInputStream.GetField;

import com.github.powerlibraries.io.builder.ByteOutBuilder;
import com.github.powerlibraries.io.builder.OutBuilder;
import com.github.powerlibraries.io.builder.StringOutBuilder;
import com.github.powerlibraries.io.builder.targets.FileTarget;
import com.github.powerlibraries.io.builder.targets.OutputStreamTarget;
import com.github.powerlibraries.io.builder.targets.Target;

/**
 * This class contains a number of useful static methods that help creating OutputStreams and Writers.
 * @author Manuel Hegner
 */
public interface Out {
	
	/**
	 * This creates an ouput of any kind to a {@link File}. The returned {@link OutBuilder} can be used
	 * to specifiy which kind of Writer or OutputStream should be created and allows you to specify further how
	 * the output chain is build.
	 * @param file the path to output to
	 * @return an {@link OutBuilder} used to specify which kind of output should be created
	 */
	public static OutBuilder file(String file) {
		if(file==null)
			throw new NullPointerException("The given file was null");
		return file(new File(file));
	}
	
	/**
	 * This creates an ouput of any kind to a {@link File}. The returned {@link OutBuilder} can be used
	 * to specifiy which kind of Writer or OutputStream should be created and allows you to specify further how
	 * the output chain is build.
	 * @param file the file to output to
	 * @return an {@link OutBuilder} used to specify which kind of output should be created
	 */
	public static OutBuilder file(File file) {
		if(file==null)
			throw new NullPointerException("The given file was null");
		return new OutBuilder(new FileTarget(file));
	}
	
	/**
	 * This creates an ouput of any kind to a {@link File}. The returned {@link OutBuilder} can be used
	 * to specifiy which kind of Writer or OutputStream should be created and allows you to specify further how
	 * the output chain is build.
	 * @param parent the parent of the file to output to
	 * @param child the output file relative to the parent
	 * @return an {@link OutBuilder} used to specify which kind of output should be created
	 */
	public static OutBuilder file(File parent, String child) {
		if(parent==null || child==null)
			throw new NullPointerException("The given file was null");
		return new OutBuilder(new FileTarget(new File(parent, child)));
	}
	
	/**
	 * This creates an ouput of any kind to a {@link File}. The returned {@link OutBuilder} can be used
	 * to specifiy which kind of Writer or OutputStream should be created and allows you to specify further how
	 * the output chain is build.
	 * @param parent the parent of the file to output to
	 * @param child the output file relative to the parent
	 * @return an {@link OutBuilder} used to specify which kind of output should be created
	 */
	public static OutBuilder file(String parent, String child) {
		if(parent==null || child==null)
			throw new NullPointerException("The given file was null");
		return new OutBuilder(new FileTarget(new File(parent, child)));
	}

	/**
	 * This creates an ouput of any kind to an {@link OutputStream}. The returned {@link OutBuilder} can be used
	 * to specifiy which kind of Writer or OutputStream should be created and allows you to specify further how
	 * the output chain is build.
	 * Be aware that if you use this method you can only build one Output with the Builder. Otherwise the given
	 * stream will be used more than once.
	 * @param outputStream the {@link OutputStream} to output to
	 * @return an {@link OutBuilder} used to specify which kind of output should be created
	 */
	public static OutBuilder stream(OutputStream outputStream) {
		if(outputStream==null)
			throw new NullPointerException("The given inputStream was null");
		return new OutBuilder(new OutputStreamTarget(outputStream));
	}
	
	/**
	 * This creates an ouput of any kind to a {@link Target}. The returned {@link OutBuilder} can be used
	 * to specifiy which kind of Writer or OutputStream should be created and allows you to specify further how
	 * the output chain is build.
	 * This method can be used with custom implementations of the target interface.
	 * @param target the target to output to
	 * @return an {@link OutBuilder} used to specify which kind of output should be created
	 */
	public static OutBuilder target(Target target) {
		if(target==null)
			throw new NullPointerException("The given target was null");
		return new OutBuilder(target);
	}
	
	/**
	 * This creates an ouput of any kind to a {@link ByteArrayOutputStream}. The returned {@link ByteOutBuilder} 
	 * can be used to specifiy which kind of Writer or OutputStream should be created and allows you 
	 * to specify further how the output chain is build.
	 * The returned {@link ByteOutBuilder} returns wrappers instead of the normal {@link OutputStream}s and 
	 * {@link Writer}s that allow you to directly access the underlying byte array.
	 * @return an {@link ByteOutBuilder} used to specify which kind of output should be created
	 */
	public static ByteOutBuilder bytes() {
		return new ByteOutBuilder();
	}
	
	/**
	 * This creates an ouput of any kind to a 
	 * {@link com.github.powerlibraries.io.helper.stringout.StringBuilderOutputStream}. 
	 * The returned {@link ByteOutBuilder} can be used to specifiy which kind of Writer
	 * or OutputStream should be created and allows you to specify further how the output chain is build.
	 * The returned {@link ByteOutBuilder} returns wrappers instead of the normal {@link OutputStream}s and 
	 * {@link Writer}s that allow you to directly access the underlying byte array.
	 * @return an {@link ByteOutBuilder} used to specify which kind of output should be created
	 */
	public static StringOutBuilder string() {
		return new StringOutBuilder();
	}
}
