package org.kostiskag.unitynetwork.common.address;

import org.kostiskag.unitynetwork.common.calculated.NumericConstraints;

import java.net.InetAddress;
import java.net.UnknownHostException;

public final class VirtualSystemAddress extends NetworkAddress {

    public static final VirtualSystemAddress[] SYSTEM_RESERVED_ADDRESSES = VirtualSystemAddress.calculateSystemReservedAddresses();

    private final int asInt;

    private static VirtualSystemAddress[] calculateSystemReservedAddresses() {
        VirtualSystemAddress[] systemReserved = new VirtualSystemAddress[NumericConstraints.SYSTEM_RESERVED_ADDRESS_NUMBER.size()];
        for(int index = 0; index < systemReserved.length; index++) {
            try {
                int host = index + 1;
                systemReserved[index] = VirtualSystemAddress.valueOf(host);
            } catch (UnknownHostException e) {
                systemReserved[index] = null;
            }
        }
        return systemReserved;
    }

    private static VirtualSystemAddress valueOf(int systemReserved) throws UnknownHostException {
        //data sanitation
        if (systemReserved < 1) {
            throw new IllegalArgumentException();
        }
        if (systemReserved > NumericConstraints.SYSTEM_RESERVED_ADDRESS_NUMBER.size()) {
            throw new IllegalArgumentException();
        }

        byte[] asByte = VirtualSystemAddress.numberTo10IpByteSystemAddress(systemReserved);
        InetAddress asInet = InetAddress.getByAddress(asByte);
        String asString = asInet.getHostAddress();
        return new VirtualSystemAddress(asString, asByte, asInet, systemReserved);
    }

    private static byte[] numberTo10IpByteSystemAddress(int numAddr) {
        return new byte[] {
                (byte) 10, //This is the network part 10.*
                (byte) (numAddr >>> 16),
                (byte) (numAddr >>> 8),
                (byte) (numAddr)};
    }

    public static int byteTo10IpSystemAddrNumber(byte[] address) {
        if (address[0] != 10) {
            throw new IllegalArgumentException();
        }
        int hostnum = 0;
        for (int i = 1; i < 4; i++) {
            hostnum = (hostnum << 8) + (address[i] & 0xff);
        }
        return hostnum;
    }

    private VirtualSystemAddress(String asString, byte[] asByte, InetAddress asInet, int asInt) throws UnknownHostException {
        super(asString,asByte,asInet);
        this.asInt = asInt;
    }

    public int getSystemHostNumber() {
        return asInt;
    }

}
