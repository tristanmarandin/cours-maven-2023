package fr.imt.cours.IL.Maven.machine.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TankTest {

    private Tank tank;

    @BeforeEach
    void setUp() {
        tank = new Tank(1.0, 0.1, 2.0);
    }

    @Test
    void testConstructor() {
        assertEquals(1.0, tank.getActualVolume(), "Initial volume should be set correctly");
        assertEquals(0.1, tank.getMinVolume(), "Minimum volume should be set correctly");
        assertEquals(2.0, tank.getMaxVolume(), "Maximum volume should be set correctly");
    }

    @Test
    void testDecreaseVolumeInTank() {
        tank.decreaseVolumeInTank(0.5);
        assertEquals(0.5, tank.getActualVolume(), "Volume should decrease correctly");
    }

    @Test
    void testDecreaseVolumeNotBelowMin() {
        tank.decreaseVolumeInTank(1.5); // Try to decrease more than the current volume
        assertEquals(tank.getMinVolume(), tank.getActualVolume(), "Volume should not go below minimum");
    }

    @Test
    void testIncreaseVolumeInTank() {
        tank.increaseVolumeInTank(0.5);
        assertEquals(1.5, tank.getActualVolume(), "Volume should increase correctly");
    }

    @Test
    void testIncreaseVolumeNotAboveMax() {
        tank.increaseVolumeInTank(2.0); // Try to increase more than the max volume
        assertEquals(tank.getMaxVolume(), tank.getActualVolume(), "Volume should not exceed maximum");
    }
}
