package edu.harvard.hul.ois.jhove.module.utf8;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class Utf8BlockTests {

	/** This test makes sure that the code blocks are declared in monotonically
	 *  increasing order without overlap, each one has a positive range,
	 *  and names aren't duplicated.
	 */
	@Test
	public void testBlocks() {
		int lastEnd = -1;
		Set<String> names = new HashSet<>();
		for (Utf8Block blk : Utf8Block.values()) {
			int start = blk.start;
			int end = blk.end;
			String name = blk.name;
			String note = "Bad block: " + name + ",  " + 
					String.format ("%05X", Integer.valueOf(start)) + " = " +
					String.format ("%05X", Integer.valueOf(end));
			assertTrue (note, end > start);
			assertTrue (note, start > lastEnd);
			assertFalse ("Duplicate name " + name, names.contains(name));
			lastEnd = end;
			names.add (name);
		}
	}

}
