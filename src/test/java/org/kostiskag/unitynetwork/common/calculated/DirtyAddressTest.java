package org.kostiskag.unitynetwork.common.calculated;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DirtyAddressTest {

    @Test
    public void nullAddrTest() {
        byte[] a = null;

        assertTrue(DirtyAddress.isDirty(a));

        assertFalse(DirtyAddress.equalsAnyDirty(a));

        assertTrue(DirtyAddress.isADirtyAddress(a));
        assertFalse(DirtyAddress.isNotADirtyAddress(a));
    }

    @Test
    public void badSizedAddrTest() {
        var a = new byte[]{0,0,0,0,0};

        assertTrue(DirtyAddress.isDirty(a));

        assertFalse(DirtyAddress.equalsAnyDirty(a));

        assertTrue(DirtyAddress.isADirtyAddress(a));
        assertFalse(DirtyAddress.isNotADirtyAddress(a));
    }

    @Test
    public void zeroAddrTest() {
        var a = new byte[]{0,0,0,0};

        assertFalse(DirtyAddress.isDirty(a));

        assertTrue(DirtyAddress.equalsAnyDirty(a));

        assertTrue(DirtyAddress.isADirtyAddress(a));
        assertFalse(DirtyAddress.isNotADirtyAddress(a));
    }

    @Test
    public void broadAddrTest() {
        var a = new byte[]{(byte)255,(byte)255,(byte)255,(byte)255};

        assertFalse(DirtyAddress.isDirty(a));

        assertTrue(DirtyAddress.equalsAnyDirty(a));

        assertTrue(DirtyAddress.isADirtyAddress(a));
        assertFalse(DirtyAddress.isNotADirtyAddress(a));
    }

    @Test
    public void acceptedRealAddrTest() {
        var a = new byte[]{(byte)1,(byte)2,(byte)3,(byte)4};

        assertFalse(DirtyAddress.isDirty(a));

        assertFalse(DirtyAddress.equalsAnyDirty(a));

        assertFalse(DirtyAddress.isADirtyAddress(a));
        assertTrue(DirtyAddress.isNotADirtyAddress(a));

        //it is not a dirty real!
        assertTrue(DirtyAddress.isNotADirtyRealAddress(a));
        assertFalse(DirtyAddress.isADirtyRealAddress(a));

        //it is a dirty virtual address!
        assertFalse(DirtyAddress.isNotADirtyVirtualAddress(a));
        assertTrue(DirtyAddress.isADirtyVirtualAddress(a));
    }

    @Test
    public void acceptedViAddrTest() {
        var a = new byte[]{(byte)10,(byte)2,(byte)3,(byte)4};

        assertFalse(DirtyAddress.isDirty(a));

        assertFalse(DirtyAddress.equalsAnyDirty(a));

        assertFalse(DirtyAddress.isADirtyAddress(a));
        assertTrue(DirtyAddress.isNotADirtyAddress(a));

        //it is not a dirty real!
        assertFalse(DirtyAddress.isNotADirtyRealAddress(a));
        assertTrue(DirtyAddress.isADirtyRealAddress(a));

        //it is a dirty virtual address!
        assertTrue(DirtyAddress.isNotADirtyVirtualAddress(a));
        assertFalse(DirtyAddress.isADirtyVirtualAddress(a));
    }


}
