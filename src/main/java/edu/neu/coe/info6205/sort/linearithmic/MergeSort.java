package edu.neu.coe.info6205.sort.linearithmic;

import edu.neu.coe.info6205.sort.Helper;
import edu.neu.coe.info6205.sort.InstrumentedHelper;
import edu.neu.coe.info6205.sort.SortWithHelper;
import edu.neu.coe.info6205.sort.elementary.InsertionSort;
import edu.neu.coe.info6205.util.Benchmark;
import edu.neu.coe.info6205.util.Benchmark_Timer;
import edu.neu.coe.info6205.util.Config;

import java.util.Arrays;

/**
 * Class MergeSort.
 *
 * @param <X> the underlying comparable type.
 */
public class MergeSort<X extends Comparable<X>> extends SortWithHelper<X> {

    public static final String DESCRIPTION = "MergeSort";

    /**
     * Constructor for MergeSort
     * <p>
     * NOTE this is used only by unit tests, using its own instrumented helper.
     *
     * @param helper an explicit instance of Helper to be used.
     */
    public MergeSort(Helper<X> helper) {
        super(helper);
        insertionSort = new InsertionSort<>(helper);
    }

    /**
     * Constructor for MergeSort
     *
     * @param N      the number elements we expect to sort.
     * @param config the configuration.
     */
    public MergeSort(int N, Config config) {
        super(DESCRIPTION + ":" + getConfigString(config), N, config);
        insertionSort = new InsertionSort<>(getHelper());
    }

    @Override
    public X[] sort(X[] xs, boolean makeCopy) {
        getHelper().init(xs.length);
        X[] result = makeCopy ? Arrays.copyOf(xs, xs.length) : xs;
        sort(result, 0, result.length);
        return result;
    }

    @Override
    public void sort(X[] a, int from, int to) {
        // CONSIDER don't copy but just allocate according to the xs/aux interchange optimization
        X[] aux = Arrays.copyOf(a, a.length);
        sort(a, aux, from, to);
    }

    private void sort(X[] a, X[] aux, int from, int to) {
        final Helper<X> helper = getHelper();
        Config config = helper.getConfig();
        boolean insurance = config.getBoolean(MERGESORT, INSURANCE);
        boolean noCopy = config.getBoolean(MERGESORT, NOCOPY);
        if (to <= from + helper.cutoff()) {
            insertionSort.sort(a, from, to);
            return;
        }

        // FIXME : implement merge sort with insurance and no-copy optimizations
        int mid = (from + to) / 2;
        sort(a, from, mid);
        sort(a, mid, to);

        if (insurance) {
            if (helper.less(a[mid-1], a[mid])) {
                return;
            }
        }

        if (noCopy) {
            mergeWithoutCopy(a, from, mid, to);
        } else {
            aux = Arrays.copyOf(a, a.length);
            merge(aux, a, from, mid, to);
        }
        // END 
    }

    // CONSIDER combine with MergeSortBasic perhaps.
    private void merge(X[] sorted, X[] result, int from, int mid, int to) {
        final Helper<X> helper = getHelper();
        int i = from;
        int j = mid;
        for (int k = from; k < to; k++)
            if (i >= mid) helper.copy(sorted, j++, result, k);
            else if (j >= to) helper.copy(sorted, i++, result, k);
            else if (helper.less(sorted[j], sorted[i])) {
                helper.incrementFixes(mid - i);
                helper.copy(sorted, j++, result, k);
            } else helper.copy(sorted, i++, result, k);
    }
    private void mergeWithoutCopy(X[] a, int from, int mid, int to) {
        final Helper<X> helper = getHelper();
        int i = from;
        int j = mid;
        while (i < mid && j < to) {
            if (helper.less(a[j], a[i])) {
                X temp = a[j];
                System.arraycopy(a, i, a, i+1, j-i);
                a[i] = temp;
                i++;
                j++;
                mid++;
            } else {
                i++;
            }
        }
    }

    public static final String MERGESORT = "mergesort";
    public static final String NOCOPY = "nocopy";
    public static final String INSURANCE = "insurance";

    private static String getConfigString(Config config) {
        StringBuilder stringBuilder = new StringBuilder();
        if (config.getBoolean(MERGESORT, INSURANCE)) stringBuilder.append(" with insurance comparison");
        if (config.getBoolean(MERGESORT, NOCOPY)) stringBuilder.append(" with no copy");
        return stringBuilder.toString();
    }

    private final InsertionSort<X> insertionSort;

    public static void main (String[] args) {
        int N = 160000;
        InstrumentedHelper<Integer> helper = new InstrumentedHelper<>("MergeSort", Config.setupConfig("false", "0", "0", "", ""));
        MergeSort<Integer> s = new MergeSort<>(helper);
        s.init(N);
        Integer[] xs = helper.random(Integer.class, r -> r.nextInt(100000));
        Benchmark<Boolean> bm = new Benchmark_Timer<>("random array sort", b -> s.sort(xs, 0, N));
        double x = bm.run(true, 20);

        long compares = helper.getCompares();
        long swaps = helper.getSwaps();
        long hits = helper.getHits();
        long fixes = helper.getFixes();
        System.out.println(compares + " compares");
        System.out.println(swaps + " swap");
        System.out.println(hits + " hits");
        System.out.println(fixes + " fixes");
        System.out.println(x + " ms");

    }
}

