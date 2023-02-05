/*
  (c) Copyright 2018, 2019 Phasmid Software
 */
package edu.neu.coe.info6205.sort.elementary;

import edu.neu.coe.info6205.sort.BaseHelper;
import edu.neu.coe.info6205.sort.Helper;
import edu.neu.coe.info6205.sort.SortWithHelper;
import edu.neu.coe.info6205.util.Benchmark;
import edu.neu.coe.info6205.util.Benchmark_Timer;
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

//    private static final int[] N_VALUES = {1000, 2000, 4000, 8000, 16000};
//    private static final String[] ORDERING_SITUATIONS = {"random", "ordered", "partially-ordered", "reverse-ordered"};
//    private static final int SEED = 12345;
//    private static final Random RANDOM = new Random(SEED);

    public static void main(String[] args) {
        for (int n = 1000; n <= 20000; n = n * 2) {
            System.out.println("n = " + n);

            Integer[] randomlyGeneratedArray = generateArrayOfDifferentType("RandomArray", n);
            Integer[] ordered = generateArrayOfDifferentType("OrderedArray",n);
            Integer[] partiallyOrdered = generateArrayOfDifferentType("PartiallyOrderedArray",n);
            Integer[] reverseOrdered = generateArrayOfDifferentType("ReverseOrderedArray",n);

            InsertionSort<Integer> insertionSort = new InsertionSort<>();

            Benchmark<Boolean> benchmarkTimer = new Benchmark_Timer<>("Passing random array", b -> insertionSort.sort(randomlyGeneratedArray));
            double timeTakenForRandomArray = benchmarkTimer.run(true, 800);
            System.out.println("Random ordered array in nanosecond is "+timeTakenForRandomArray + " for array size of length "+n);

            Benchmark<Boolean> benchmarkTimer1 = new Benchmark_Timer<>("Passing ordered array", b -> insertionSort.sort(ordered));
            double timeTakenForOrderedArray = benchmarkTimer1.run(true, 800);
            System.out.println("Ordered array in nanosecond is "+timeTakenForOrderedArray + " for array size of length "+n);

            Benchmark<Boolean> benchmarkTimer3 = new Benchmark_Timer<>("Passing ordered array", b -> insertionSort.sort(partiallyOrdered));
            double timeTakenForPartiallyOrderedArray = benchmarkTimer3.run(true, 800);
            System.out.println("Partially ordered array in nanosecond is "+timeTakenForPartiallyOrderedArray + " for array size of length "+n);

            Benchmark<Boolean> benchmarkTimer4 = new Benchmark_Timer<>("Passing reverse ordered array", b -> insertionSort.sort(reverseOrdered));
            double timeTakenForReverseOrderedArray = benchmarkTimer4.run(true, 800);
            System.out.println("The insertion sort time of reverse ordered array in nanosecond is "+timeTakenForReverseOrderedArray + " for array size of length "+n);

            System.out.println();
        }
    }

    public static Integer[] generateArrayOfDifferentType(String typeOfArray, int n){

        Integer[] arr = new Integer[n];
        Random random = new Random();

        switch(typeOfArray){

            case "RandomArray" :
                for (int i = 0; i < n; i++)
                    arr[i] = random.nextInt();
                break;

            case "OrderedArray" :
                for (int i = 0; i < n; i++)
                    arr[i] = i;
                break;

            case "PartiallyOrderedArray" :
                for (int i = 0; i < n/2; i++)
                    arr[i] = i;

                for (int i = n/2; i < n; i++)
                    arr[i] = random.nextInt();
                break;

            case "ReverseOrderedArray" :
                for (int i = 0; i < n; i++)
                    arr[i] = n - i - 1;
                break;
        }

        return arr;

    }
}
