package org.github.power.io;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;

import org.github.power.io.helper.ByteArrayWriter;
import org.github.power.io.helper.CompressorRegistry;

public interface Write {
	//writing versions
		/**
		 * Creates a new BufferedWriter to a compressed file. This method tries to wrap the stream
		 * in an appropriate compressor by using the file extension. To register new extensions
		 * use {@link CompressorRegistry}
		 * @param file the file to open a stream to
		 * @return a new BufferedWriter for the file
		 * @throws IOException if the compressing stream throws an IOException or if the file is not found
		 */
		public static BufferedWriter writeCompressedFile(File file) throws IOException {
			return new BufferedWriter(new OutputStreamWriter(Output.compressedFile(file)));
		}
		
		/**
		 * Creates a new BufferedWriter to a file. 
		 * @param file the file to open a stream to
		 * @return a new BufferedWriter for the file
		 * @throws FileNotFoundException if the file exists but is a 
		 * directory rather than a regular file, does not exist but cannot be created, 
		 * or cannot be opened for any other reason
		 */
		public static BufferedWriter file(File file) throws FileNotFoundException {
			return new BufferedWriter(new OutputStreamWriter(Output.streamFile(file)));
		}
		
		/**
		 * Creates a new ByteWriter to write to a byte array. The ByteWriter is a normal Writer
		 * that also allows you to access the underlying ByteArrayOutputStream's methods. 
		 * @return a new ByteWriter
		 */
		public static ByteArrayWriter bytes() {
			ByteArrayOutputStream byteOut=new ByteArrayOutputStream();
			return new ByteArrayWriter(new OutputStreamWriter(byteOut), byteOut);
		}
		
		/**
		 * Creates a new ByteWriter to write to a byte array. The ByteWriter is a normal Writer
		 * that also allows you to access the underlying ByteArrayOutputStream's methods. 
		 * @param bufferSize the initial byte buffer size
		 * @return a new ByteWriter
		 */
		public static ByteArrayWriter bytes(int bufferSize) {
			ByteArrayOutputStream byteOut=new ByteArrayOutputStream(bufferSize);
			return new ByteArrayWriter(new OutputStreamWriter(byteOut), byteOut);
		}
		
		/**
		 * Creates a new StringWriter to write to a String.
		 * @return a new StringWriter
		 */
		public static StringWriter writeString() {
			return new StringWriter();
		}
		
		/**
		 * Creates a new StringWriter to write to a String.
		 * @param the initial buffer size
		 * @return a new StringWriter
		 */
		public static StringWriter writeString(int initalSize) {
			return new StringWriter(initalSize);
		}
		
		//reading with charset
		/**
		 * Creates a new BufferedWriter to a compressed file. This method tries to wrap the stream
		 * in an appropriate compressor by using the file extension. To register new extensions
		 * use {@link CompressorRegistry}
		 * @param file the file to open a stream to
		 * @param charset the charset to use to encode the file
		 * @return a new BufferedWriter for the file
		 * @throws IOException if the compressing stream throws an IOException or if the file is not found
		 */
		public static BufferedWriter readCompressedFile(File file, Charset charset) throws IOException {
			return new BufferedWriter(new OutputStreamWriter(Output.compressedFile(file), charset));
		}
		
		/**
		 * Creates a new BufferedWriter to a file. 
		 * @param file the file to open a stream to
		 * @param charset the charset to use to encode the file
		 * @return a new BufferedWriter for the file
		 * @throws FileNotFoundException if the file exists but is a 
		 * directory rather than a regular file, does not exist but cannot be created, 
		 * or cannot be opened for any other reason
		 */
		public static BufferedWriter readFile(File file, Charset charset) throws FileNotFoundException {
			return new BufferedWriter(new OutputStreamWriter(Output.streamFile(file), charset));
		}
		
		/**
		 * Creates a new ByteWriter to write to a byte array. The ByteWriter is a normal Writer
		 * that also allows you to access the underlying ByteArrayOutputStream's methods. 
		 * @param charset the charset to use to encode the file
		 * @return a new ByteWriter
		 */
		public static ByteArrayWriter bytes(Charset charset) {
			ByteArrayOutputStream byteOut=new ByteArrayOutputStream();
			return new ByteArrayWriter(new OutputStreamWriter(byteOut, charset), byteOut);
		}
		
		/**
		 * Creates a new ByteWriter to write to a byte array. The ByteWriter is a normal Writer
		 * that also allows you to access the underlying ByteArrayOutputStream's methods.
		 * @param charset the charset to use to encode the file 
		 * @param bufferSize the initial byte buffer size
		 * @return a new ByteWriter
		 */
		public static ByteArrayWriter bytes(int bufferSize, Charset charset) {
			ByteArrayOutputStream byteOut=new ByteArrayOutputStream(bufferSize);
			return new ByteArrayWriter(new OutputStreamWriter(byteOut, charset), byteOut);
		}
}
