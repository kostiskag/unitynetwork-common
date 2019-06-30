package org.kostiskag.unitynetwork.common.calculated;

import org.kostiskag.unitynetwork.common.address.VirtualAddress;

import java.net.UnknownHostException;

public enum UsableVirtualAddresses {

    NETWORK_ADDRESS("10.0.0.0"),
    NETWORK_SUBNET_MASK_ADDRESS("10.255.255.255"),
    FIRST_NETWORK_HOST_ADDRESS("10.0.0.1"),
    LAST_NETWORK_HOST_ADDRESS("10.255.255.254"),
    FIRST_HOST_ADDRESS(1),
    LAST_HOST_ADDRESS(NumericConstraints.VIRTUAL_NETWORK_ADDRESS_EFFECTIVE_CAPACITY.size());

    VirtualAddress addr;

    UsableVirtualAddresses(String strAddr) {
        try {
            this.addr = VirtualAddress.valueOf(strAddr);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException();
        }
    }

    UsableVirtualAddresses(int intAddr) {
        try {
            this.addr = VirtualAddress.valueOf(intAddr);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException();
        }
    }

    public VirtualAddress getVirtualAddress() {
        return addr;
    }
}
