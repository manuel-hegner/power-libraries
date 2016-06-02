package com.github.powerlibraries.io.functions;

import java.io.Writer;
import java.util.function.Consumer;

/**
 * This helper class is a normal {@link Consumer} for {@link Writer}s.
 * @see Consumer
 * @see Writer
 */
@FunctionalInterface
public interface WriterConsumer extends Consumer<Writer>{}
