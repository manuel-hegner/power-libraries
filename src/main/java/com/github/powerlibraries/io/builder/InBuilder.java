package com.github.powerlibraries.io.builder;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.github.powerlibraries.io.builder.sources.Source;
import com.github.powerlibraries.io.functions.InputStreamWrapper;
import com.github.powerlibraries.io.functions.ReaderWrapper;
import com.github.powerlibraries.io.helper.CompressorRegistry;

/**
 * This builder is used to create an input chain.
 * 
 * @author Manuel Hegner
 *
 */
@SuppressWarnings("unchecked")
public class InBuilder extends CharsetHolder<InBuilder>{
	private Source source;
	private boolean decompress=false;
	private Base64.Decoder base64Decoder=null;
	private InputStreamWrapper decompressionWrapper;
	private List<InputStreamWrapper> streamWrappers;
	private List<ReaderWrapper> readerWrappers;

	public InBuilder(Source source) {
		this.source=source;
	}
	
	/**
	 * Adds a wrapper around the generated InputStream before creating a Writer or 
	 * a special type of input. This wrapper will be applied before decompression. 
	 * @param wrapper the wrapper to apply to the generated InputStream
	 * @return this builder
	 */
	public InBuilder wrap(InputStreamWrapper wrapper) {
		if(streamWrappers==null)
			streamWrappers=new ArrayList<>();
		streamWrappers.add(wrapper);
		return this;
	}
	
	/**
	 * Adds a wrapper around the generated Reader before creating a Reader or 
	 * a special type of input. This wrapper will only be applied if the created 
	 * input uses Readers. 
	 * @param wrapper the wrapper to apply to the generated Reader
	 * @return this builder
	 */
	public InBuilder wrap(ReaderWrapper wrapper) {
		if(readerWrappers==null)
			readerWrappers=new ArrayList<>();
		readerWrappers.add(wrapper);
		return this;
	}
	
	/**
	 * This method will tell the builder to decompress the bytes. The returned reader or stream will contain an 
	 * appropriate decompressor. If the defined source specifies a name with a file ending, the builder will try to 
	 * use the appropriate decompressor for the file extension. If there is no extension or it is unknown this
	 * simply adds a {@link DeflaterInputStream} to the chain.
	 * 
	 * If you want to add file extensions to the automatic selection see {@link CompressorRegistry#registerWrapper}.
	 * @return this builder
	 */
	public InBuilder decompress() {
		decompress=true;
		return this;
	}
	
	/**
	 * This method will tell the builder to decompress the bytes using the given compression, e.g. <code>GZipInputStream::new</code>.
	 * @param wrapper the wrapper used to decompress the bytes
	 * @return this builder
	 */
	public InBuilder decompress(InputStreamWrapper wrapper) {
		decompress=true;
		decompressionWrapper=wrapper;
		return this;
	}
	
	/**
	 * This method will add a {@link Base64.Decoder} to this chain.
	 * @return this builder
	 */
	public InBuilder decodeBase64() {
		base64Decoder=Base64.getDecoder();
		return this;
	}
	
	/**
	 * This method will add a {@link Base64.Decoder} to this chain.
	 * @param decoder the specific decoder that should be used.
	 * @return this builder
	 */
	public InBuilder decodeBase64(Base64.Decoder decoder) {
		base64Decoder=decoder;
		return this;
	}
	
	/**
	 * This method creates a simple {@link InputStream} from this builder with all the chosen options.
	 * @return an {@link InputStream}
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 */
	public InputStream asStream() throws IOException {
		return createInputStream();
	}
	
	/**
	 * This method creates a {@link BufferedReader} from this builder with all the chosen options. It uses the default 
	 * {@link Charset} for that.
	 * @return a {@link BufferedReader}
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 */
	public BufferedReader asReader() throws IOException {
		return new BufferedReader(new InputStreamReader(createInputStream(), getCharset()));
	}
	
	/**
	 * This method creates an {@link ObjectInputStream} from this builder with all the chosen options.
	 * @return a {@link ObjectInputStream}
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 */
	public ObjectInputStream asObjects() throws IOException {
		return new ObjectInputStream(new BufferedInputStream(createInputStream()));
	}
	
	/**
	 * This method creates an {@link DataInputStream} from this builder with all the chosen options.
	 * @return a {@link DataInputStream}
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 */
	public DataInputStream asData() throws IOException {
		return new DataInputStream(new BufferedInputStream(createInputStream()));
	}
	
	/**
	 * This method creates an {@link ZipInputStream} from this builder with all the chosen options.
	 * @return a {@link ZipInputStream}
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 */
	public ZipInputStream asZip() throws IOException {
		return new ZipInputStream(new BufferedInputStream(createInputStream()));
	}
	
	/**
	 * This method reads an XML document from the selected source into a {@link Document}.
	 * It uses a default {@link DocumentBuilderFactory} and {@link DocumentBuilder}.
	 * @return the parsed Document
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 * @throws SAXException if any parse errors occur
	 */
	public Document readXML() throws IOException, SAXException {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			return readXML(builder);
		} catch (ParserConfigurationException e) {
			throw new RuntimeException("DocumentBuilder creation failed", e);
		}
	}
	/**
	 * This method reads an XML document from the selected source into a {@link Document}.
	 * @param documentBuilder a fresh {@link DocumentBuilder} that is used as parser
	 * @return the parsed Document
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 * @throws SAXException if any parse errors occur
	 */
	public Document readXML(DocumentBuilder documentBuilder) throws IOException, SAXException {
		try(BufferedInputStream in=new BufferedInputStream(this.asStream())) {
			return documentBuilder.parse(in);
		}
	}
	
	/**
	 * This method reads the complete input in a String. Lines are seperated with a single '\n'.
	 * @return a String containing the whole content of the file
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 */
	public String readAll() throws IOException {
		try(BufferedReader in=this.asReader()) {
			StringBuilder sb=new StringBuilder();
			String l;
			while((l=in.readLine())!=null) {
				sb.append(l).append('\n');
			}
			if(sb.length()==0)
				return "";
			else
				return sb.substring(0, sb.length()-1);
		}
	}
	
	/**
	 * This method simply reads the first object from the input using an 
	 * {@link ObjectInputStream} and returns it. 
	 * @param <T> the expected type of the read object
	 * @return the read Object
	 * @throws ClassNotFoundException if the class of a serialized object cannot be found.
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 */
	public <T> T readObject() throws ClassNotFoundException, IOException {
		try(ObjectInputStream in=this.asObjects()) {
			return (T)in.readObject();
		}
	}
	
	/**
	 * This method reads as many objects from the input using an 
	 * {@link ObjectInputStream} as it can and returns them. Be aware that
	 * this method will throw an exception if there are other information in the stream
	 * than objects (e.g. primitives). 
	 * @param <T> the expected supertype of the read objects
	 * @return a list of read objects
	 * @throws ClassNotFoundException if the class of a serialized object cannot be found.
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 */
	public <T> List<T> readObjects() throws ClassNotFoundException, IOException {
		try(ObjectInputStream in=this.asObjects()) {
			int length = in.readInt();
			ArrayList<T> objects=new ArrayList<>(length);
			for(int i=0;i<length;i++) {
				try {
					objects.add((T)in.readObject());
				} catch(ClassNotFoundException e) {
					throw new ClassNotFoundException("Error while trying to deserialize object "+i, e);
				} catch(IOException e) {
					throw new IOException("Error while trying to deserialize object "+i, e);
				}
			}
			return objects;
		}
	}
	
	/**
	 * This method reads the complete input in a {@link List} of Strings. Each element of the list 
	 * represents one line of the source document.
	 * @return a list of lines containing the whole content of the file
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 */
	public List<String> readLines() throws IOException {
		try(BufferedReader in=this.asReader()) {
			ArrayList<String> list=new ArrayList<>();
			String l;
			while((l=in.readLine())!=null)
				list.add(l);
			return list;
		}
	}
	
	/**
	 * This method reads to the given array from offset to offset+length from the defined {@link InputStream} and closes it.
	 * @param bytes the byte array to write
	 * @param offset the offset in the byte array
	 * @param length the length of the bytes to read
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 */
	public void readBytes(byte[] bytes, int offset, int length) throws IOException {
		try(InputStream in = this.asStream()) {
			in.read(bytes, offset, length);
		}
	}
	
	/**
	 * This method reads to the given array completely from the {@link InputStream} and closes it.
	 * @param bytes the byte array to read
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 */
	public void readBytes(byte[] bytes) throws IOException {
		readBytes(bytes, 0, bytes.length);
	}
	
	/**
	 * This method reads the given number of bytes from the defined {@link InputStream}, closes it, and returns the read bytes.
	 * @param length the number of bytes to read
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 */
	public byte[] readBytes(int length) throws IOException {
		byte[] bytes = new byte[length];
		readBytes(bytes);
		return bytes;
	}
	
	/**
	 * Copies the content of this {@link InputStream} to the given {@link OutputStream}.
	 * This does not close the {@link OutputStream}.
	 * @param out the {@link OutputStream} to copy to
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 */
	public void copyTo(OutputStream out) throws IOException {
		try(InputStream in=createInputStream()) {
			byte[] buffer = new byte[8192];
			int len = 0;
			while ((len=in.read(buffer)) != -1)
				out.write(buffer, 0, len);
		}
	}
	
	/**
	 * Copies the content of this {@link BufferedReader} to the given {@link Writer}.
	 * This does not close the {@link Writer}.
	 * @param out the {@link Writer} to copy to
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 */
	public void copyTo(Writer out) throws IOException {
		try(BufferedReader in=this.asReader()) {
			char[] buffer = new char[4096];
			int len = 0;
			while ((len=in.read(buffer)) != -1)
				out.write(buffer, 0, len);
		}
	}
	
	/**
	 * This method reads the complete input in a {@link Stream} of Strings. Each element of the stream 
	 * represents one line of the source document. The stream is lazily populated. Be aware that the created
	 * reader is only closed if the created stream is closed.
	 * @return a {@link Stream} containing the lines of this input
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 */
	public Stream<String> streamLines() throws IOException {
		BufferedReader in=this.asReader();
		return in.lines().onClose(() ->  {
			try {
				in.close();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}	
		});
	}

	private InputStream createInputStream() throws IOException {
		InputStream stream=source.openStream();
		if(decompress) {
			if(decompressionWrapper!=null)
				stream=decompressionWrapper.wrap(stream);
			else if(source.hasName() && CompressorRegistry.getInstance().canWrapInput(source.getName()))
				stream=CompressorRegistry.getInstance().wrap(source.getName(), stream);
			else
				stream=new InflaterInputStream(stream);
		}
		if(base64Decoder!=null)
			stream=base64Decoder.wrap(stream);
		return stream;
	}
	
	/**
	 * @return the source used by this InBuilder
	 */
	public Source getSource() {
		return source;
	}
}
