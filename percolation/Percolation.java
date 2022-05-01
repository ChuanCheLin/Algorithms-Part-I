/* *****************************************************************************
 *  Name:              Eric Lin
 *  Last modified:     January 21, 2022
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF uf;
    private int[][] grid;
    private final int closed = 0;
    private final int open = 1;
    private int size;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("out of range");
        }

        uf = new WeightedQuickUnionUF(n * n + 2);
        size = n;
        // make grid
        grid = new int[n + 1][n + 1];
        for (int i = 0; i < n + 1; i++) {
            for (int j = 0; j < n; j++) {
                grid[i][j] = closed;
            }
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 1 || row > size || col < 1 || col > size) {
            throw new IllegalArgumentException("out of range");
        }

        if (!isOpen(row, col)) {
            // set to open
            grid[row][col] = open;
            // union with other 4 adjacent points
            // top
            if (row != 1) {
                if (isOpen(row - 1, col))
                    uf.union(grid2UF(row, col), grid2UF(row - 1, col));
            }
            else {
                uf.union(grid2UF(row, col), 0);
            }
            // bottom
            if (row != size) {
                if (isOpen(row + 1, col))
                    uf.union(grid2UF(row, col), grid2UF(row + 1, col));
            }
            else {
                uf.union(grid2UF(row, col), size * size + 1);
            }
            if (col != 1) {
                if (isOpen(row, col - 1))
                    uf.union(grid2UF(row, col), grid2UF(row, col - 1));
            }
            if (col != size) {
                if (isOpen(row, col + 1))
                    uf.union(grid2UF(row, col), grid2UF(row, col + 1));
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 1 || row > size || col < 1 || col > size) {
            throw new IllegalArgumentException("out of range");
        }
        return (grid[row][col] == open);
    }

    //
    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 1 || row > size || col < 1 || col > size) {
            throw new IllegalArgumentException("out of range");
        }
        return uf.find(grid2UF(row, col)) == uf.find(0);
    }

    //
    // returns the number of open sites
    public int numberOfOpenSites() {
        int num = 0;
        for (int i = 0; i < size + 1; i++) {
            for (int j = 0; j < size + 1; j++) {
                if (grid[i][j] == open)
                    num++;
            }
        }
        return num;
    }

    //
    // does the system percolate?
    public boolean percolates() {
        return uf.find(0) == uf.find(size * size + 1);
    }

    // converts coordinates of grid to index of UF
    private int grid2UF(int i, int j) {
        return (i - 1) * size + (j - 1) + 1;
    }

    public static void main(String[] args) {
        // Percolation percolation = new Percolation(0);
        // percolation.open(1, 1);
        // percolation.open(2, 1);
        // percolation.open(3, 1);
        // System.out.println(percolation.numberOfOpenSites());
    }
}
