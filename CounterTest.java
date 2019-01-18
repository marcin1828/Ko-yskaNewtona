import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        assertAll("nnsad",
                () -> assertEquals(arrayResult[0], returnedFromRhsArray[0], 0.05),
                () -> assertEquals(arrayResult[1], returnedFromRhsArray[1], 0.05));
    }

    @Test
    void rungeTest() {
    }

    @Test
    void nextDegreeTest() {
    }
}