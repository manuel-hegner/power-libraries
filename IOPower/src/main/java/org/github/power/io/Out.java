package org.github.power.io;

import java.io.File;
import java.io.OutputStream;

import org.github.power.io.builder.ByteOutBuilder;
import org.github.power.io.builder.OutBuilder;
import org.github.power.io.builder.targets.FileTarget;
import org.github.power.io.builder.targets.OutputStreamTarget;
import org.github.power.io.builder.targets.Target;

public interface Out {
	
	public static OutBuilder file(String file) {
		if(file==null)
			throw new NullPointerException("The given file was null");
		return file(new File(file));
	}
	
	public static OutBuilder file(File file) {
		if(file==null)
			throw new NullPointerException("The given file was null");
		return new OutBuilder(new FileTarget(file));
	}

	public static OutBuilder stream(OutputStream inputStream) {
		if(inputStream==null)
			throw new NullPointerException("The given inputStream was null");
		return new OutBuilder(new OutputStreamTarget(inputStream));
	}
	
	public static OutBuilder target(Target target) {
		if(target==null)
			throw new NullPointerException("The given target was null");
		return new OutBuilder(target);
	}
	
	public static ByteOutBuilder bytes() {
		return new ByteOutBuilder();
	}
}
