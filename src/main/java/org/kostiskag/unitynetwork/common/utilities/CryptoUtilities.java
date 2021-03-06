/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kostiskag.unitynetwork.common.utilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.*;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 *
 * @author Konstantinos Kagiampakis
 */
public class CryptoUtilities {

	public static final String SALT = "=UrBN&RLJ=dBshBX3HFn!S^Au?yjqV8MBx7fMyg5p6U8T^%2kp^X-sk9EQeENgVEj%DP$jNnz&JeF?rU-*meW5yFkmAvYW_=mA+E$F$xwKmw=uSxTdznSTbunBKT*-&!";

	public static String hashedPasswordAlgorithm(String plainPassword) throws GeneralSecurityException {
		try {
			return HashUtilities.SHA256(CryptoUtilities.SALT + plainPassword);
		} catch (GeneralSecurityException e) {
			throw e;
		}
	}

	public static String validatePasswordAlgorithm(String username, String hashedPassword) throws GeneralSecurityException {
		try {
			String data = HashUtilities.SHA256(CryptoUtilities.SALT) +
					HashUtilities.SHA256(username) +
					hashedPassword;
			return HashUtilities.SHA256(data);
		} catch (GeneralSecurityException e) {
			throw e;
		}
	}

	public static String transmitPasswordAlgorithm(String username, String plainPassword) throws GeneralSecurityException {
		return validatePasswordAlgorithm(username, hashedPasswordAlgorithm(plainPassword));
	}

	/**
	 * Generates a random question
	 * 
	 * @return
	 */
	public static String generateQuestion() {
		SecureRandom random = new SecureRandom();
		return new BigInteger(1024, random).toString(32);
	}

	/**
	 * Generates an AES 256 session key
	 * 
	 * @return
	 */
	public static SecretKey generateAESSessionkey() throws GeneralSecurityException {
		try {
			KeyGenerator AES_keygen = KeyGenerator.getInstance("AES");
			AES_keygen.init(128, new SecureRandom());
			return AES_keygen.generateKey();
		} catch (NoSuchAlgorithmException e) {
			throw new GeneralSecurityException(e);
		}
	}
	
	public static byte[] aesEncrypt(String message, SecretKey key) throws GeneralSecurityException {
		try {
			Cipher AesCipher = Cipher.getInstance("AES");
			AesCipher.init(Cipher.ENCRYPT_MODE, key);
	        return AesCipher.doFinal(message.getBytes());
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			throw new GeneralSecurityException(e);
		}
	}

	public static String aesDecrypt(byte[] chiphered, SecretKey key) throws GeneralSecurityException {
		try {
			Cipher AesCipher = Cipher.getInstance("AES");
			AesCipher.init(Cipher.DECRYPT_MODE, key);
	        return new String(AesCipher.doFinal(chiphered), StandardCharsets.ISO_8859_1);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			throw new GeneralSecurityException(e);
		}
	}
	
	/**
	 * Generates a 2048 RSA key pair
	 * 
	 * @return keypair
	 */
	public static KeyPair generateRSAkeyPair() throws GeneralSecurityException {
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(2048, new SecureRandom());
			return kpg.genKeyPair();
		} catch (NoSuchAlgorithmException e) {
			throw new GeneralSecurityException(e);
		}
	}

	public static byte[] encryptWithPublic(String text, PublicKey key) throws GeneralSecurityException {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return cipher.doFinal(text.getBytes());
		} catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
			throw new GeneralSecurityException(e);
		}
	}

	public static String decryptWithPrivate(byte[] text, PrivateKey key) throws GeneralSecurityException {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, key);
			return new String(cipher.doFinal(text));
		} catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
			throw new GeneralSecurityException(e);
		}
	}
	
	public static <A> byte[] objectToBytes(A obj) throws IOException {
		try (ByteArrayOutputStream b = new ByteArrayOutputStream();
			 ObjectOutputStream out = new ObjectOutputStream(b)) {
			out.writeObject(obj);
			return b.toByteArray();
		} catch (IOException e) {
			throw e;
		}
	}

	/**
	 * remember to define a type argument to the object of your preference ex. PrivateKey
	 *
	 * @param bytes
	 * @return
	 */
	public static <A>  A bytesToObject(byte[] bytes) throws IOException, GeneralSecurityException {
		try (ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
			 ObjectInputStream in = new ObjectInputStream(bin)) {
			return (A) in.readObject(); //It seems i cant avoid casting...
		} catch (IOException e) {
			throw e;
		} catch (ClassNotFoundException ex) {
			throw new GeneralSecurityException(ex);
		}
	}
	
	public static <A> String objectToBase64StringRepresentation(A obj) throws IOException {
		return new String(Base64.getEncoder().encode(objectToBytes(obj)));
	}
	
	public static <A> A base64StringRepresentationToObject(String base64Str) throws IOException, GeneralSecurityException {
		return bytesToObject(Base64.getDecoder().decode(base64Str.getBytes()));
	}
	
	public static String bytesToBase64String(byte[] b) {
		return new String(Base64.getEncoder().encode(b));
	}
	
	public static byte[] base64StringTobytes(String base64Str) {
		return Base64.getDecoder().decode(base64Str.getBytes());
	}

	@Deprecated
	public static <A> void objectToFile(A obj, File file) throws IOException {
		objectToFile(obj, file.toPath());
	}

	public static <A> void objectToFile(A obj, Path filePath) throws IOException {
		try (var fileOut = Files.newOutputStream(filePath, StandardOpenOption.WRITE);
			 var out = new ObjectOutputStream(fileOut)) {
			out.writeObject(obj);
		} catch (IOException e) {
			throw e;
		}
	}

	@Deprecated
	public static <A> A fileToObject(File file) throws GeneralSecurityException, IOException {
		return fileToObject(file.toPath());
	}

	public static <A> A fileToObject(Path filePath) throws GeneralSecurityException, IOException {
		try (var fileIn = Files.newInputStream(filePath, StandardOpenOption.READ);
			 var in = new ObjectInputStream(fileIn)) {
			return (A) in.readObject(); //It seems i cant avoid casting...
		} catch (ClassNotFoundException e) {
			throw new GeneralSecurityException(e);
		} catch (IOException e) {
			throw e;
		}
	}
}
