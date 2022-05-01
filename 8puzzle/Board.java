/* *****************************************************************************
 *  Name:              Eric Lin
 *  Last modified:     February 22, 2022
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Board {
    private final int[][] tiles;
    private final int n;
    private int blankrow;
    private int blankcol;
    private Board twin = null;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.tiles = tiles;
        this.n = tiles.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.tiles[i][j] == 0) {
                    this.blankrow = i;
                    this.blankcol = j;
                    break;
                }
            }
        }
    }

    // // string representation of this board
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                stringBuilder.append(String.format("%2d ", tiles[i][j]));
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    // board dimension n
    public int dimension() {
        return this.n;
    }

    //
    // number of tiles out of place
    public int hamming() {
        int count = 0;
        int hamming = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.tiles[i][j] == 0) {
                    count++;
                }
                else if (this.tiles[i][j] != ++count) {
                    hamming++;
                }
            }
        }
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int count = 0;
        int manhat = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.tiles[i][j] == 0) {
                    count++;
                }
                else if (this.tiles[i][j] != ++count) {
                    int a = Math.abs(row(tiles[i][j]) - i);
                    int b = Math.abs(col(tiles[i][j]) - j);
                    manhat = manhat + a + b;
                }
            }
        }
        return manhat;
    }

    // // is this board the goal board?
    public boolean isGoal() {
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.tiles[i][j] != ++count) {
                    if (this.tiles[i][j] != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        if (this.toString().equals(y.toString())) {
            return true;
        }
        return false;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> neighbor = new Stack<Board>();
        if (this.blankrow > 0) {
            Board newboard = new Board(swapup());
            neighbor.push(newboard);
        }
        if (this.blankrow < n - 1) {
            Board newboard = new Board(swapdown());
            neighbor.push(newboard);
        }
        if (this.blankcol > 0) {
            Board newboard = new Board(swapleft());
            neighbor.push(newboard);
        }
        if (this.blankcol < n - 1) {
            Board newboard = new Board(swapright());
            neighbor.push(newboard);
        }
        return neighbor;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        if (this.twin == null) {
            int row1 = StdRandom.uniform(n);
            int col1 = StdRandom.uniform(n);
            int row2 = StdRandom.uniform(n);
            int col2 = StdRandom.uniform(n);
            while (tiles[row1][col1] == 0) {
                row1 = StdRandom.uniform(n);
                col1 = StdRandom.uniform(n);
            }
            while (tiles[row2][col2] == 0 || tiles[row2][col2] == tiles[row1][col1]) {
                row2 = StdRandom.uniform(n);
                col2 = StdRandom.uniform(n);
            }
            Board twinBoard = new Board(swap(row1, col1, row2, col2));
            this.twin = twinBoard;
            return twinBoard;
        }
        else {
            return this.twin;
        }
    }

    private int[][] swapup() {
        int[][] newtiles = duplicate();
        newtiles[blankrow][blankcol] = newtiles[blankrow - 1][blankcol];
        newtiles[blankrow - 1][blankcol] = 0;
        return newtiles;
    }

    private int[][] swapdown() {
        int[][] newtiles = duplicate();
        newtiles[blankrow][blankcol] = newtiles[blankrow + 1][blankcol];
        newtiles[blankrow + 1][blankcol] = 0;
        return newtiles;
    }

    private int[][] swapright() {
        int[][] newtiles = duplicate();
        newtiles[blankrow][blankcol] = newtiles[blankrow][blankcol + 1];
        newtiles[blankrow][blankcol + 1] = 0;
        return newtiles;
    }

    private int[][] swapleft() {
        int[][] newtiles = duplicate();
        newtiles[blankrow][blankcol] = newtiles[blankrow][blankcol - 1];
        newtiles[blankrow][blankcol - 1] = 0;
        return newtiles;
    }

    private int[][] swap(int row1, int col1, int row2, int col2) {
        int[][] newtiles = duplicate();
        int temp = newtiles[row1][col1];
        newtiles[row1][col1] = newtiles[row2][col2];
        newtiles[row2][col2] = temp;
        return newtiles;
    }

    // first col is 0
    private int col(int num) {
        int ans = num % n;
        if (ans == 0) {
            return n - 1;
        }
        return ans - 1;
    }

    // first row is 0
    private int row(int num) {
        int ans = (num - 1) / n;
        return ans;
    }

    private int[][] duplicate() {
        int[][] temp = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                temp[i][j] = tiles[i][j];
            }
        }
        return temp;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        // StdOut.println(initial.manhattan());

        // for (Board a : initial.neighbors()) {
        //     StdOut.println(a.toString());
        // }
        StdOut.println(initial.twin().toString());
    }
}
