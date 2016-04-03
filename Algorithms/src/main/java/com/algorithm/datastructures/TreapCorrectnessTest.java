package com.algorithm.datastructures;

import java.util.Random;

/**
 * This class contains the driver program to test the question 1 and 2 of the
 * assignment.
 * 
 * From this class, you can verify all the parts of Question one and two.
 * 
 * It will make few random nodes in the treap and test out all the Question one
 * and Question 2 for the correctness.
 * 
 * Use this class to verify the changes I have made in Treap.java and
 * PersistentTreap.java as well.
 * 
 * @author Ferhan Jamal
 * @see Treap, PersistentTreap
 */
public class TreapCorrectnessTest {

    private static final Random r = new Random(0);
    private static Treap<Integer> originalTreap = new Treap<Integer>();

    public static void main(String[] args) {
	splitTest();
	rankTest();
	sortedArrayToTreapTest();
	persistentTreapTest();
    }

    // This is for part (a) of question one.
    private static void splitTest() {
	System.out.println("----------------------------------------");
	System.out.println("STARTED::Treap Split Test");
	int n = 10;
	// adding elements in Treap
	for (int i = 0; i < n; i++) {
	    originalTreap.add(r.nextInt(2 * n));
	}

	System.out.println("Original Size: " + originalTreap.size());
	System.out.println("Original Treap elements:" + originalTreap.toString());
	System.out.println("After splitting: " + originalTreap.split(5));
	System.out.println("Size after splitting: " + originalTreap.size()); // this also return exact size of treap after splitting as well which means part (b) of first question is also done I guess.
	System.out.println("ENDED:: Treap Split Test");
	System.out.println("----------------------------------------");

	System.out.println("----------------------------------------");
	System.out.println("STARTED:: Merge Test");

	Treap<Integer> newTreap = new Treap<Integer>();

	int n1 = 5;
	for (int i = 0; i < n1; i++) {
	    newTreap.add(r.nextInt(100 - 80) + 80);
	    // newTreap1.add(r.nextInt(21 - 9) + 9);
	}

	System.out.println("New Treap size: " + newTreap.size());
	System.out.println("New Treap elements: " + newTreap.toString());

	boolean status = originalTreap.merge(newTreap);
	System.out.println("Status after merging: " + status);
	System.out.println("Size after merging: " + originalTreap.size()); // also return exact size of treap after merging as well which means part (b) of first question is also done I guess.
	System.out.println("Elements in Original Treap after merging: " + originalTreap.toString());
	System.out.println("ENDED:: Merge Test");
	System.out.println("----------------------------------------");
    }

    // this is for part (c) of first question
    private static void rankTest() {
	System.out.println("----------------------------------------");
	System.out.println("STARTED::Treap Rank Test");
	int x = originalTreap.get(3);
	System.out.println(x);
	System.out.println("ENDED::Treap Rank Test");
	System.out.println("----------------------------------------");
    }

    // This is for part (d) of question one.
    private static void sortedArrayToTreapTest() {
	System.out.println("----------------------------------------");
	System.out.println("STARTED::Sorted Array to Treap Test");

	// Creating sorted array
	Integer a[] = new Integer[10];
	for (int i = 0; i < 10; i++) {
	    a[i] = i * i;
	}
	// Creating treap out of sorted array
	Treap<Integer> sortedArrayTreap = new Treap<Integer>(a, new DefaultComparator<Integer>());

	// Printing treap to test
	System.out.println(sortedArrayTreap.toString());
	System.out.println("ENDED::Sorted Array to Treap Test");
	System.out.println("----------------------------------------");
    }

    // this is for second question
    private static void persistentTreapTest() {
	System.out.println("----------------------------------------");
	System.out.println("STARTED::Persistent Treap Test");

	PersistentTreap<Integer> persistedTreap = new PersistentTreap<Integer>();
	int n = 10, curr = 0, toCheck = -2;
	// adding initial elements in Treap (creating versions from 0 to 9)
	System.out.print("Inserting: ");
	for (int i = 0; i < n; i++) {
	    curr = r.nextInt(2 * n);
	    System.out.print("" + curr + " ");
	    if (toCheck == 0)
		toCheck = curr;
	    else if (toCheck < 0)
		toCheck++;
	    persistedTreap.add(curr);
	}

	// removing element we want to test against (creating 11th version)
	System.out.println("\n\n" + persistedTreap.findElementPath(toCheck, persistedTreap.getCurrentVersion()));

	System.out.println("Removing: " + toCheck + " " + persistedTreap.remove(toCheck));

	System.out.println(persistedTreap.findElementPath(toCheck, persistedTreap.getCurrentVersion()) + "\n");

	// try to find it in all versions
	for (int i = 1; i <= persistedTreap.getCurrentVersion(); i++) {
	    System.out.println(persistedTreap.find(i, toCheck));
	    System.out.println(persistedTreap.findElementPath(toCheck, i));
	}

	// try to obtain last removed element from latest - 1 persisted tree
	// (should find)
	// System.out.println(persistedTreap.find(persistedTreap.getCurrentVersion()
	// - 1, toCheck));

	// Printing treap to test
	// System.out.println(sortedArrayTreap.toString());
	System.out.println("ENDED::Persistent to Treap Test");
	System.out.println("----------------------------------------");
    }
}
