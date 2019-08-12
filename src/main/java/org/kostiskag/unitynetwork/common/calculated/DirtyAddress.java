package org.kostiskag.unitynetwork.common.calculated;

import java.util.Set;
import java.util.Arrays;

public class DirtyAddress {

    public enum DirtyAddressAddresses {
        ZERO(new byte[]{0, 0, 0, 0}),
        FULL_BROADCAST(new byte[]{(byte) 255, (byte) 255, (byte) 255, (byte) 255}),
        TEN_ZERO(new byte[]{(byte) 10, 0, 0, 0}),
        TEN_BROADCAST(new byte[]{(byte) 10, (byte) 255, (byte) 255, (byte) 255});

        private byte[] addr;

        DirtyAddressAddresses(byte[] addr) {
            if (addr == null || addr.length != NumericConstraints.IP_ADDRESS_LENGTH_IN_BYTES.size()) {
                this.addr = new byte[]{0, 0, 0, 0};
                return;
            }
            this.addr = addr;
        }

        public byte[] getAddr() {
            return addr;
        }
    }

    public static boolean isDirty(byte[] byteAddress) {
        return byteAddress == null || byteAddress.length != NumericConstraints.IP_ADDRESS_LENGTH_IN_BYTES.size();
    }

    public static boolean equalsAnyDirty(byte[] byteAddress) {
        var dirty = Set.of(DirtyAddressAddresses.values());
        return dirty.stream().anyMatch(a -> Arrays.equals(a.getAddr(), byteAddress));
    }

    public static boolean isADirtyAddress(byte[] byteAddress) {
        return isDirty(byteAddress) || equalsAnyDirty(byteAddress);
    }

    public static boolean isNotADirtyAddress(byte[] byteAddress) {
        return !isADirtyAddress(byteAddress);
    }

    public static boolean isADirtyVirtualAddress(byte[] byteAddress) {
        return isADirtyAddress(byteAddress) || byteAddress[0] != (byte) 10;
    }

    public static boolean isNotADirtyVirtualAddress(byte[] byteAddress) {
        return !isADirtyVirtualAddress(byteAddress);
    }

    public static boolean isADirtyRealAddress(byte[] byteAddress) {
        return isADirtyAddress(byteAddress) || byteAddress[0] == (byte) 10;
    }

    public static boolean isNotADirtyRealAddress(byte[] byteAddress) {
        return !isADirtyRealAddress(byteAddress);
    }

}