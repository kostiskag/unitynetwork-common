package org.kostiskag.unitynetwork.common.address;

import org.junit.BeforeClass;
import org.junit.Test;
import org.kostiskag.unitynetwork.common.calculated.NumericConstraints;

import java.net.UnknownHostException;

import static org.junit.Assert.*;

public class VirtualSystemAddressTest {

	@BeforeClass
	public static void before() {

	}

	@Test
	public void reservedArrayLength() {
		assertEquals(NumericConstraints.SYSTEM_RESERVED_ADDRESS_NUMBER.size(), VirtualSystemAddress.SYSTEM_RESERVED_ADDRESSES.length);
	}

	@Test
	public void elements() {
		if (VirtualSystemAddress.SYSTEM_RESERVED_ADDRESSES.length >=1) {
			assertEquals("10.0.0.1", VirtualSystemAddress.SYSTEM_RESERVED_ADDRESSES[0].asString());
			System.out.println(VirtualSystemAddress.SYSTEM_RESERVED_ADDRESSES[0].toString());
		}
		if (VirtualSystemAddress.SYSTEM_RESERVED_ADDRESSES.length >=2) {
			assertEquals("10.0.0.2", VirtualSystemAddress.SYSTEM_RESERVED_ADDRESSES[1].asString());
			System.out.println(VirtualSystemAddress.SYSTEM_RESERVED_ADDRESSES[1].toString());
		}
		if (VirtualSystemAddress.SYSTEM_RESERVED_ADDRESSES.length >= NumericConstraints.SYSTEM_RESERVED_ADDRESS_NUMBER.size()) {
			assertEquals("10.0.0.128", VirtualSystemAddress.SYSTEM_RESERVED_ADDRESSES[NumericConstraints.SYSTEM_RESERVED_ADDRESS_NUMBER.size()-1].asString());
			System.out.println(VirtualSystemAddress.SYSTEM_RESERVED_ADDRESSES[NumericConstraints.SYSTEM_RESERVED_ADDRESS_NUMBER.size()-1].toString());
		}
	}

	@Test
	public void equality() {
		VirtualSystemAddress v1 = VirtualSystemAddress.SYSTEM_RESERVED_ADDRESSES[0];
		VirtualSystemAddress v2 = VirtualSystemAddress.SYSTEM_RESERVED_ADDRESSES[0];
		VirtualSystemAddress v3 = VirtualSystemAddress.SYSTEM_RESERVED_ADDRESSES[1];

		assertEquals(v1, (NetworkAddress) v1);
		assertEquals(v1, v2);
		assertEquals(v2, v1);
		assertNotEquals(v1, v3);
		assertNotEquals(v3, v1);
	}
}
