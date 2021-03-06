package org.kostiskag.unitynetwork.common.address;

import org.junit.Assert.*;
import org.junit.Test;

import java.net.UnknownHostException;

import static org.junit.Assert.*;

public class PhysicalAddressTest {

    @Test
    public void equalityTest() throws UnknownHostException {
        NetworkAddress n = new NetworkAddress("10.0.0.1");
        PhysicalAddress ph = PhysicalAddress.valueOf("11.0.0.1");
        VirtualAddress v = VirtualAddress.valueOf("10.0.0.1");

        assertTrue(ph.equals(PhysicalAddress.valueOf("11.0.0.1")));
        assertFalse(ph.equals(n));
        assertFalse(ph.equals(v));
    }
}
