package org.kostiskag.unitynetwork.common.routing.packet;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kostiskag.unitynetwork.common.address.VirtualAddress;


public class UnityPacketTest {

    @BeforeClass
    public static void beforeClass() {

    }

    @AfterClass
    public static void afterClass() {

    }

    @Before
    public void before() {

    }

    @Test
    public void dpingPacketTest() {
        byte[] p = UnityPacket.buildDpingPacket();
        assertTrue(UnityPacket.isUnity(p));
        assertTrue(UnityPacket.isDping(p));

        assertFalse(UnityPacket.isUping(p));
        assertFalse(UnityPacket.isKeepAlive(p));
        assertFalse(UnityPacket.isLongRoutedAck(p));
        assertFalse(UnityPacket.isShortRoutedAck(p));
        assertFalse(UnityPacket.isMessage(p));
    }

    @Test
    public void upingPacketTest() {
        byte[] p = UnityPacket.buildUpingPacket();
        assertTrue(UnityPacket.isUnity(p));
        assertTrue(UnityPacket.isUping(p));

        assertFalse(UnityPacket.isDping(p));
        assertFalse(UnityPacket.isKeepAlive(p));
        assertFalse(UnityPacket.isLongRoutedAck(p));
        assertFalse(UnityPacket.isShortRoutedAck(p));
        assertFalse(UnityPacket.isMessage(p));
    }

    @Test
    public void keepAlivePacketTest() {
        byte[] p = UnityPacket.buildKeepAlivePacket();
        assertTrue(UnityPacket.isUnity(p));
        assertTrue(UnityPacket.isKeepAlive(p));

        assertFalse(UnityPacket.isUping(p));
        assertFalse(UnityPacket.isDping(p));
        assertFalse(UnityPacket.isLongRoutedAck(p));
        assertFalse(UnityPacket.isShortRoutedAck(p));
        assertFalse(UnityPacket.isMessage(p));
    }

    @Test
    public void shortRoutedAckPacketTest() throws IOException {
        byte[] p = UnityPacket.buildShortRoutedAckPacket((short) 15);
        assertTrue(UnityPacket.isUnity(p));
        assertTrue(UnityPacket.isShortRoutedAck(p));
        assertEquals(UnityPacket.getShortRoutedAckTrackNum(p), 15);

        assertFalse(UnityPacket.isKeepAlive(p));
        assertFalse(UnityPacket.isUping(p));
        assertFalse(UnityPacket.isDping(p));
        assertFalse(UnityPacket.isLongRoutedAck(p));
        assertFalse(UnityPacket.isMessage(p));
    }

    @Test
    public void longRoutedAckPacketTest() throws IOException {
        byte[] p = UnityPacket.buildLongRoutedAckPacket(VirtualAddress.valueOf("10.0.2.3").asByte(), VirtualAddress.valueOf("10.0.2.4").asByte(), (short) 225);
        assertTrue(UnityPacket.isUnity(p));
        assertTrue(UnityPacket.isLongRoutedAck(p));

        assertFalse(UnityPacket.isKeepAlive(p));
        assertFalse(UnityPacket.isUping(p));
        assertFalse(UnityPacket.isDping(p));
        assertFalse(UnityPacket.isShortRoutedAck(p));
        assertFalse(UnityPacket.isMessage(p));

        assertEquals(UnityPacket.getLongRoutedAckTrackNum(p), 225);
        assertEquals(UnityPacket.getSourceAddress(p), VirtualAddress.valueOf("10.0.2.3"));
        assertEquals(UnityPacket.getDestAddress(p), VirtualAddress.valueOf("10.0.2.4"));
    }

    @Test
    public void messagePacketTest() throws IOException {
        byte[] p = UnityPacket.buildMessagePacket(VirtualAddress.valueOf("10.0.2.3").asByte(), VirtualAddress.valueOf("10.0.2.4").asByte(), "Hello!!! Message!!!");
        assertTrue(UnityPacket.isUnity(p));
        assertTrue(UnityPacket.isMessage(p));

        assertFalse(UnityPacket.isKeepAlive(p));
        assertFalse(UnityPacket.isUping(p));
        assertFalse(UnityPacket.isDping(p));
        assertFalse(UnityPacket.isLongRoutedAck(p));
        assertFalse(UnityPacket.isShortRoutedAck(p));

        assertEquals(UnityPacket.getMessageMessage(p), "Hello!!! Message!!!");
        assertEquals(UnityPacket.getSourceAddress(p), VirtualAddress.valueOf("10.0.2.3"));
        assertEquals(UnityPacket.getDestAddress(p), VirtualAddress.valueOf("10.0.2.4"));
    }

}
