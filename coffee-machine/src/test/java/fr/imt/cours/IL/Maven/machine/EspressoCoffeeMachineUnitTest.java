package fr.imt.cours.IL.Maven.machine;

import fr.imt.cours.IL.Maven.machine.component.BeanTank;
import fr.imt.cours.IL.Maven.machine.component.WaterTank;
import fr.imt.cours.IL.Maven.machine.exception.CoffeeTypeCupDifferentOfCoffeeTypeTankException;
import fr.imt.cours.IL.Maven.machine.exception.LackOfWaterInTankException;
import fr.imt.cours.IL.Maven.machine.exception.MachineNotPluggedException;
import fr.imt.cours.IL.Maven.storage.cupboard.coffee.type.CoffeeType;
import fr.imt.cours.IL.Maven.storage.cupboard.container.CoffeeContainer;
import fr.imt.cours.IL.Maven.storage.cupboard.container.Cup;
import fr.imt.cours.IL.Maven.storage.cupboard.exception.CupNotEmptyException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class EspressoCoffeeMachineUnitTest {
    private EspressoCoffeeMachine espressoMachineUnderTest;
    private Cup mockCup;
    private BeanTank primaryBeanTank;
    private BeanTank secondaryBeanTank;
    private WaterTank waterTank;
    private EspressoCoffeeMachine machine;
    private Random mockRandom;

    @BeforeEach
    public void setUp() {

        ///// FIRST TEST SERIE /////
        espressoMachineUnderTest = new EspressoCoffeeMachine(0.5, 10, 0.5, 10, 700);
        mockCup = Mockito.mock(Cup.class);
        primaryBeanTank = espressoMachineUnderTest.getBeanTank();
        secondaryBeanTank = Mockito.mock(BeanTank.class); // Assuming there's a getter for this or some way to mock.
        waterTank = espressoMachineUnderTest.getWaterTank();


        // Set the cup to be empty and have a certain capacity
        Mockito.when(mockCup.isEmpty()).thenReturn(true);
        Mockito.when(mockCup.getCapacity()).thenReturn(0.15);

        // Plug in the machine to start with
        espressoMachineUnderTest.plugToElectricalPlug();

        // Assume there is enough water and beans to make a coffee
        espressoMachineUnderTest.addWaterInTank(2);
        primaryBeanTank.increaseCoffeeVolumeInTank(0.5, CoffeeType.ARABICA);
        Mockito.when(secondaryBeanTank.getBeanCoffeeType()).thenReturn(CoffeeType.ROBUSTA);

        ///// SECOND TEST SERIE /////
        machine = new EspressoCoffeeMachine(0.5, 10, 0.5, 10, 700);
        mockRandom = Mockito.mock(Random.class);
        machine.setRandomGenerator(mockRandom);
    }

    ///// FIRST TEST SERIE /////
    @Test
    public void testMakeACoffeeSuccess() throws Exception {
        // Test making a coffee successfully with the primary bean tank
        CoffeeContainer coffeeContainer = espressoMachineUnderTest.makeACoffee(mockCup, CoffeeType.ARABICA);
        Assertions.assertNotNull(coffeeContainer);
        Assertions.assertFalse(coffeeContainer.isEmpty());
        // Other assertions can be made here based on the expected state of the coffeeContainer
    }

    // Test for MachineNotPluggedException
    @Test
    public void testMakeACoffeeNotPluggedIn() {
        // Assuming there's no direct way to unplug the machine, we could directly set the `isPlugged` field to false.
        // This would typically be done through reflection if the field were private and there was no setter.
        // Since it's a protected field in CoffeeMachine, it can be directly accessed here if EspressoCoffeeMachine is in the same package.
        // However, if the field is not accessible (e.g., private or package-private in a different package), you would need to use reflection.

        // Directly set the machine to be unplugged, if possible
        espressoMachineUnderTest.isPlugged = false;

        Assertions.assertThrows(MachineNotPluggedException.class, () -> {
            espressoMachineUnderTest.makeACoffee(mockCup, CoffeeType.ARABICA);
        });
    }

    // Test for LackOfWaterInTankException
    @Test
    public void testMakeACoffeeNoWaterException() {
        waterTank.decreaseVolumeInTank(waterTank.getActualVolume() + 1); // Empty the tank
        Assertions.assertThrows(LackOfWaterInTankException.class, () ->
                espressoMachineUnderTest.makeACoffee(mockCup, CoffeeType.ARABICA)
        );
    }

    // Test for CupNotEmptyException
    @Test
    public void testMakeACoffeeCupNotEmptyException() {
        Mockito.when(mockCup.isEmpty()).thenReturn(false); // Set the cup to be non-empty
        Assertions.assertThrows(CupNotEmptyException.class, () ->
                espressoMachineUnderTest.makeACoffee(mockCup, CoffeeType.ARABICA)
        );
    }

    // Test for CoffeeTypeCupDifferentOfCoffeeTypeTankException
    @Test
    public void testMakeACoffeeDifferentBeanTypeException() {
        // Assume primary tank has ARABICA, but we request ROBUSTA which is only in the secondary tank
        Assertions.assertThrows(CoffeeTypeCupDifferentOfCoffeeTypeTankException.class, () ->
                espressoMachineUnderTest.makeACoffee(mockCup, CoffeeType.ROBUSTA)
        );
    }

    // Test that a coffee machine failure sets the machine to out of order
    @Test
    public void testCoffeeMachineFailure() {
        Random mockRandom = Mockito.mock(Random.class);
        Mockito.when(mockRandom.nextGaussian()).thenReturn(2.0); // Force a failure condition
        espressoMachineUnderTest.setRandomGenerator(mockRandom);
        espressoMachineUnderTest.coffeeMachineFailure();
        Assertions.assertTrue(espressoMachineUnderTest.isOutOfOrder());
    }

    ///// SECOND TEST SERIE
    @Test
    void testCoffeeMachineFailureCausesOutOfOrder() {
        when(mockRandom.nextGaussian()).thenReturn(2.0); // A value that should indicate a failure
        machine.coffeeMachineFailure();
        assertTrue(machine.isOutOfOrder(), "Machine should be set to out of order when random value indicates failure.");
    }

    @Test
    void testCoffeeMachineDoesNotFailForLowRandomValue() {
        when(mockRandom.nextGaussian()).thenReturn(0.5); // A value that should not indicate a failure
        machine.coffeeMachineFailure();
        assertFalse(machine.isOutOfOrder(), "Machine should not be out of order when random value is below failure threshold.");
    }

    @Test
    void testPlugToElectricalPlugSetsIsPlugged() {
        machine.plugToElectricalPlug();
        assertTrue(machine.isPlugged(), "isPlugged should be true after calling plugToElectricalPlug.");
    }

    @Test
    void testResetSetsOutOfOrderToFalse() {
        machine.setOutOfOrder(true); // Directly setting for test purposes, assuming there's a setter
        machine.reset();
        assertFalse(machine.isOutOfOrder(), "isOutOfOrder should be false after reset.");
    }

    @Test
    void testAddWaterInTankIncreasesVolumeCorrectly() {
        double initialVolume = machine.getWaterTank().getActualVolume();
        machine.addWaterInTank(1.0);
        assertEquals(initialVolume + 1.0, machine.getWaterTank().getActualVolume(), "addWaterInTank should increase the volume correctly.");
    }

    @Test
    void testAddWaterInTankDoesNotExceedMaxVolume() {
        double maxVolume = machine.getWaterTank().getMaxVolume();
        machine.addWaterInTank(maxVolume + 1.0); // Attempt to add too much water
        assertEquals(maxVolume, machine.getWaterTank().getActualVolume(), "addWaterInTank should not exceed the maximum volume.");
    }

    @Test
    void testAddCoffeeInBeanTankIncreasesVolumeCorrectly() {
        double initialVolume = machine.getBeanTank().getActualVolume();
        machine.addCoffeeInBeanTank(0.5, CoffeeType.ARABICA);
        assertEquals(initialVolume + 0.5, machine.getBeanTank().getActualVolume(), "addCoffeeInBeanTank should increase the volume correctly.");
    }

    @Test
    void testAddCoffeeInBeanTankDoesNotExceedMaxVolume() {
        double maxVolume = machine.getBeanTank().getMaxVolume();
        machine.addCoffeeInBeanTank(maxVolume + 1.0, CoffeeType.ARABICA); // Attempt to add too much coffee
        assertEquals(maxVolume, machine.getBeanTank().getActualVolume(), "addCoffeeInBeanTank should not exceed the maximum volume.");
    }

    @Test
    void testSecondaryBeanTankUsedWhenPrimaryTankDoesNotHaveRightTypeOfBeans() throws Exception {
        // Set primary bean tank to a different type of beans
        primaryBeanTank.setBeanCoffeeType(CoffeeType.ARABICA);
        secondaryBeanTank.setBeanCoffeeType(CoffeeType.ROBUSTA);

        CoffeeContainer coffeeContainer = machine.makeACoffee(mockCup, CoffeeType.ROBUSTA);

        assertNotNull(coffeeContainer);
        assertEquals(CoffeeType.ROBUSTA, coffeeContainer.getCoffeeType());
        // Additional assertions or verifications can be made here
    }

    @Test
    void testPrimaryBeanTankUsedWhenItHasRightTypeOfBeans() throws Exception {
        // Set primary bean tank to the desired type of beans
        primaryBeanTank.setBeanCoffeeType(CoffeeType.ARABICA);

        CoffeeContainer coffeeContainer = machine.makeACoffee(mockCup, CoffeeType.ARABICA);

        assertNotNull(coffeeContainer);
        assertEquals(CoffeeType.ARABICA, coffeeContainer.getCoffeeType());
        // Additional assertions or verifications can be made here
    }

    @AfterEach
    public void tearDown() {
        // Perform cleanup, if necessary
    }
}

