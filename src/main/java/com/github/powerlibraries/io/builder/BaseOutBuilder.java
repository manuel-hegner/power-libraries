package com.github.powerlibraries.io.builder;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.ZipOutputStream;

import com.github.powerlibraries.io.builder.targets.Target;
import com.github.powerlibraries.io.functions.OutputStreamWrapper;
import com.github.powerlibraries.io.functions.WriterWrapper;
import com.github.powerlibraries.io.helper.CompressorRegistry;

/**
 * This builder is used to create an output chain.
 * 
 * @author Manuel Hegner
 *
 */
@SuppressWarnings("unchecked")
public abstract class BaseOutBuilder <SELF extends BaseOutBuilder<SELF>> extends CharsetHolder<SELF> {
	private Target target;
	private boolean compress=false;
	private Base64.Encoder base64Encoder=null;
	private OutputStreamWrapper compressionWrapper;
	private List<OutputStreamWrapper> streamWrappers;
	private List<WriterWrapper> writerWrappers;

	public BaseOutBuilder(Target target) {
		this.target=target;
	}
	
	/**
	 * Adds a wrapper around the generated OutputStream before creating a Writer or 
	 * a special type of output. This wrapper will be applied before compression. 
	 * @param wrapper the wrapper to apply to the generated OutputStream
	 * @return this builder
	 */
	public SELF wrap(OutputStreamWrapper wrapper) {
		if(streamWrappers==null)
			streamWrappers=new ArrayList<>();
		streamWrappers.add(wrapper);
		return (SELF)this;
	}
	
	/**
	 * Adds a wrapper around the generated Writer before creating a Writer or 
	 * a special type of output. This wrapper will only be applied if the created 
	 * output uses Writers. 
	 * @param wrapper the wrapper to apply to the generated Writer
	 * @return this builder
	 */
	public SELF wrap(WriterWrapper wrapper) {
		if(writerWrappers==null)
			writerWrappers=new ArrayList<>();
		writerWrappers.add(wrapper);
		return (SELF)this;
	}
	
	/**
	 * This method will tell the builder to compress the bytes. The returned writer or stream will contain an appropriate
	 * compressor. If the defined target specifies a name with a file ending, the builder will try to 
	 * use the appropriate compressor for the file extension. If there is no extension or it is unknown this
	 * simply adds a {@link DeflaterOutputStream} to the chain.
	 * 
	 * If you want to add file extensions to the automatic selection see {@link CompressorRegistry#registerWrapper}.
	 * @return this builder
	 */
	public SELF compress() {
		compress=true;
		return (SELF)this;
	}
	
	/**
	 * This method will tell the builder to compress the bytes using the given compression, e.g. <code>GZipOutputStream::new</code>.
	 * @param wrapper the wrapper used to compress the bytes
	 * @return this builder
	 */
	public SELF compress(OutputStreamWrapper wrapper) {
		compress=true;
		compressionWrapper=wrapper;
		return (SELF)this;
	}
	
	/**
	 * This method will add a {@link Base64.Encoder} to this chain.
	 * @return this builder
	 */
	public SELF encodeBase64() {
		base64Encoder=Base64.getEncoder();
		return (SELF)this;
	}
	
	/**
	 * This method will add a {@link Base64.Encoder} to this chain.
	 * @param encoder the specific Base64 encoder that should be used.
	 * @return this builder
	 */
	public SELF encodeBase64(Base64.Encoder encoder) {
		base64Encoder=encoder;
		return (SELF)this;
	}
	
	/**
	 * This method creates a simple {@link OutputStream} from this builder with all the chosen options.
	 * @return an {@link OutputStream}
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 */
	public OutputStream asStream() throws IOException {
		return createOutputStream();
	}
	
	/**
	 * This method creates a {@link BufferedWriter} from this builder with all the chosen options. It uses the default 
	 * {@link Charset} for that.
	 * @return a {@link BufferedWriter}
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 */
	public BufferedWriter asWriter() throws IOException {
		Writer writer = new OutputStreamWriter(createOutputStream(), getCharset());
		if(writerWrappers!=null) {
			for(WriterWrapper w:writerWrappers)
				writer=w.wrap(writer);
		}
		return new BufferedWriter(writer);
	}
	
	/**
	 * This method creates a {@link PrintWriter} from this builder with all the chosen options.
	 * @return a {@link PrintWriter}
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 */
	public PrintWriter asPrint() throws IOException {
		return new PrintWriter(asWriter());
	}
	
	/**
	 * This method creates an {@link ObjectOutputStream} from this builder with all the chosen options.
	 * @return an {@link ObjectOutputStream}
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 */
	public ObjectOutputStream asObjects() throws IOException {
		return new ObjectOutputStream(new BufferedOutputStream(createOutputStream()));
	}
	
	/**
	 * This method creates a {@link DataOutputStream} from this builder with all the chosen options.
	 * @return a {@link DataOutputStream}
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 */
	public DataOutputStream asData() throws IOException {
		return new DataOutputStream(new BufferedOutputStream(createOutputStream()));
	}
	
	/**
	 * This method creates a {@link ZipOutputStream} from this builder with all the chosen options.
	 * @return a {@link ZipOutputStream}
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 */
	public ZipOutputStream asZip() throws IOException {
		return new ZipOutputStream(new BufferedOutputStream(createOutputStream()));
	}

	/**
	 * This method wraps the OutputStream created by the target object with other streams depending on what options
	 * the user chose.
	 * @return an OutputStream
	 * @throws IOException if any element of the chain throws an {@link IOException}
	 */
	protected OutputStream createOutputStream() throws IOException {
		OutputStream stream=target.openStream();
		if(base64Encoder!=null)
			stream=base64Encoder.wrap(stream);
		if(compress) {
			if(compressionWrapper!=null)
				stream=compressionWrapper.wrap(stream);
			else if(target.hasName() && CompressorRegistry.getInstance().canWrapOutput(target.getName()))
				stream=CompressorRegistry.getInstance().wrap(target.getName(), stream);
			else
				stream=new DeflaterOutputStream(stream);
		}
		if(streamWrappers!=null) {
			for(OutputStreamWrapper w:streamWrappers)
				stream=w.wrap(stream);
		}
		return stream;
	}
	
	/**
	 * @return the target this builder was created with
	 */
	public Target getTarget() {
		return target;
	}
}
