package com.github.powerlibraries.io.builder;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.UncheckedIOException;
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
