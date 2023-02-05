/*
  (c) Copyright 2018, 2019 Phasmid Software
 */
package edu.neu.coe.info6205.sort.elementary;

import edu.neu.coe.info6205.sort.BaseHelper;
import edu.neu.coe.info6205.sort.Helper;
import edu.neu.coe.info6205.sort.SortWithHelper;
import edu.neu.coe.info6205.util.Config;
import java.util.Arrays;
import java.util.Random;

public class InsertionSort<X extends Comparable<X>> extends SortWithHelper<X> {

    /**
     * Constructor for any sub-classes to use.
     *
     * @param description the description.
     * @param N           the number of elements expected.
     * @param config      the configuration.
     */
    protected InsertionSort(String description, int N, Config config) {
        super(description, N, config);
    }

    /**
     * Constructor for InsertionSort
     *
     * @param N      the number elements we expect to sort.
     * @param config the configuration.
     */
    public InsertionSort(int N, Config config) {
        this(DESCRIPTION, N, config);
    }

    public InsertionSort(Config config) {
        this(new BaseHelper<>(DESCRIPTION, config));
    }

    /**
     * Constructor for InsertionSort
     *
     * @param helper an explicit instance of Helper to be used.
     */
    public InsertionSort(Helper<X> helper) {
        super(helper);
    }

    public InsertionSort() {
        this(BaseHelper.getHelper(InsertionSort.class));
    }

    /**
     * Sort the sub-array xs:from:to using insertion sort.
     *
     * @param xs   sort the array xs from "from" to "to".
     * @param from the index of the first element to sort
     * @param to   the index of the first element not to sort
     */
    public void sort(X[] xs, int from, int to) {
        final Helper<X> helper = getHelper();

        // FIXME
        for (int i = from+1; i<to; i++) {

            for(int j=i-1; j>=from; j--){
                if(!helper.swapStableConditional(xs, j+1))
                    break;

            }

        }
        // END
    }

    public static final String DESCRIPTION = "Insertion sort";

    public static <T extends Comparable<T>> void sort(T[] ts) {
        new InsertionSort<T>().mutatingSort(ts);
    }

    private static final int[] N_VALUES = {1000, 2000, 4000, 8000, 16000};
    private static final String[] ORDERING_SITUATIONS = {"random", "ordered", "partially-ordered", "reverse-ordered"};
    private static final int SEED = 12345;
    private static final Random RANDOM = new Random(SEED);

    public static void main(String[] args) {
        for (int n : N_VALUES) {
            Integer[] array = new Integer[n];
            for (String orderingSituation : ORDERING_SITUATIONS) {
                switch (orderingSituation) {
                    case "random":
                        for (int i = 0; i < n; i++) {
                            array[i] = RANDOM.nextInt();
                        }
                        break;
                    case "ordered":
                        for (int i = 0; i < n; i++) {
                            array[i] = i;
                        }
                        break;
                    case "partially-ordered":
                        for (int i = 0; i < n; i++) {
                            array[i] = RANDOM.nextInt(n / 10);
                        }
                        break;
                    case "reverse-ordered":
                        for (int i = 0; i < n; i++) {
                            array[i] = n - i - 1;
                        }
                        break;
                }
                long start = System.nanoTime();
                InsertionSort.sort(array);
                long end = System.nanoTime();
//                System.out.println(String.format("%d elements in %s order: %d nanoseconds", n, orderingSituation, end - start));
                System.out.println(String.format("%d elements in %s order: %.3f milliseconds", n, orderingSituation, (end - start) / 1_000_000.0));
                Arrays.fill(array, null);
            }
        }
    }
}
