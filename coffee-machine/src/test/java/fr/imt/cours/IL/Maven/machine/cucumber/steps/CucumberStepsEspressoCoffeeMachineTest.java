package fr.imt.cours.IL.Maven.machine.cucumber.steps;

import fr.imt.cours.IL.Maven.machine.EspressoCoffeeMachine;
import fr.imt.cours.IL.Maven.storage.cupboard.coffee.type.CoffeeType;
import fr.imt.cours.IL.Maven.storage.cupboard.container.CoffeeContainer;
import fr.imt.cours.IL.Maven.storage.cupboard.container.Cup;
import fr.imt.cours.IL.Maven.storage.cupboard.container.Mug;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

public class CucumberStepsEspressoCoffeeMachineTest {

    public EspressoCoffeeMachine espressoMachine;
    public Mug mug;
    public Cup cup;
    public CoffeeContainer containerWithCoffee;

    @Given("an espresso coffee machine with {double} l of min capacity, {double} l of max capacity, {double} l per h of water flow for the pump")
    public void givenAnEspressoCoffeeMachine(double minimalWaterCapacity, double maximalWaterCapacity, double pumpWaterFlow){
        espressoMachine = new EspressoCoffeeMachine(minimalWaterCapacity, maximalWaterCapacity, minimalWaterCapacity, maximalWaterCapacity, pumpWaterFlow);
    }

    // ... (keep other step definitions the same)

    @And("I add {double} liter of {string} in the primary bean tank")
    public void iAddLitersOfCoffeeBeansInPrimaryTank(double beanVolume, String coffeeType) {
        espressoMachine.addCoffeeInBeanTank(beanVolume, CoffeeType.valueOf(coffeeType));
    }

    @And("I add {double} liter of {string} in the secondary bean tank")
    public void iAddLitersOfCoffeeBeansInSecondaryTank(double beanVolume, String coffeeType) {
        espressoMachine.addCoffeeInSecondaryBeanTank(beanVolume, CoffeeType.valueOf(coffeeType)); // Assuming this method exists in EspressoCoffeeMachine
    }

    // ... (adapt the rest of the step definitions accordingly)

    @Then("the espresso machine should be correctly connected to the electrical network")
    public void theEspressoMachineShouldBeCorrectlyConnectedToTheElectricalNetwork() {
        Assertions.assertTrue(espressoMachine.isPlugged());
    }
}

