package edu.neu.coe.info6205.threesum;

import edu.neu.coe.info6205.util.Stopwatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * Implementation of ThreeSum which follows the approach of dividing the solution-space into
 * N sub-spaces where each sub-space corresponds to a fixed value for the middle index of the three values.
 * Each sub-space is then solved by expanding the scope of the other two indices outwards from the starting point.
 * Since each sub-space can be solved in O(N) time, the overall complexity is O(N^2).
 * <p>
 * NOTE: The array provided in the constructor MUST be ordered.
 */
public class ThreeSumQuadratic implements ThreeSum {
    /**
     * Construct a ThreeSumQuadratic on a.
     * @param a a sorted array.
     */
    public ThreeSumQuadratic(int[] a) {
        this.a = a;
        length = a.length;
    }

    public Triple[] getTriples() {
        List<Triple> triples = new ArrayList<>();
        for (int i = 0; i < length; i++) triples.addAll(getTriples(i));
        Collections.sort(triples);
        return triples.stream().distinct().toArray(Triple[]::new);
    }

    /**
     * Get a list of Triples such that the middle index is the given value j.
     *
     * @param j the index of the middle value.
     * @return a Triple such that
     */
    public List<Triple> getTriples(int j) {
        List<Triple> triples = new ArrayList<>();
        // FIXME : for each candidate, test if a[i] + a[j] + a[k] = 0.
        int i = 0;
        int k = length - 1;
        while (i < j && j < k) {
            int sum = a[i] + a[j] + a[k];
            if (sum == 0) {
                triples.add(new Triple(a[i], a[j], a[k]));
                i++;
                k--;
            } else if (sum < 0) {
                i++;
            } else {
                k--;
            }
        }
        // END 
        return triples;
    }

    private final int[] a;
    private final int length;

    public static void main(String[] args) {

            Supplier<int[]> intsSupplier = new Source(8000, 1000).intsSupplier(10);
            int[] ints = intsSupplier.get();
            ThreeSum calculateTarget = new ThreeSumQuadratic(ints);

            Stopwatch start = new Stopwatch();
            Triple[] triplesQuadratic = calculateTarget.getTriples();
            long lap = start.lap();
            System.out.println("Lap in miliseconds is: "+lap);
            start.close();

    }

}
