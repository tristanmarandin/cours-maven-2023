package fr.imt.cours.IL.Maven.machine.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CoffeeGrinderTest {
    private CoffeeGrinder coffeeGrinder;
    private BeanTank mockBeanTank;

    @BeforeEach
    void setUp() {
        coffeeGrinder = new CoffeeGrinder(1000); // 1 second grinding time
        mockBeanTank = mock(BeanTank.class);
    }

    @Test
    void testGrindCoffee() throws InterruptedException {
        try (MockedStatic<Thread> mockedThread = Mockito.mockStatic(Thread.class)) {
            // Perform the action
            double grindingTime = coffeeGrinder.grindCoffee(mockBeanTank);

            // Assert the grinding time is correct
            assertEquals(1000, grindingTime, "The grinding time should be 1000 ms");

            // Verify that increaseVolumeInTank is called on the BeanTank with the correct volume
            verify(mockBeanTank).increaseVolumeInTank(0.2);

            // Verify that Thread.sleep was called with the correct time
            mockedThread.verify(() -> Thread.sleep(1000), times(1));
        }
    }
}