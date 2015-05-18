package org.github.power.io.builder.targets;

import java.io.IOException;
import java.io.OutputStream;


/**
 * This interface represents the final consuming element in an output chain. A target is simply an object that is able
 * to open an OutputStream.
 * @author Manuel Hegner
 *
 */
public interface Target {
	
	/**
	 * @return an OutputStream
	 * @throws IOException if openeing the OutputStream throws an {@link IOException}
	 */
	public OutputStream openStream() throws IOException;

	/**
	 * @return true, if the target has any kind of name
	 */
	public default boolean hasName() {
		return false;
	}

	/**
	 * @return the name of target. This method throws an {@link UnsupportedOperationException} by default.
	 */
	public default String getName() {
		throw new UnsupportedOperationException();
	}
}
