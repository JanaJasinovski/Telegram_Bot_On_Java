package org.pizza.bot;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PizzaStore {

    public final static List<String> PIZZA_TYPE_LIST = new ArrayList<>(Arrays.asList("Chicken", "Hawaiian", "Pepperoni"));
    public final static Map<String, Double> PIZZA_SIZES_MAP = new HashMap<>();
    public final static Map<String, Double> DRINKS_MAP = new HashMap<>();

    static {
        PIZZA_SIZES_MAP.put("Small", 10.00);
        PIZZA_SIZES_MAP.put("Medium", 15.00);
        PIZZA_SIZES_MAP.put("Large", 20.00);

        DRINKS_MAP.put(Drink.Water.toString(), 1.00);
        DRINKS_MAP.put(Drink.Soda.toString(), 5.00);
        DRINKS_MAP.put(Drink.Coffee.toString(), 2.00);
    }
}
