/**
 * Node contains all the data required to manage a single state in time for the water jug problem
 * - This includes the goal state, bucket information, and the heuristic values
 * - The class Bucket is used to maintain information about each bucket within the water jug problem
 *
 * CS 6511 Spring 2022
 * Authors: Joyce Lee, Charles Peeke, Matthew Stoffer, Xi Wang
 */

/**
 * Structure to represent the buckets for the water jug problem.
 * Each bucket contains a capacity for the bucket as well as a level of water currently in the bucket
 */
class Bucket {
    int capacity;
    int level;

    /**
     * Constructor to create a bucket given the capacity of the bucket and current level of water in the bucket
     * @param cap Capacity of the bucket
     * @param lvl Water level in the bucket
     */
    public Bucket(int cap, int lvl) {
        capacity = cap;
        level = lvl;
    }

    /**
     * Overrides default equals function
     * Checks if the capacity and level of the buckets are the same
     * The buckets are not the same if one has water and the other does not
     * @param o Bucket to compare to
     * @return Whether the buckets are equal or not
     */
    @Override
    public boolean equals(Object o) {
        Bucket test = (Bucket) o;
        if (this.capacity == test.capacity && this.level == test.level)
        {
            return true;
        }
        return false;
    }

    /**
     * Ovveride for the toString method to nicely organize information about the bucket
     * @return String representation of the bucket's level and capacity
     */
    @Override
    public String toString() {
        String result = "";
        result += ("Level: " + this.level + "\t | \t");
        result += ("Capacity: " + this.capacity + "\t | \n");
        return result;
    }
}

/**
 * Node represents the information about a particular state or vertex in the graph for the search
 */
public class Node {
    Bucket[] buckets;
    int solution;
    double g;
    double h;
    int goal;
    Node parent;
    int sum;

    /**
     * Constructor to create a new Node state based on a list of buckets and the goal value
     * @param bucks List of buckets for the state
     * @param goal Goal value for the state
     */
    public Node(Bucket[] bucks, int goal) {
        buckets = bucks;
        this.goal = goal;
    }

    /**
     * Constructor that creates a new Node based from another node data.
     * Primarily used to create neighbors from other nodes.
     * @param base Node data extracted to create a new Node
     */
    public Node(Node base) {
        this.solution = base.solution;
        this.g = base.g;
        this.h = base.h;
        this.goal = base.goal;
        this.parent = base;
        this.buckets = new Bucket[base.buckets.length];
        for (int i = 0; i < base.buckets.length; i++) {
            this.buckets[i] = new Bucket(base.buckets[i].capacity, base.buckets[i].level);
        }
    }

    /**
     * Fill a bucket given the index of the bucket to capacity of the bucket
     * @param i Index of the bucket to be filled
     */
    public void fillBucket(int i) {
        this.buckets[i].level = this.buckets[i].capacity;
    }

    /**
     * Calculate the Heuristic Values for a Node in state
     * Uses the Parent G value, and calculates the H value from the goal, solution, and bucket information
     */
    public void calculateHeuristic() {

        double parent = this.parent == null ? 0 : this.parent.g;
        this.g = parent + 1;
        if(this.goal - this.solution == 0) {
            this.h=0;
        } else {
            this.h = (this.goal - this.solution)/this.buckets.length;
        }
    }

    /**
     * Calculate the total sum of the water currently in the buckets
     * @return Sum of all the water in the buckets
     */
    public int getSum(){
        int sum = 0;
        for (Bucket bucket : this.buckets) {
            sum = sum + bucket.level;
        }
        this.sum = sum;
        return this.sum;
    }

    /**
     * Pour the water from Bucket i into Bucket j
     * - Will only pour until i is empty, or until j is full, whichever comes first
     * @param i Bucket to pour from
     * @param j Bucket Index to pour into
     */
    public void pour(int i, int j) {
        int space = this.buckets[j].capacity - this.buckets[j].level;
        // If i has more water than j has space for (5 -> 3)
        if (this.buckets[i].level > space) {
            // Only fill the space
            this.buckets[i].level -= space;
            this.buckets[j].level += space;
        } else {
            // Or put all of i into j (3 -> 5)
            this.buckets[j].level += this.buckets[i].level;
            emptyBucket(i);
        }

    }

    /**
     * Empty the water from Bucket i
     * @param i Index of the Bucket to empty water from
     */
    public void emptyBucket(int i) {
        this.buckets[i].level = 0;
    }

    /**
     * Add the water from Bucket at index i into the solution
     * @param i Index of the Bucket to pour into the solution
     */
    public void addToSolution(int i) {
        this.solution += this.buckets[i].level;
        emptyBucket(i);
    }


    /**
     * Override for the equals function
     * Two nodes are equal if
     * - the bucket states are equal
     * - the solution state is equal
     * @param o Node object to compare to
     * @return Whether the Node states are equal
     */
    @Override
    public boolean equals(Object o) {
        Node test = (Node)o;
        // If there are different solution values
        if (this.solution != test.solution) {
            return false;
        }
        // If any buckets aren't the same, they aren't the same Node
        for (int i = 0; i < buckets.length; i++) {
            if (!this.buckets[i].equals(test.buckets[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Override for the toString method
     * Creates a string representation of the buckets, solution, and heuristic values
     * @return String formatted to display information about the current node state
     */
    @Override
    public String toString() {
        String result = "";
        result += "=========\n";
        result += ("Buckets:\n");
        for (int i = 0; i < this.buckets.length; i++) {
            result += ("Bucket: " + i + "\t | \t");
            result += this.buckets[i].toString();
        }
        result += ("Soln Level: " + this.solution + "\n");
        result += ("g: " + this.g + "\n");
        result += ("h: " + this.h + "\n");
        result += ("f: " + (this.g + this.h) + "\n");
        result += ("=========");
        return result;
    }
}
