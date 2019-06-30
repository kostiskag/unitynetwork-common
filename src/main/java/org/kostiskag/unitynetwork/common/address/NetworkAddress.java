package org.kostiskag.unitynetwork.common.address;

import java.util.Arrays;
import java.util.Objects;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

import org.kostiskag.unitynetwork.common.calculated.NumericConstraints;

public class NetworkAddress {

    protected static final String VIRTUAL_ADDRESS_PREFIX = "10.";
    private static final Pattern IP_ADDRESS_PATTERN = Pattern.compile(
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    private final String asString;
    private final byte[] asByte;
    private final InetAddress asInet;

    protected NetworkAddress(String address) throws UnknownHostException {
        //sanitation here
        this.asInet = NetworkAddress.networkAddressToInetAddress(address);
        this.asString = address;
        this.asByte = asInet.getAddress();
    }

    protected NetworkAddress(InetAddress asInet) throws UnknownHostException {
        if (asInet == null) {
            throw new UnknownHostException("null inet given.");
        }

        this.asString = asInet.getHostAddress();

        if (!NetworkAddress.validate(asString)) {
            throw new UnknownHostException("the given ip is invalid");
        }

        this.asInet = asInet;
        this.asByte = asInet.getAddress();
    }

    protected NetworkAddress(String asString, byte[] asByte, InetAddress asInet) throws UnknownHostException {
        if (!NetworkAddress.validate(asString)) {
            throw new UnknownHostException("the given ip is invalid");
        }
        if (!asInet.getHostAddress().equals(asString) || !Arrays.equals(asInet.getAddress(), asByte)) {
            throw new UnknownHostException("The network address was broken by its instantiation!");
        }

        this.asString = asString;
        this.asByte = asByte;
        this.asInet = asInet;
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
