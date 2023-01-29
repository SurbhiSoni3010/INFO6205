package edu.neu.coe.info6205.threesum;

import edu.neu.coe.info6205.util.Stopwatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * Implementation of ThreeSum which follows the simple optimization of
 * requiring a sorted array, then using binary search to find an element x where
 * -x the sum of a pair of elements.
 * <p>
 * The array provided in the constructor MUST be ordered.
 * <p>
 * This algorithm runs in O(N^2 log N) time.
 */
class ThreeSumQuadrithmic implements ThreeSum {
    /**
     * Construct a ThreeSumQuadrithmic on a.
     * @param a a sorted array.
     */
    public ThreeSumQuadrithmic(int[] a) {
        this.a = a;
        length = a.length;
    }

    public Triple[] getTriples() {
        List<Triple> triples = new ArrayList<>();
        for (int i = 0; i < length; i++)
            for (int j = i + 1; j < length; j++) {
                Triple triple = getTriple(i, j);
                if (triple != null) triples.add(triple);
            }
        Collections.sort(triples);
        return triples.stream().distinct().toArray(Triple[]::new);
    }

    public Triple getTriple(int i, int j) {
        int index = Arrays.binarySearch(a, -a[i] - a[j]);
        if (index >= 0 && index > j) return new Triple(a[i], a[j], a[index]);
        else return null;
    }

    private final int[] a;
    private final int length;
    public static void main(String[] args) {
        Supplier<int[]> intsSupplier = new Source(8000, 1000).intsSupplier(10);
        int[] ints = intsSupplier.get();
        ThreeSum calculateTarget = new ThreeSumQuadrithmic(ints);

        Stopwatch start = new Stopwatch();
        Triple[] triplesQuadratic = calculateTarget.getTriples();
        long lap = start.lap();
        System.out.println("Lap in miliseconds is: "+lap);
        start.close();
    }
}
