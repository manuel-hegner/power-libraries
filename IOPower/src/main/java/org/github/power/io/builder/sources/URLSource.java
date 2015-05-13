package org.github.power.io.builder.sources;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class URLSource implements Source {

	private URL url;

	public URLSource(URL url) {
		this.url=url;
	}
	
	@Override
	public InputStream openStream() throws IOException {
		return url.openStream();
	}

	@Override
	public boolean hasName() {
		return true;
	}

	@Override
	public String getName() {
		return url.toString();
	}
}
