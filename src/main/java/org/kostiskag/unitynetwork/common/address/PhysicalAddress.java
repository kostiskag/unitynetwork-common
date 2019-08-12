package org.kostiskag.unitynetwork.common.address;

import org.kostiskag.unitynetwork.common.calculated.DirtyAddress;

import java.net.UnknownHostException;

/**
 *
 * @author Konstantinos Kagiampakis
 */
public final class PhysicalAddress extends NetworkAddress {

    private PhysicalAddress(String asString) throws UnknownHostException {
        super(asString);
    }

    private PhysicalAddress(byte[] fromByte) throws UnknownHostException {
        super(fromByte);
    }

    public static PhysicalAddress valueOf(String address) throws UnknownHostException {
        //this enforces physical addressed to not be virtual addresses
        if (address.startsWith(VIRTUAL_ADDRESS_PREFIX)) {
            throw new UnknownHostException("The given ip address is a part of the virtual network and may not be used outside of it!");
        }
        return new PhysicalAddress(address);
    }

    public static PhysicalAddress valueOf(byte[] fromByte) throws UnknownHostException {
        //this enforces physical addressed to not be virtual addresses
        if (DirtyAddress.isADirtyRealAddress(fromByte)) {
            throw new UnknownHostException("The given ip address is a part of the virtual network and may not be used outside of it!");
        }
        return new PhysicalAddress(fromByte);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhysicalAddress)) return false;
        PhysicalAddress ph = (PhysicalAddress) o;
        return super.equals(o);
    }
}
