package fr.imt.cours.IL.Maven.machine.component;

import fr.imt.cours.IL.Maven.storage.cupboard.coffee.type.CoffeeType;

public class BeanTank extends Tank{

    private CoffeeType beanCoffeeType;
    /**
     * Réservoir de graines de café de la cafetière
     *
     * @param initialVolume Volume de graines à mettre dans le réservoir à sa création
     * @param minVolume     Volume de graines minimal du réservoir
     * @param maxVolume     Volume de graines maximal du réservoir
     * @param beanCoffeeType Type de café dans le réservoir
     */
    public BeanTank(double initialVolume, double minVolume, double maxVolume, CoffeeType beanCoffeeType) {
        super(initialVolume, minVolume, maxVolume);
        this.beanCoffeeType = beanCoffeeType;
    }

    public void increaseCoffeeVolumeInTank(double coffeeVolume, CoffeeType coffeeType){
        this.increaseVolumeInTank(coffeeVolume);
        this.beanCoffeeType = coffeeType;
    }
    public CoffeeType getBeanCoffeeType() {
        return beanCoffeeType;
    }


    public void setBeanCoffeeType(CoffeeType beanCoffeeType) {
        this.beanCoffeeType = beanCoffeeType;
    }
}
