package org.bitmessagej.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
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

public class TestNetworkAddress {

	private static final String ipv6Address = "fe80:0:0:0:202:b3ff:fe1e:8329";
	private static final String ipv4Address = "192.168.0.1";
	
	private static Codec<NetworkAddress> codec;
	
	@BeforeClass
	public static void init() {
		codec = Codecs.create(NetworkAddress.class);
	}
	
	private String hexForNetworkAddress(NetworkAddress networkAddress) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		BitChannel channel = new OutputStreamBitChannel(buffer);
		codec.encode(networkAddress, channel, null);
		return Hex.encodeHexString(buffer.toByteArray());
	}
	
	private NetworkAddress networkAddressForHex(String hex) throws DecodingException, DecoderException {
		ByteBuffer buffer = ByteBuffer.wrap(Hex.decodeHex(hex.toCharArray()));
		DefaultBitBuffer bitBuffer = new DefaultBitBuffer(buffer);
		return codec.decode(bitBuffer, null, new DefaultBuilder());
	}
	
	@Test
	public void testSerializeNetworkAddress() throws IOException {
		NetworkAddress networkAddress = new NetworkAddress();
		InetAddress address = InetAddress.getByName(ipv4Address);
		networkAddress.setNetworkAddress(address);
		networkAddress.setPortNumber(8080);
		Time time = new Time();
		time.setValue(1417404258250l);
		networkAddress.setTime(time);
		Assert.assertEquals("0000014a03e097ca00000001000000000000000100000000000000000000ffffc0a800011f90", hexForNetworkAddress(networkAddress));
	}
	
	@Test
	public void testDeserializeNetworkAddress() throws DecodingException, DecoderException, UnknownHostException {
		NetworkAddress networkAddress = networkAddressForHex("0000014a03e097ca00000001000000000000000100000000000000000000ffffc0a800011f90");
		InetAddress address = networkAddress.getIpAddress();
		Assert.assertTrue(address instanceof Inet4Address);
		Assert.assertEquals(new Long(1417404258250l), networkAddress.getTime().getValue());
		Assert.assertEquals(new Integer(8080), networkAddress.getPortNumber());
		Assert.assertEquals("/" + ipv4Address, address.toString());
		Assert.assertEquals(new Long(1l), networkAddress.getStreamNumber());
		Assert.assertEquals(new Long(1l), networkAddress.getServices());
	}
	
	@Test
	public void testSerializeIPv6NetworkAddress() throws IOException {
		NetworkAddress networkAddress = new NetworkAddress();
		InetAddress address = InetAddress.getByName(ipv6Address);
		networkAddress.setNetworkAddress(address);
		networkAddress.setPortNumber(8080);
		Time time = new Time();
		time.setValue(1417404258250l);
		networkAddress.setTime(time);
		Assert.assertEquals("0000014a03e097ca000000010000000000000001fe800000000000000202b3fffe1e83291f90", hexForNetworkAddress(networkAddress));
	}
	
	@Test
	public void testDeserializeIPv6NetworkAddress() throws DecodingException, DecoderException, UnknownHostException {
		NetworkAddress networkAddress = networkAddressForHex("0000014a03e097ca000000010000000000000001fe800000000000000202b3fffe1e83291f90");
		InetAddress address = networkAddress.getIpAddress();
		Assert.assertTrue(address instanceof Inet6Address);
		Assert.assertEquals(new Long(1417404258250l), networkAddress.getTime().getValue());
		Assert.assertEquals(new Integer(8080), networkAddress.getPortNumber());
		Assert.assertEquals("/" + ipv6Address, address.toString());
		Assert.assertEquals(new Long(1l), networkAddress.getStreamNumber());
		Assert.assertEquals(new Long(1l), networkAddress.getServices());
	}
}
