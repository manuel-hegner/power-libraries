package com.github.powerlibraries.io.builder;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.Iterator;
import java.util.Objects;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import com.github.powerlibraries.io.builder.targets.ByteArrayTarget;
import com.github.powerlibraries.io.helper.byteout.BADataOutputStream;
import com.github.powerlibraries.io.helper.byteout.BAObjectOutputStream;
import com.github.powerlibraries.io.helper.byteout.BAOutputStream;
import com.github.powerlibraries.io.helper.byteout.BAPrintWriter;
import com.github.powerlibraries.io.helper.byteout.BAWriter;
import com.github.powerlibraries.io.helper.byteout.BAZipOutputStream;

/**
 * This builder is used to create an output chain. In contrast to the normal {@link BaseOutBuilder} this class
 * returns custom writers and streams that allow you to directly access the underlying byte buffer that is 
 * used as a final element in the chain. 
 * 
 * @see BaseOutBuilder
 * @author Manuel Hegner
 *
 */
public class ByteOutBuilder extends BaseOutBuilder<ByteOutBuilder> {

	private ByteArrayTarget target;
	
	public ByteOutBuilder() {
		super(new ByteArrayTarget());
		this.target=(ByteArrayTarget) super.getTarget();
	}
	
	@Override
	public BAOutputStream fromStream() throws IOException {
		return new BAOutputStream(createOutputStream(), target.getLastStream());
	}
	
	@Override
	public BAWriter fromWriter() throws IOException {
		return new BAWriter(new OutputStreamWriter(createOutputStream(), getCharset()), target.getLastStream());
	}
	
	@Override
	public BAPrintWriter fromPrint() throws IOException {
		return new BAPrintWriter(super.fromWriter(), target.getLastStream());
	}
	
	@Override
	public BAObjectOutputStream fromObjects() throws IOException {
		return new BAObjectOutputStream(new BufferedOutputStream(createOutputStream()), target.getLastStream());
	}
	
	@Override
	public BADataOutputStream fromData() throws IOException {
		return new BADataOutputStream(new BufferedOutputStream(createOutputStream()), target.getLastStream());
	}
	
	@Override
	public BAZipOutputStream fromZip() throws IOException {
		return new BAZipOutputStream(new BufferedOutputStream(createOutputStream()), target.getLastStream());
	}
	
	
	
	
	//WRITE METHODS THAT RETURN THE BYTE ARRAYS
	/**
	 * This method writes the given Object to the output by calling {@link Objects#toString()}.
	 * @param o the object to write to the output
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 * @return the written byte array
	 */
	public byte[] write(Object o) throws IOException {
		try(BAWriter out=this.fromWriter()) {
			out.write(Objects.toString(o));
			return out.toByteArray();
		}
	}
	
	/**
	 * This method writes the given {@link Iterable} to the output by calling {@link Objects#toString()} on each
	 * of the elements and writing them on seperate lines.
	 * @param iterable the {@link Iterable} to write to the output
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 * @return the written byte array
	 */
	public byte[] writeLines(Iterable<?> iterable) throws IOException {
		return writeLines(iterable.iterator());
	}
	
	/**
	 * This method writes the given array to the output by calling {@link Objects#toString()} on each
	 * of the elements and writing them on seperate lines.
	 * @param array the array to write to the output
	 * @param <T> the array element type
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 * @return the written byte array
	 */
	public <T> byte[] writeLines(T[] array) throws IOException {
		try(BAWriter out=this.fromWriter()) {
			for(int i=0;i<array.length;i++) {
				if(i>0)
					out.newLine();
				out.write(Objects.toString(array[i]));
			}
			return out.toByteArray();
		}
	}
	
	/**
	 * This method writes the given remaining content of the {@link Iterator} to the output by calling 
	 * {@link Objects#toString()} on each of the elements and writing them on seperate lines.
	 * @param iterator the {@link Iterator} to write to the output
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 * @return the written byte array
	 */
	public byte[] writeLines(Iterator<?> iterator) throws IOException {
		try(BAWriter out=this.fromWriter()) {
			while(iterator.hasNext()) {
				out.write(Objects.toString(iterator.next()));
				if(iterator.hasNext())
					out.newLine();
			}
			return out.toByteArray();
		}
	}
	
	/**
	 * This method writes the given XML document to the output. It uses a 
	 * default {@link TransformerFactory} and {@link Transformer}.
	 * @param document the document to write
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 * @throws TransformerException if an unrecoverable error occurs during the course of the transformation
	 * @return the written byte array
	 */
	public byte[] writeXML(Document document) throws IOException, TransformerException {
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		return writeXML(document, transformer);
	}
	
	/**
	 * This method writes the given XML document to the output.
	 * @param document the document to write
	 * @param transformer the transformer to use
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 * @throws TransformerException if an unrecoverable error occurs during the course of the transformation
	 * @return the written byte array
	 */
	public byte[] writeXML(Document document, Transformer transformer) throws IOException, TransformerException {
		try(BAOutputStream out=this.fromStream()) {
			transformer.transform(new DOMSource(document), new StreamResult(out));
			return out.toByteArray();
		}
	}
	
	/**
	 * Copies the content of the given {@link InputStream} to this output
	 * @param in the {@link InputStream} to copy from
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 * @return the written byte array
	 */
	public byte[] copyFrom(InputStream in) throws IOException {
		try(BAOutputStream out=this.fromStream();
				InputStream input=in;) {
			byte[] buffer = new byte[8192];
			int len = 0;
			while ((len=input.read(buffer)) != -1)
				out.write(buffer, 0, len);
			return out.toByteArray();
		}
	}
	
	/**
	 * Copies the content of the given {@link Reader} to this output
	 * @param in the {@link Reader} to copy from
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 * @return the written byte array
	 */
	public byte[] copyFrom(Reader in) throws IOException {
		try(BAWriter out=this.fromWriter();
				Reader input=in;) {
			char[] buffer = new char[4096];
			int len = 0;
			while ((len=input.read(buffer)) != -1)
				out.write(buffer, 0, len);
			return out.toByteArray();
		}
	}
}
