package edu.neu.coe.info6205.union_find;

import java.util.Random;

/**
 * This interface models the concept of Union-Find, a special case of the Connections interface
 */
public interface UF extends Connections {



    /**
     * Returns the number of components.
     *
     * @return the number of components (between {@code 1} and {@code n})
     */
    int components();

    /**
     * Returns the component identifier for the component containing site {@code p}.
     *
     * @param p the integer representing one site
     * @return the component identifier for the component containing site {@code p}
     * @throws IllegalArgumentException unless {@code 0 <= p < n}
     */
    int find(int p);

    /**
     * Merges the component containing site {@code p} with the
     * the component containing site {@code q}.
     *
     * @param p the integer representing one site
     * @param q the integer representing the other site
     * @throws IllegalArgumentException unless
     *                                  both {@code 0 <= p < n} and {@code 0 <= q < n}
     */
    void union(int p, int q);

    /**
     * Returns true if the the two sites are in the same component.
     *
     * @param p the integer representing one site
     * @param q the integer representing the other site
     * @return {@code true} if the two sites {@code p} and {@code q} are in the same component;
     * {@code false} otherwise
     * @throws IllegalArgumentException unless
     *                                  both {@code 0 <= p < n} and {@code 0 <= q < n}
     */
    default boolean isConnected(int p, int q) {
        return find(p) == find(q);
    }

    /**
     * Returns the number of sites (objects) in this UF object.
     *
     * @return the number of sites.
     */
    int size();
    public static int count(int n) {
        UF_HWQUPC uf = new UF_HWQUPC(n);
        Random random = new Random();
        int connections = 0;
        while (uf.components() != 1) {
            int p = random.nextInt(n);
            int q = random.nextInt(n);
            connections++;
            if (!uf.isConnected(p, q)) {
                uf.union(p, q);

            }
        }
        return connections;
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int connections= count(n);
        System.out.println("Number of connections: " + connections);
    }


}
