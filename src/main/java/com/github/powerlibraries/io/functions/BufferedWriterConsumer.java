package com.github.powerlibraries.io.functions;

import java.io.BufferedWriter;
import java.util.function.Consumer;

/**
 * This helper class is a normal {@link Consumer} for {@link BufferedWriter}s.
 * @see Consumer
 * @see BufferedWriter
 */
@FunctionalInterface
public interface BufferedWriterConsumer extends Consumer<BufferedWriter>{}
