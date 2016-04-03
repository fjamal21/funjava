public class QuickSort {
	 
    static int A[] = { 2, 8, 7, 1, 3, 5, 6, 4 };

	public static void quickSort(int A[], int low, int high) {
		int pivot;
		// Termination condition!
		if (high > low) {
			pivot = Partition(A, low, high);
			quickSort(A, low, pivot - 1);
			quickSort(A, pivot + 1, high);
		}
	}
 
	private static int Partition(int[] A, int low, int high) {
		int left, right, pivot_item = A[low];
		left = low;
		right = high;
		while (left < right) {
			// Move left while item < pivot
			while (A[left] <= pivot_item)
				left++;
			// Move right while item > pivot
			while (A[right] > pivot_item)
				right--;
			if (left < right)
				swap(A, left, right);
		}
		// right is final position for the pivot
		A[low] = A[right];
		A[right] = pivot_item;
		return right;
	}
 
	private static void swap(int[] A, int left, int right) {
		int temp = 0;
		temp = A[left];
		A[left] = A[right];
		A[right] = temp;
	}
 
	public static void main(String[] args) {
		quickSort(A, 0, 7);
		for (int i = 0; i < A.length; i++) {
			System.out.print(A[i] + ", ");
		}
	}
 
}
