package org.kostiskag.unitynetwork.common.address;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Objects;

import org.kostiskag.unitynetwork.common.calculated.NumericConstraints;

/**
 *
 * @author Konstantinos Kagiampakis
 */
public final class VirtualAddress extends NetworkAddress{

    private final int asInt;

    private VirtualAddress(String asString, byte[] asByte, InetAddress asInet, int asInt) throws UnknownHostException {
        super(asString,asByte,asInet);
        if (!asString.startsWith(VIRTUAL_ADDRESS_PREFIX)) {
            throw new UnknownHostException("The given ip address is not a part of the virtual network");
        }
        this.asInt = asInt;
    }

    /**
     * Warning!!! there will not be an auto conversion here!
     * it is safer to use valueOf(int) unless you want a specific address
     * like a network, system or subnet address
     *
     * @param vAddress
     * @return
     * @throws UnknownHostException
     */
    public static VirtualAddress valueOf(String vAddress) throws UnknownHostException {
        if (!NetworkAddress.validate(vAddress)) {
            throw new UnknownHostException("The given ip is invalid");
        }
        if (!vAddress.startsWith(VIRTUAL_ADDRESS_PREFIX)) {
            throw new UnknownHostException("The given ip address is not a part of the virtual network");
        }

        InetAddress asInet = NetworkAddress.networkAddressToInetAddress(vAddress);
        byte[] asByte = asInet.getAddress();
        int asInt = VirtualAddress.byteTo10IpAddrNumber(asByte);
        return new VirtualAddress(vAddress, asByte, asInet, asInt);
    }

    /**
     * Counting starts from 1, as you say the 1st host address,
     * the 2nd host address and so on...
     *
     * @param numericVAddress
     * @return
     * @throws UnknownHostException
     */
    public static VirtualAddress valueOf(int numericVAddress) throws UnknownHostException {
        //data sanitation exists in numberTo10IpByteHostAddress
        byte[] asByte = VirtualAddress.numberTo10IpByteHostAddress(numericVAddress);
        InetAddress asInet = VirtualAddress._10IpByteToInetAddress(asByte);
        String asString = asInet.getHostAddress();
        return new VirtualAddress(asString, asByte, asInet, numericVAddress);
    }

    public int asInt() {
        return asInt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VirtualAddress)) return false;
        VirtualAddress vaddr = (VirtualAddress) o;
        return asInt == vaddr.asInt
                && super.equals(o);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(asInt);
        result = 31 * result + super.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                ": asString: '" + super.asString() + '\'' +
                ", asInt: " + asInt +
                ", asByte: " + Arrays.toString(super.asByte()) +
                ", asInet: " + super.asInet() +
                '}';
    }

    /**
     * Host counting starts from 1,
     * this method will calculate the effective host number and apply it to an address
     *
     * @param numAddr
     * @return
     * @throws IllegalArgumentException in cases the provided host number is below 1
     */
    public static byte[] numberTo10IpByteHostAddress(int numAddr) {
        if (numAddr < 1) {
            throw new IllegalArgumentException();
        }
        int hostnum = numAddr + NumericConstraints.SYSTEM_RESERVED_ADDRESS_NUMBER.size();
        if (hostnum > NumericConstraints.VIRTUAL_NETWORK_ADDRESS_EFFECTIVE_CAPACITY.size()) {
            // in other words the last permitted numAddress is:
            // NumericConstraints.VIRTUAL_NETWORK_ADDRESS_EFFECTIVE_CAPACITY.size()
            throw new IllegalArgumentException();
        }

        return new byte[] {
            0x0a, //This is the network part 10.*
            (byte) (hostnum >>> 16),
            (byte) (hostnum >>> 8),
            (byte) (hostnum)};
    }



    public static InetAddress _10IpByteToInetAddress(byte[] byteAddress) throws UnknownHostException {
        return InetAddress.getByAddress(byteAddress);
    }

    public static String numberTo10ipAddr(int numAddr) throws UnknownHostException {
        byte[] address = VirtualAddress.numberTo10IpByteHostAddress(numAddr);
        return VirtualAddress._10IpByteToInetAddress(address).getHostAddress();
    }

    //---------------reverse process----------------
    public static int byteTo10IpAddrNumber(byte[] address) {
        if (address[0] != 10) {
            throw new IllegalArgumentException();
        }
        int hostnum = 0;
        for (int i = 1; i < 4; i++) {
            hostnum = (hostnum << 8) + (address[i] & 0xff);
        }
        return hostnum - NumericConstraints.SYSTEM_RESERVED_ADDRESS_NUMBER.size();
    }

    public static int _10IpAddrToNumber(String vaddress) throws UnknownHostException {
        return byteTo10IpAddrNumber(NetworkAddress.networkAddressToInetAddress(vaddress).getAddress());
    }
}
