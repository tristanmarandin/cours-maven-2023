Feature: EspressoMachineFeature
  As a user, I want to make a coffee using an Espresso Coffee Machine

  Scenario: A user plugs the espresso coffee machine and makes an Arabica coffee
    Given an espresso coffee machine with 0.10 l of min capacity, 3.0 l of max capacity, 600.0 l per h of water flow for the pump
    And a "mug" with a capacity of 0.25
    When I plug the espresso machine to electricity
    And I add 1 liter of water in the water tank
    And I add 0.5 liter of "ARABICA" in the primary bean tank
    And I add 0.5 liter of "ROBUSTA" in the secondary bean tank
    And I make a coffee "ARABICA"
    Then the espresso machine returns a coffee mug not empty
    And a coffee volume equals to 0.25
    And a coffee "mug" containing a coffee type "ARABICA"
