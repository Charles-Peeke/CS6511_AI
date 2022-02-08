import java.util.ArrayList;

/**
 * This is the main driver class for the Water Jug Problem
 * The main function takes an input file and returns the number of steps required to fill and move water
 * until the solution bucket has the goal amount of water in it.
 *
 * CS 6511 Spring 2022
 * Authors: Joyce Lee, Charles Peeke, Matthew Stoffer, Xi Wang
 */

/**
 * This class contains the driver functionality for the A Star Search as it relates to the Water Jug Problem
 */
public class WaterJug {

    /**
     * This is the driver method for the water jug problem
     * @throws Exception : Occurs when a file cannot be read
     */
    public static void main(String[] args) throws Exception {
        // Read in the input file
        FileReader fR = new FileReader();
        fR.readFile("./data/input");

        // Save the data about the pitchers and the goal water total
        int[] inBuckets = fR.pitchers;
        int goal = fR.goal;

        // Perform the Search
        int result = aStarSearch(inBuckets, goal);

        // Display the results
        System.out.println("Steps: " + result);
    }

    /**
     * This function calculates the aStarSearch for the given parameters of the buckets and their capacity values
     * - OpenList keeps nodes that have not already been visited
     * - ClosedList keeps nodes that have already been visited
     *
     * Each iteration contains the same principles:
     * - Check if the current state has met the goal requirements (if so, the search is done)
     * - Find the neighbors of the current node based on how the water can move
     * - Add the neighbors to the list of nodes to explore if they have not already been explored or queued
     *
     * @param inBuckets An array that contains the capacities of each bucket
     * @param goal The goal amount of water that should be in the solution bucket to finish the search
     * @return The number of steps taken to find the solution from the given buckets
     */
    public static int aStarSearch(int[] inBuckets, int goal) {

        // Check if the solution is possible given the bucket values
        if (!solutionPossible(inBuckets, goal)) {
            return -1;
        }

        // Create the initial starting state
        Bucket[] startBuckets = new Bucket[inBuckets.length];
        for (int i = 0; i < inBuckets.length; i++) {
            startBuckets[i] = new Bucket(inBuckets[i], 0);
        }
        Node start = new Node(startBuckets, goal);

        // Initialize the lists of nodes we have visited and have not visited
        ArrayList<Node> openList = new ArrayList<>();
        ArrayList<Node> closedList = new ArrayList<>();

        openList.add(start);

        // Perform the search until there are no more unvisited nodes to search
        while (openList.size() > 0) {

            // Get Node with smallest f -- g + h
            int minIndex = getMinIndex(openList);
            Node current = openList.remove(minIndex);
            closedList.add(current);

            // Explore the current node if it has not met the goal conditions yet.
            if (current.solution != goal) {

                // Get the neighbors for the current node
                ArrayList<Node> neighbors = getAllNeighbors(current);

                // Search through all the neighbors to calculate heuristics for the states
                int neighborCount = neighbors.size();
                for (int i = 0; i < neighborCount; i++) {

                    // Get the current neighbor to explore
                    Node testNeighbor = neighbors.get(i);

                    // If the node is not already queued to be visited and has not been visited already,
                    // set the parent and add 1 to the number of steps taken to find the node
                    if (!nodeInClosedSet(testNeighbor, closedList) && nodeLocInOpenSet(testNeighbor, openList) == -1) {
                        openList.add(testNeighbor);
                        testNeighbor.parent = current;
                        testNeighbor.g = current.g+1;
                    } else {
                        // If the new path to the node is more optimal than the previous, set the new path
                        if (testNeighbor.g> current.g+testNeighbor.getSum()) {
                            testNeighbor.g = current.g+1;
                            testNeighbor.parent = current;

                            // If we found a better solution, remove it from visited
                            // Add the better solution state to the nodes to be visited
                            if (nodeInClosedSet(testNeighbor, closedList)) {
                                closedList.remove(testNeighbor);
                                openList.add(testNeighbor);
                            }
                        }
                    }
                }
            }
            // In the case that the current state is the goal state, the search is done
            if (current.solution == goal) {
                int steps = findSteps(current);
                return steps;
            }
        }
        return -1;
    }

    /**
     * Search the given list of nodes yet to be visited and determine if the given node is in the open List
     * If it is in the open list and has a smaller value f() then return the position of the found node
     * @param node The node to search for within the given list
     * @param openList The list containing all nodes that are created, but have not yet been visited
     * @return The location of the node or -1 if the node was not found
     */
    public static int nodeLocInOpenSet(Node node, ArrayList<Node> openList) {
        int size = openList.size();
        for (int i = 0; i < size; i++) {
            Node test = openList.get(i);
            if (node.equals(test)) {
                if ((node.g + node.h) < (test.g + test.h)) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Determines if the give node already exists within the closed List
     * @param node The node to search for within the given list
     * @param closedList The list containing all nodes that have already been visited
     * @return Whether the node was found in the list or not
     */
    public static boolean nodeInClosedSet(Node node, ArrayList<Node> closedList) {
        int size = closedList.size();
        for (int i = 0; i < size; i++) {
            Node test = closedList.get(i);
            if (node.equals(test)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Finds all the possible neighbors of a give Node
     * - Fill each bucket that is not full
     * - Pour each bucket with water into another bucket
     * - Empty each bucket with water in it
     * - Pour each bucket with water in it into the solution
     * @param current Node of the current state to find neighbors of
     * @return List of all nodes that neighbor the current state node
     */
    public static ArrayList<Node> getAllNeighbors(Node current) {
        ArrayList<Node> neighbors = new ArrayList<Node>();
        for (int i = 0; i < current.buckets.length; i++) {
            Bucket bucket = current.buckets[i];
            // Need to fill water
            if (bucket.level == 0) {
                Node neighbor = new Node(current);
                // Fill the Bucket
                neighbor.fillBucket(i);

                // Calculate the estimated "distance" from here to goal
                neighbor.calculateHeuristic();

                // Found a neighbor
                neighbors.add(neighbor);
            }
            // If bucket level != 0
            else {
                // If there's space, we can fill the bucket to capacity
                if (bucket.capacity > bucket.level) {
                    Node neighbor = new Node(current);
                    neighbor.fillBucket(i);
                    neighbor.calculateHeuristic();
                    neighbors.add(neighbor);
                }
                // Empty Bucket
                if (bucket.level > 0) {
                    Node neighbor = new Node(current);
                    neighbor.emptyBucket(i);
                    neighbor.calculateHeuristic();
                    neighbors.add(neighbor);

                    // POUR I into SOLUTION
                    if (current.solution + bucket.level <= current.goal) {
                        neighbor = new Node(current);
                        neighbor.addToSolution(i);
                        neighbor.calculateHeuristic();
                        neighbors.add(neighbor);
                    }
                }
                // Pour I into any other bucket
                for (int j = 0; j < current.buckets.length; j++) {
                    // We can't pour from the same bucket into the same bucket
                    if (i == j) continue;
                    // POUR I into J
                    // IF J has SPACE
                    if ((current.buckets[j].capacity - current.buckets[j].level) > 0) {
                        Node neighbor = new Node(current);

                        // Pour what it can from I to J
                        neighbor.pour(i, j);

                        // Calculate the estimated "distance" from here to goal
                        neighbor.calculateHeuristic();

                        // Found a neighbor
                        neighbors.add(neighbor);
                    }
                }
            }
        }
        return neighbors;
    }

    /**
     * This calculates the number of steps from the current node to the original node
     * @param current Node to calculate the number of steps
     * @return Number of steps to reach the current Node
     */
    public static int findSteps(Node current) {
        if (current.parent == null) {
            return 0;
        }
//        System.out.println(current.solution);
//        System.out.println(current);
        return 1 + findSteps(current.parent);
    }

    /**
     * Gets the index of the node with the minimum based on the heuristic calculation
     * @param openList List of nodes not yet visited
     * @return Index that contains the smallest heuristic
     */
    public static int getMinIndex(ArrayList<Node> openList) {
        int minIndex = 0;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < openList.size(); i++) {
            Node temp = openList.get(i);
            if ((temp.g + temp.h) <= min) {
                min = (int) temp.g + (int) temp.h;
                minIndex = i;
            }
        }
        return minIndex;
    }

    /**
     * Tests if the solution is possible based on the input bucket values and the goal state
     * If there are only even buckets, there is no way to reach an odd value goal
     * @param inBuckets An array that contains the capacities of each bucket
     * @param goal The goal amount of water that should be in the solution bucket to finish the search
     * @return Whether the solution is possible or not
     */
    public static boolean solutionPossible(int[] inBuckets, int goal) {
        int gcd = inBuckets[0];
        // Calculate the GCD across all given buckets
        for (int i = 1; i < inBuckets.length; i++) {
            gcd = calculateGCD(gcd, inBuckets[i]);
            // If at any point, some combination of buckets divides the goal, then we know the solution is possible
            if (goal % gcd == 0) {
                return true;
            }
        }
        if (goal % gcd == 0) {
            return true;
        }
        // If no combination of buckets has reached a GCD divisible by the goal, there is no possible solution
        return false;
    }

    /**
     * Standard GCD calculation given two numbers
     * @param a First number to calculate the GCD
     * @param b Second Number to calculate the GCD
     * @return GCD of the two numbers
     */
    public static int calculateGCD(int a, int b) {
        if (a == 0)
            return b;
        return calculateGCD(b % a, a);
    }

}
