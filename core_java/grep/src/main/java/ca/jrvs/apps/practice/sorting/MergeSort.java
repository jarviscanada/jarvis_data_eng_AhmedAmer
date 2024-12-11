package ca.jrvs.apps.practice.sorting;

public class MergeSort {

    public static void mergeSort(final int[] array) {
        final int N = array.length;
        if (N <= 1) return;
        int mid = N / 2;
        final int[] left = new int[mid];
        final int[] right = new int[N - mid];
        System.arraycopy(array, 0, left, 0, mid);
        if (N - mid >= 0)
            System.arraycopy(array, mid, right, 0, N - mid);

        mergeSort(left);
        mergeSort(right);

        merge(array, left, right);
    }

    static void merge(int[] array, int[] leftArray, int[] rightArray) {
        int i = 0, j = 0, k = 0;
        while (i < leftArray.length && j < rightArray.length) {
            if (leftArray[i] < rightArray[j]) {
                array[k++] = leftArray[i++];
            }
            else {
                array[k++] = rightArray[j++];
            }
        }
        while (i < leftArray.length) {
            array[k++] = leftArray[i++];
        }
        while (j < rightArray.length) {
            array[k++] = rightArray[j++];
        }
    }
}
