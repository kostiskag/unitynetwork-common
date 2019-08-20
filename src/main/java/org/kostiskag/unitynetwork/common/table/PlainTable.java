package org.kostiskag.unitynetwork.common.table;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;


/**
 * WE USED to have synchronized methods for table access but this
 * approach was found to be ineffective as one calling thread was
 * frequently in the need to call several methods before
 * making a complete and meaningful action on the table like ex.
 * 1. check if bn named "pakis" exists
 * 2. if yes get me its instance
 *
 * with sync methods there was no guarantee that after method 1 another thread wouldn't
 * have deleted "pakis" before 2 was called!
 *
 * SO NOW, the caller gets the orb, does his action by calling one or several methods
 * gives the orb back when he completes his action!
 *
 * This method also lets the caller use Optionals and streams as by their nature there was no
 * guarantee for which point in time the caller would decide to consume one!
 *
 * To improve matters further and to be sure the caller owns the orb before calling
 * anything, he has to also pass it as argument in the calling method!
 *
 * Contract to follow!!!:
 *
 * When a method is marked as  : @Locking(LockScope.EXTERNAL)
 * this is what a caller should do:
 *
 * try {
 *     Lock l = aquireLock();
 *     findSmth(lock, args);
 *     findAnotherSmth(lock, args);
 * } catch interupted {
 *     log("unbelivable!!!");
 * } finally {
 *     readLock.unlock();
 * }
 *
 * The inner method on its turn has to validate the lock to ensure it was not called from
 * a caller without having a lock
 *
 * public int getSmth(Lock lock, String name) throws InterruptedException {
 *    validateLock(lock);
 *    do stuff...
 *    return ...
 * }
 *
 * if you want in the subclass you can build a method with an internal locking
 * in this case annotate the method with
 *
 * @Locking(LockingScope.INTERNAL)
 *
 *
 * Elements:
 *
 * @param <E> the elements of this Table should implement Comparable!
 */
public abstract class PlainTable<E extends Comparable> implements AutoCloseable {
    protected static final TimeUnit TIMEOUT_UNIT = TimeUnit.SECONDS;
    protected static final int TIMEOUT_SECONDS = 5;

    private final Lock orb = new ReentrantLock(true);
    protected final Set<E> nodes;

    protected enum LockingScope {
        INTERNAL, EXTERNAL, NO_LOCK;
    }

    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.METHOD)
    protected @interface Locking {
        LockingScope value() default LockingScope.EXTERNAL;
    }

    protected PlainTable() {
        nodes = new TreeSet<E>((a, b) -> a.compareTo(b));
    }

    protected PlainTable(Collection<E> in) {
        nodes = new TreeSet<E>((a, b) -> a.compareTo(b));
        nodes.addAll(in);
    }

    public final Lock aquireLock() throws InterruptedException {
        orb.tryLock(TIMEOUT_SECONDS, TIMEOUT_UNIT);
        return orb;
    }

    /**
     * To be used inside a finally block
     *
     */
    public final void releaseLock() {
        orb.unlock();
    }

    /**
     * To be used with a try with resources block
     *
     * @throws Exception
     */
    @Override
    public final void close() throws Exception {
        releaseLock();
    }

    /**
     * To be called by all inner methods
     * Ensures the lock is called before attempting to call a method
     *
     * @param lock
     * @throws InterruptedException
     */
    protected final void validateLock(Lock lock) throws InterruptedException {
        if (lock.equals(orb)) {
            throw new InterruptedException("the given lock is not the BNtable's orb!");
        }
    }

    /**
     * To be used by a method of the subclass
     * the using method is responsible for the lock!
     *
     * @return
     */
    @Locking(LockingScope.NO_LOCK)
    protected Stream<E> getStream() {
        return nodes.stream();
    }

    /**
     * To be used by a method of the subclass
     * the using method is responsible for the lock!
     *
     * @return
     */
    @Locking(LockingScope.NO_LOCK)
    protected int getSize() throws InterruptedException {
        return nodes.size();
    }

    @Locking(LockingScope.EXTERNAL)
    public Optional<E> getOptionalEntry(Lock lock, E toBeChecked) throws InterruptedException {
        validateLock(lock);
        return nodes.stream()
            .filter(e -> e.equals(toBeChecked))
            .findFirst();
    }

    /**
     * since there are locks now, getOptionalEntry should be favoured instead
     * since it is based in Optionals
     *
     */
    @Deprecated
    @Locking(LockingScope.EXTERNAL)
    public boolean isOnline(Lock lock, E toBeChecked) throws InterruptedException {
        return getOptionalEntry(lock, toBeChecked).isPresent();
    }

    //recommended to use method is
    @Deprecated
    @Locking(LockingScope.EXTERNAL)
    public E getNodeEntry(Lock lock, E toBeChecked) throws IllegalAccessException, InterruptedException {
        validateLock(lock);
        Optional<E> e = getOptionalEntry(lock, toBeChecked);
        if (e.isPresent()) {
            return e.get();
        }
        throw new IllegalAccessException("the given node was not found on table "+toBeChecked);
    }
}
