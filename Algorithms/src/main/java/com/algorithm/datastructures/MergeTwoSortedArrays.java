public class MergeTwoSortedArrays {
	public void merge(int A[], int m, int B[], int n) {
		while (n > 0) {
			// the original A has all merged into the new A, and merge the left
			// B
			if (m <= 0 || A[m - 1] < B[n - 1])
				A[n + m - 1] = B[--n];
			else
				A[n + m - 1] = A[--m];
		}
	}
}
