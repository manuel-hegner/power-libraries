package org.github.power.io.builder.sources;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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

	@Override
	public String getName() {
		return file.getName();
	}
}
