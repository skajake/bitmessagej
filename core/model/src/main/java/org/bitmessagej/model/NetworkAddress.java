package org.bitmessagej.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.lang3.ArrayUtils;
import org.codehaus.preon.Codec;
import org.codehaus.preon.Codecs;
import org.codehaus.preon.annotation.Bound;
import org.codehaus.preon.annotation.BoundList;
import org.codehaus.preon.annotation.BoundNumber;
import org.codehaus.preon.buffer.ByteOrder;
import org.codehaus.preon.channel.BitChannel;
import org.codehaus.preon.channel.OutputStreamBitChannel;

public class NetworkAddress {
	
	@Bound
	private Time time;
	@BoundNumber(byteOrder=ByteOrder.BigEndian, size="32")
	private Long streamNumber;
	@BoundNumber(byteOrder=ByteOrder.BigEndian, size="64")
	private Long services;
	@BoundList(size="12")
	private byte[] ipAddress1;
	@BoundList(size="4")
	private byte[] ipAddress2;
	@BoundNumber(byteOrder=ByteOrder.BigEndian, size="16")
	private Integer portNumber;
	
	private byte[] prefix;
	
	public NetworkAddress() {
		prefix = createPrefix();
		setServices(1l);
		setStreamNumber(1L);
	}
	
	protected byte[] createPrefix() {
		Codec<Ipv4Prefix> prefixCodec = createIpv4PrefixCodec();
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		BitChannel channel = new OutputStreamBitChannel(buffer);
		try {
			prefixCodec.encode(new Ipv4Prefix(), channel, null);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return buffer.toByteArray();
	}
	
	protected Codec<Ipv4Prefix> createIpv4PrefixCodec() {
		return Codecs.create(Ipv4Prefix.class);
	}
	
	public Time getTime() {
		return time;
	}
	public void setTime(Time time) {
		this.time = time;
	}
	public Long getStreamNumber() {
		return streamNumber;
	}
	public void setStreamNumber(Long streamNumber) {
		this.streamNumber = streamNumber;
	}
	public Long getServices() {
		return services;
	}
	public void setServices(Long services) {
		this.services = services;
	}
	public InetAddress getIpAddress() throws UnknownHostException {
		if(ArrayUtils.isEquals(prefix, this.ipAddress1)) {
			return InetAddress.getByAddress(ipAddress2);
		} else {
			return InetAddress.getByAddress(ArrayUtils.addAll(ipAddress1, ipAddress2));
		}
	}
	public void setNetworkAddress(InetAddress address)  {
		if(address instanceof Inet6Address) {
			this.ipAddress1 = ArrayUtils.subarray(address.getAddress(), 0, 12);
			this.ipAddress2 = ArrayUtils.subarray(address.getAddress(), 12, 16);
		} else {
			this.ipAddress1 = prefix;
			this.ipAddress2 = address.getAddress();
		}
	}
	public Integer getPortNumber() {
		return portNumber;
	}
	public void setPortNumber(Integer portNumber) {
		this.portNumber = portNumber;
	}
	
}
