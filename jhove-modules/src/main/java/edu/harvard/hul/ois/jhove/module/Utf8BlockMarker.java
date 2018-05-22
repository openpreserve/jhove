/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2004 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/

package edu.harvard.hul.ois.jhove.module;

import java.util.ArrayList;
import java.util.List;

import edu.harvard.hul.ois.jhove.Property;
import edu.harvard.hul.ois.jhove.PropertyArity;
import edu.harvard.hul.ois.jhove.PropertyType;

/**
 *
 * @author Gary McGath
 *
 */
public class Utf8BlockMarker {
    private boolean[] blocksUsed;

    public Utf8BlockMarker () {
        blocksUsed = new boolean [Utf8Block.unicodeBlock.length];
    }

    public void markBlock (int code) {
        for (int i=0; i<Utf8Block.unicodeBlock.length; i++) {
            if (Utf8Block.unicodeBlock[i].getStart () <= code &&
            Utf8Block.unicodeBlock[i].getEnd   () >= code) {
                blocksUsed[i] = true;
                break;
            }
        }
    }

    /** Returns a Property listing the blocks that have been 
     *  marked as used.  If no blocks have been marked,
     *  returns null. */
    public Property getBlocksUsedProperty (String name)
    {  
        List<String> block = new ArrayList<> (blocksUsed.length);
        for (int i=0; i<blocksUsed.length; i++) {
            if (blocksUsed[i]) {
            block.add (Utf8Block.unicodeBlock[i].getName ());
            }
        }
        if (block.isEmpty ()) {
            return null;
        }
        return new Property (name,
            PropertyType.STRING,
            PropertyArity.LIST,
            block);
    }

    /** Clears all marked blocks. */
    public void reset ()
    {
        for (int i=0; i<Utf8Block.unicodeBlock.length; i++) {
            blocksUsed[i] = false;
        }
    }
}
