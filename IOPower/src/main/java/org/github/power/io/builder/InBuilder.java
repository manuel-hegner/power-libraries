package org.github.power.io.builder;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.DeflaterInputStream;
import java.util.zip.ZipInputStream;

import org.github.power.io.builder.sources.Source;
import org.github.power.io.helper.CompressorRegistry;


public class InBuilder {
	private Source source;
	private boolean uncompress=false;
	private boolean decodeBase64=false;

	public InBuilder(Source source) {
		this.source=source;
	}
	
	public InBuilder uncompress() {
		uncompress=true;
		return this;
	}
	
	public InBuilder decodeBase64() {
		decodeBase64=true;
		return this;
	}
	
	public InputStream toStream() throws IOException {
		return createInputStream();
	}
	
	public BufferedReader toReader() throws IOException {
		return new BufferedReader(new InputStreamReader(createInputStream()));
	}
	
	public BufferedReader toReader(Charset charset) throws IOException {
		return new BufferedReader(new InputStreamReader(createInputStream(), charset));
	}
	
	public ObjectInputStream toObjects() throws IOException {
		return new ObjectInputStream(new BufferedInputStream(createInputStream()));
	}
	
	public DataInputStream toData() throws IOException {
		return new DataInputStream(new BufferedInputStream(createInputStream()));
	}
	
	public ZipInputStream toZip() throws IOException {
		return new ZipInputStream(new BufferedInputStream(createInputStream()));
	}
	
	public String readAll() throws IOException {
		return readAll(Charset.defaultCharset());
	}
	
	public String readAll(Charset charset) throws IOException {
		try(BufferedReader in=this.toReader(charset)) {
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
	
	public List<String> readLines() throws IOException {
		return readLines(Charset.defaultCharset());
	}
	
	public List<String> readLines(Charset charset) throws IOException {
		try(BufferedReader in=this.toReader(charset)) {
			ArrayList<String> list=new ArrayList<>();
			String l;
			while((l=in.readLine())!=null)
				list.add(l);
			return list;
		}
	}
	
	public Stream<String> streamLines() throws IOException {
		return streamLines(Charset.defaultCharset());
	}
	
	public Stream<String> streamLines(Charset charset) throws IOException {
		try(BufferedReader in=this.toReader(charset)) {
			return in.lines();
		}
	}

	@SuppressWarnings("resource")
	private InputStream createInputStream() throws IOException {
		InputStream stream=source.openStream();
		if(uncompress) {
			if(source.hasName() && CompressorRegistry.getInstance().canWrap(source.getName()))
				stream=CompressorRegistry.getInstance().wrap(source.getName(), stream);
			else
				stream=new DeflaterInputStream(stream);
		}
		if(decodeBase64)
			stream=Base64.getDecoder().wrap(stream);
		return stream;
	}
	
	public Source getSource() {
		return source;
	}
}
