package org.bitmessagej.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Date;

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

public class TestTime {

private static Codec<Time> codec;
	
	@BeforeClass
	public static void init() {
		codec = Codecs.create(Time.class);
	}
	
	private String hexForTime(Time time) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		BitChannel channel = new OutputStreamBitChannel(buffer);
		codec.encode(time, channel, null);
		return Hex.encodeHexString(buffer.toByteArray());
	}
	
	private Time timeForHex(String hex) throws DecodingException, DecoderException {
		ByteBuffer buffer = ByteBuffer.wrap(Hex.decodeHex(hex.toCharArray()));
		DefaultBitBuffer bitBuffer = new DefaultBitBuffer(buffer);
		return codec.decode(bitBuffer, null, new DefaultBuilder());
	}
	
	@Test
	public void testSerializeNewInstance() throws IOException {
		Time time = new Time();
		Date now = new Date();
		String actual = hexForTime(time);
		String expected = new String(Hex.encodeHex(ByteBuffer.allocate(8).putLong(now.getTime()).array()));
		String unexpected = new String(Hex.encodeHex(ByteBuffer.allocate(8).putLong(new Date(100).getTime()).array()));
		Assert.assertEquals(expected.substring(0, 11), actual.substring(0, 11));
		Assert.assertNotEquals(unexpected.substring(0, 11), actual.substring(0, 11));
	}
	
	@Test
	public void testSerializeDate() throws IOException {
		Time time = new Time();
		Date now = new Date(2354098);
		time.setDate(now);
		String actual = hexForTime(time);
		String expected = new String(Hex.encodeHex(ByteBuffer.allocate(8).putLong(now.getTime()).array()));
		String unexpected = new String(Hex.encodeHex(ByteBuffer.allocate(8).putLong(new Date(100).getTime()).array()));
		Assert.assertEquals(expected, actual);
		Assert.assertNotEquals(unexpected, actual);
	}
	
	@Test
	public void testDeserializeDateLong() throws IOException, DecodingException, DecoderException {
		Time time = timeForHex("000000000023ebb2");
		Assert.assertEquals(new Long(2354098), time.getValue());
	}
	
	@Test
	public void testDeserializeDate() throws IOException, DecodingException, DecoderException {
		Date date = new Date(1415837393000l);
		Time time = timeForHex("00000149a67c2068");
		Assert.assertEquals(date, time.getDate());
	}
	
}
