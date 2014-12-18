package org.bitmessagej.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class HashUtils {
	
	public static byte[] doubleRoundSHA512(byte[] payload) throws NoSuchAlgorithmException, NoSuchProviderException {
		Security.addProvider(new BouncyCastleProvider());
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-512", "BC");
		byte[] roundOne = messageDigest.digest(payload);
		return messageDigest.digest(roundOne);
	}

}
