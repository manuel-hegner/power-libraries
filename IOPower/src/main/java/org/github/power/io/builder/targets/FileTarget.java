package org.github.power.io.builder.targets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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
