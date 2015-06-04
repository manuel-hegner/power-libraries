package com.github.powerlibraries.io.builder;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import com.github.powerlibraries.io.builder.targets.ByteArrayTarget;
import com.github.powerlibraries.io.helper.byteout.BADataOutputStream;
import com.github.powerlibraries.io.helper.byteout.BAObjectOutputStream;
import com.github.powerlibraries.io.helper.byteout.BAOutputStream;
import com.github.powerlibraries.io.helper.byteout.BAPrintWriter;
import com.github.powerlibraries.io.helper.byteout.BAWriter;
import com.github.powerlibraries.io.helper.byteout.BAZipOutputStream;

/**
 * This builder is used to create an output chain. In contrast to the normal {@link OutBuilder} this class
 * returns custom writers and streams that allow you to directly access the underlying byte buffer that is 
 * used as a final element in the chain. 
 * 
 * @see OutBuilder
 * @author Manuel Hegner
 *
 */
public class ByteOutBuilder extends OutBuilder {

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
		return new BAWriter(new OutputStreamWriter(createOutputStream()), target.getLastStream());
	}
	
	@Override
	public BAWriter fromWriter(Charset charset) throws IOException {
		return new BAWriter(new OutputStreamWriter(createOutputStream(), charset), target.getLastStream());
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
