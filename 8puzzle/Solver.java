/* *****************************************************************************
 *  Name:              Eric Lin
 *  Last modified:     February 22, 2022
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private MinPQ<SearchNode> minPQ;
    private MinPQ<SearchNode> minPQtwin;
    private Board goalboard;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new NullPointerException();
        int n = initial.dimension();
        int[][] goal = new int[n][n];
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == n - 1 && j == n - 1) {
                    goal[i][j] = 0;
                }
                else {
                    goal[i][j] = ++count;
                }
            }
        }
        goalboard = new Board(goal);

        minPQ = new MinPQ<SearchNode>();
        minPQtwin = new MinPQ<SearchNode>();

        minPQ.insert(new SearchNode(initial, 0, null));
        minPQtwin.insert(new SearchNode(initial.twin(), 0, null));

        // A* search
        SearchNode minNode;
        while (!minPQ.min().board.equals(goalboard) && !minPQtwin.min().board.equals(goalboard)) {
            minNode = minPQ.delMin();
            for (Board neighbor : minNode.board.neighbors()) {
                if (minNode.moves == 0) {
                    minPQ.insert(new SearchNode(neighbor, minNode.moves + 1, minNode));
                }
                else if (!neighbor.equals(minNode.previousNode.board)) {
                    minPQ.insert(new SearchNode(neighbor, minNode.moves + 1, minNode));
                }
            }
            // A* search twin
            minNode = minPQtwin.delMin();
            for (Board neighbor : minNode.board.neighbors()) {
                if (minNode.moves == 0) {
                    minPQtwin.insert(new SearchNode(neighbor, minNode.moves + 1, minNode));
                }
                else if (!neighbor.equals(minNode.previousNode.board)) {
                    minPQtwin.insert(new SearchNode(neighbor, minNode.moves + 1, minNode));
                }
            }
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        if (minPQ.min().board.equals(goalboard)) {
            return true;
        }
        return false;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) {
            return -1;
        }
        return minPQ.min().moves;
    }

    // // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) {
            return null;
        }
        Stack<Board> solution = new Stack<Board>();
        SearchNode current = minPQ.min();
        solution.push(current.board);
        while (current.previousNode != null) {
            solution.push(current.previousNode.board);
            current = current.previousNode;
        }
        return solution;
    }

    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private int moves;
        private int priority;
        private SearchNode previousNode;

        public SearchNode(Board board, int moves, SearchNode previousNode) {
            this.board = board;
            this.moves = moves;
            this.previousNode = previousNode;
            priority = moves + board.manhattan();
        }

        public int compareTo(SearchNode that) {
            return (this.priority - that.priority);
        }
    }

    // test client (see below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
