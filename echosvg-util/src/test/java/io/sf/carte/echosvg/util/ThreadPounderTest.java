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

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class ThreadPounderTest {

	private StringBuilder buf = new StringBuilder(32);

	@Disabled
	@Test
	public void testThreadPounder() throws InterruptedException {
		final int sz = 200;
		List<Runnable> l = new ArrayList<>(sz);
		for (int i = 0; i < sz; i++) {
			final int x = i;
			l.add(new Runnable() {
				@Override
				public void run() {
					appendLn("Thread " + x);
				}
			});
		}

		ThreadPounder tp = new ThreadPounder(l);
		appendLn("Starting:");
		tp.start();
		appendLn("All Started:");
		System.out.println(buf);
	}

	void appendLn(CharSequence seq) {
		synchronized (buf) {
			buf.append(seq).append('\n');
		}
	}

}
