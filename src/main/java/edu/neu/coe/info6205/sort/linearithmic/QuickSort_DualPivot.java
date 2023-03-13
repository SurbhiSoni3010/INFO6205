package edu.neu.coe.info6205.sort.linearithmic;

import edu.neu.coe.info6205.sort.Helper;
import edu.neu.coe.info6205.sort.InstrumentedHelper;
import edu.neu.coe.info6205.util.Benchmark;
import edu.neu.coe.info6205.util.Benchmark_Timer;
import edu.neu.coe.info6205.util.Config;

import java.util.ArrayList;
import java.util.List;

/**
 * Class QuickSort_DualPivot which extends QuickSort.
 *
 * @param <X> the underlying comparable type.
 */
public class QuickSort_DualPivot<X extends Comparable<X>> extends QuickSort<X> {

    public static final String DESCRIPTION = "QuickSort dual pivot";

    public QuickSort_DualPivot(String description, int N, Config config) {
        super(description, N, config);
        setPartitioner(createPartitioner());
    }

    /**
     * Constructor for QuickSort_3way
     *
     * @param helper an explicit instance of Helper to be used.
     */
    public QuickSort_DualPivot(Helper<X> helper) {
        super(helper);
        setPartitioner(createPartitioner());
    }

    /**
     * Constructor for QuickSort_3way
     *
     * @param N      the number elements we expect to sort.
     * @param config the configuration.
     */
    public QuickSort_DualPivot(int N, Config config) {
        this(DESCRIPTION, N, config);
    }

    @Override
    public Partitioner<X> createPartitioner() {
        return new Partitioner_DualPivot(getHelper());
    }

    public class Partitioner_DualPivot implements Partitioner<X> {

        public Partitioner_DualPivot(Helper<X> helper) {
            this.helper = helper;
        }

        /**
         * Method to partition the given partition into smaller partitions.
         *
         * @param partition the partition to divide up.
         * @return an array of partitions, whose length depends on the sorting method being used.
         */
        public List<Partition<X>> partition(Partition<X> partition) {
            final X[] xs = partition.xs;
            final int lo = partition.from;
            final int hi = partition.to - 1;
            helper.swapConditional(xs, lo, hi);
            int lt = lo + 1;
            int gt = hi - 1;
            int i = lt;
            X v1 = xs[lo];
            X v2 = xs[hi];
            // NOTE: we are trying to avoid checking on instrumented for every time in the inner loop for performance reasons (probably a silly idea).
            // NOTE: if we were using Scala, it would be easy to set up a comparer function and a swapper function. With java, it's possible but much messier.
            if (helper.instrumented()) {
                helper.incrementHits(2); // XXX these account for v1 and v2.
                while (i <= gt) {
                    if (helper.compare(xs, i, v1) < 0) helper.swap(xs, lt++, i++);
                    else if (helper.compare(xs, i, v2) > 0) helper.swap(xs, i, gt--);
                    else i++;
                }
                helper.swap(xs, lo, --lt);
                helper.swap(xs, hi, ++gt);
            } else {
                while (i <= gt) {
                    X x = xs[i];
                    if (x.compareTo(v1) < 0) swap(xs, lt++, i++);
                    else if (x.compareTo(v2) > 0) swap(xs, i, gt--);
                    else i++;
                }
                swap(xs, lo, --lt);
                swap(xs, hi, ++gt);
            }

            List<Partition<X>> partitions = new ArrayList<>();
            partitions.add(new Partition<>(xs, lo, lt));
            partitions.add(new Partition<>(xs, lt + 1, gt));
            partitions.add(new Partition<>(xs, gt + 1, hi + 1));
            return partitions;
        }

        // CONSIDER invoke swap in BaseHelper.
        private void swap(X[] ys, int i, int j) {
            X temp = ys[i];
            ys[i] = ys[j];
            ys[j] = temp;
        }

        private final Helper<X> helper;
    }
    public static void main (String[] args) {
        int N = 80000;
        InstrumentedHelper<Integer> helper = new InstrumentedHelper<>("QuickSort_DualPivot", Config.setupConfig("false", "0", "0", "", ""));

        QuickSort_DualPivot<Integer> s = new QuickSort_DualPivot<>(helper);
        s.init(N);

        Integer[] xs = helper.random(Integer.class, r -> r.nextInt(400000));

        Partitioner<Integer> partitioner = s.createPartitioner();
        List<Partition<Integer>> partitions = partitioner.partition(new Partition<>(xs, 0, xs.length));
        Partition<Integer> p0 = partitions.get(0);
        Partition<Integer> p1 = partitions.get(1);
        Partition<Integer> p2 = partitions.get(2);

        Benchmark<Boolean> bm = new Benchmark_Timer<>("random array sort", b -> s.sort(xs, 0, p0.to, 0));
        double x = bm.run(true, 20);
        Benchmark<Boolean> bm1 = new Benchmark_Timer<>("random array sort", b -> s.sort(xs, p1.from, p1.to, 0));
        double x1 = bm.run(true, 20);
        Benchmark<Boolean> bm2 = new Benchmark_Timer<>("random array sort", b -> s.sort(xs, p2.from, N, 0));
        double x2 = bm.run(true, 20);

        long compares = helper.getCompares();
        long swaps = helper.getSwaps();
        long hits = ((InstrumentedHelper<Integer>) helper).getHits();
        long fixes = helper.getFixes();
        double time = (x + x1 + x2);
        System.out.println(compares + " compares");
        System.out.println(swaps + " swap");
        System.out.println(hits + " hits");
        System.out.println(fixes + " fixes");
        System.out.println(time + " ms");

    }
}

