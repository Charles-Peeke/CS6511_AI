import junit.framework.TestCase;

import java.util.ArrayList;

public class WaterJugTest extends TestCase {

    public void testAStarSearch() {
        int steps = WaterJug.aStarSearch(new int[]{1, 2, 3}, 3);
        assertEquals(2, steps);
    }

    public void testAStarSearchManySteps() {
        int steps = WaterJug.aStarSearch(new int[]{1, 2, 3}, 6);
        assertEquals(4, steps);
    }

    public void testAStarSearchNoSolution() {
        int steps = WaterJug.aStarSearch(new int[]{3}, 4);
        assertEquals(-1, steps);
    }

    public void testNodeLocInOpenSet() {
        ArrayList<Node> openList = new ArrayList<>();

        Bucket[] testBuckets = {
                new Bucket(1, 0),
                new Bucket(2, 0),
                new Bucket(3, 0),
        };

        Node test = new Node(testBuckets, 3);
        test.g = 10;
        test.h = 10;
        openList.add(test);

        Bucket[] testBuckets2 = {
                new Bucket(1, 1),
                new Bucket(2, 0),
                new Bucket(3, 0),
        };

        Node test2 = new Node(testBuckets2, 3);
        openList.add(test2);

        Bucket[] testBuckets3 = {
                new Bucket(1, 0),
                new Bucket(2, 2),
                new Bucket(3, 0),
        };

        Node test3 = new Node(testBuckets3, 3);
        openList.add(test3);
        Node testFound2 = new Node(testBuckets3, 3);
        testFound2.g = -1;

        assertEquals(2, WaterJug.nodeLocInOpenSet(testFound2, openList));

    }

    public void testNodeInOpenSetFound() {
        ArrayList<Node> openList = new ArrayList<>();

        Bucket[] testBuckets = {
                new Bucket(1, 0),
                new Bucket(2, 0),
                new Bucket(3, 0),
        };

        Node test = new Node(testBuckets, 3);
        test.g = 10;
        test.h = 10;
        openList.add(test);
        Node testFound = new Node(testBuckets, 3);
        testFound.g = 1;
        testFound.h = 1;
        assertEquals(0, WaterJug.nodeLocInOpenSet(testFound, openList));
    }

    public void testNodeInOpenSetNotFound() {
        ArrayList<Node> openList = new ArrayList<>();

        Bucket[] testBuckets2 = {
                new Bucket(1, 1),
                new Bucket(2, 0),
                new Bucket(3, 0),
        };

        Node test2 = new Node(testBuckets2, 3);
        assertEquals(-1, WaterJug.nodeLocInOpenSet(test2, openList));
    }

    public void testNodeInClosedSet() {
        ArrayList<Node> closedSet = new ArrayList<>();

        Bucket[] testBuckets = {
                new Bucket(1, 0),
                new Bucket(2, 0),
                new Bucket(3, 0),
        };

        Node test = new Node(testBuckets, 3);
        closedSet.add(test);

        Bucket[] testBuckets2 = {
                new Bucket(1, 1),
                new Bucket(2, 0),
                new Bucket(3, 0),
        };

        Node test2 = new Node(testBuckets2, 3);
        assertFalse(WaterJug.nodeInClosedSet(test2, closedSet));
        closedSet.add(test2);

        Bucket[] testBuckets3 = {
                new Bucket(1, 0),
                new Bucket(2, 2),
                new Bucket(3, 0),
        };

        Node test3 = new Node(testBuckets3, 3);
        closedSet.add(test3);
        Node testFound2 = new Node(testBuckets3, 3);

        assertTrue(WaterJug.nodeInClosedSet(testFound2, closedSet));
    }

    public void testNodeInClosedSetFound() {
        ArrayList<Node> closedSet = new ArrayList<>();

        Bucket[] testBuckets = {
                new Bucket(1, 0),
                new Bucket(2, 0),
                new Bucket(3, 0),
        };

        Node test = new Node(testBuckets, 3);
        closedSet.add(test);
        Node testFound = new Node(testBuckets, 3);
        assertTrue(WaterJug.nodeInClosedSet(testFound, closedSet));
    }

    public void testNodeInClosedSetNotFound() {
        ArrayList<Node> closedSet = new ArrayList<>();

        Bucket[] testBuckets2 = {
                new Bucket(1, 1),
                new Bucket(2, 0),
                new Bucket(3, 0),
        };

        Node test2 = new Node(testBuckets2, 3);
        assertFalse(WaterJug.nodeInClosedSet(test2, closedSet));
        closedSet.add(test2);
        assertTrue(WaterJug.nodeInClosedSet(test2, closedSet));

    }

    public void testGetAllNeighbors() {
        Bucket[] testBuckets = {
                new Bucket(1, 0),
                new Bucket(2, 0),
                new Bucket(3, 0),
        };

        Node test = new Node(testBuckets, 3);
        assertEquals(0, test.buckets[0].level);

        ArrayList<Node> neighbors = WaterJug.getAllNeighbors(test);
        assertEquals(3, neighbors.size());
        assertEquals(1, neighbors.get(0).buckets[0].level);
        assertEquals(2, neighbors.get(1).buckets[1].level);
        assertEquals(3, neighbors.get(2).buckets[2].level);
        assertFalse(neighbors.get(0).equals(test));

        Bucket[] testBuckets2 = {
                new Bucket(1, 0),
                new Bucket(2, 0),
                new Bucket(3, 3),
        };

        Node test2 = new Node(testBuckets2, 3);
        neighbors = WaterJug.getAllNeighbors(test2);
        // Fill 2 Buckets // Pour 3 into Soln // Empty 3 // Pour 3 into 1 or 2
        assertEquals(2+2+1+1, neighbors.size());
        // Fill Bucket 0
        assertEquals(1, neighbors.get(0).buckets[0].level);
        assertEquals(0, neighbors.get(0).buckets[1].level);
        assertEquals(3, neighbors.get(0).buckets[2].level);
        // Full Bucket 1
        assertEquals(0, neighbors.get(1).buckets[0].level);
        assertEquals(2, neighbors.get(1).buckets[1].level);
        assertEquals(3, neighbors.get(1).buckets[2].level);
        // Empty Bucket 2
        assertEquals(0, neighbors.get(2).buckets[0].level);
        assertEquals(0, neighbors.get(2).buckets[1].level);
        assertEquals(0, neighbors.get(2).buckets[2].level);
        // Pour Bucket 2 into solution
        assertEquals(0, neighbors.get(3).buckets[0].level);
        assertEquals(0, neighbors.get(3).buckets[1].level);
        assertEquals(0, neighbors.get(3).buckets[2].level);
        assertEquals(3, neighbors.get(3).solution);
        // Pour Bucket 2 into Bucket 1 or 2
        assertEquals(1, neighbors.get(4).buckets[0].level);
        assertEquals(0, neighbors.get(4).buckets[1].level);
        assertEquals(2, neighbors.get(4).buckets[2].level);

        assertEquals(0, neighbors.get(5).buckets[0].level);
        assertEquals(2, neighbors.get(5).buckets[1].level);
        assertEquals(1, neighbors.get(5).buckets[2].level);


    }

    public void testFindSteps() {
        Bucket[] testBuckets = { new Bucket(1, 0) };

        Node test = new Node(testBuckets, 3);
        Node test2 = new Node(testBuckets, 3);
        test2.parent = test;
        Node test3 = new Node(testBuckets, 3);
        test3.parent = test2;

        assertEquals(0, WaterJug.findSteps(test));
        assertEquals(1, WaterJug.findSteps(test2));
        assertEquals(2, WaterJug.findSteps(test3));
    }

    public void testGetMinIndex() {
        ArrayList<Node> openList = new ArrayList<>();

        Bucket[] testBuckets = {
                new Bucket(1, 0),
                new Bucket(2, 0),
                new Bucket(3, 0),
        };

        Node test = new Node(testBuckets, 3);
        test.g = 10;
        test.h = 10;
        openList.add(test);
        Node testFound = new Node(testBuckets, 3);
        testFound.g = 1;
        testFound.h = 1;

        Bucket[] testBuckets2 = {
                new Bucket(1, 1),
                new Bucket(2, 0),
                new Bucket(3, 0),
        };

        Node test2 = new Node(testBuckets2, 3);
        openList.add(test2);

        Bucket[] testBuckets3 = {
                new Bucket(1, 0),
                new Bucket(2, 2),
                new Bucket(3, 0),
        };

        Node test3 = new Node(testBuckets3, 3);
        openList.add(test3);
        Node testFound2 = new Node(testBuckets3, 3);
        testFound2.g = -1;

        assertEquals(2, WaterJug.getMinIndex(openList));

    }

    public void testSolutionPossible() {
        int[] testBuckets = {1};
        for (int i = 1; i <= 100; i++) {
            assertTrue(WaterJug.solutionPossible(testBuckets, i));
        }

        int[] testBuckets2 = {2, 4, 6};
        assertFalse(WaterJug.solutionPossible(testBuckets2, 1));


        int[] testBuckets3 = {3, 5};
        assertTrue(WaterJug.solutionPossible(testBuckets3, 6));
    }

    public void testCalculateGCD() {
        assertEquals(1, WaterJug.calculateGCD(13, 7));
        assertEquals(2, WaterJug.calculateGCD(24, 2));
        assertEquals(3, WaterJug.calculateGCD(33, 12));
    }
}