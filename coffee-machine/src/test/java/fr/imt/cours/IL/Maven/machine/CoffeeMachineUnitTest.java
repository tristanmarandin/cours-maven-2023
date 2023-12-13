package fr.imt.cours.IL.Maven.machine;

import fr.imt.cours.IL.Maven.machine.exception.CoffeeTypeCupDifferentOfCoffeeTypeTankException;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CoffeeMachineUnitTest {
    public CoffeeMachine coffeeMachineUnderTest;

    /**
     * @BeforeEach est une annotation permettant d'exécuter la méthode annotée avant chaque test unitaire
     * Ici avant chaque test on initialise la machine à café
     */
    @BeforeEach
    public void beforeTest(){
        coffeeMachineUnderTest = new CoffeeMachine(
                0,10,
                0,10,  700);
    }

    /**
     * On vient tester si la machine ne se met pas en défaut
     */
    @Test
    public void testMachineFailureTrue(){
        //On créé un mock de l'objet random
        Random randomMock = Mockito.mock(Random.class, Mockito.withSettings().withoutAnnotations());
        //On vient ensuite stubber la méthode nextGaussian pour pouvoir contrôler la valeur retournée
        //ici on veut qu'elle retourne 1.0
        //when : permet de définir quand sur quelle méthode établir le stub
        //thenReturn : va permettre de contrôler la valeur retournée par le stub
        Mockito.when(randomMock.nextGaussian()).thenReturn(1.0);
        //On injecte ensuite le mock créé dans la machine à café
        coffeeMachineUnderTest.setRandomGenerator(randomMock);

        //On vérifie que le booleen outOfOrder est bien à faux avant d'appeler la méthode
        Assertions.assertFalse(coffeeMachineUnderTest.isOutOfOrder());
        //Ou avec Hamcrest
        assertThat(false, is(coffeeMachineUnderTest.isOutOfOrder()));

        //on appelle la méthode qui met la machine en défaut
        //On a mocké l'objet random donc la valeur retournée par nextGaussian() sera 1
        //La machine doit donc se mettre en défaut
        coffeeMachineUnderTest.coffeeMachineFailure();

        Assertions.assertTrue(coffeeMachineUnderTest.isOutOfOrder());
        assertThat(true, is(coffeeMachineUnderTest.isOutOfOrder()));
    }

    /**
     * On vient tester si la machine se met en défaut
     */
    @Test
    public void testMachineFailureFalse(){
        //On créé un mock de l'objet random
        Random randomMock = Mockito.mock(Random.class, Mockito.withSettings().withoutAnnotations());
        //On vient ensuite stubber la méthode nextGaussian pour pouvoir contrôler la valeur retournée
        //ici on veut qu'elle retourne 0.6
        //when : permet de définir quand sur quelle méthode établir le stub
        //thenReturn : va permettre de contrôler la valeur retournée par le stub
        Mockito.when(randomMock.nextGaussian()).thenReturn(0.6);
        //On injecte ensuite le mock créé dans la machine à café
        coffeeMachineUnderTest.setRandomGenerator(randomMock);

        //On vérifie que le booleen outOfOrder est bien à faux avant d'appeler la méthode
        Assertions.assertFalse(coffeeMachineUnderTest.isOutOfOrder());
        //Ou avec Hamcrest
        assertThat(false, is(coffeeMachineUnderTest.isOutOfOrder()));

        //on appelle la méthode qui met la machine en défaut
        //On a mocker l'objet random donc la valeur retournée par nextGaussian() sera 0.6
        //La machine doit donc NE PAS se mettre en défaut
        coffeeMachineUnderTest.coffeeMachineFailure();

        Assertions.assertFalse(coffeeMachineUnderTest.isOutOfOrder());
        //Ou avec Hamcrest
        assertThat(false, is(coffeeMachineUnderTest.isOutOfOrder()));
    }

    /**
     * On test que la machine se branche correctement au réseau électrique
     */
    @Test
    public void testPlugMachine(){
        Assertions.assertFalse(coffeeMachineUnderTest.isPlugged());

        coffeeMachineUnderTest.plugToElectricalPlug();

        Assertions.assertTrue(coffeeMachineUnderTest.isPlugged());
    }

    /**
     * On test qu'une exception est bien levée lorsque que le cup passé en paramètre retourne qu'il n'est pas vide
     * Tout comme le test sur la mise en défaut afin d'avoir un comportement isolé et indépendant de la machine
     * on vient ici mocker un objet Cup afin d'en maitriser complétement son comportement
     * On ne compte pas sur "le bon fonctionnement de la méthode"
     */
    @Test
    public void testMakeACoffeeCupNotEmptyException(){
        Cup mockCup = Mockito.mock(Cup.class);
        Mockito.when(mockCup.isEmpty()).thenReturn(false);

        coffeeMachineUnderTest.plugToElectricalPlug();

        coffeeMachineUnderTest.addWaterInTank(8.0);
        //assertThrows( [Exception class expected], [lambda expression with the method that throws an exception], [exception message expected])
        //AssertThrows va permettre de venir tester la levée d'une exception, ici lorsque que le contenant passé en
        //paramètre n'est pas vide
        //On teste à la fois le type d'exception levée mais aussi le message de l'exception
        Assertions.assertThrows(CupNotEmptyException.class, ()->{
                coffeeMachineUnderTest.makeACoffee(mockCup, CoffeeType.MOKA);
            });
    }


    @Test
    public void testAddWaterInTank() {
        double initialVolume = coffeeMachineUnderTest.getWaterTank().getActualVolume();
        coffeeMachineUnderTest.addWaterInTank(1.0);
        Assertions.assertEquals(initialVolume + 1.0, coffeeMachineUnderTest.getWaterTank().getActualVolume(), "Water should be added to the tank");
    }

    @Test
    public void testAddCoffeeInBeanTank() {
        double initialVolume = coffeeMachineUnderTest.getBeanTank().getActualVolume();
        coffeeMachineUnderTest.addCoffeeInBeanTank(0.5, CoffeeType.ARABICA);
        Assertions.assertEquals(initialVolume + 0.5, coffeeMachineUnderTest.getBeanTank().getActualVolume(), "Coffee should be added to the bean tank");
    }

    @Test
    public void testMakeACoffeeSuccess() throws Exception {
        // Assuming the machine is plugged in and has enough water and coffee
        coffeeMachineUnderTest.plugToElectricalPlug();
        coffeeMachineUnderTest.addWaterInTank(2.0);
        coffeeMachineUnderTest.addCoffeeInBeanTank(1.0, CoffeeType.ARABICA);

        Cup mockCup = Mockito.mock(Cup.class);
        Mockito.when(mockCup.isEmpty()).thenReturn(true);
        Mockito.when(mockCup.getCapacity()).thenReturn(0.15);

        CoffeeContainer coffeeContainer = coffeeMachineUnderTest.makeACoffee(mockCup, CoffeeType.ARABICA);
        Assertions.assertNotNull(coffeeContainer, "A coffee container should be returned");
        Assertions.assertFalse(coffeeContainer.isEmpty(), "The returned coffee container should not be empty");
    }

    @Test
    public void testCoffeeMachineFailureSetsOutOfOrder() {
        // Set the random generator to always return a high value that will trigger a failure
        Random randomMock = Mockito.mock(Random.class);
        Mockito.when(randomMock.nextGaussian()).thenReturn(2.0);
        coffeeMachineUnderTest.setRandomGenerator(randomMock);

        coffeeMachineUnderTest.coffeeMachineFailure();

        Assertions.assertTrue(coffeeMachineUnderTest.isOutOfOrder(), "Coffee machine should be out of order after failure");
    }

    @Test
    public void testPlugMachineWhenAlreadyPlugged(){
        coffeeMachineUnderTest.plugToElectricalPlug(); // First plug
        Assertions.assertTrue(coffeeMachineUnderTest.isPlugged());

        coffeeMachineUnderTest.plugToElectricalPlug(); // Second plug
        Assertions.assertTrue(coffeeMachineUnderTest.isPlugged(), "Machine should remain plugged when already plugged");
    }

    @Test
    public void testResetSetsOutOfOrderToFalse(){
        coffeeMachineUnderTest.setOutOfOrder(true);
        coffeeMachineUnderTest.reset();
        Assertions.assertFalse(coffeeMachineUnderTest.isOutOfOrder(), "reset() should set isOutOfOrder to false");
    }

    @Test
    public void testResetWhenNotOutOfOrder(){
        coffeeMachineUnderTest.setOutOfOrder(false);
        coffeeMachineUnderTest.reset();
        Assertions.assertFalse(coffeeMachineUnderTest.isOutOfOrder(), "reset() should not affect isOutOfOrder when it is already false");
    }

    @Test
    public void testAddWaterInFullTank() {
        double maxCapacity = coffeeMachineUnderTest.getWaterTank().getMaxVolume();
        coffeeMachineUnderTest.addWaterInTank(maxCapacity); // Fill to max capacity
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            coffeeMachineUnderTest.addWaterInTank(1.0); // Try adding more water
        });
    }

    @Test
    public void testAddNegativeAmountOfWater() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            coffeeMachineUnderTest.addWaterInTank(-1.0); // Try adding negative water
        });
    }

    @Test
    public void testAddCoffeeInFullBeanTank() {
        double maxCapacity = coffeeMachineUnderTest.getBeanTank().getMaxVolume();
        coffeeMachineUnderTest.addCoffeeInBeanTank(maxCapacity, CoffeeType.ARABICA); // Fill to max capacity
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            coffeeMachineUnderTest.addCoffeeInBeanTank(0.5, CoffeeType.ARABICA); // Try adding more coffee
        });
    }

    @Test
    public void testAddNegativeAmountOfCoffeeBeans() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            coffeeMachineUnderTest.addCoffeeInBeanTank(-0.5, CoffeeType.ARABICA); // Try adding negative coffee
        });
    }

    @Test
    public void testChangeTypeOfCoffeeBeansInTank() {
        coffeeMachineUnderTest.addCoffeeInBeanTank(0.5, CoffeeType.ARABICA);
        coffeeMachineUnderTest.addCoffeeInBeanTank(0.5, CoffeeType.ROBUSTA); // Change type
        Assertions.assertEquals(CoffeeType.ROBUSTA, coffeeMachineUnderTest.getBeanTank().getBeanCoffeeType(), "Bean type should change in the tank");
    }

    @Test
    public void testMakeCoffeeWhenNotPlugged() {
        Cup mockCup = Mockito.mock(Cup.class);
        Mockito.when(mockCup.isEmpty()).thenReturn(true);

        Assertions.assertThrows(MachineNotPluggedException.class, () -> {
            coffeeMachineUnderTest.makeACoffee(mockCup, CoffeeType.ARABICA);
        });
    }

    @Test
    public void testMakeCoffeeContainerNotEmpty() {
        coffeeMachineUnderTest.plugToElectricalPlug();
        Cup mockCup = Mockito.mock(Cup.class);
        Mockito.when(mockCup.isEmpty()).thenReturn(false);

        Assertions.assertThrows(CupNotEmptyException.class, () -> {
            coffeeMachineUnderTest.makeACoffee(mockCup, CoffeeType.ARABICA);
        });
    }

    @Test
    public void testMakeCoffeeDifferentTypeInTank() {
        coffeeMachineUnderTest.plugToElectricalPlug();
        coffeeMachineUnderTest.addCoffeeInBeanTank(1.0, CoffeeType.ARABICA);
        Cup mockCup = Mockito.mock(Cup.class);
        Mockito.when(mockCup.isEmpty()).thenReturn(true);

        Assertions.assertThrows(CoffeeTypeCupDifferentOfCoffeeTypeTankException.class, () -> {
            coffeeMachineUnderTest.makeACoffee(mockCup, CoffeeType.ROBUSTA);
        });
    }

    /*
    @Test
    public void testMakeCremaCoffeeWithSimpleMachine() {
        coffeeMachineUnderTest.plugToElectricalPlug();
        Cup mockCup = Mockito.mock(Cup.class);
        Mockito.when(mockCup.isEmpty()).thenReturn(true);

        Assertions.assertThrows(CannotMakeCremaWithSimpleCoffeeMachine.class, () -> {
            coffeeMachineUnderTest.makeACoffee(mockCup, CoffeeType.CREMA);
        });
    }


    @Test
    public void testMakeCoffeeWhenMachineOutOfOrder() {
        coffeeMachineUnderTest.plugToElectricalPlug();
        coffeeMachineUnderTest.setOutOfOrder(true); // Simulating out of order
        Cup mockCup = Mockito.mock(Cup.class);
        Mockito.when(mockCup.isEmpty()).thenReturn(true);

        // Use assertThrows to verify that the specific exception is thrown
        Assertions.assertThrows(CannotMakeCremaWithSimpleCoffeeMachine.class, () -> {
            coffeeMachineUnderTest.makeACoffee(mockCup, CoffeeType.CREMA);
        });
    }

    @Test
    public void testMakeCoffeeWhenNotEnoughWater() {
        coffeeMachineUnderTest.plugToElectricalPlug();
        // Assuming the mockCup requires more water than is available in the tank
        Cup mockCup = Mockito.mock(Cup.class);
        Mockito.when(mockCup.isEmpty()).thenReturn(true);
        Mockito.when(mockCup.getCapacity()).thenReturn(Double.MAX_VALUE);

        Assertions.assertThrows(LackOfWaterInTankException.class, () -> {
            coffeeMachineUnderTest.makeACoffee(mockCup, CoffeeType.ARABICA);
        });
    }
    */

    @Test
    public void testMakeCoffeeSuccess() throws Exception {
        coffeeMachineUnderTest.plugToElectricalPlug();
        coffeeMachineUnderTest.addWaterInTank(2.0);
        coffeeMachineUnderTest.addCoffeeInBeanTank(1.0, CoffeeType.ARABICA);

        Cup mockCup = Mockito.mock(Cup.class);
        Mockito.when(mockCup.isEmpty()).thenReturn(true);
        Mockito.when(mockCup.getCapacity()).thenReturn(0.15);

        CoffeeContainer coffeeContainer = coffeeMachineUnderTest.makeACoffee(mockCup, CoffeeType.ARABICA);
        Assertions.assertNotNull(coffeeContainer, "A coffee container should be returned");
        Assertions.assertFalse(coffeeContainer.isEmpty(), "The returned coffee container should not be empty");
        Assertions.assertEquals(CoffeeType.ARABICA, coffeeContainer.getCoffeeType(), "The coffee type in the container should match the requested type");
    }

    @Test
    public void testIncrementNbCoffeeMadeAfterMakingCoffee() throws Exception {
        coffeeMachineUnderTest.plugToElectricalPlug();
        coffeeMachineUnderTest.addWaterInTank(2.0);
        coffeeMachineUnderTest.addCoffeeInBeanTank(1.0, CoffeeType.ARABICA);

        int initialNbCoffeeMade = coffeeMachineUnderTest.getNbCoffeeMade();
        Cup mockCup = Mockito.mock(Cup.class);
        Mockito.when(mockCup.isEmpty()).thenReturn(true);
        Mockito.when(mockCup.getCapacity()).thenReturn(0.15);

        coffeeMachineUnderTest.makeACoffee(mockCup, CoffeeType.ARABICA);
        Assertions.assertEquals(initialNbCoffeeMade + 1, coffeeMachineUnderTest.getNbCoffeeMade(), "Number of coffees made should be incremented by 1");
    }

    @Test
    public void testCoffeeMachineNotFailOnLowRandomValue() {
        Random mockRandom = Mockito.mock(Random.class);
        Mockito.when(mockRandom.nextGaussian()).thenReturn(0.5); // Less than 1
        coffeeMachineUnderTest.setRandomGenerator(mockRandom);

        coffeeMachineUnderTest.coffeeMachineFailure();

        Assertions.assertFalse(coffeeMachineUnderTest.isOutOfOrder(), "Machine should not be out of order on low random value");
    }

    @Test
    public void testToString() {
        String expectedString = "Your coffee machine has : \n" +
                "- water tank : " + coffeeMachineUnderTest.getWaterTank().toString() + "\n" +
                "- water pump : " + coffeeMachineUnderTest.getWaterPump().toString() + "\n" +
                "- electrical resistance : " + coffeeMachineUnderTest.getElectricalResistance().toString() + "\n" +
                "- is plugged : " + coffeeMachineUnderTest.isPlugged() + "\n" +
                "and made " + coffeeMachineUnderTest.getNbCoffeeMade() + " coffees";

        Assertions.assertEquals(expectedString, coffeeMachineUnderTest.toString(), "toString() should return the correct string representation of the object");
    }

    @Test
    public void testMaxMinValuesForTanks() {
        // Test with max values
        coffeeMachineUnderTest.addWaterInTank(coffeeMachineUnderTest.getWaterTank().getMaxVolume());
        coffeeMachineUnderTest.addCoffeeInBeanTank(coffeeMachineUnderTest.getBeanTank().getMaxVolume(), CoffeeType.ARABICA);
        Assertions.assertEquals(coffeeMachineUnderTest.getWaterTank().getMaxVolume(), coffeeMachineUnderTest.getWaterTank().getActualVolume());
        Assertions.assertEquals(coffeeMachineUnderTest.getBeanTank().getMaxVolume(), coffeeMachineUnderTest.getBeanTank().getActualVolume());

        // Test with min values
        coffeeMachineUnderTest.addWaterInTank(-coffeeMachineUnderTest.getWaterTank().getActualVolume());
        coffeeMachineUnderTest.addCoffeeInBeanTank(-coffeeMachineUnderTest.getBeanTank().getActualVolume(), CoffeeType.ARABICA);
        Assertions.assertEquals(0, coffeeMachineUnderTest.getWaterTank().getActualVolume());
        Assertions.assertEquals(0, coffeeMachineUnderTest.getBeanTank().getActualVolume());
    }

    @AfterEach
    public void afterTest(){

    }
}
