package com.github.powerlibraries.io.builder;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Objects;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import com.github.powerlibraries.io.IOConfig;
import com.github.powerlibraries.io.builder.targets.StringTarget;
import com.github.powerlibraries.io.helper.stringout.SBDataOutputStream;
import com.github.powerlibraries.io.helper.stringout.SBObjectOutputStream;
import com.github.powerlibraries.io.helper.stringout.SBOutputStream;
import com.github.powerlibraries.io.helper.stringout.SBPrintWriter;
import com.github.powerlibraries.io.helper.stringout.SBWriter;
import com.github.powerlibraries.io.helper.stringout.SBZipOutputStream;

/**
 * This builder is used to create an output chain. In contrast to the normal {@link OutBuilder} this class
 * returns custom writers and streams that allow you to directly access the underlying byte buffer that is 
 * used as a final element in the chain and to directly return strings. 
 * 
 * @see OutBuilder
 * @author Manuel Hegner
 *
 */
public class StringOutBuilder extends BaseOutBuilder<StringOutBuilder> {

	private StringTarget target;
	
	public StringOutBuilder() {
		super(new StringTarget(IOConfig.getDefaultCharset()));
		this.target=(StringTarget) super.getTarget();
	}
	
	@Override
	public SBOutputStream asStream() throws IOException {
		return new SBOutputStream(createOutputStream(), target.getLastStream());
	}
	
	@Override
	public SBWriter asWriter() throws IOException {
		return new SBWriter(new OutputStreamWriter(createOutputStream(), getCharset()), target.getLastStream());
	}
	
	@Override
	public SBPrintWriter asPrint() throws IOException {
		return new SBPrintWriter(super.asWriter(), target.getLastStream());
	}
	
	@Override
	public SBObjectOutputStream asObjects() throws IOException {
		return new SBObjectOutputStream(new BufferedOutputStream(createOutputStream()), target.getLastStream());
	}
	
	@Override
	public SBDataOutputStream asData() throws IOException {
		return new SBDataOutputStream(new BufferedOutputStream(createOutputStream()), target.getLastStream());
	}
	
	@Override
	public SBZipOutputStream asZip() throws IOException {
		return new SBZipOutputStream(new BufferedOutputStream(createOutputStream()), target.getLastStream());
	}
	
	
	
	
	//WRITE METHODS THAT RETURN THE BYTE ARRAYS
	/**
	 * This method writes the given Object to the output by calling {@link Objects#toString()}.
	 * @param o the object to write to the output
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 * @return the resulting string
	 */
	public String write(Object o) throws IOException {
		try(SBWriter out=this.asWriter()) {
			out.write(Objects.toString(o));
			return out.getResult();
		}
	}
	
	/**
	 * This method writes the given {@link Iterable} to the output by calling {@link Objects#toString()} on each
	 * of the elements and writing them on seperate lines.
	 * @param iterable the {@link Iterable} to write to the output
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 * @return the resulting string
	 */
	public String writeLines(Iterable<?> iterable) throws IOException {
		return writeLines(iterable.iterator());
	}
	
	/**
	 * This method writes the given array to the output by calling {@link Objects#toString()} on each
	 * of the elements and writing them on seperate lines.
	 * @param array the array to write to the output
	 * @param <T> the array element type
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 * @return the resulting string
	 */
	public <T> String writeLines(T[] array) throws IOException {
		try(SBWriter out=this.asWriter()) {
			for(int i=0;i<array.length;i++) {
				if(i>0)
					out.newLine();
				out.write(Objects.toString(array[i]));
			}
			return out.getResult();
		}
	}
	
	/**
	 * This method writes the given remaining content of the {@link Iterator} to the output by calling 
	 * {@link Objects#toString()} on each of the elements and writing them on seperate lines.
	 * @param iterator the {@link Iterator} to write to the output
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 * @return the resulting string
	 */
	public String writeLines(Iterator<?> iterator) throws IOException {
		try(SBWriter out=this.asWriter()) {
			while(iterator.hasNext()) {
				out.write(Objects.toString(iterator.next()));
				if(iterator.hasNext())
					out.newLine();
			}
			return out.getResult();
		}
	}
	
	/**
	 * This method writes the given XML document to the output. It uses a 
	 * default {@link TransformerFactory} and {@link Transformer}.
	 * @param document the document to write
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 * @throws TransformerException if an unrecoverable error occurs during the course of the transformation
	 * @return the resulting string
	 */
	public String writeXML(Document document) throws IOException, TransformerException {
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		return writeXML(document, transformer);
	}
	
	/**
	 * This method writes the given XML document to the output.
	 * @param document the document to write
	 * @param transformer the transformer to use
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 * @throws TransformerException if an unrecoverable error occurs during the course of the transformation
	 * @return the resulting string
	 */
	public String writeXML(Document document, Transformer transformer) throws IOException, TransformerException {
		try(SBOutputStream out=this.asStream()) {
			transformer.transform(new DOMSource(document), new StreamResult(out));
			return out.getResult();
		}
	}
	
	/**
	 * Copies the content of the given {@link InputStream} to this output
	 * @param in the {@link InputStream} to copy from
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 * @return the resulting string
	 */
	public String copyFrom(InputStream in) throws IOException {
		try(SBOutputStream out=this.asStream();
				InputStream input=in;) {
			byte[] buffer = new byte[8192];
			int len = 0;
			while ((len=input.read(buffer)) != -1)
				out.write(buffer, 0, len);
			return out.getResult();
		}
	}
	
	/**
	 * Copies the content of the given {@link Reader} to this output
	 * @param in the {@link Reader} to copy from
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 * @return the resulting string
	 */
	public String copyFrom(Reader in) throws IOException {
		try(SBWriter out=this.asWriter();
				Reader input=in;) {
			char[] buffer = new char[4096];
			int len = 0;
			while ((len=input.read(buffer)) != -1)
				out.write(buffer, 0, len);
			return out.getResult();
		}
	}
	
	/**
	 * This method simply writes the given object to the underlying output
	 * using an {@link ObjectOutputStream}.
	 * @param object the object to serialize
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 * @return the resulting string
	 */
	public String writeObject(Object object) throws IOException {
		try(SBObjectOutputStream out=this.asObjects()) {
			out.writeObject(object);
			return out.getResult();
		}
	}
	
	/**
	 * This method writes the given objects to the underlying output
	 * using an {@link ObjectOutputStream}. It does this by simply calling
	 * {@link ObjectOutputStream#writeObject(Object)} with each given object.
	 * @param objects the objects to serialize
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 * @return the resulting string
	 */
	public String writeObjects(Object... objects) throws IOException {
		try(SBObjectOutputStream out=this.asObjects()) {
			for(Object o:objects)
				out.writeObject(o);
			return out.getResult();
		}
	}
	
	@Override
	protected void setCharset(Charset charset) {
		super.setCharset(charset);
		target.getLastStream().setCharset(charset);
	}
}
