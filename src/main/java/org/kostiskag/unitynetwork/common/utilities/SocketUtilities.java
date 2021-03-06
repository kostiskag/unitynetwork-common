package org.kostiskag.unitynetwork.common.utilities;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.SecretKey;

/**
 * low-level socket methods here
 *
 * @author Konstantinos Kagiampakis
 */
public class SocketUtilities {

	public static final String pre = "^SOCKET METHODS ";       
    
    public static InetAddress getAddress(String PhAddress) throws UnknownHostException {
        InetAddress IPaddress = null;
        try {
            IPaddress = InetAddress.getByName(PhAddress);
        } catch (UnknownHostException ex) {
        	 throw ex;
        }
        return IPaddress;
    }
    
    public static Socket absoluteConnect(InetAddress IPaddress, int authPort) throws IOException {
        return absoluteConnect(IPaddress,authPort,8000);
    }

    public static Socket absoluteConnect(InetAddress IPaddress, int authPort, int timeout) throws IOException {
        Socket socket = null;
        try {
            socket = new Socket(IPaddress, authPort);
            socket.setSoTimeout(timeout);
        } catch (IOException ex) {
            throw ex;
        }
        return socket;
    }

    public static DataInputStream makeDataReader(Socket socket) throws IOException {
    	//BufferedInputStream bin = new BufferedInputStream();
		DataInputStream dataStream = new DataInputStream(socket.getInputStream());
		return dataStream;
    }

    public static DataOutputStream makeDataWriter(Socket socket) throws IOException {
        DataOutputStream dataStream = new DataOutputStream(socket.getOutputStream());
        return dataStream;
    }
    
    public static void sendData(byte[] toSend, DataOutputStream writer) throws IOException {
    	writer.write(toSend);
    }
    
    public static byte[] receiveData(DataInputStream reader) throws IOException {
    	byte[] byteT = new byte[]{0x00};
    	byte[] bytes = new byte[2048];
    	for (int i=0; i<2; i++) {
	    	int read = reader.read(bytes);
			if (read > 0) {
                byteT = new byte[read];
                System.arraycopy(bytes, 0, byteT, 0, read);

//				if (byteT[0] == (int)0) {
//					//"RECEIVED a zero char";
//			    } else if (byteT[0] == (int)13) {
//					//"RECEIVED a new line char";
//				} else if (byteT[0] == (int)10) {
//					//"received a return char";
//				}
                return byteT;
            }
//			} else if (read == 0){
//				//"RECEIVED zero";
//			} else {
//				//"RECEIVED "+read;
//			}
    	}
    	return byteT; 		
    }
    
    public static byte[] sendReceiveData(byte[] toSend, DataInputStream reader, DataOutputStream writer) throws IOException {
    	sendData(toSend, writer);
    	byte[] received = receiveData(reader);
    	return received;
    }

    public static void sendPlainStringData(String message, DataOutputStream writer) throws IOException {
    	if (message == null) {
            throw new IOException(pre+"NO DATA TO SEND");
        } else if (message.isEmpty()) {
        	//line feed
        	message = "\n";
        }        
    	//include a line feed and a return char
    	//message += "\n\r";
    	byte[] toSend = message.getBytes();        
        sendData(toSend, writer);
    }
    
    public static String[] receivePlainStringData(DataInputStream reader) throws IOException {
    	byte[] received = receiveData(reader);
    	String receivedMessage = new String(received, StandardCharsets.UTF_8);
        String[] args = receivedMessage.split("\\s+");
        return args;
    }
    
    public static String[] sendReceivePlainStringData(String data, DataInputStream reader, DataOutputStream writer) throws IOException {
    	sendPlainStringData(data, writer);
    	String[] args = receivePlainStringData(reader);
    	return args;
    }
    
    public static void sendAESEncryptedStringData(String message, DataOutputStream writer, SecretKey sessionKey) throws GeneralSecurityException, IOException {
    	if (message == null) {
            throw new IOException(pre+"NO DATA TO SEND");
        } else if (message.isEmpty()) {
        	//line feed
        	message = "\n";
        }        
    	//include a line feed and a return char
    	//message += "\n\r";
    	byte[] chiphered = CryptoUtilities.aesEncrypt(message, sessionKey);
        sendData(chiphered, writer);
    }
    
    public static String[] receiveAESEncryptedStringData(DataInputStream reader, SecretKey sessionKey) throws GeneralSecurityException, IOException {
    	byte[] received = receiveData(reader);
    	String decrypted = CryptoUtilities.aesDecrypt(received, sessionKey);
    	String[] args = decrypted.split("\\s+");
        return args;
    }
    
    public static String receiveAESEncryptedString(DataInputStream reader, SecretKey sessionKey) throws GeneralSecurityException, IOException {
    	byte[] received = receiveData(reader);
    	String decrypted = CryptoUtilities.aesDecrypt(received, sessionKey);
    	return decrypted;
    }
    
    public static String[] sendReceiveAESEncryptedStringData(String message, DataInputStream reader, DataOutputStream writer, SecretKey sessionKey) throws GeneralSecurityException, IOException  {
    	sendAESEncryptedStringData(message, writer, sessionKey);
    	return receiveAESEncryptedStringData(reader, sessionKey);
    }
    
    public static void sendRSAEncryptedStringData(String message, DataOutputStream writer, PublicKey key) throws GeneralSecurityException, IOException {
    	if (message == null) {
            throw new IOException(pre+" NO DATA TO SEND");
        } else if (message.isEmpty()) {
        	//line feed
        	message = "\n";
        }        
    	//include a line feed and a return char
    	//message += "\n\r";
    	byte[] chiphered = CryptoUtilities.encryptWithPublic(message, key);
        sendData(chiphered, writer);
    }
    
    public static String[] receiveRSAEncryptedStringData(DataInputStream reader, PrivateKey priv) throws GeneralSecurityException, IOException {
    	byte[] received = receiveData(reader);
    	String decrypted = CryptoUtilities.decryptWithPrivate(received, priv);
    	return decrypted.split("\\s+");
    }
    
    public static void connectionClose(Socket socket) throws IOException {
        if (!socket.isClosed()) {
            socket.close();
        }
    }
    
    /*
     * Deprecated
     */
    
    @Deprecated
    public static BufferedReader makeReadWriter(Socket socket) throws IOException {
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        return inputReader;
    }

    @Deprecated
    public static PrintWriter makeWriteWriter(Socket socket) throws IOException {
        PrintWriter outputWriter = new PrintWriter(socket.getOutputStream(), true);
        return outputWriter;
    }

    @Deprecated
    public static String[] sendData(String data,PrintWriter outputWriter,BufferedReader inputReader) throws IOException  {
        if (outputWriter==null) {
        	throw new IOException(pre + "SEND DATA FAILED, NO CONNECTION");
        } else if (inputReader==null){
        	throw new IOException(pre + "SEND DATA FAILED, NO CONNECTION");
        } else if (data == null) {
            throw new IOException(pre+"NO DATA TO SEND");
        } else if (data.isEmpty()) {
            throw new IOException(pre+"NO DATA TO SEND");
        }
        
        outputWriter.println(data);
        String receivedMessage = null;
        String[] args = null;
        
        try {
			receivedMessage = inputReader.readLine();
		} catch (IOException e) {
			throw e;
		}

        args = receivedMessage.split("\\s+");
        return args;
    }

    @Deprecated
    public static void sendFinalData(String data,PrintWriter outputWriter) throws IOException  {
        if (data == null) {
            throw new IOException(pre + "NO DATA TO SEND");
        }
        outputWriter.println(data);
    }

    @Deprecated
    public static String[] readData(BufferedReader inputReader) throws IOException {
    	if (inputReader == null){
            throw new IOException(pre + "READ DATA FAILED, NO CONNECTION");
    	}
        
        String receivedMessage = null;
        String[] args = null;
        
        try {
			receivedMessage = inputReader.readLine();
		} catch (IOException e) {
			throw e;
		}
               
        System.out.println(pre + receivedMessage);
        args = receivedMessage.split("\\s+");
        return args;
    }
}
