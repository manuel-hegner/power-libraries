package org.github.power.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.github.powerlibraries.io.In;
import com.github.powerlibraries.io.Out;

public class SerializationTests {
	@Test
	public void complexTest() throws IOException, ClassNotFoundException {
		List<Object> l = new ArrayList<>();
		l.add(Arrays.asList(new Integer[]{4,649,-1234}));
		l.add("tEST");
		l.add(new File("test.txt"));
		l.add(Integer.valueOf(12341));
		
		byte[] bytes = Out.bytes().compress().writeObjects(l.toArray());
		List<Object> copy = In.bytes(bytes).decompress().readObjects();
		
		Assert.assertEquals(l, copy);
	}
}
