package org.kostiskag.unitynetwork.common.table;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.locks.Lock;

import org.kostiskag.unitynetwork.common.address.NetworkAddress;
import org.kostiskag.unitynetwork.common.entry.NodeEntry;


public abstract class NodeTable<A extends NetworkAddress, N extends NodeEntry<A>> extends PlainTable<N> {

    protected NodeTable() { }

    protected NodeTable(Collection<N> in) {
        super(in);
    }

    @Locking(LockingScope.EXTERNAL)
    public final Optional<N> getOptionalNodeEntry(Lock lock, String hostname) throws InterruptedException {
        validateLock(lock);
        return getStream()
                .filter(n -> n.getHostname().equals(hostname))
                .findFirst();
    }

    @Locking(LockingScope.EXTERNAL)
    public final Optional<N> getOptionalNodeEntry(Lock lock, A address) throws InterruptedException {
        validateLock(lock);
        return getStream()
                .filter(n -> n.getAddress().equals(address))
                .findFirst();
    }

    @Deprecated
    @Locking(LockingScope.EXTERNAL)
    public final boolean isOnline(Lock lock, String hostname) throws InterruptedException {
        return getOptionalNodeEntry(lock, hostname).isPresent();
    }

    @Deprecated
    @Locking(LockingScope.EXTERNAL)
    public final N getNodeEntry(Lock lock, String hostname) throws InterruptedException, IllegalAccessException {
        validateLock(lock);
        Optional<N> n = getOptionalNodeEntry(lock, hostname);
        if (n.isPresent()) {
            return n.get();
        }
        throw new IllegalAccessException("the given node was not found on table "+hostname);
    }

    @Deprecated
    @Locking(LockingScope.EXTERNAL)
    public final boolean isOnline(Lock lock, A address) throws InterruptedException {
        return getOptionalNodeEntry(lock, address).isPresent();
    }

    @Deprecated
    @Locking(LockingScope.EXTERNAL)
    public final N getNodeEntry(Lock lock, A address) throws InterruptedException, IllegalAccessException {
        validateLock(lock);
        Optional<N> n = getOptionalNodeEntry(lock, address);
        if (n.isPresent()) {
            return n.get();
        }
        throw new IllegalAccessException("the given node address was not found on table "+address.asString());
    }
}
