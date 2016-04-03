package com.algorithm.datastructures;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the Patricia Trie data structure. This is a tree whose
 * internal nodes all have 2 or more children and whose edges are labelled with
 * strings. Each leaf, v, corresponds to a string stored in the trie that can be
 * obtained by concatenating the labels of all edges on the path from the root
 * to v.
 */
public class PatriciaTrie {

    protected static class Edge {
	Node target;
	PString label;

	public Edge(Node target, PString label) {
	    this.target = target;
	    this.label = label;
	}
    }

    protected static class Node {
	Edge[] edges; // the children of this node
	int nc; // the number of children
	boolean end;

	public Node() {
	    edges = new Edge[128];
	    nc = 0;
	    end = false;
	}
    }

    /**
     * The number of strings stored in the trie
     */
    protected int n;

    /**
     * The root
     */
    protected Node r;

    public PatriciaTrie() {
	r = new Node();
	n = 0;
    }

    /**
     * Add the x to this trie
     * 
     * @param x
     *            the string to add
     * 
     * @return true if x was successfully added or false if x is already in the
     *         trie
     */
    public boolean add(PString x) {
	// added by Ferhan
	Node current = r;
	int sz = x.length();
	for (int i = 0; i < sz; i++) {
	    PString ch = x.subString(i, 1);
	    if (current.edges[x.charAt(i)] != null) {
		current = current.edges[x.charAt(i)].target;
		if (i == sz - 1 && !current.end)
		    return false; // already exists, return false
		else if (i == sz - 1) {
		    current.end = true;
		    return true;
		}
	    } else {
		current.edges[x.charAt(i)] = new Edge(new Node(), ch);
		current.nc++;
		current = current.edges[x.charAt(i)].target;
		if (i == sz - 1) {
		    current.end = true;
		    return true;
		}
	    }
	}
	return false; // should never happen
    }

    /**
     * Remove x from this trie
     * 
     * @param x
     *            the string to remove
     * 
     * @return true if x was successfully removed or false if x is not stored in
     *         the trie
     */
    public boolean remove(PString x) {
	// added by Ferhan
	Node current = r;
	for (int i = 0; i < x.length(); i++) {
	    if (current.edges[x.charAt(i)] != null) {
		current = current.edges[x.charAt(i)].target;
	    } else {
		return false;
	    }
	    if (i == x.length() - 1) {
		// delete the string x.
		current.end = false;
		return true;
	    }
	}
	return false;
    }

    /**
     * Return the PString stored in this trie that is equal to x
     * 
     * @param x
     * 
     * @return a PString equal x if x was found and null otherwise
     */

    public PString search(PString x) {
	// added by Ferhan
	Node current = r;
	for (int i = 0; i < x.length(); i++) {
	    if (current.edges[x.charAt(i)] != null) {
		current = current.edges[x.charAt(i)].target;
	    } else {
		return null; /// PString not found, and no further edges to explore
	    }
	    if (i == x.length() - 1 && current.end) {
		return x; // PString was found
	    }
	}
	return null;
    }

    /**
     * Find a string whose prefix matches x
     * 
     * @return a PString y such that the prefix of y is equal to x or null if no
     *         such string exists
     */
    public PString prefixSearch1(PString x) {
	// added by Ferhan
	PString[] pStrings = prefixSearchMany(x);
	if (pStrings == null)
	    return null;
	return pStrings[0];
    }

    /**
     * Find all strings whose prefix matches x
     * 
     * @param x
     * 
     * @return an array containing every string whose prefix matches x
     */
    public PString[] prefixSearchMany(PString x) {
	// added by Ferhan
	Node current = r;
	for (int i = 0; i < x.length(); i++) {
	    if (current.edges[x.charAt(i)] != null) {
		current = current.edges[x.charAt(i)].target;
	    } else {
		return null; // prefix not exhausted, and no further edges to explore
	    }
	}
	List<PString> pStrings = new ArrayList<PString>();
	PString[] pStringArray;
	String s = new String(x.data); // TODO: Add append/concatenate to PString to avoid doing this, for now its fine I guess.
	findAllChildPStrings(current, pStrings, s);
	pStringArray = new PString[pStrings.size()];

	return pStrings.toArray(pStringArray);
    }

    /**
     * Below method will find all the childPString
     * 
     * @param node
     * @param pStrings
     * @param currentString
     */
    private void findAllChildPStrings(Node node, List<PString> pStrings, String currentString) {
	// added by Ferhan
	if (node.end)
	    pStrings.add(new PString(currentString));
	for (int i = 0; i < node.edges.length; i++) {
	    if (node.edges[i] != null)
		findAllChildPStrings(node.edges[i].target, pStrings, currentString + ((char) i));
	}
    }
}
