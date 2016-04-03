package com.algorithm.datastructures;

import java.util.Random;

/**
 * This class is mainly used for performance testing on the various operations
 * of treaps.
 * 
 * 
 * @author Ferhan Jamal
 * @see Treap, PersistentTreap
 * 
 */
public class TreapPerformanceTest {

    private static final Random r = new Random(0);
    private static Treap<Integer> originalTreap = new Treap<Integer>();

    public static void main(String[] args) {
	splitPerformanceTest();
	rankPerformanceTest();
	sortedArrayToTreapPerformanceTest();
	persistentTreapPerformanceTest();
    }

    // This is for testing performance of part (a) of question one.
    private static void splitPerformanceTest() {
	System.out.println("----------------------------------------");
	System.out.println("STARTED::Treap Split Performance Test");
	int n = 50000; // creating treap with 50,000 elements
	// adding elements in Treap
	long start_add = System.nanoTime();
	for (int i = 0; i < n; i++) {
	    originalTreap.add(r.nextInt(100 * n));
	}
	long end_add = (System.nanoTime() - start_add) / 1000000L; // converting to milliseconds

	System.out.println("Time it takes to insert elements in the treap: " + end_add + " ms");
	System.out.println("Original Size: " + originalTreap.size());

	int split = 10000;
	long start_split = System.nanoTime();
	for (int i = 0; i < split; i++) {
	    originalTreap.split(r.nextInt(2 * split));
	}
	long end_split = (System.nanoTime() - start_split) / 1000000L;

	System.out.println("Time it took to split: " + end_split + " ms");
	System.out.println("Size after splitting: " + originalTreap.size()); // this also return exact size of treap after splitting as well which means part (b) of first question is also done I guess.
	System.out.println("ENDED:: Treap Split Performance Test");
	System.out.println("----------------------------------------");

	System.out.println("----------------------------------------");
	System.out.println("STARTED:: Merge Performance Test");

	Treap<Integer> newTreap = new Treap<Integer>();

	// generating a new treap of 10,000 elements
	int n1 = 10000;
	for (int i = 0; i < n1; i++) {
	    newTreap.add(r.nextInt(8000 * n1));
	}

	System.out.println("New Treap size: " + newTreap.size());
	long start_merge = System.nanoTime();
	boolean status = originalTreap.merge(newTreap);
	long end_merge = (System.nanoTime() - start_merge) / 1000000L;

	System.out.println("Time take to merge: " + end_merge + " ms");

	System.out.println("Status after merging: " + status);
	System.out.println("Size after merging: " + originalTreap.size()); // also return exact size of treap after merging as well which means part (b) of first question is also done I guess.
	System.out.println("ENDED:: Merge Performance Test");
	System.out.println("----------------------------------------");
    }

    // this is for part (c) of first question
    private static void rankPerformanceTest() {
	System.out.println("----------------------------------------");
	System.out.println("STARTED::Treap Rank Performance Test");
	long start_rank = System.nanoTime();
	int x = originalTreap.get(3);
	long end_rank = (System.nanoTime() - start_rank) / 1000000L;
	System.out.println("Time it took to get the rank: " + end_rank);
	System.out.println("Find Rank Item: " + x);
	System.out.println("ENDED::Treap Rank Performance Test");
	System.out.println("----------------------------------------");
    }

    // This is for part (d) of question one.
    private static void sortedArrayToTreapPerformanceTest() {
	System.out.println("----------------------------------------");
	System.out.println("STARTED::Sorted Array to Treap Performance Test");

	// Creating sorted array
	Integer a[] = new Integer[100000];
	for (int i = 0; i < 100000; i++) {
	    a[i] = i * i;
	}

	long start_sorted_array = System.nanoTime();
	// Creating treap out of sorted array
	Treap<Integer> sortedArrayTreap = new Treap<Integer>(a, new DefaultComparator<Integer>());
	long end_sorted_array = (System.nanoTime() - start_sorted_array) / 1000000L;
	System.out.println("Time it took for sorted array to treap: " +end_sorted_array+ " ms");
	// Printing treap to test
	System.out.println("ENDED::Sorted Array to Treap Performance Test");
	System.out.println("----------------------------------------");
    }

    // this is for second question
    private static void persistentTreapPerformanceTest() {
	System.out.println("----------------------------------------");
	System.out.println("STARTED::Persistent Treap Performance Test");

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
	System.out.println("ENDED::Persistent to Treap Performance Test");
	System.out.println("----------------------------------------");
    }
}
