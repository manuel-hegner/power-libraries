package com.github.powerlibraries.io.functions;

import java.io.OutputStream;
import java.util.function.Consumer;

/**
 * This helper class is a normal {@link Consumer} for {@link OutputStream}s.
 * @see Consumer
 * @see OutputStream
 */
@FunctionalInterface
public interface OutputStreamConsumer extends Consumer<OutputStream>{}
