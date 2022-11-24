import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class HippodromeTest {

    @Test
    public void constructorTesNullArgument() {
        assertThrows(IllegalArgumentException.class, () -> new Hippodrome(null));
    }

    @Test
    public void messageTesNullArgument() {
        try {
            new Hippodrome(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Horses cannot be null.", e.getMessage());
        }
    }

    @Test
    public void constructorTestNullArgumentInArrayList() {
        assertThrows(IllegalArgumentException.class, () -> new Hippodrome(new ArrayList<>()));
    }


    @Test
    public void messageTestNullArgumentInArrayList() {
        try {
            new Hippodrome(new ArrayList<>());
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Horses cannot be empty.", e.getMessage());
        }
    }
//--------------------------

    @Test   // -> этот тест более правильный в отличии, от верхнего
    public void getHorsesTest() throws NoSuchFieldException, IllegalAccessException {
        List<Horse> horses = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            horses.add(new Horse("Id" + i, i, i));
        }
        Hippodrome hippodrome = new Hippodrome(horses);
        Field declaredField = Hippodrome.class.getDeclaredField("horses");
        declaredField.setAccessible(true);
        List<Horse> horseList = (ArrayList<Horse>) declaredField.get(hippodrome);
        assertEquals(horses, horseList);
        assertSame(horses, horseList);
    }

    @Test
    public void moveTest() {
        // arrange  -> подготовка к тесту
        List<Horse> horses = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            horses.add(mock(Horse.class));
        }

        // act  -> что тестируем
        new Hippodrome(horses).move();

        //assert  -> проверка
        horses.forEach(horse -> verify(horse).move());
    }

    @Test
    void getWinnerTest() {
        Horse horse = new Horse("name",1,1.5);
        Horse horse1 = new Horse("name1",1,4);
        Horse horse2 = new Horse("name2",1,3);
        Horse horse3 = new Horse("name3",1,2);

        Hippodrome hippodrome = new Hippodrome(List.of(horse1,horse,horse2,horse3));

        assertSame(horse1, hippodrome.getWinner());// -> assertSame потому что мы проверяем объект который сами и создали,
        // если будем проверять, через assertEquals, то нужно переопределить методы EqualsAndHashCode
    }
}