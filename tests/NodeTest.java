import junit.framework.TestCase;

public class NodeTest extends TestCase {

    public void testFillBucket() {
        Bucket[] testBuckets = {
                new Bucket(1, 0),
                new Bucket(2, 0),
                new Bucket(3, 0),
        };

        Node test = new Node(testBuckets, 3);

        test.fillBucket(0);
        assertEquals(1, test.buckets[0].level);
        assertEquals(1, test.buckets[0].capacity);

        test.fillBucket(1);
        assertEquals(2, test.buckets[1].level);
        assertEquals(2, test.buckets[1].capacity);

        test.fillBucket(2);
        assertEquals(3, test.buckets[2].level);
        assertEquals(3, test.buckets[2].capacity);
    }

    public void testCalculateHeuristic() {
        Bucket[] testBuckets = {
                new Bucket(1, 0),
                new Bucket(2, 0),
                new Bucket(3, 0),
        };

        Node test = new Node(testBuckets, 3);

        test.calculateHeuristic();

        assertEquals(1.0, test.g);
        assertEquals(1.0, test.h);

        Bucket[] testBuckets2 = {
                new Bucket(1, 0),
                new Bucket(2, 0),
                new Bucket(3, 0),
        };

        Node test2 = new Node(testBuckets2, 6);

        test2.calculateHeuristic();

        assertEquals(1.0, test2.g);
        assertEquals(2.0, test2.h);

        Bucket[] testBuckets3 = {
                new Bucket(2, 0),
                new Bucket(3, 0),
        };

        Node test3 = new Node(testBuckets3, 1);

        test3.calculateHeuristic();

        assertEquals(1.0, test3.g);
        assertEquals(0.0, test3.h);

    }

    public void testPour() {
        Bucket[] testBuckets = {
                new Bucket(1, 0),
                new Bucket(2, 0),
                new Bucket(3, 0),
        };

        Node test = new Node(testBuckets, 3);

        test.pour(0, 1);
        assertEquals(0, test.buckets[0].level);
        assertEquals(0, test.buckets[1].level);

        test.fillBucket(0);
        test.pour(0, 1);
        assertEquals(0, test.buckets[0].level);
        assertEquals(1, test.buckets[1].level);

        test.pour(1, 2);
        assertEquals(0, test.buckets[1].level);
        assertEquals(1, test.buckets[2].level);

    }

    public void testEmptyBucket() {
        Bucket[] testBuckets = {
                new Bucket(1, 1),
                new Bucket(2, 2),
                new Bucket(3, 3),
        };

        Node test = new Node(testBuckets, 3);

        test.emptyBucket(0);
        assertEquals(0, test.buckets[0].level);
        assertEquals(0, test.solution);


        test.emptyBucket(1);
        assertEquals(0, test.buckets[0].level);
        assertEquals(0, test.buckets[1].level);
        assertEquals(0, test.solution);

        test.emptyBucket(2);
        assertEquals(0, test.buckets[0].level);
        assertEquals(0, test.buckets[1].level);
        assertEquals(0, test.buckets[2].level);
        assertEquals(0, test.solution);

    }

    public void testAddToSolution() {
        Bucket[] testBuckets = {
                new Bucket(1, 1),
                new Bucket(2, 2),
                new Bucket(3, 3),
        };

        Node test = new Node(testBuckets, 6);

        test.addToSolution(0);
        assertEquals(0, test.buckets[0].level);
        assertEquals(1, test.solution);


        test.addToSolution(1);
        assertEquals(0, test.buckets[0].level);
        assertEquals(0, test.buckets[1].level);
        assertEquals(3, test.solution);

        test.addToSolution(2);
        assertEquals(0, test.buckets[0].level);
        assertEquals(0, test.buckets[1].level);
        assertEquals(0, test.buckets[2].level);
        assertEquals(6, test.solution);
    }

    public void testTestEquals() {
        Bucket[] testBuckets = {
                new Bucket(1, 0),
                new Bucket(2, 0),
                new Bucket(3, 0),
        };
        Bucket[] testBuckets2 = {
                new Bucket(1, 0),
                new Bucket(2, 0),
                new Bucket(3, 0),
        };
        Bucket[] testBuckets3 = {
                new Bucket(1, 0),
                new Bucket(2, 0),
                new Bucket(3, 3),
        };

        Node test = new Node(testBuckets, 6);
        Node testEqual = new Node(testBuckets2, 6);
        Node testNotEqual = new Node(testBuckets3, 6);

        assertTrue(test.equals(testEqual));
        assertFalse(test.equals(testNotEqual));

    }
}