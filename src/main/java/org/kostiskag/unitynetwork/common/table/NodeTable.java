package org.kostiskag.unitynetwork.common.table;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.locks.Lock;

import org.kostiskag.unitynetwork.common.address.NetworkAddress;
import org.kostiskag.unitynetwork.common.entry.NodeEntry;

/**
 *
 * @author Konstantinos Kagiampakis
 */
public abstract class NodeTable<A extends NetworkAddress, N extends NodeEntry<A>> extends PlainTable<N> {

    protected NodeTable() { }

    protected NodeTable(Collection<N> in) {
        super(in);
    }

    @Locking(LockingScope.EXTERNAL)
    public final Optional<N> getOptionalEntry(Lock lock, String hostname) throws InterruptedException {
        validateLock(lock);
        return getStream()
                .filter(n -> n.getHostname().equals(hostname))
                .findFirst();
    }

    @Locking(LockingScope.EXTERNAL)
    public final Optional<N> getOptionalEntry(Lock lock, A address) throws InterruptedException {
        validateLock(lock);
        return getStream()
                .filter(n -> n.getAddress().equals(address))
                .findFirst();
    }

    @Deprecated
    @Locking(LockingScope.EXTERNAL)
    public final boolean checkEntry(Lock lock, String hostname) throws InterruptedException {
        return getOptionalEntry(lock, hostname).isPresent();
    }

    @Deprecated
    @Locking(LockingScope.EXTERNAL)
    public final N getEntry(Lock lock, String hostname) throws InterruptedException, IllegalAccessException {
        validateLock(lock);
        Optional<N> n = getOptionalEntry(lock, hostname);
        if (n.isPresent()) {
            return n.get();
        }
        throw new IllegalAccessException("the given node was not found on table "+hostname);
    }

    @Deprecated
    @Locking(LockingScope.EXTERNAL)
    public final N getEntry(Lock lock, A address) throws InterruptedException, IllegalAccessException {
        validateLock(lock);
        Optional<N> n = getOptionalEntry(lock, address);
        if (n.isPresent()) {
            return n.get();
        }
        throw new IllegalAccessException("the given node address was not found on table "+address.asString());
    }

    @Deprecated
    @Locking(LockingScope.EXTERNAL)
    public final boolean checkEntry(Lock lock, A address) throws InterruptedException {
        return getOptionalEntry(lock, address).isPresent();
    }
}
