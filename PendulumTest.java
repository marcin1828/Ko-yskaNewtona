import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PendulumTest {

    @Test
    void checkCollisionTest() {

        //wahadła zachodzą na siebie
        Pendulum pendulum1 = new Pendulum("Wahadło1",100,30,50,400);
        Pendulum pendulum2 = new Pendulum("Wahadło2",150,30,50,400);
        assertTrue(pendulum1.checkCollision(pendulum2));

        //wahadła stykają się
        pendulum2.setSphereX(160);
        assertTrue(pendulum1.checkCollision(pendulum2));

        //wahadła oddalone od siebie
        pendulum2.setSphereX(161);
        assertFalse(pendulum1.checkCollision(pendulum2));

    }
}