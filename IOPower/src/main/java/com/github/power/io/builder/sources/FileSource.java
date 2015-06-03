package com.github.power.io.builder.sources;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class represents a source that is a f as an input.
 * @author Manuel Hegner
 */
public class FileSource implements Source {

	private File file;

	public FileSource(File file) {
		this.file=file;
	}
	
	@Override
	public InputStream openStream() throws IOException {
		return new FileInputStream(file);
	}
	
	@Override
	public boolean hasName() {
		return true;
	}

	/**
	 * @return the file name
	 */
	@Override
	public String getName() {
		return file.getName();
	}
}
