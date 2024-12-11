package ca.jrvs.apps.practice.sorting;

public class QuickSort {

    public void quickSort(int[] array, int begin, int end) {
        int partitionIndex = partition(array, begin, end);
        quickSort(array, begin, partitionIndex - 1);
        quickSort(array, partitionIndex + 1, end);
    }

    int partition(int[] array, int begin, int end) {
        int pivot = array[end];
        int i = begin - 1;
        for (int j = begin; j < end; j++) {
            if (array[j] <= pivot) {
                i++;
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }
        int temp = array[i + 1];
        array[i + 1] = array[end];
        array[end] = temp;
        return i + 1;
    }
}
