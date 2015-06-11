package org.github.power.io;

import java.awt.Point;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Base64;

import org.junit.Assert;
import org.junit.Test;

import com.github.powerlibraries.io.Out;
import com.github.powerlibraries.io.helper.byteout.BAObjectOutputStream;

public class OutTests {
	
	@Test
	public void testObjectOut() throws IOException {
		//create expected
		String expected;
		try(ByteArrayOutputStream bos=new ByteArrayOutputStream();
			ObjectOutputStream out=new ObjectOutputStream(bos)) {
			
			out.writeObject(new Point(4, 2));
			out.close();
			expected=Base64.getEncoder().encodeToString(bos.toByteArray());
		}
		
		try(BAObjectOutputStream out=Out.bytes().encodeBase64().asObjects()) {
			out.writeObject(new Point(4, 2));
			out.close();
			Assert.assertEquals(expected, out.toString());
		}
	}
}
