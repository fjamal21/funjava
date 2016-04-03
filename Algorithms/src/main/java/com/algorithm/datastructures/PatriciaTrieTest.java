package com.algorithm.datastructures;

import java.util.Random;
import java.util.TreeSet;

/**
 * 
 * @author Ferhan Jamal
 * 
 */
public class PatriciaTrieTest {

    private static final String CHAR_LIST = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private static final int RANDOM_STRING_LENGTH = 10;
    private static Random random = new Random();

    /**
     * @param args
     */
    public static void main(String[] args) {

	correctnessTestPatriciaTrie();
	performanceTestExample();

    }

    /**
     * Below method is just use for correctness of PatriciaTrie to perform
     * certain operations such as insert, search, remove.
     */
    private static void correctnessTestPatriciaTrie() {

	System.out.println("############################################################");
	System.out.println("PatriciaTrie Correctness Test Started!");

	PatriciaTrie trie = new PatriciaTrie();
	PString str;
	// this is for adding purpose.
	for (int i = 0; i < 10; i++) {
	    String number = generateRandomString();
	    str = new PString(number);
	    trie.add(str);
	}

	// below is use for removing the method
	str = new PString("hello");
	System.out.println(trie.remove(str) ? "Yes Removed" : "Not Removed");

	// below is use for find the string
	System.out.println(trie.search(str) == null ? "NULL" : trie.search(str));
	str = new PString("world");
	trie.add(str);
	System.out.println(trie.search(str) == null ? "NULL" : trie.search(str));

	// Find by prefixes
	PString[] pStrings = trie.prefixSearchMany(new PString("b"));
	if (pStrings != null) {
	    for (PString pString : pStrings) {
		System.out.println(new String(pString.data));
	    }
	}

	System.out.println("PatriciaTrie Correctness Test Ended!");
	System.out.println("############################################################");

    }

    /**
     * Below method is use to do performance test on TreeSet and PatriciaTrie as
     * well. This includes correctness test on TreeSet as well.
     */
    public static void performanceTestExample() {

	int n = 100000;
	System.out.println("############################################################");
	System.out.println("Tree Set Performance Test Started!");
	System.out.println("Inserting " + n + " elements in the TreeSet");
	TreeSet<String> tree = new TreeSet<String>();

	// this is for adding purpose.
	long start_add = System.nanoTime();
	for (int i = 0; i < n; i++) {
	    String number = generateRandomString();
	    //	    System.out.println(number);
	    tree.add(number);
	}
	long end_add = (System.nanoTime() - start_add) / 1000000L; // converting to milliseconds
	System.out.println("Time it takes to insert " + n + " elements in the TreeSet: " + end_add + " ms");

	// this is for iterating purpose.
	long start_iterate = System.nanoTime();
	for (String data : tree) {
	    //	    System.out.println(data);
	}
	long end_iterate = (System.nanoTime() - start_iterate) / 1000000L; // converting to milliseconds
	System.out.println("Time it takes to iterate elements in the TreeSet: " + end_iterate + " ms");

	// test for those string which starts with "bw"
	long start_findPrefix = System.nanoTime();
	for (String dd : tree) {
	    if (dd.startsWith("bw")) {
		break;
	    }
	}
	long end_findPrefix = (System.nanoTime() - start_findPrefix) / 1000000L; // converting to milliseconds
	System.out.println("Time it takes to find prefix elements in the TreeSet: " + end_findPrefix + " ms");

	// test for those string which starts with "bw"
	long start_findSuffix = System.nanoTime();
	for (String dd : tree) {
	    if (dd.endsWith("bw")) {
		break;
	    }
	}
	long end_findSuffix = (System.nanoTime() - start_findSuffix) / 1000000L; // converting to milliseconds
	System.out.println("Time it takes to find suffix elements in the TreeSet: " + end_findSuffix + " ms");
	System.out.println("Tree Set Performance Test Ended!");
	System.out.println("############################################################");

	System.out.println("############################################################");
	System.out.println("PatriciaTrie Performance Test Started!");
	System.out.println("Inserting " + n + " elements in the PatriciaTrie");

	PatriciaTrie trie = new PatriciaTrie();
	PString str;

	// this is for adding purpose.
	long start_trieAdd = System.nanoTime();
	for (int i = 0; i < n; i++) {
	    String number = generateRandomString();
	    str = new PString(number);
	    trie.add(str);
	}
	long end_trieAdd = (System.nanoTime() - start_trieAdd) / 1000000L; // converting to milliseconds
	System.out.println("Time it takes to insert  " + n + "  elements in the PatriciaTrie: " + end_trieAdd + " ms");

	// this is for remove purpose.
	long start_trieRemove = System.nanoTime();
	System.out.println(trie.remove(new PString("hello")) == true ? "Node Removed" : "Node is not removed");
	long end_trieRemove = (System.nanoTime() - start_trieRemove) / 1000000L; // converting to milliseconds
	System.out.println("Time it takes to remove in PatriciaTrie: " + end_trieRemove + " ms");

	// this is for prefix purpose.
	long start_triePrefix = System.nanoTime();

	// Find by prefixes
	PString[] pStrings = trie.prefixSearchMany(new PString("A"));
	if (pStrings != null) {
	    for (PString pString : pStrings) {
		System.out.println(new String(pString.data));
	    }
	}

	long end_triePrefix = (System.nanoTime() - start_triePrefix) / 1000000L; // converting to milliseconds

	System.out.println("Time it takes to find prefix  elements in the PatriciaTrie: " + end_triePrefix + " ms");
	System.out.println("PatriciaTrie Performance Test Ended!");
	System.out.println("############################################################");

    }

    /**
     * This method generates random string
     * 
     * @return
     */
    public static String generateRandomString() {

	StringBuffer randStr = new StringBuffer();
	for (int i = 0; i < RANDOM_STRING_LENGTH; i++) {
	    int number = getRandomNumber();
	    char ch = CHAR_LIST.charAt(number);
	    randStr.append(ch);
	}
	return randStr.toString();
    }

    /**
     * This method generates random numbers
     * 
     * @return int
     */
    private static int getRandomNumber() {
	int randomInt = 0;
	randomInt = random.nextInt(CHAR_LIST.length());
	if (randomInt - 1 == -1) {
	    return randomInt;
	} else {
	    return randomInt - 1;
	}
    }
}
