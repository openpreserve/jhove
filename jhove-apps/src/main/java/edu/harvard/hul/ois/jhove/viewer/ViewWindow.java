/**********************************************************************
 * Jhove - JSTOR/Harvard Object Validation Environment
 * Copyright 2004-2012 by JSTOR and the President and Fellows of Harvard College
 **********************************************************************/


package edu.harvard.hul.ois.jhove.viewer;

import java.awt.Dimension;
import java.io.*;
import javax.swing.*;
import javax.swing.tree.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.*;
import edu.harvard.hul.ois.jhove.*;

/**
 *   This is the main window for viewing the results of a file analysis.
 *   Multiple instances of InfoWindow are allowed; they are deleted
 *   when closed.  The URI of the analyzed object is used as the window
 *   title.  A JTree is used to display the information.
 * 
 *   The earlier version of this class displayed a single file.  This
 *   version is controlled by a ViewHandler, which displays multiple
 *   files within a single window.  There can still be multiple
 *   ViewWindows, but each will come from a separate operation.
 *   Dragging a directory to the main window will produce a single
 *   ViewWindow; dragging a group of files will produce
 *   multiple windows.
 */
public class ViewWindow extends InfoWindow {

    private java.util.List<RepInfo> _info;
    private JMenuItem _closeAllItem;
    private ActionListener _closeAllListener;
    private DefaultMutableTreeNode _rootNode;
    private JTree tree;

    /**
     *  Constructor.
     * 
     *  @param app    The associated App object.
     *  @param base   The JhoveBase object for the application.
     *  @param jhwin  The main JhoveWindow.
     */  
    public ViewWindow (App app, JhoveBase base, JhoveWindow jhwin) 
    {
        super ("JHOVE Results", app, base);
        setSaveActionListener (e -> saveInfo ());

        _info = new LinkedList<> ();
        // The root element should no longer be a 
        // RepTreeRoot, but some other flavor of 
        // DefaultMutableTreeNode.  It will have RepTreeRoots
        // (now somewhat misleadingly named) added to it.
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Documents");
        _rootNode = root;
        TreeModel treeModel = new DefaultTreeModel (root);
        tree = new JTree ();
        tree.setModel (treeModel);
        tree.setShowsRootHandles (true);
        TreeCellRenderer rend = tree.getCellRenderer ();
        if (rend instanceof DefaultTreeCellRenderer) {
            // it should be
            DefaultTreeCellRenderer trend =
                (DefaultTreeCellRenderer) rend;
            trend.setOpenIcon (null);
            trend.setClosedIcon (null);
            trend.setLeafIcon (null);
        }
        JScrollPane scrollPane = new JScrollPane (tree);
        getContentPane ().add (scrollPane, "Center");

        // Add a small panel at the bottom, since on some OS's there
        // may be stuff near the bottom of a window which will conflict
        // with the scroll bar.
        JPanel panel = new JPanel ();
        panel.setMinimumSize (new Dimension (8, 8));
        getContentPane ().add (panel, "South");

        setDefaultCloseOperation (WindowConstants.DISPOSE_ON_CLOSE);
        setSize (400, 600);

        // Set up to handle "close all documents" from main window
        if (jhwin != null) {
            _closeAllItem = jhwin.getCloseAllItem ();
            _closeAllListener = e -> closeFromMenu ();
            _closeAllItem.addActionListener (_closeAllListener);

        }
    }

    /** Appends the representation of a RepInfo object to the 
     *  tree.  The RepInfo object is saved into a list so that 
     *  the window contents can be saved to a file later.
     */
    public void addRepInfo (RepInfo info, JhoveBase base)
    {
        _info.add (info);
        RepTreeRoot node = new RepTreeRoot (info, base);
        _rootNode.add (node);
    }

    /** Expands the tree appropriately when everything is build. */
    public void expandRows ()
    {
        tree.expandRow (0);
        if (tree.getRowCount() == 2) {
            // Just one file -- expand it
            tree.expandRow (1);
        }
    }

    /**
     * Saves the information to a file specified by the user. 
     */
    private void saveInfo ()
    {
        PrintWriter wtr = doSaveDialog ();
        if (wtr == null) {
            return;
        }
        OutputHandler handler;
        try {
            handler = selectHandler ();
            handler.reset ();
            handler.setWriter (wtr);
            handler.showHeader ();
            for (RepInfo info : _info) {
                handler.show (info);
            }
            handler.showFooter ();
            wtr.close ();
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog
                (this, 
                e.getMessage(), 
                "Error writing file", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Invoked when the "Close" menu item is selected.
     *  Overrides the parent class's method to delete
     *  the window rather than hiding it. */
    @Override
    protected void closeFromMenu ()
    {
        super.closeFromMenu ();
        if (_closeAllItem != null) {
            _closeAllItem.removeActionListener (_closeAllListener);
        }
        dispose ();
    }
}
