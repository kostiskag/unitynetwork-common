package org.kostiskag.unitynetwork.common.routing;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author Konstantinos Kagiampakis
 */
public final class QueueManager<E> {
	private final int maxCapacity;
	private final int maxWaitTime;
	private final Queue<E> queue;
	private final AtomicBoolean kill = new AtomicBoolean(false);
	
	/**
	 * This constructor can be used from the bluenode and for each local rednode
	 * or bluenode instance.
	 *
	 * @param maxCapacity
	 * @param maxWaitTimeSec
	 */
	public QueueManager(int maxCapacity, int maxWaitTimeSec) {
		this.maxCapacity = maxCapacity;
		this.maxWaitTime = maxWaitTimeSec * 1000;
		this.queue = new PriorityQueue();
	}

	public synchronized int getlen() {
		return queue.size();
	}

	public synchronized int getSpace() {
		if (queue.size() <  maxCapacity) {
			return maxCapacity - queue.size();
		} else {
			return 0;
		}		
	}
	
	public synchronized boolean hasSpace() {
		if (queue.size() <  maxCapacity) {
			return true;
		} else {
			return false;
		}	
	}
	
	/**
	 * Offer may make the calling thread to WAIT until empty
	 * 
	 * @param data
	 */
	public synchronized void offer(E data) {
		while (queue.size() == maxCapacity && !kill.get()) {
			try {
				wait();
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
		if (kill.get()) {
			return;
		}
		queue.add(data);
		notify();
	}

	/**
	 * A thread which is not allowed to wait may call this first. This method
	 * REPLACES the oldest packet with a new one if the queue is full
	 * 
	 * @return
	 */
	public synchronized void offerNoWait(E data) {
		if (queue.size() < maxCapacity) {
			queue.add(data);
			notify();
		} else {
			queue.poll();
			queue.add(data);
			notify();
		}
	}

	/**
	 * In order to not poll something which may not be
	 * later sent we can poll first. Determine where the packet goes
	 * and then poll.
	 * 
	 * @return
	 */
	public synchronized E peek() {
		while (queue.isEmpty() && !kill.get()) {
			try {
				wait();
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
		if (kill.get()) {
			return null;
		}
		E data = queue.peek();
		return data;
	}
	
	public synchronized E poll() {
		while (queue.isEmpty() && !kill.get()) {
			try {
				wait();
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}

		if (kill.get()) {
			return null;
		}

		E data = queue.poll();
		notify();
		return data;
	}
	
	public synchronized E pollWithTimeout() throws Exception {
		while (queue.isEmpty() && !kill.get()) {
			try {
				wait(maxWaitTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (queue.isEmpty()) {
				throw new Exception("empty");
			}
		}

		if (kill.get()) {
			return null;
		}

		E data = queue.poll();
		notify();
		return data;
	}

	public synchronized void clear() {
		queue.clear();
		notifyAll();
	}

	public synchronized void exit() {
		kill.set(true);
		queue.clear();
		notifyAll();
	}

}
