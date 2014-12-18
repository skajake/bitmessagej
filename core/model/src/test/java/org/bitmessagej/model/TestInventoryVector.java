package org.bitmessagej.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import org.apache.commons.codec.binary.Hex;
import org.codehaus.preon.Codec;
import org.codehaus.preon.Codecs;
import org.codehaus.preon.channel.BitChannel;
import org.codehaus.preon.channel.OutputStreamBitChannel;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestInventoryVector {

	private static Codec<InventoryVector> codec;
	
	@BeforeClass
	public static void init() {
		codec = Codecs.create(InventoryVector.class);
	}
	
	private String hexForInventoryVector(InventoryVector inventoryVector) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		BitChannel channel = new OutputStreamBitChannel(buffer);
		codec.encode(inventoryVector, channel, null);
		return Hex.encodeHexString(buffer.toByteArray());
	}
	
	@Test
	public void testSerialize() throws NoSuchAlgorithmException, NoSuchProviderException, IOException {
		String test = "Hello World";
		InventoryVector vector = new InventoryVector();
		vector.hashBytes(test.getBytes());
		String hex = hexForInventoryVector(vector);
		Assert.assertEquals("b26b99cdce38b1b470f99297ac229cdd98bcad7b3fbb9d04b416f590e65cd2fd", hex);
	}
	
}
