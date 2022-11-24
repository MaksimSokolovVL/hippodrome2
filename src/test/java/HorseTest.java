import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.mockStatic;

class HorseTest {

    private Horse koHb;
    private final String horseName = "KoHb";
    private final Double horseSpeed = 0.5;
    private final Double horseDistance = 0.0;

    @BeforeEach
    void initHorse() {
        koHb = new Horse(horseName, horseSpeed, horseDistance);
    }

    @AfterEach
    void destroyHorse() {
        koHb = null;
    }

    @Test
    void constructorNameNullExceptionTest() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Horse(null, horseSpeed));
        assertEquals("Name cannot be null.", exception.getMessage());
    }


    @ParameterizedTest  // -> тест с параметрами
    @ValueSource(strings = {"", " ", "  ", "\t", "\n"})
    void listParameterNameExceptionTest(String name) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Horse(name, horseSpeed));
        assertEquals("Name cannot be blank.", exception.getMessage());
    }

    @Test
    void constructorNumberSpeedExceptionTest() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Horse("KoHb", -1.0));
        assertEquals("Speed cannot be negative.", exception.getMessage());
    }

    @Test
    void constructorNumberDistanceExceptionTest() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Horse("KoHb", horseSpeed, -1.0));
        assertEquals("Distance cannot be negative.", exception.getMessage());
    }


    @Test
    void getName() throws NoSuchFieldException, IllegalAccessException {
        Field name = Horse.class.getDeclaredField("name");
        name.setAccessible(true);
        String valueName = (String) name.get(koHb);
        assertEquals(horseName, valueName);
    }

    @Test
    void getSpeed() throws NoSuchFieldException, IllegalAccessException {
        Field speed = Horse.class.getDeclaredField("speed");
        speed.setAccessible(true);
        Double valueSpeed = (Double) speed.get(koHb);
        assertEquals(horseSpeed, valueSpeed);
    }

    @Test
    void getDistance() throws IllegalAccessException, NoSuchFieldException {
        Field distance = Horse.class.getDeclaredField("distance");
        distance.setAccessible(true);
        Double valueDistance = (Double) distance.get(koHb);
        assertEquals(horseDistance, valueDistance);
    }

    @Test
    void moveUsesGetRandom() {      //статический мок  ->  т.к. public static double getRandomDouble статический
        try (MockedStatic<Horse> mockedStatic = mockStatic(Horse.class)) {
            koHb.move();
            mockedStatic.verify(() -> Horse.getRandomDouble(0.2, 0.9)); // -> проверяем, что метод вызывается!  ->  с любыми значениями Double
        }
    }

    /**
     * Проверить, что метод присваивает дистанции значение высчитанное по формуле:
     * distance + speed * getRandomDouble(0.2, 0.9). Для этого нужно замокать getRandomDouble,
     * чтобы он возвращал определенные значения, которые нужно задать параметризовав тест.
     */
    @ParameterizedTest
    @CsvSource({"0.1", "0.2", "0.5", "0.8", "1.0", "999.999", "0.0"})
    void move(double random) {
// arrange  -> подготовка к тесту
        try (MockedStatic<Horse> mockedStatic = mockStatic(Horse.class)) {
            Horse horse = new Horse("name", 21, 283);
            mockedStatic.when(() -> Horse.getRandomDouble(0.2, 0.9)).thenReturn(random); //задаем поведение для статического метода getRandomDouble()
            //Мы говорим: используя объект mockedStatic, что .when(() "когда", будет вызван метод
            // com.company.Horse.getRandomDouble(с определенными аргументами "0.2,0.9")), .thenReturn(random) "вернуть" random.
            // random это наше значение параметров @ValueSource

// act  -> теперь запускаем метод, который проверяем
            horse.move();

//assert  -> проверка, что дистанция высчитана по этой формуле == результату метода .getDistance()
            assertEquals(283 + 21 * random, horse.getDistance());
        }
    }
}