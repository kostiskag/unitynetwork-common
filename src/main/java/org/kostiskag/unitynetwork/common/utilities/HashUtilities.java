package org.kostiskag.unitynetwork.common.utilities;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 
 * @author Konstantinos Kagiampakis
 */
public class HashUtilities {
 
    public static String bytesToHexStr(byte[] data) { 
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < data.length; i++) { 
            int halfByte = (data[i] >>> 4) & 0x0f;
            int twoHalfs = 0;
            do { 
                if ((0 <= halfByte) && (halfByte <= 9)) {
                    str.append((char) ('0' + halfByte));
                } else {
                    str.append((char) ('a' + (halfByte - 10)));
                }
                halfByte = data[i] & 0x0f;
            } while(twoHalfs++ < 1);
        } 
        return str.toString();
    } 
    
    public static byte[] hexStrToByteArray(String hexStr) {
        int len = hexStr.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexStr.charAt(i), 16) << 4)
                                 + Character.digit(hexStr.charAt(i+1), 16));
        }
        return data;
    }
    
    public static int bytesToUnsignedInt(byte[] data) {
    	return new BigInteger(1, data).intValue(); 
    }
    
    public static byte[] unsignedIntTo4ByteArray(int num) {
    	if (num >= 0 && num < Math.pow(2, 24)) {
    		return new byte[] {
    		        (byte)(num >> 24),
    		        (byte)(num >> 16),
    		        (byte)(num >> 8),
    		        (byte) num };
    	}
    	return null;
    }
    
    public static byte[] unsignedIntTo2ByteArray(int num) {
    	if (num >= 0 && num < Math.pow(2, 16)) {
    		return new byte[] {
    		        (byte)(num >> 8),
    		        (byte)num };    		
    	}
    	return null;
    }
    
    public static byte[] unsignedIntTo1ByteArray(int num) {
    	if (num >= 0 && num < Math.pow(2, 8)) {
    		return new byte[] {(byte)num };
    	}
    	return null;
    }
    
    public static byte unsignedIntTo1Byte(int num) {
    	if (num >= 0 && num < Math.pow(2, 8)) {
    		return (byte)num ;
    	}
    	return 0x00;
    }
    
    public static byte buildByteFromBits(String flags) {
    	flags = flags.replaceAll(" ", "");
    	if (flags.length() == 8) {
    		return  (byte) Integer.parseInt(flags, 2);
    	}
    	return 0x00;
    }

    @Deprecated
    public static String MD5(String text) 
    throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(text.getBytes(StandardCharsets.ISO_8859_1), 0, text.length());
        return bytesToHexStr(md.digest());
    }
    
    public static String SHA256(String text) 
    	    throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(text.getBytes(StandardCharsets.ISO_8859_1), 0, text.length());
        return bytesToHexStr(md.digest());
    }
} 