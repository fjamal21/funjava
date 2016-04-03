package com.algorithm.datastructures;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class PersistentTreap<T> extends BinarySearchTree<PersistentTreap.Node<T>, T> implements SSet<T> {
    /**
     * A random number source
     */
    Random rand;

    /**
     * The current version number - increment this after every add(x) or
     * remove(x) operation
     */
    int version;
    List<Node<T>> vr = new ArrayList<Node<T>>();

    protected static class Node<T> extends BinarySearchTree.BSTNode<Node<T>, T> {
	int p;
    }

    public PersistentTreap(Comparator<T> c) {
	super(new Node<T>(), c);
	rand = new Random();
    }

    public PersistentTreap() {
	this(new DefaultComparator<T>());
    }

    public boolean add(T x) {
	Node<T> u = newNode();
	u.x = x;
	u.p = rand.nextInt();
	Node<T> p = findLast(u.x); // findLast() has been overriden to add persistence feature to it
	boolean isAdd = addChild(p, u);
	if (isAdd) {
	    bubbleUp(u); // changes induced by bubble (rotations) now wont matter, because we have persisted the dynamic chain / path
	    version++; // changes for first assignment
	    return true;
	}

	vr.remove(vr.get(vr.size() - 1)); // don't save since it didn't add anything (can be optimized)
	return false;
    }

    protected Node<T> findLast(T x) {
	// Make copy of root, and version it (thus persisting path)
	// Since path from root to node being inserted is cloned, the space required is log(n)
	Node<T> rd = cloneNode(r, nil);
	vr.add(rd);

	// Make copy of path from root to value being inserted / deleted as we go (thus limiting it to constant time)
	Node<T> w = r, prev = nil;
	Node<T> wd = rd;
	while (w != nil) {
	    prev = w;
	    int comp = c.compare(x, (T) w.x);
	    if (comp < 0) {
		w = (Node<T>) w.left;
		wd.left = cloneNode(w, wd);
		wd = (Node<T>) wd.left;
	    } else if (comp > 0) {
		w = (Node<T>) w.right;
		wd.right = cloneNode(w, wd);
		wd = (Node<T>) wd.right;
	    } else {
		wd = cloneNode(w, wd);
		wd.left = wd.right = nil;
		return w;
	    }
	}
	return prev;
    }

    private Node<T> cloneNode(Node<T> orig, Node<T> parent) {
	if (orig == null) {
	    return null; // first insert ever or empty tree
	}
	Node<T> copy = new Node<T>();
	copy.x = orig.x;
	copy.p = orig.p;
	copy.left = orig.left;
	copy.right = orig.right;
	copy.parent = parent; // setting up parent as the cloned parent
	return copy;
    }

    protected void bubbleUp(Node<T> u) {
	while (u.parent != nil && u.parent.p > u.p) {
	    if (u.parent.right == u) {
		rotateLeft(u.parent);
	    } else {
		rotateRight(u.parent);
	    }
	}
	if (u.parent == nil) {
	    r = u;
	}
    }

    public boolean remove(T x) {
	Node<T> u = findLast(x);
	if (u != nil && c.compare(u.x, x) == 0) {
	    trickleDown(u);
	    splice(u);
	    version++; // changes for first assignment
	    return true;
	}
	vr.remove(vr.get(vr.size())); // don't save since it didn't remove anything (can be optimized)
	return false;
    }

    /**
     * Do rotations to make u a leaf
     */
    protected void trickleDown(Node<T> u) {
	while (u.left != nil || u.right != nil) {
	    if (u.left == nil) {
		rotateLeft(u);
	    } else if (u.right == nil) {
		rotateRight(u);
	    } else if (u.left.p < u.right.p) {
		rotateRight(u);
	    } else {
		rotateLeft(u);
	    }
	    if (r == u) {
		r = u.parent;
	    }
	}
    }

    public int getCurrentVersion() {
	return version;
    }

    /**
     * Runs find(x) on version v of the treap
     * 
     * @param v
     * @param x
     */
    public T find(int v, T x) {
	if (v < 0 || v > version)
	    throw new IndexOutOfBoundsException();
	Node<T> versionedRoot = (Node<T>) vr.get(v - 1);
	Node<T> w = versionedRoot;
	while (w != null && w != nil) { // we do version nulls (empty list)
	    int comp = c.compare(x, (T) w.x);
	    if (comp < 0) {
		w = (Node<T>) w.left;
	    } else if (comp > 0) {
		w = (Node<T>) w.right;
	    } else {
		return (T) w.x;
	    }
	}
	return null;
    }

    // Just for testing
    public String findElementPath(T x, int version) {
	Node<T> w = r;
	if (version != -1) {
	    w = (Node<T>) vr.get(version - 1);
	}
	if (w == null) {
	    return "not found (this version has null root)";
	}
	StringBuilder sb = new StringBuilder();
	if (c.compare(r.x, x) == 0)
	    return "" + r.x;
	sb.append(r.x + " -> ");
	while (w != null && w != nil) {
	    int comp = c.compare(x, (T) w.x);
	    if (comp < 0) {
		w = (Node<T>) w.left;
	    } else if (comp > 0) {
		w = (Node<T>) w.right;
	    } else {
		return sb.append("" + w.x).toString();
	    }
	    if (w != null && w != nil)
		sb.append(w.x + " -> ");
	}
	return "not found";
    }
}
