package com.example.projectdatastructure;

public class MergeSort {

    // Method to compare two elements
    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    // stably merge a[lo .. mid] with a[mid+1 ..hi] using aux[lo .. hi]
    private static void merge(Comparable[] a, Comparable[] aux, int lo, int mid, int hi) {
        // copy to aux[]
        for (int k = lo; k <= hi; k++) {
            aux[k] = a[k];
        }

        // merge back to a[]
        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid) a[k] = aux[j++];
            else if (j > hi) a[k] = aux[i++];
            else if (less(aux[j], aux[i])) a[k] = aux[j++];
            else a[k] = aux[i++];
        }
    }

    // mergesort a[lo..hi] using auxiliary array aux[lo..hi]
    private static void sort(Comparable[] a, Comparable[] aux, int lo, int hi) {
        if (hi <= lo) return;
        int mid = lo + (hi - lo) / 2;
        sort(a, aux, lo, mid);
        sort(a, aux, mid + 1, hi);
        merge(a, aux, lo, mid, hi);
    }

    // Method that takes an array and returns a sorted array
    public static Comparable[] sortAndReturn(Comparable[] inputArray) {
        Comparable[] a = inputArray.clone(); // Create a copy of the input array to sort
        Comparable[] aux = new Comparable[a.length];
        sort(a, aux, 0, a.length - 1);
        return a; // Return the sorted array
    }
}
