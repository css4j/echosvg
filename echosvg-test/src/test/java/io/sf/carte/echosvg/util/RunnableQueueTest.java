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
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

/**
 * RunnableQueue Stress Test
 * 
 */
public class RunnableQueueTest {

	private int activeThreads;
	private Random rand;
	private RunnableQueue rq;

	private StringBuilder buf = new StringBuilder(28000);

	@Test
	public void testRunnable() throws InterruptedException {
		runTest(20);
	}

	/**
	 * Runs a new RunnableQueueTest.
	 * 
	 * @param nThreads number of runnables to queue
	 * @throws InterruptedException 
	 */
	private void runTest(int nThreads) throws InterruptedException {
		rq = RunnableQueue.createRunnableQueue();

		List<Runnable> l = new ArrayList<>(nThreads);
		rand = new Random(2345);

		// Two switch flickers to make things interesting...
		l.add(new SwitchFlicker());
		l.add(new SwitchFlicker());

		for (int i = 0; i < nThreads; i++) {
			Runnable rqRable = new RQRable(i, rand.nextInt(50) + 1);
			l.add(new TPRable(rq, i, rand.nextInt(4) + 1, rand.nextInt(500) + 1, 20, rqRable));
		}

		synchronized (this) {
			ThreadPounder tp = new ThreadPounder(l);
			tp.start();
			activeThreads = nThreads;
			while (activeThreads != 0) {
				wait();
			}
		}

	}

	void appendLn(CharSequence seq) {
		buf.append(seq).append('\n');
	}

	private class SwitchFlicker implements Runnable {
		@Override
		public void run() {
			boolean suspendp, waitp;
			int time;
			while (true) {
				try {
					synchronized (rand) {
						suspendp = rand.nextBoolean();
						waitp = rand.nextBoolean();
						time = rand.nextInt(500);
					}
					if (suspendp) {
						// 1/2 of the time suspend, 1/2 time wait, 1/2 the
						// time don't
						rq.suspendExecution(waitp);
						appendLn("Suspended - " + (waitp ? "Wait" : "Later"));
						Thread.sleep(time / 10);
					} else {
						// 1/2 the time resume
						rq.resumeExecution();
						appendLn("Resumed");
						Thread.sleep(time);
					}
				} catch (InterruptedException ie) {
				}
			}
		}
	}

	private static final int INVOKE_LATER = 1;
	private static final int INVOKE_AND_WAIT = 2;
	private static final int PREEMPT_LATER = 3;
	private static final int PREEMPT_AND_WAIT = 4;

	private class TPRable implements Runnable {

		RunnableQueue rq;
		int idx;
		int style;
		long repeatDelay;
		int count;
		Runnable rqRable;

		TPRable(RunnableQueue rq, int idx, int style, long repeatDelay, int count, Runnable rqRable) {
			this.rq = rq;
			this.idx = idx;
			this.style = style;
			this.repeatDelay = repeatDelay;
			this.count = count;
			this.rqRable = rqRable;
		}

		@Override
		public void run() {
			try {
				while (count-- != 0) {
					switch (style) {
					case INVOKE_LATER:
						synchronized (rqRable) {
							appendLn("     InvL #" + idx);
							rq.invokeLater(rqRable);
							appendLn("Done InvL #" + idx);
							rqRable.wait();
						}
						break;
					case INVOKE_AND_WAIT:
						appendLn("     InvW #" + idx);
						rq.invokeAndWait(rqRable);
						appendLn("Done InvW #" + idx);
						break;
					case PREEMPT_LATER:
						synchronized (rqRable) {
							appendLn("     PreL #" + idx);
							rq.preemptLater(rqRable);
							appendLn("Done PreL #" + idx);
							rqRable.wait();
						}
						break;
					case PREEMPT_AND_WAIT:
						appendLn("     PreW #" + idx);
						rq.preemptAndWait(rqRable);
						appendLn("Done PreW #" + idx);
						break;
					}

					if (repeatDelay < 0)
						break;
					Thread.sleep(repeatDelay);
				}
			} catch (InterruptedException ie) {
			}
			synchronized (RunnableQueueTest.this) {
				activeThreads--;
				RunnableQueueTest.this.notify();
			}
		}
	}

	private class RQRable implements Runnable {
		int idx;
		long dur;

		RQRable(int idx, long dur) {
			this.idx = idx;
			this.dur = dur;
		}

		@Override
		public void run() {
			try {
				appendLn("      B Rable #" + idx);
				Thread.sleep(dur);
				appendLn("      E Rable #" + idx);
				synchronized (this) {
					notify();
				}
			} catch (InterruptedException ie) {
			}
		}
	}

}
