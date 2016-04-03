package com.algorithm.datastructures;

/**
 * This class represents a string for which we can extract substrings in O(1)
 * time
 * 
 */
public class PString {
    int i; // index of first character  
    int m; // length
    byte[] data; // data 

    public PString(String s) {
	data = s.getBytes();
	i = 0;
	m = s.length();
    }

    protected PString(byte[] data, int i, int m) {
	this.data = data;
	this.i = i;
	this.m = m;
    }

    /**
     * Return a new string consisting of characters j,...,j+n-1
     * 
     * @param j
     * @param n
     * @return
     */
    public PString subString(int j, int n) {
	if (j < 0 || j >= m)
	    throw new IndexOutOfBoundsException();
	if (n < 0 || j + n > m)
	    throw new IndexOutOfBoundsException();
	return new PString(data, i + j, n);
    }

    /**
     * The length of this string
     * 
     * @return
     */
    public int length() {
	return m;
    }

    /**
     * Return the character at index j
     * 
     * @param j
     * @return
     */
    public char charAt(int j) {
	return (char) data[i + j];
    }
}
