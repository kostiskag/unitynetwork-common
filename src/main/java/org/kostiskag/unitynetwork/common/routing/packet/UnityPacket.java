package org.kostiskag.unitynetwork.common.routing.packet;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.io.IOException;

import org.kostiskag.unitynetwork.common.address.VirtualAddress;
import org.kostiskag.unitynetwork.common.calculated.DirtyAddress;
import org.kostiskag.unitynetwork.common.utilities.HashUtilities;


/**
 * The unity packet is a payload carried out with UDP in order to 
 * send or receive system and synchronization messages between the nodes appart from
 * the exchanging IPv4 packet traffic.
 * 
 * A unity packet's first byte is 0x00 as compared to an IPv4 packet which is 0x45.
 * After the version byte, the code byte follows
 * The code is an unsigned int of 1byte size found in the second byte of the packet
 * depending on the code, the package may change form after it.
 * The code signifies the type of the expected operation.
 * 
 * below are the defined code numbers:
 * 
 * 0  KEEP ALIVE
 * 1  UPING
 * 2  DPING
 * 3  SHORT ROUTED ACK -> Short Ack -> ACK_S
 * 4  END TO END ROUTED ACK -> Long Ack -> ACK_L
 * 5  MESSAGE
 * 
 * --------------------------------
 *  * | 1 byte version | 1 byte code | -> KEEP ALIVE, UPING, DPING they need no payload
 * --------------------------------
 *    or      /\ the above types do not need routing \/ the below need rooting
 * --------------------------------------------------
 * | 1 byte version | 1 byte code |  2 bytes number |-> SHORT ACK keeps a packet tracking number to aid control flow mechanisms 
 * --------------------------------------------------
 *    or
 * --------------------------------------------------------------------------------------------
 * | 1 byte version | 1 byte code | Source IP  (4 bytes) | Dest IP (4 bytes) | 2 bytes number |-> LONG ACK keeps a packet tracking number to aid control flow mechanisms 
 * --------------------------------------------------------------------------------------------
 *    or
 * ---------------------------------------------------------------------------------------------
 * | 1 byte version | 1 byte code | Source IP  (4 bytes) | Dest IP (4 bytes) | message N bytes | -> MESSAGE
 * ---------------------------------------------------------------------------------------------
 * 
 * 
 * @author Konstantinos Kagiampakis
 */
public class UnityPacket {

	private static final byte UNITY_VERSION = 0;
	private static final byte MIN_LEN = 2;
	
	private static final byte KEEP_ALIVE = 0;
	private static final byte UPING = 1;
	private static final byte DPING = 2;
	private static final byte ACK_S = 3;
	private static final byte ACK_L = 4;
	private static final byte MESSAGE = 5;
	
	private static final byte[] noPayload = new byte[]{};
	
	public static boolean isUnity(byte[] packet) {
		if (packet != null && packet.length >= MIN_LEN) {
			byte version = packet[0];
			if (version == UNITY_VERSION) {
				return true;
			}
		}
		return false;
    }
    
    public static boolean isKeepAlive(byte[] packet) {
    	if (packet != null && packet.length == 2) {
			byte code = packet[1];
			if (code == KEEP_ALIVE) {
				return true;
			}
    	}
		return false;
    }	
    
    public static boolean isUping(byte[] packet) {
    	if (packet != null && packet.length == 2) {
			byte code = packet[1];
			if (code == UPING) {
				return true;
			}
		}
		return false;
    }	
    
    public static boolean isDping(byte[] packet) {
    	if (packet != null && packet.length == 2) {
			byte code = packet[1];
			if (code == DPING) {
				return true;
			}
    	}
		return false;
    }	
    
    public static boolean isLongRoutedAck(byte[] packet) {
    	if (packet != null && packet.length == 12) {
			byte code = packet[1];
			if (code == ACK_L) {
				return true;
			}
    	}
		return false;
    }
    
    public static boolean isShortRoutedAck(byte[] packet) {
    	if (packet != null && packet.length == 4) {
			byte code = packet[1];
			if (code == ACK_S) {
				return true;
			}
    	}
		return false;
    }
    
    public static boolean isMessage(byte[] packet) {
    	if (packet != null && packet.length > 10) {
			byte code = packet[1];
			if (code == MESSAGE) {
				return true;
			}
    	}
		return false;
    }
    
    public static short getLongRoutedAckTrackNum(byte[] packet) throws IOException {
    	if (isLongRoutedAck(packet)) {
    		byte[] byteNum = Arrays.copyOfRange(packet,10, 12);
    		return HashUtilities.bytesToUnsignedShort(byteNum);
    	}
    	throw new IOException("The packet was not a long routed ack packet");
    }
    
    public static short getShortRoutedAckTrackNum(byte[] packet) throws IOException {
    	if (isShortRoutedAck(packet)) {
    		byte[] byteNum = Arrays.copyOfRange(packet,2,4);
    		return HashUtilities.bytesToUnsignedShort(byteNum);
    	}
    	throw new IOException("The packet was not an short routed ack packet");
    }
    
    public static String getMessageMessage(byte[] packet) throws IOException {
        if (isMessage(packet)) {
	    	byte[] payload = new byte[packet.length -2 -8];
	        System.arraycopy(packet, 10, payload, 0, packet.length -2 -8);
	        return new String(payload,"utf-8");
        }
        throw new IOException("The packet was not a message packet");
    }

	public static VirtualAddress getSourceAddress(byte[] packet) throws IOException {
		return VirtualAddress.valueOf(getSourceAddressAsByteArray(packet));
	}

	public static byte[] getSourceAddressAsByteArray(byte[] packet) throws IOException {
		return getAddress(packet, 2);
    }

	public static VirtualAddress getDestAddress(byte[] packet) throws IOException {
		return VirtualAddress.valueOf(getDestAddressAsByteArray(packet));
	}

    public static byte[] getDestAddressAsByteArray(byte[] packet) throws IOException {
		return getAddress(packet, 6);
    }

	private static byte[] getAddress(byte[] packet, int offset) throws IOException {
		if (isMessage(packet) || isLongRoutedAck(packet)) {
			byte[] addr = new byte[4];
			IntStream.range(0,4).forEach(i -> {
				addr[i] = packet[offset + i];
			});
			if (DirtyAddress.isADirtyVirtualAddress(addr)) {
				throw new IOException("Bad destination address found in package");
			}
			return addr;
		}
		throw new IOException("The packet was not a message nor an ack packet");
	}
    
    public static byte[] buildKeepAlivePacket() {
		return buildPacket(KEEP_ALIVE, noPayload);
	}
	
	public static byte[] buildUpingPacket() {
		return buildPacket(UPING, noPayload);
	}
	
	public static byte[] buildDpingPacket() {
		return buildPacket(DPING, noPayload);
	}
	
	public static byte[] buildShortRoutedAckPacket(short trackNumber) {
		byte[] trackNumBytes = HashUtilities.unsignedIntTo2ByteArray(trackNumber);
		return buildPacket(ACK_S, trackNumBytes);
	}
	
	public static byte[] buildLongRoutedAckPacket(byte[] source, byte[] dest, short trackNumber) {
		byte[] trackNumBytes = HashUtilities.unsignedIntTo2ByteArray(trackNumber);
		byte[] payload = new byte[10];
		
		System.arraycopy(source,0, payload,0,4);
        System.arraycopy(dest,0, payload,4,4);
        System.arraycopy(trackNumBytes,0, payload,8,2);
		return buildPacket(ACK_L, payload);
	}
	
	public static byte[] buildMessagePacket(byte[] source, byte[] dest, String message) {
		if (DirtyAddress.isADirtyVirtualAddress(source) || DirtyAddress.isADirtyVirtualAddress(dest) || message == null) {
			return null;
		}
		byte[] messg = message.getBytes();
		byte[] payload = new byte[8 + messg.length];
		
        System.arraycopy(source,0, payload,0,4);
        System.arraycopy(dest,0, payload,4,4);
        System.arraycopy(messg,0, payload,8, messg.length);
        return buildPacket(MESSAGE, payload);
	}
	
	private static byte[] buildPacket(byte type, byte[] payload) {
		byte[] packet = new byte[1 + 1 + payload.length];
		packet[0] = UNITY_VERSION;
        packet[1] = type;
        System.arraycopy(payload, 0, packet, 2, payload.length);
        return packet;
    }
}
