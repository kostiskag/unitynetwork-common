package org.kostiskag.unitynetwork.common.table;

import java.util.Optional;
import java.util.concurrent.locks.Lock;

import org.kostiskag.unitynetwork.common.address.NetworkAddress;
import org.kostiskag.unitynetwork.common.entry.NodeEntry;


public abstract class NodeTable<N extends NodeEntry> extends PlainTable<N> {

    public Optional<N> getOptionalNodeEntry(Lock lock, String hostname) throws InterruptedException {
        validateLock(lock);
        return nodes.stream()
                .filter(n -> n.getHostname().equals(hostname))
                .findFirst();
    }

    public Optional<N> getOptionalNodeEntry(Lock lock, NetworkAddress address) throws InterruptedException {
        validateLock(lock);
        return nodes.stream()
                .filter(n -> n.getAddress().equals(address))
                .findFirst();
    }

    @Deprecated
    public boolean isOnline(Lock lock, String hostname) throws InterruptedException {
        return getOptionalNodeEntry(lock, hostname).isPresent();
    }

    @Deprecated
    public N getNodeEntry(Lock lock, String hostname) throws InterruptedException, IllegalAccessException {
        validateLock(lock);
        Optional<N> n = getOptionalNodeEntry(lock, hostname);
        if (n.isPresent()) {
            return n.get();
        }
        throw new IllegalAccessException("the given node was not found on table "+hostname);
    }
}
