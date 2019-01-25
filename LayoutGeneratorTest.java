import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LayoutGeneratorTest {

    @Test
    void generate() {

        //sprawdzam czy tworzone jest 5 wahadeł
        LayoutGenerator lg1 = new LayoutGenerator();
        lg1.generate(5,false);
        assertEquals(lg1.getListOfPendulums().size(),5);

        //sprawdzam czy dodawane jest kolejne 7 wahadeł
        lg1.generate(7,false);
        assertEquals(lg1.getListOfPendulums().size(),12);

        //sprawdzam poprawność usuwania wahadeł
        lg1.clearListOfPendulums();
        assertEquals(lg1.getListOfPendulums().size(),0);

    }
}


