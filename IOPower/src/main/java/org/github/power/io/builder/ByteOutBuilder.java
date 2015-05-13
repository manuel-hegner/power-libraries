package org.github.power.io.builder;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import org.github.power.io.builder.targets.ByteArrayTarget;
import org.github.power.io.builder.targets.Target;
import org.github.power.io.helper.byteout.BADataOutputStream;
import org.github.power.io.helper.byteout.BAObjectOutputStream;
import org.github.power.io.helper.byteout.BAOutputStream;
import org.github.power.io.helper.byteout.BAPrintWriter;
import org.github.power.io.helper.byteout.BAWriter;
import org.github.power.io.helper.byteout.BAZipOutputStream;

public class ByteOutBuilder extends OutBuilder {

	private ByteArrayTarget target; 
	
	public ByteOutBuilder() {
		super(new ByteArrayTarget());
		this.target=(ByteArrayTarget) super.getTarget();
	}
	
	@Override
	public BAOutputStream fromStream() throws IOException {
		return new BAOutputStream(wrapOutputStream(target), target.getLastStream());
	}
	
	@Override
	public BAWriter fromWriter() throws IOException {
		return new BAWriter(new OutputStreamWriter(wrapOutputStream(target)), target.getLastStream());
	}
	
	@Override
	public BAWriter fromWriter(Charset charset) throws IOException {
		return new BAWriter(new OutputStreamWriter(wrapOutputStream(target), charset), target.getLastStream());
	}
	
	@Override
	public BAPrintWriter fromPrint() throws IOException {
		return new BAPrintWriter(super.fromWriter(), target.getLastStream());
	}
	
	@Override
	public BAPrintWriter fromPrint(Charset charset) throws IOException {
		return new BAPrintWriter(super.fromWriter(charset), target.getLastStream());
	}
	
	@Override
	public BAObjectOutputStream fromObjects() throws IOException {
		return new BAObjectOutputStream(new BufferedOutputStream(wrapOutputStream(target)), target.getLastStream());
	}
	
	@Override
	public BADataOutputStream fromData() throws IOException {
		return new BADataOutputStream(new BufferedOutputStream(wrapOutputStream(target)), target.getLastStream());
	}
	
	@Override
	public BAZipOutputStream fromZip() throws IOException {
		return new BAZipOutputStream(new BufferedOutputStream(wrapOutputStream(target)), target.getLastStream());
	}
	
	@Override
	public ByteOutBuilder compress() {
		super.compress();
		return this;
	}
	
	@Override
	public ByteOutBuilder encodeBase64() {
		super.encodeBase64();
		return this;
	}
}
