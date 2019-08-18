package org.kostiskag.unitynetwork.common.routing.packet;

import java.util.stream.IntStream;
import java.io.IOException;

import org.kostiskag.unitynetwork.common.address.VirtualAddress;
import org.kostiskag.unitynetwork.common.calculated.DirtyAddress;
import org.kostiskag.unitynetwork.common.utilities.HashUtilities;


/**
 *
 * @author Konstantinos Kagiampakis
 */
public class IPv4Packet {
	
	public static final byte IP_VERSION = 69;
	public static final byte MIN_LEN = 20;
	
    public static boolean isIPv4(byte[] packet) {
    	if (packet != null && packet.length >= MIN_LEN) {
			int version = HashUtilities.bytesToUnsignedInt(new byte[] {packet[0]});
			if (version == IP_VERSION) {
				return true;
			}
    	}
        return false;
    }

    public static VirtualAddress getSourceAddress(byte[] packet) throws IOException {
		return VirtualAddress.valueOf(getSourceAddressAsByteArray(packet));
	}

    public static byte[] getSourceAddressAsByteArray(byte[] packet) throws IOException {
    	return getAddressAsByte(packet, 12);
    }

	public static VirtualAddress getDestAddress(byte[] packet) throws IOException {
		return VirtualAddress.valueOf(getDestAddressAsByteArray(packet));
	}

    public static byte[] getDestAddressAsByteArray(byte[] packet) throws IOException {
    	return getAddressAsByte(packet, 16);
    }

    private static byte[] getAddressAsByte(byte[] packet, int offset) throws IOException {
		if (isIPv4(packet)) {
			byte[] addr = new byte[4];
			IntStream.range(0,4).forEach(i -> {
				addr[i] = packet[offset+i];
			});
			if(DirtyAddress.isADirtyVirtualAddress(addr)) {
				throw new IOException ("This is a dirty virtual address");
			}
			return addr;
		}
		throw new IOException ("This is not an IPv4 packet.");
	}
}

