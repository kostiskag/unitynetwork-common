package org.kostiskag.unitynetwork.common.address;

import java.util.Arrays;
import java.util.Objects;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

import org.kostiskag.unitynetwork.common.calculated.DirtyAddress;
import org.kostiskag.unitynetwork.common.calculated.NumericConstraints;

public class NetworkAddress {

    protected static final String VIRTUAL_ADDRESS_PREFIX = "10.";
    private static final Pattern IP_ADDRESS_PATTERN = Pattern.compile(
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    private final String asString;
    private final byte[] asByte;
    private final InetAddress asInet;

    protected NetworkAddress(String fromString) throws UnknownHostException {
        //sanitation here
        this.asInet = NetworkAddress.networkAddressToInetAddress(fromString);
        this.asString = fromString;
        this.asByte = asInet.getAddress();
    }

    protected NetworkAddress(InetAddress fromInet) throws UnknownHostException {
        if (fromInet == null) {
            throw new UnknownHostException("null inet given.");
        }

        this.asString = fromInet.getHostAddress();

        if (!NetworkAddress.validate(asString)) {
            throw new UnknownHostException("the given ip is invalid");
        }

        this.asInet = fromInet;
        this.asByte = fromInet.getAddress();
    }

    protected NetworkAddress(byte[] fromByte) throws UnknownHostException {
        if (DirtyAddress.isADirtyAddress(fromByte)) {
            throw new UnknownHostException("dirty address given!");
        }
        this.asByte = fromByte;
        this.asInet = InetAddress.getByAddress(fromByte);
        this.asString = this.asInet.getHostAddress();
    }

    protected NetworkAddress(String fromString, byte[] fromByte, InetAddress fromInet) throws UnknownHostException {
        if (!NetworkAddress.validate(fromString)) {
            throw new UnknownHostException("the given ip is invalid");
        }
        if (!fromInet.getHostAddress().equals(fromString) || !Arrays.equals(fromInet.getAddress(), fromByte)) {
            throw new UnknownHostException("The network address was broken by its instantiation!");
        }

        this.asString = fromString;
        this.asByte = fromByte;
        this.asInet = fromInet;
    }

    public String asString() {
        return asString;
    }

    public byte[] asByte() {
        return asByte;
    }

    public InetAddress asInet() {
        return asInet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NetworkAddress)) return false;
        NetworkAddress addr = (NetworkAddress) o;
        return Objects.equals(asString, addr.asString) &&
                Arrays.equals(asByte, addr.asByte) &&
                Objects.equals(asInet, addr.asInet);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(asString, asInet);
        result = 31 * result + Arrays.hashCode(asByte);
        return result;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                ": asString: '" + asString + '\'' +
                ", asByte: " + Arrays.toString(asByte) +
                ", asInet: " + asInet +
                '}';
    }

    public static InetAddress networkAddressToInetAddress(String address) throws UnknownHostException {
        if (!NetworkAddress.validate(address)) {
            throw new UnknownHostException("The given address is not an ip address.");
        }
        return InetAddress.getByName(address);
    }

    public static boolean validate(String ip) {
        if (ip == null) {
            return false;
        }
        return IP_ADDRESS_PATTERN.matcher(ip).matches();
    }
}
