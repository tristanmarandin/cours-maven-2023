package fr.imt.cours.IL.Maven.machine.component;

import fr.imt.cours.IL.Maven.storage.cupboard.coffee.type.CoffeeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
class BeanTankTest {

    @Spy
    private BeanTank beanTank;

    @BeforeEach
    void setUp() {
        // Since we're using a Spy, we need to call the real constructor manually
        beanTank = spy(new BeanTank(1.0, 0.1, 2.0, CoffeeType.ARABICA));
    }

    @Test
    void testIncreaseCoffeeVolumeInTank() {
        // Setup the coffee type and volume to increase
        CoffeeType newType = CoffeeType.ROBUSTA;
        double volumeToAdd = 0.5;

        // Perform the action
        beanTank.increaseCoffeeVolumeInTank(volumeToAdd, newType);

        // Assert the coffee volume has increased
        assertEquals(1.5, beanTank.getActualVolume(), "The coffee volume should have increased by 0.5");

        // Assert the coffee type has changed
        assertEquals(newType, beanTank.getBeanCoffeeType(), "The coffee type should have changed to ROBUSTA");
    }
}

