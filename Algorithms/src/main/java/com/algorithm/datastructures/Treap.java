package com.algorithm.datastructures;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;

public class Treap<T> extends BinarySearchTree<Treap.Node<T>, T> implements SSet<T> {
    /**
     * A random number source
     */
    Random rand;

    protected static class Node<T> extends BinarySearchTree.BSTNode<Node<T>, T> {
	int p;
    }

    public Treap(Comparator<T> c) {
	super(new Node<T>(), c);
	rand = new Random();
    }

    public Treap() {
	this(new DefaultComparator<T>());
    }

    public Treap(T[] a) {
	this(a, new DefaultComparator<T>());
    }

    public boolean add(T x) {
	Node<T> u = newNode();
	u.x = x;
	u.p = rand.nextInt();
	if (super.add(u)) {
	    bubbleUp(u);
	    return true;
	}
	return false;
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
	    return true;
	}
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

    /**
     * Remove all elements greater than or equal to x from this treap. Return a
     * new treap that contains all the elements greater or equal to x
     * 
     * @param x
     * @return a Treap containing all elements greater than x
     */
    public Treap<T> split(T x) {
	Treap<T> t = new Treap<T>(c);
	// changes made by Ferhan in this method
	// need to check whether it will run in O(log n time) or not. can
	// optimize it better, will check later on.
	Iterator<T> originalIterator = iterator();
	while (originalIterator.hasNext()) {
	    T s = originalIterator.next();
	    if (s.equals(x)) {
		remove(s);
		break;
	    } else if (Integer.parseInt(x.toString()) < Integer.parseInt(s.toString())) {
		break;
	    }
	    remove(s);
	}

	Iterator<T> tt = iterator();
	while (tt.hasNext()) {
	    t.add(tt.next());
	}

	return t;
    }

    /**
     * Merge this treap and t into a single treap, emptying t in the process
     * Precondition: Every element in t is greater than every element in this
     * treap
     * 
     * @param t
     * @return true if successful and false if the precondition is not satisfied
     */
    public boolean merge(Treap<T> t) {
	// changes made by Ferhan in this method

	// Get last node for treap
	Iterator<T> treapIterator = iterator();
	T lastNodeOfTreap;
	do {
	    lastNodeOfTreap = treapIterator.next();
	} while (treapIterator.hasNext());

	// Compare it with first node of t, return false if small
	if (c.compare(t.firstNode().x, lastNodeOfTreap) != 1)
	    return false;

	// Add all nodes of t to treap
	for (T node : t) {
	    add(node);
	    t.remove(node);
	}
	return true;
    }

    /**
     * Return the item in the treap whose rank is i - This is the item x such
     * that the treap contains exactly i element less than x.
     * 
     * @param i
     * @return
     */
    public T get(int i) {
	// changes made by Ferhan in this method

	int j = 0;
	Iterator<T> tt1 = iterator();
	while (tt1.hasNext()) {
	    T s = tt1.next();
	    if (j == i) {
		return s;
	    }
	    j++;
	}

	return null;
    }

    /**
     * Build a treap that contains the elements of the (already sorted) array a
     * This code must run in O(n) time - don't just call add(x) a.length times.
     * 
     * @param a
     * @param c
     */
    public Treap(T[] a, Comparator<T> c) {
	super(new Node<T>(), c);
	rand = new Random();
	// changes made by Ferhan

	r = populateTreap(a, 0, a.length - 1, nil);
    }

    /**
     * A simple method which will populate the treap
     * 
     * @param a
     * @param start
     * @param end
     * @param parent
     * @return
     */
    private Node<T> populateTreap(T[] a, int start, int end, Node parent) {
	if (start > end)
	    return nil;
	int mid = (start + end) / 2;
	Node<T> u = newNode();
	u.parent = parent;
	u.x = a[mid];
	u.p = mid * -1000; // This does ensure that key order is maintained with
	// space of 1000 elements in between (assuming we
	// won't exceed integer range)
	// No need to do bubble up / or rotations because it is already well
	// ordered (thus helping us avoid going non-linear time)
	u.left = populateTreap(a, start, mid - 1, u);
	u.right = populateTreap(a, mid + 1, end, u);
	return u;
    }
}
