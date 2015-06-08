package com.github.powerlibraries.io.builder;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import java.util.zip.DeflaterInputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.github.powerlibraries.io.builder.sources.Source;
import com.github.powerlibraries.io.helper.CompressorRegistry;

/**
 * This builder is used to create an input chain.
 * 
 * @author Manuel Hegner
 *
 */
public class InBuilder extends CharsetHolder<InBuilder>{
	private Source source;
	private boolean decompress=false;
	private Base64.Decoder base64Decoder=null;

	public InBuilder(Source source) {
		this.source=source;
	}
	
	/**
	 * This method will tell the builder to decompress the bytes. The returned reader or stream will contain an 
	 * appropriate decompressor. If the defined source specifies a name with a file ending, the builder will try to 
	 * use the appropriate decompressor for the file extension. If there is no extension or it is unknown this
	 * simply adds a {@link DeflaterOutputStream} to the chain.
	 * 
	 * If you want to add file extensions to the automatic selection see {@link CompressorRegistry#registerWrapper}.
	 * @return this builder
	 */
	public InBuilder decompress() {
		decompress=true;
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
	public InputStream toStream() throws IOException {
		return createInputStream();
	}
	
	/**
	 * This method creates a {@link BufferedReader} from this builder with all the chosen options. It uses the default 
	 * {@link Charset} for that.
	 * @return a {@link BufferedReader}
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 */
	public BufferedReader toReader() throws IOException {
		return new BufferedReader(new InputStreamReader(createInputStream(), getCharset()));
	}
	
	/**
	 * This method creates an {@link ObjectInputStream} from this builder with all the chosen options.
	 * @return a {@link ObjectInputStream}
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 */
	public ObjectInputStream toObjects() throws IOException {
		return new ObjectInputStream(new BufferedInputStream(createInputStream()));
	}
	
	/**
	 * This method creates an {@link DataInputStream} from this builder with all the chosen options.
	 * @return a {@link DataInputStream}
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 */
	public DataInputStream toData() throws IOException {
		return new DataInputStream(new BufferedInputStream(createInputStream()));
	}
	
	/**
	 * This method creates an {@link ZipInputStream} from this builder with all the chosen options.
	 * @return a {@link ZipInputStream}
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 */
	public ZipInputStream toZip() throws IOException {
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
		try(BufferedInputStream in=new BufferedInputStream(this.toStream())) {
			return documentBuilder.parse(in);
		}
	}
	
	/**
	 * This method reads the complete input in a String. Lines are seperated with a single '\n'.
	 * @return a String containing the whole content of the file
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 */
	public String readAll() throws IOException {
		try(BufferedReader in=this.toReader()) {
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
	 * This method reads the complete input in a {@link List} of Strings. Each element of the list 
	 * represents one line of the source document.
	 * @return a String containing the whole content of the file
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 */
	public List<String> readLines() throws IOException {
		try(BufferedReader in=this.toReader()) {
			ArrayList<String> list=new ArrayList<>();
			String l;
			while((l=in.readLine())!=null)
				list.add(l);
			return list;
		}
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
	 * Copies the content of this {@link Reader} to the given {@link Writer}.
	 * This does not close the {@link Writer}.
	 * @param out the {@link Writer} to copy to
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 */
	public void copyTo(Writer out) throws IOException {
		try(BufferedReader in=this.toReader()) {
			char[] buffer = new char[4096];
			int len = 0;
			while ((len=in.read(buffer)) != -1)
				out.write(buffer, 0, len);
		}
	}
	
	/**
	 * This method reads the complete input in a {@link Stream} of Strings. Each element of the stream 
	 * represents one line of the source document. The stream is lazily populated. Be aware that the created
	 * reader is only closed if the created stream is fully explored. This means this operation should
	 * not be used with {@link Stream#findAny()} or similar terminal operations or the underlying stream
	 * will not be closed. Use {@link InBuilder#readAll()}.stream() if you require these methods.
	 * @return a String containing the whole content of the file
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 */
	@SuppressWarnings("resource")
	public Stream<String> streamLines() throws IOException {
		BufferedReader in=this.toReader();
		
		Iterator<String> iter = new Iterator<String>() {
            String nextLine = null;

            @Override
            public boolean hasNext() {
                if (nextLine != null) {
                    return true;
                } else {
                    try {
                        nextLine = in.readLine();
                        if(nextLine==null)
                        	in.close();
                        return (nextLine != null);
                    } catch (IOException e) {
                    	try {
							in.close();
						} catch (IOException e1) {}
                        throw new UncheckedIOException(e);
                    }
                }
            }

            @Override
            public String next() {
                if (nextLine != null || hasNext()) {
                    String line = nextLine;
                    nextLine = null;
                    return line;
                } else {
                	try {
						in.close();
					} catch (IOException e1) {}
                    throw new NoSuchElementException();
                }
            }
        };
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(
                iter, Spliterator.ORDERED | Spliterator.NONNULL), false);
		
	}

	@SuppressWarnings("resource")
	private InputStream createInputStream() throws IOException {
		InputStream stream=source.openStream();
		if(decompress) {
			if(source.hasName() && CompressorRegistry.getInstance().canWrapInput(source.getName()))
				stream=CompressorRegistry.getInstance().wrap(source.getName(), stream);
			else
				stream=new DeflaterInputStream(stream);
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
