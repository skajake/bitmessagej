package org.bitmessagej.model;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import org.bitmessagej.util.HashUtils;
import org.codehaus.preon.annotation.BoundList;

public class InventoryVector {

	@BoundList(size="32")
	private byte[] hash;
	
	public void hashBytes(byte[] bytesToHash) throws NoSuchAlgorithmException, NoSuchProviderException {
		hash = HashUtils.doubleRoundSHA512(bytesToHash);
	}

	public byte[] getHash() {
		return hash;
	}

	public void setHash(byte[] hash) {
		this.hash = hash;
	}
	
}
