package com.github.powerlibraries.io.builder;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Iterator;
import java.util.Objects;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import com.github.powerlibraries.io.builder.targets.Target;

public class OutBuilder extends BaseOutBuilder<OutBuilder> {

	public OutBuilder(Target target) {
		super(target);
	}

	/**
	 * This method writes the given Object to the output by calling {@link Objects#toString()}.
	 * @param o the object to write to the output
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 */
	public void write(Object o) throws IOException {
		try(BufferedWriter out=this.fromWriter()) {
			out.write(Objects.toString(o));
		}
	}
	
	/**
	 * This method writes the given {@link Iterable} to the output by calling {@link Objects#toString()} on each
	 * of the elements and writing them on seperate lines.
	 * @param iterable the {@link Iterable} to write to the output
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 */
	public void writeLines(Iterable<?> iterable) throws IOException {
		writeLines(iterable.iterator());
	}
	
	/**
	 * This method writes the given array to the output by calling {@link Objects#toString()} on each
	 * of the elements and writing them on seperate lines.
	 * @param array the array to write to the output
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 */
	public <T> void writeLines(T[] array) throws IOException {
		try(BufferedWriter out=this.fromWriter()) {
			for(int i=0;i<array.length;i++) {
				if(i>0)
					out.newLine();
				out.write(Objects.toString(array[i]));
			}
		}
	}
	
	/**
	 * This method writes the given remaining content of the {@link Iterator} to the output by calling 
	 * {@link Objects#toString()} on each of the elements and writing them on seperate lines.
	 * @param iterator the {@link Iterator} to write to the output
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 */
	public void writeLines(Iterator<?> iterator) throws IOException {
		try(BufferedWriter out=this.fromWriter()) {
			while(iterator.hasNext()) {
				out.write(Objects.toString(iterator.next()));
				if(iterator.hasNext())
					out.newLine();
			}
		}
	}
	
	/**
	 * This method writes the given XML document to the output. It uses a 
	 * default {@link TransformerFactory} and {@link Transformer}.
	 * @param document the document to write
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 * @throws TransformerException if an unrecoverable error occurs during the course of the transformation
	 */
	public void writeXML(Document document) throws IOException, TransformerException {
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		writeXML(document, transformer);
	}
	
	/**
	 * This method writes the given XML document to the output.
	 * @param document the document to write
	 * @param transformer the transformer to use
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 * @throws TransformerException if an unrecoverable error occurs during the course of the transformation
	 */
	public void writeXML(Document document, Transformer transformer) throws IOException, TransformerException {
		try(BufferedOutputStream out=new BufferedOutputStream(createOutputStream())) {
			
			transformer.transform(new DOMSource(document), new StreamResult(out));
		}
	}
	
	/**
	 * Copies the content of the given {@link InputStream} to this output
	 * @param in the {@link InputStream} to copy from
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 */
	public void copyFrom(InputStream in) throws IOException {
		try(OutputStream out=this.fromStream();
				InputStream input=in;) {
			byte[] buffer = new byte[8192];
			int len = 0;
			while ((len=input.read(buffer)) != -1)
				out.write(buffer, 0, len);
		}
	}
	
	/**
	 * Copies the content of the given {@link Reader} to this output
	 * @param in the {@link Reader} to copy from
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 */
	public void copyFrom(Reader in) throws IOException {
		try(BufferedWriter out=this.fromWriter();
				Reader input=in;) {
			char[] buffer = new char[4096];
			int len = 0;
			while ((len=input.read(buffer)) != -1)
				out.write(buffer, 0, len);
		}
	}
}
