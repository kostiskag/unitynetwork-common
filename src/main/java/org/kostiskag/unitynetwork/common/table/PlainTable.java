package org.kostiskag.unitynetwork.common.table;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

public abstract class PlainTable<E extends Comparable> {
    protected static final int TIMEOUT_SECONDS = 5;
    protected final Set<E> nodes;
    protected final Lock orb = new ReentrantLock(true);

    protected PlainTable() {
        nodes = new TreeSet<E>((a, b) -> a.compareTo(b));
    }

    protected PlainTable(Collection<E> in) {
        nodes = new TreeSet<E>((a, b) -> a.compareTo(b));
        nodes.addAll(in);
    }

    /**
    WE USED to have synchronized methods for table access but this
    approach was found to be ineffective as one calling thread was
    frequently in the need to call several methods before
    making a complete and meaningful action on the table like ex.
    1. check if bn named "pakis" exists
    2. if yes get me its instance

    with sync methods there was no guarantee that after method 1 another thread wouldn't
    have deleted "pakis" before 2 was called!

    SO NOW, the caller gets the orb, does his action by calling one or several methods
    gives the orb back when he completes his action!

    This method also lets the caller use Optionals and streams as by their nature there was no
    guarantee for which point in time the caller would decide to consume one!

    To improve matters further and to be sure the caller owns the orb before calling
    anything, he has to also pass it as argument in the calling method!

    every caller should do :
    try {
        Lock l = aquireLock();
        findSmth(lock, args);
        findAnotherSmth(lock, args);
    } catch interupted {
        log("unbelivable!!!");
    } finally {
        readLock.unlock();
    }

    The inner method on its turn has to validate the lock to ensure it was not called from
    a caller without having a lock

    public int getSmth(Lock lock, String name) throws InterruptedException {
        validateLock(lock);
        do stuff...
        return ...
    }
	**/
    public Lock aquireLock() throws InterruptedException {
        orb.tryLock(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        return orb;
    }

    /**
     * To be used inside a finally block
     */
    public void releaseLock() {
        orb.unlock();
    }

    /**
     * To be called by all inner methods
     * Ensures the lock is called before attempting to call a method
     *
     * @param lock
     * @throws InterruptedException
     */
    protected void validateLock(Lock lock) throws InterruptedException {
        if (lock != orb) {
            throw new InterruptedException("the given lock is not the BNtable's orb!");
        }
    }

    //accessors!
    public int getSize(Lock lock) throws InterruptedException {
        validateLock(lock);
        return nodes.size();
    }

    protected Stream<E> getStream() {
        return nodes.stream();
    }

    public Optional<E> getOptionalNodeEntry(Lock lock, E toBeChecked) throws InterruptedException {
        validateLock(lock);
        return nodes.stream()
                .filter(e -> e.equals(toBeChecked))
                .findFirst();
    }

    //since there are locks now getOptionalNodeEntry and getOptionalNodeEntry should be favoured instead
    //to use optional to check use Optional<N> n; n.isPresent();
    @Deprecated
    public boolean isOnline(Lock lock, E toBeChecked) throws InterruptedException {
        return getOptionalNodeEntry(lock, toBeChecked).isPresent();
    }

    //to use optional to get use Optional<N> n; n.get();
    @Deprecated
    public E getNodeEntry(Lock lock, E toBeChecked) throws IllegalAccessException, InterruptedException {
        validateLock(lock);
        Optional<E> e = getOptionalNodeEntry(lock, toBeChecked);
        if (e.isPresent()) {
            return e.get();
        }
        throw new IllegalAccessException("the given node was not found on table "+toBeChecked);
    }

}
