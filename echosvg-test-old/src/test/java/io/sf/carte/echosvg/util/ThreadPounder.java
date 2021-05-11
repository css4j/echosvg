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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * The purpose of this class is to invoke a series of runnables as closely to
 * synchronously as possible. It does this by starting a thread for each one,
 * getting the threads into there run method, then quickly running through (in
 * random order) and notifying each thread.
 */
public class ThreadPounder {
	List<Runnable> runnables;
	Object[] threads;
	Object lock = new Object();

	public ThreadPounder(List<Runnable> runnables) throws InterruptedException {
		this(runnables, new Random(1234));
	}

	public ThreadPounder(List<Runnable> runnables, Random rand) throws InterruptedException {
		this.runnables = new ArrayList<>(runnables);
		Collections.shuffle(this.runnables, rand);
		threads = new Object[this.runnables.size()];
		int i = 0;
		Iterator<Runnable> iter = this.runnables.iterator();
		synchronized (lock) {
			while (iter.hasNext()) {
				Thread t = new SyncThread(iter.next());
				t.start();
				lock.wait();
				threads[i] = t;
				i++;
			}
		}
	}

	public void start() {
		synchronized (this) {
			this.notifyAll();
		}

	}

	class SyncThread extends Thread {
		Runnable toRun;
		public long runTime;

		public SyncThread(Runnable toRun) {
			this.toRun = toRun;
		}

		@Override
		public void run() {
			try {
				synchronized (ThreadPounder.this) {
					synchronized (lock) {
						// Let pounder know I'm ready to go
						lock.notify();
					}
					// Wait for pounder to wake me up.
					ThreadPounder.this.wait();
				}
				toRun.run();
			} catch (InterruptedException ie) {
			}
		}
	}

}
