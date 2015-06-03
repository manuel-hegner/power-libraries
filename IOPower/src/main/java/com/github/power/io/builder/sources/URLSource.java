package com.github.power.io.builder.sources;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * This method is a source that wraps an {@link URL}.
 * @author Manuel Hegner
 *
 */
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

	/**
	 * @return the result of {@link URL#toString}
	 */
	@Override
	public String getName() {
		return url.toString();
	}
}
