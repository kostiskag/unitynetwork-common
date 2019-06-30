package org.kostiskag.unitynetwork.common.address;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import java.net.UnknownHostException;

public class VirtualAddressTest {

	@BeforeClass
	public static void before() {

	}

	@Test
	public void stringConstructor() throws UnknownHostException {
		VirtualAddress v = null;
		try {
			 v = VirtualAddress.valueOf(VirtualAddress.numberTo10ipAddr(3));
		} catch (UnknownHostException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		assertEquals(v,VirtualAddress.valueOf(3));
	}

	@Test
	public void numericConstructor() throws UnknownHostException {
		int address = 15;
		VirtualAddress v = null;
		try {
			v = VirtualAddress.valueOf(VirtualAddress.numberTo10ipAddr(15));
		} catch (UnknownHostException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		assertEquals(v,VirtualAddress.valueOf(15));
	}

	@Test
	public void equalityTest() throws UnknownHostException {
		VirtualAddress v1, v2, v3;
		v1 = VirtualAddress.valueOf(28);
		v2 = VirtualAddress.valueOf(VirtualAddress.numberTo10ipAddr(28));

		assertEquals(v1, v1);
		assertEquals(v1, VirtualAddress.valueOf(28));
		assertEquals(v2, v1);
		assertEquals(v1, v2);

		v2 = VirtualAddress.valueOf("10.0.0.30");
		v3 = VirtualAddress.valueOf(50);

		assertNotEquals(v1, v2);
		assertNotEquals(v2, v1);
		assertNotEquals(v1, v3);
		assertNotEquals(v2, v3);
	}

	@Test
	public void hashCodeTest() throws UnknownHostException {
		VirtualAddress v1, v2, v3;
		v1 = VirtualAddress.valueOf(28);
		v2 = VirtualAddress.valueOf(VirtualAddress.numberTo10ipAddr(28));
		v3 = VirtualAddress.valueOf(50);

		assertEquals(v1.hashCode(), v2.hashCode());
		assertNotEquals(v1.hashCode(), v3.hashCode());
		assertNotEquals(v2.hashCode(), v3.hashCode());
	}

	//@Test
	public void _10IpAddrToNumberTest() {
		try {
			//breach!!!! on the reverse proccess
			assertEquals(VirtualAddress._10IpAddrToNumber("10.0.0.3"), 2);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			assertFalse(true);
		}
	}

	//@Test
	public void numberTo10ipAddrTest() {
		try {
			assertEquals(VirtualAddress.numberTo10ipAddr(2), "10.0.0.3");
		} catch (UnknownHostException e) {
			e.printStackTrace();
			assertFalse(true);
		}
	}
}
