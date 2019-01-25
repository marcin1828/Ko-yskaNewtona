import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CounterTest {


    public int add(int a, int b) {
        return a + b;
    }

    @Test
    public void addTest() {
        assertTrue(add(2, 2) == 4);
    }

    @Test
    void rhsTest() {
        Counter counter = new Counter();
        double inputArray[] = {2, 2};
        double arrayResult[] = {2, -8.92021};

        double[] returnedFromRhsArray = counter.rhs(inputArray);
        Assertions.assertAll("assertion",
                () -> assertEquals(arrayResult[0], returnedFromRhsArray[0], 0.05),
                () -> assertEquals(arrayResult[1], returnedFromRhsArray[1], 0.05));
    }

    @Test
    void rungeTest() {
    }

    @Test
    void nextDegreeTest() {

        Counter counter1 = new Counter();
        Counter counter2 = new Counter();

        double inputArray[] = {1, 1};
        double array1[] = counter1.nextDegree(inputArray);
        double array2[] = counter2.nextDegree(inputArray);

        Assertions.assertAll("assertion",
                () -> assertEquals(array1[0], array2[0]),
                () -> assertEquals(array1[1], array2[1]));

    }
}