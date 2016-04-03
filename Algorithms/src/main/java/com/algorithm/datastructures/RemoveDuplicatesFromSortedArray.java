public class RemoveDuplicatesFromSortedArray {
	public int removeDuplicates(int[] A) {
		int len = A.length;
		int i = 0;
		if (len <= 1)
			return len;
		for (int j = 1; j < len; j++) {
			if (A[j] != A[i])
				A[++i] = A[j];
		}
		return i + 1;
	}
}
