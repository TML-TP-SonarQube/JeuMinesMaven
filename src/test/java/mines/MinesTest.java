package mines;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MinesTest {

    @Test
    void mainTest(){
        Mines mine = new Mines();
        Mines.main(new String[]{});
        assertTrue(mine.getComponents().length > 0);

    }

}