package com.github.powerlibraries.io.builder.targets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This class represents a file as the target of the output chain. 
 * @author Manuel Hegner
 *
 */
public class FileTarget implements Target {

	private File file;

	public FileTarget(File file) {
		this.file=file;
	}
	
	@Override
	public OutputStream openStream() throws IOException {
		return new FileOutputStream(file);
	}
	
	@Override
	public boolean hasName() {
		return true;
	}

	@Override
	public String getName() {
		return file.getName();
	}
}
