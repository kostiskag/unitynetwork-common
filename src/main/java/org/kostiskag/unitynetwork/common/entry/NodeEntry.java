package org.kostiskag.unitynetwork.common.entry;

import org.kostiskag.unitynetwork.common.address.NetworkAddress;
import org.kostiskag.unitynetwork.common.address.PhysicalAddress;
import org.kostiskag.unitynetwork.common.address.VirtualAddress;
import org.kostiskag.unitynetwork.common.calculated.NumericConstraints;
import org.kostiskag.unitynetwork.common.utilities.FixedDate;

import java.sql.Time;

public class NodeEntry<A extends NetworkAddress> implements Comparable<NodeEntry>{

    private final String hostname;
    private final A address;
    private final Object timeLock = new Object();
    private FixedDate regTimestamp;

    public NodeEntry(String hostname, A address) throws IllegalAccessException {
        if (hostname == null || address == null) {
            throw new IllegalAccessException("given hostname address were null!");
        } else if (hostname.isEmpty() || hostname.length() > NumericConstraints.MAX_STR_LEN_SMALL.size()) {
            throw new IllegalAccessException("given hostname not valid!");
        } else if (!(address instanceof VirtualAddress) && !(address instanceof PhysicalAddress)) {
            //Only permited addresses are either VirtualAddress or Physical Address no NetworkAddress
            throw new IllegalAccessException("wrong address data type!");
        }
        this.hostname = hostname;
        this.address = address;
        this.regTimestamp = new FixedDate();
    }

    public String getHostname() {
        return hostname;
    }

    public A getAddress() {
        return address;
    }

    public FixedDate getTimestamp() {
        synchronized (timeLock) {
            return regTimestamp;
        }
    }

    public void updateTimestamp() {
        synchronized (timeLock) {
            this.regTimestamp = new FixedDate();
        }
    }

    @Override
    public int compareTo(NodeEntry o) {
        if (this.address.equals(o.address)) {
            return 0;
        }
        return this.getHostname().compareTo(o.getHostname());
    }
}
