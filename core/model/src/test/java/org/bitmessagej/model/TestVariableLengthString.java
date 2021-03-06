package org.bitmessagej.model;

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

public class TestVariableLengthString {
	
	private static Codec<VariableLengthString> codec;
	private static final String loremIpsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
	private static final String loremIpsumHex = "4c6f72656d20697073756d20646f6c6f722073697420616d65742c20636f6e73656374657475722061646970697363696e6720656c69742c2073656420646f20656975736d6f642074656d706f7220696e6369646964756e74207574206c61626f726520657420646f6c6f7265206d61676e6120616c697175612e20557420656e696d206164206d696e696d2076656e69616d2c2071756973206e6f737472756420657865726369746174696f6e20756c6c616d636f206c61626f726973206e69736920757420616c697175697020657820656120636f6d6d6f646f20636f6e7365717561742e2044756973206175746520697275726520646f6c6f7220696e20726570726568656e646572697420696e20766f6c7570746174652076656c697420657373652063696c6c756d20646f6c6f726520657520667567696174206e756c6c612070617269617475722e204578636570746575722073696e74206f6363616563617420637570696461746174206e6f6e2070726f6964656e742c2073756e7420696e2063756c706120717569206f666669636961206465736572756e74206d6f6c6c697420616e696d20696420657374206c61626f72756d2e";
	private static final String helloWorld = "Hello World";
	private static final String helloWorldHex = "48656c6c6f20576f726c64";
	private static String loremIpsumLong;
	private static String loremIpsumLongHex;
	
	@BeforeClass
	public static void init() {
		codec = Codecs.create(VariableLengthString.class);
		StringBuilder string = new StringBuilder();
		StringBuilder hexString = new StringBuilder();
		for(int i = 0; i < 200; i++) {
			string.append(loremIpsum);
			hexString.append(loremIpsumHex);
		}
		loremIpsumLong = string.toString();
		loremIpsumLongHex = hexString.toString();
	}
	
	private String hexForVariableLengthString(VariableLengthString variableLengthString) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		BitChannel channel = new OutputStreamBitChannel(buffer);
		codec.encode(variableLengthString, channel, null);
		return Hex.encodeHexString(buffer.toByteArray());
	}
	
	private VariableLengthString variableLengthStringForHex(String hex) throws DecodingException, DecoderException {
		ByteBuffer buffer = ByteBuffer.wrap(Hex.decodeHex(hex.toCharArray()));
		DefaultBitBuffer bitBuffer = new DefaultBitBuffer(buffer);
		return codec.decode(bitBuffer, null, new DefaultBuilder());
	}
	
	@Test
	public void testSerializeShortString() throws IOException {
		VariableLengthString variableLengthString = new VariableLengthString();
		variableLengthString.setValue(helloWorld);
		Assert.assertEquals("0b" + helloWorldHex, hexForVariableLengthString(variableLengthString));
	}
	
	@Test
	public void testDeserializeShortString() throws IOException, DecodingException, DecoderException {
		VariableLengthString variableLengthString = variableLengthStringForHex("0b" + helloWorldHex);
		Assert.assertEquals(helloWorld, variableLengthString.getValue());
	}
	
	@Test
	public void testSerializeString() throws IOException {
		VariableLengthString variableLengthString = new VariableLengthString();
		variableLengthString.setValue(loremIpsum);
		Assert.assertEquals("fd01bd" + loremIpsumHex, hexForVariableLengthString(variableLengthString));
	}
	
	@Test
	public void testDeserializeString() throws IOException, DecodingException, DecoderException {
		VariableLengthString variableLengthString = variableLengthStringForHex("fd01bd" + loremIpsumHex);
		Assert.assertEquals(loremIpsum, variableLengthString.getValue());
	}
	
	@Test
	public void testSerializeLongString() throws IOException {
		VariableLengthString variableLengthString = new VariableLengthString();
		variableLengthString.setValue(loremIpsumLong);
		Assert.assertEquals("fe00015ba8" + loremIpsumLongHex, hexForVariableLengthString(variableLengthString));
	}
	
	@Test
	public void testDeserializeLongString() throws IOException, DecodingException, DecoderException {
		VariableLengthString variableLengthString = variableLengthStringForHex("fe00015ba8" + loremIpsumLongHex);
		Assert.assertEquals(loremIpsumLong, variableLengthString.getValue());
	}

}
