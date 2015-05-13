package org.github.power.io.builder;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.ZipOutputStream;

import org.github.power.io.builder.targets.Target;
import org.github.power.io.helper.CompressorRegistry;

public class OutBuilder {
	private Target target;
	private boolean compress=false;
	private boolean encodeBase64=false;

	public OutBuilder(Target target) {
		this.target=target;
	}
	
	public OutBuilder compress() {
		compress=true;
		return this;
	}
	
	public OutBuilder encodeBase64() {
		encodeBase64=true;
		return this;
	}
	
	public OutputStream fromStream() throws IOException {
		return wrapOutputStream(target);
	}
	
	public BufferedWriter fromWriter() throws IOException {
		return new BufferedWriter(new OutputStreamWriter(wrapOutputStream(target)));
	}
	
	public BufferedWriter fromWriter(Charset charset) throws IOException {
		return new BufferedWriter(new OutputStreamWriter(wrapOutputStream(target), charset));
	}
	
	public PrintWriter fromPrint() throws IOException {
		return new PrintWriter(fromWriter());
	}
	
	public PrintWriter fromPrint(Charset charset) throws IOException {
		return new PrintWriter(fromWriter(charset));
	}
	
	public ObjectOutputStream fromObjects() throws IOException {
		return new ObjectOutputStream(new BufferedOutputStream(wrapOutputStream(target)));
	}
	
	public DataOutputStream fromData() throws IOException {
		return new DataOutputStream(new BufferedOutputStream(wrapOutputStream(target)));
	}
	
	public ZipOutputStream fromZip() throws IOException {
		return new ZipOutputStream(new BufferedOutputStream(wrapOutputStream(target)));
	}
	
	@SuppressWarnings("resource")
	protected OutputStream wrapOutputStream(Target target) throws IOException {
		OutputStream stream=target.openStream();
		if(encodeBase64)
			stream=Base64.getEncoder().wrap(stream);
		if(compress) {
			if(target.hasName() && CompressorRegistry.getInstance().canWrap(target.getName()))
				stream=CompressorRegistry.getInstance().wrap(target.getName(), stream);
			else
				stream=new DeflaterOutputStream(stream);
		}
		return stream;
	}
	
	public Target getTarget() {
		return target;
	}
}
