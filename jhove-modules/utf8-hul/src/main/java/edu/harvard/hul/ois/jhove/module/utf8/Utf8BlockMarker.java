/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2004 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove.module.utf8;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.harvard.hul.ois.jhove.Property;
import edu.harvard.hul.ois.jhove.PropertyArity;
import edu.harvard.hul.ois.jhove.PropertyType;

/**
 *
 * @author Gary McGath
 *
 */
public class Utf8BlockMarker {
	private Set<Utf8Block> blocksUsed;

    public Utf8BlockMarker () {
        this.blocksUsed = new HashSet<>();
    }

    public void markBlock (int code) {
        Utf8Block block = Utf8Block.blockFromInt(code);
        if (block == null) {
            return;
        }
        this.blocksUsed.add(block);
    }

    /** Returns a Property listing the blocks that have been 
     *  marked as used.  If no blocks have been marked,
     *  returns null. */
    public Property getBlocksUsedProperty (String name)
    {  
        if (this.blocksUsed.isEmpty()) {
            return null;
        }
        List<String> blocks = new ArrayList<>();
        for (Utf8Block block : EnumSet.copyOf(this.blocksUsed)) {
            blocks.add(block.name);
        }
        return new Property(name,
                            PropertyType.STRING,
                            PropertyArity.LIST,
                            blocks);
    }

    /** Clears all marked blocks. */
    public void reset ()
    {
        this.blocksUsed.clear();
    }
}
