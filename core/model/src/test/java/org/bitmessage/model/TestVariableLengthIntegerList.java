package org.bitmessage.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

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

public class TestVariableLengthIntegerList {

	private static Codec<VariableLengthIntegerList> codec;
	private static Codec<VariableLengthInteger> integerCodec;
	
	@BeforeClass
	public static void init() {
		codec = Codecs.create(VariableLengthIntegerList.class);
		integerCodec = Codecs.create(VariableLengthInteger.class);
	}
	
	private String hexForVariableLengthIntegerList(VariableLengthIntegerList variableLengthIntegerList) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		BitChannel channel = new OutputStreamBitChannel(buffer);
		codec.encode(variableLengthIntegerList, channel, null);
		return Hex.encodeHexString(buffer.toByteArray());
	}
	
	private VariableLengthIntegerList variableLengthIntegerListForHex(String hex) throws DecodingException, DecoderException {
		ByteBuffer buffer = ByteBuffer.wrap(Hex.decodeHex(hex.toCharArray()));
		DefaultBitBuffer bitBuffer = new DefaultBitBuffer(buffer);
		return codec.decode(bitBuffer, null, new DefaultBuilder());
	}
	
	private String hexForVariableLengthInteger(VariableLengthInteger variableLengthInteger) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		BitChannel channel = new OutputStreamBitChannel(buffer);
		integerCodec.encode(variableLengthInteger, channel, null);
		return Hex.encodeHexString(buffer.toByteArray());
	}
	
	private List<Long> createLongList(Long size) {
		List<Long> longs = new ArrayList<Long>();
		for(long i = 0; i < size; i++) {
			longs.add(new Long(i));
		}
		return longs;
	}
	
	private String createIntegerHex(List<Long> longs) throws IOException {
		StringBuilder stringBuilder = new StringBuilder();
		for(Long longValue : longs) {
			VariableLengthInteger variableLengthInteger = new VariableLengthInteger();
			variableLengthInteger.setValue(longValue);
			stringBuilder.append(hexForVariableLengthInteger(variableLengthInteger));
		}
		return stringBuilder.toString();
	}
	
	@Test
	public void testCreateIntegerHex() throws IOException {
		Assert.assertEquals("00010203040506070809", createIntegerHex(createLongList(10l)));
	}
	
	@Test
	public void testEmptyList() throws IOException {
		VariableLengthIntegerList variableLengthIntegerList = new VariableLengthIntegerList();
		Assert.assertEquals("00", hexForVariableLengthIntegerList(variableLengthIntegerList));
	}
	
	@Test
	public void testDeserializeEmptyList() throws DecodingException, DecoderException {
		VariableLengthIntegerList variableLengthIntegerList = variableLengthIntegerListForHex("00");
		Long[] actual = variableLengthIntegerList.getList().toArray(new Long[variableLengthIntegerList.getList().size()]);
		Long[] expected = new Long[0];
		Assert.assertArrayEquals(expected, actual);
	}

	@Test
	public void testSerializeShortList() throws IOException {
		VariableLengthIntegerList variableLengthIntegerList = new VariableLengthIntegerList();
		List<Long> longs = createLongList(10l);
		variableLengthIntegerList.setList(longs);
		Assert.assertEquals("0a" + createIntegerHex(longs), hexForVariableLengthIntegerList(variableLengthIntegerList));
	}
	
	@Test
	public void testDeserializeShortList() throws DecodingException, DecoderException, IOException {
		List<Long> longs = createLongList(10l);
		VariableLengthIntegerList variableLengthIntegerList = variableLengthIntegerListForHex("0a" + createIntegerHex(longs));
		Long[] actual = variableLengthIntegerList.getList().toArray(new Long[variableLengthIntegerList.getList().size()]);
		Long[] expected = longs.toArray(new Long[longs.size()]);
		Assert.assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testSerializeList() throws IOException {
		VariableLengthIntegerList variableLengthIntegerList = new VariableLengthIntegerList();
		List<Long> longs = createLongList(280l);
		variableLengthIntegerList.setList(longs);
		Assert.assertEquals("fd0118" + createIntegerHex(longs), hexForVariableLengthIntegerList(variableLengthIntegerList));
	}
	
	@Test
	public void testDeserializeList() throws DecodingException, DecoderException, IOException {
		List<Long> longs = createLongList(280l);
		VariableLengthIntegerList variableLengthIntegerList = variableLengthIntegerListForHex("fd0118" + createIntegerHex(longs));
		Long[] actual = variableLengthIntegerList.getList().toArray(new Long[variableLengthIntegerList.getList().size()]);
		Long[] expected = longs.toArray(new Long[longs.size()]);
		Assert.assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testSerializeLongList() throws IOException {
		VariableLengthIntegerList variableLengthIntegerList = new VariableLengthIntegerList();
		List<Long> longs = createLongList(98000l);
		variableLengthIntegerList.setList(longs);
		Assert.assertEquals("fe00017ed0" + createIntegerHex(longs), hexForVariableLengthIntegerList(variableLengthIntegerList));
	}
	
	@Test
	public void testDeserializeLongList() throws DecodingException, DecoderException, IOException {
		List<Long> longs = createLongList(98000l);
		VariableLengthIntegerList variableLengthIntegerList = variableLengthIntegerListForHex("fe00017ed0" + createIntegerHex(longs));
		Long[] actual = variableLengthIntegerList.getList().toArray(new Long[variableLengthIntegerList.getList().size()]);
		Long[] expected = longs.toArray(new Long[longs.size()]);
		Assert.assertArrayEquals(expected, actual);
	}
	
}
