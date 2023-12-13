package fr.imt.cours.IL.Maven.machine.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WaterPumpTest {

    private WaterPump waterPump;
    private WaterTank waterTank;
    @BeforeEach
    void setUp() {
        waterPump = new WaterPump(0.5); // 0.5L per second capacity
        waterTank = new WaterTank(2.0, 0.1, 3.0);

    }

    @Test
    void testPumpWater() throws InterruptedException {
        try (MockedStatic<Thread> mockedThread = Mockito.mockStatic(Thread.class)) {
            double pumpingTime = waterPump.pumpWater(1.0, waterTank);
            assertEquals(4000, pumpingTime, "Pumping time should be calculated correctly for 1L of water");
            assertEquals(1.0, waterTank.getActualVolume(), "WaterTank volume should decrease by 1L");
        }
    }

    @Test
    void testInterruptedExceptionHandling() {
        try (MockedStatic<Thread> mockedThread = Mockito.mockStatic(Thread.class)) {
            mockedThread.when(() -> Thread.sleep(anyLong())).thenThrow(InterruptedException.class);
            assertThrows(InterruptedException.class, () -> waterPump.pumpWater(1.0, waterTank));
        }
    }
}