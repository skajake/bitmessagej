package org.bitmessage.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.codehaus.preon.Codec;
import org.codehaus.preon.Codecs;
import org.codehaus.preon.DecodingException;
import org.codehaus.preon.DefaultBuilder;
import org.codehaus.preon.buffer.DefaultBitBuffer;
import org.codehaus.preon.channel.BitChannel;
import org.codehaus.preon.channel.OutputStreamBitChannel;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestVariableLengthInteger {
	
	private static Codec<VariableLengthInteger> codec;
	
	@BeforeClass
	public static void init() {
		codec = Codecs.create(VariableLengthInteger.class);
	}
	
	private String hexForVariableLengthInteger(VariableLengthInteger variableLengthInteger) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		BitChannel channel = new OutputStreamBitChannel(buffer);
		codec.encode(variableLengthInteger, channel, null);
		return Hex.encodeHexString(buffer.toByteArray());
	}
	
	private VariableLengthInteger variableLengthIntegerForHex(String hex) throws DecodingException, DecoderException {
		ByteBuffer buffer = ByteBuffer.wrap(Hex.decodeHex(hex.toCharArray()));
		DefaultBitBuffer bitBuffer = new DefaultBitBuffer(buffer);
		return codec.decode(bitBuffer, null, new DefaultBuilder());
	}

	@Test
	public void testSerializeInt8() throws IOException {
		VariableLengthInteger variableLengthInteger = new VariableLengthInteger();
		variableLengthInteger.setValue(10);
		Assert.assertEquals("0a", hexForVariableLengthInteger(variableLengthInteger));
	}
	
	@Test
	public void testDeserializeInt8() throws IOException, DecoderException, DecodingException {
		Assert.assertEquals(10, variableLengthIntegerForHex("0a").getValue());
	}
	
	@Test
	public void testSerializeInt0xfc() throws IOException {
		VariableLengthInteger variableLengthInteger = new VariableLengthInteger();
		variableLengthInteger.setValue(0xfc);
		Assert.assertEquals("fc", hexForVariableLengthInteger(variableLengthInteger));
	}
	
	@Test
	public void testDeserializeInt0xfc() throws IOException, DecoderException, DecodingException {
		Assert.assertEquals(0xfc, variableLengthIntegerForHex("fc").getValue());
	}
	
	@Test
	public void testSerializeInt0xfd() throws IOException {
		VariableLengthInteger variableLengthInteger = new VariableLengthInteger();
		variableLengthInteger.setValue(0xfd);
		Assert.assertEquals("fd00fd", hexForVariableLengthInteger(variableLengthInteger));
	}
	
	@Test
	public void testDeserializeInt0xfd() throws IOException, DecodingException, DecoderException {
		Assert.assertEquals(0xfd, variableLengthIntegerForHex("fd00fd").getValue());
	}
	
	@Test
	public void testSerializeInt0xfe() throws IOException {
		VariableLengthInteger variableLengthInteger = new VariableLengthInteger();
		variableLengthInteger.setValue(0xfe);
		Assert.assertEquals("fd00fe", hexForVariableLengthInteger(variableLengthInteger));
	}
	
	@Test
	public void testDeserializeInt0xfe() throws IOException, DecodingException, DecoderException {
		Assert.assertEquals(0xfe, variableLengthIntegerForHex("fd00fe").getValue());
	}
	
	@Test
	public void testSerializeInt0xff() throws IOException {
		VariableLengthInteger variableLengthInteger = new VariableLengthInteger();
		variableLengthInteger.setValue(0xff);
		Assert.assertEquals("fd00ff", hexForVariableLengthInteger(variableLengthInteger));
	}
	
	@Test
	public void testDeserializeInt0xff() throws IOException, DecodingException, DecoderException {
		Assert.assertEquals(0xff, variableLengthIntegerForHex("fd00ff").getValue());
	}
	
	@Test
	public void testSerializeInt16() throws IOException {
		VariableLengthInteger variableLengthInteger = new VariableLengthInteger();
		variableLengthInteger.setValue(310);
		Assert.assertEquals("fd0136", hexForVariableLengthInteger(variableLengthInteger));
	}
	
	@Test
	public void testDeserializeInt16() throws IOException, DecodingException, DecoderException {
		Assert.assertEquals(310, variableLengthIntegerForHex("fd0136").getValue());
	}
	
	@Test
	public void testSerializeInt0xfffe() throws IOException {
		VariableLengthInteger variableLengthInteger = new VariableLengthInteger();
		variableLengthInteger.setValue(0xfffe);
		Assert.assertEquals("fdfffe", hexForVariableLengthInteger(variableLengthInteger));
	}
	
	@Test
	public void testDeserializeInt0xfffe() throws IOException, DecodingException, DecoderException {
		Assert.assertEquals(0xfffe, variableLengthIntegerForHex("fdfffe").getValue());
	}
	
	@Test
	public void testSerializeInt0xffff() throws IOException {
		VariableLengthInteger variableLengthInteger = new VariableLengthInteger();
		variableLengthInteger.setValue(0xffff);
		Assert.assertEquals("fdffff", hexForVariableLengthInteger(variableLengthInteger));
	}
	
	@Test
	public void testDeserializeInt0xffff() throws IOException, DecodingException, DecoderException {
		Assert.assertEquals(0xffff, variableLengthIntegerForHex("fdffff").getValue());
	}
	
	@Test
	public void testSerializeInt0x010000() throws IOException {
		VariableLengthInteger variableLengthInteger = new VariableLengthInteger();
		variableLengthInteger.setValue(0x010000);
		Assert.assertEquals("fe00010000", hexForVariableLengthInteger(variableLengthInteger));
	}
	
	@Test
	public void testDeserializeInt0x010000() throws IOException, DecodingException, DecoderException {
		Assert.assertEquals(0x010000, variableLengthIntegerForHex("fe00010000").getValue());
	}
	
	@Test
	public void testSerializeInt32() throws IOException {
		VariableLengthInteger variableLengthInteger = new VariableLengthInteger();
		variableLengthInteger.setValue(66536);
		Assert.assertEquals("fe000103e8", hexForVariableLengthInteger(variableLengthInteger));
	}
	
	@Test
	public void testDeserializeInt32() throws IOException, DecodingException, DecoderException {
		Assert.assertEquals(66536l, variableLengthIntegerForHex("fe000103e8").getValue());
	}
	
	@Test
	public void testSerialize0() throws IOException {
		VariableLengthInteger variableLengthInteger = new VariableLengthInteger();
		variableLengthInteger.setValue(0);
		Assert.assertEquals("00", hexForVariableLengthInteger(variableLengthInteger));
	}
	
}
