/*

   See the NOTICE file distributed with this work for additional
   information regarding copyright ownership.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package io.sf.carte.echosvg.util;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A reference queue cleaner thread.
 * <p>
 * When the objects in the {@link ReferenceQueue} returned by
 * {@link #getReferenceQueue()} implement the {@link ReferenceCleared}
 * interface, they are {@link ReferenceCleared#cleared() cleared()} by the
 * {@code CleanerThread}.
 * </p>
 * <p>
 * The cleaner thread can be disposed by using the {@link #shutdown()} method or
 * by a timeout. The timeout only applies after a first reference is obtained
 * from the {@link ReferenceQueue}, and it can be set with
 * {@link #setTimeout(long)}.
 * </p>
 *
 * @author <a href="mailto:deweese@apache.org">l449433</a>
 * @author Carlos Amengual
 * @author For later modifications, see Git history.
 * @version $Id$
 */
public class CleanerThread extends Thread {

	private static long queueTimeoutMs = 45000;

	private static final ReentrantLock queueLock = new ReentrantLock();
	private static ReferenceQueue<Object> queue = null;
	private static volatile CleanerThread thread = null;
	private static volatile boolean newCaller = false;

	/**
	 * Get a reference queue that is being cleaned by a {@code CleanerThread}.
	 * 
	 * @return the reference queue.
	 */
	public static ReferenceQueue<Object> getReferenceQueue() {
		newCaller = true;
		queueLock.lock();
		try {
			if (thread == null) {
				if (queue == null) {
					queue = new ReferenceQueue<>();
				}
				thread = new CleanerThread();
				thread.start();
			}
		} finally {
			queueLock.unlock();
		}
		return queue;
	}

	/**
	 * If objects registered with the reference queue associated with this class
	 * implement this interface then the 'cleared' method will be called when the
	 * reference is queued.
	 */
	public interface ReferenceCleared {
		/* Called when the reference is cleared */
		void cleared();
	}

	/**
	 * A SoftReference subclass that automatically registers with the cleaner
	 * ReferenceQueue.
	 */
	public abstract static class SoftReferenceCleared<T> extends SoftReference<T> implements ReferenceCleared {
		public SoftReferenceCleared(T o) {
			super(o, CleanerThread.getReferenceQueue());
		}
	}

	/**
	 * A WeakReference subclass that automatically registers with the cleaner
	 * ReferenceQueue.
	 */
	public abstract static class WeakReferenceCleared<T> extends WeakReference<T> implements ReferenceCleared {
		public WeakReferenceCleared(T o) {
			super(o, CleanerThread.getReferenceQueue());
		}
	}

	/**
	 * A PhantomReference subclass that automatically registers with the cleaner
	 * ReferenceQueue.
	 */
	public abstract static class PhantomReferenceCleared<T> extends PhantomReference<T> implements ReferenceCleared {
		public PhantomReferenceCleared(T o) {
			super(o, CleanerThread.getReferenceQueue());
		}
	}

	protected CleanerThread() {
		super("EchoSVG CleanerThread");
		setDaemon(true);
	}

	@Override
	public void run() {
		long timeoutMs = 0;
		while (thread != null) {
			newCaller = false;
			try {
				Reference<?> ref;
				try {
					ref = queue.remove(timeoutMs);
				} catch (InterruptedException ie) {
					break;
				}

				if (ref == null) {
					// If no reference was found before timeout AND nobody
					// has called getReferenceQueue() meanwhile, shutdown.
					if (!newCaller) {
						shutdownMe(this);
						break;
					} else {
						timeoutMs = 0;
					}
				} else {
					if (ref instanceof ReferenceCleared) {
						ReferenceCleared rc = (ReferenceCleared) ref;
						rc.cleared();
					}
					timeoutMs = queueTimeoutMs;
				}
			} catch (ThreadDeath td) {
				throw td;
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}

	/**
	 * Shut down the cleaner thread.
	 * <p>
	 * Web apps are encouraged to call this method explicitly when shutting down.
	 * </p>
	 */
	public static void shutdown() {
		queueLock.lock();
		try {
			if (thread != null) {
				thread.interrupt();
				thread = null;
			}
			queue = null;
		} finally {
			queueLock.unlock();
		}
	}

	private static void shutdownMe(CleanerThread shutdownThread) {
		queueLock.lock();
		try {
			/*
			 * Other threads may have called 'shutdown()' and 'getReferenceQueue()' right
			 * before this method being invoked, so to avoid the potential race condition
			 * (admittedly a very small chance) only reset the queue if the current static
			 * thread is the same as the argument CleanerThread.
			 */
			if (thread == shutdownThread) {
				thread = null;
			}
		} finally {
			queueLock.unlock();
		}
	}

	/**
	 * Set the timeout of the cleaner thread, in milliseconds.
	 * <p>
	 * The timeout will only take effect <i>after</i> a first reference is pulled
	 * from the queue by the cleaner thread.
	 * </p>
	 * <p>
	 * If the timeout is zero, the thread will never time out. Even if the timeout
	 * is non-zero, you are encouraged to call {@link #shutdown()} to terminate the
	 * cleaner thread.
	 * </p>
	 * <p>
	 * The default timeout is of 45 seconds.
	 * </p>
	 * 
	 * @param timeoutMs the timeout in milliseconds.
	 * @throws IllegalArgumentException If the value of the timeout argument is
	 *                                  negative.
	 */
	public static void setTimeout(long timeoutMs) {
		if (timeoutMs < 0) {
			throw new IllegalArgumentException("Negative timeout value");
		}

		CleanerThread.queueTimeoutMs = timeoutMs;
	}

}
