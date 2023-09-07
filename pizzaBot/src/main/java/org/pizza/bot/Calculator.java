package org.pizza.bot;

import org.bson.Document;

public class Calculator {

    public static double getFinalPrice(String chatId) {
        double result = 0;

        Document customerDetails = MongoDB.getCustomerAttributes(chatId);

        String pizzaSize = customerDetails.getString(MongoDB.PIZZA_SIZE);
        String drink = customerDetails.getString(MongoDB.DRINK);

        result += PizzaStore.PIZZA_SIZES_MAP.get(pizzaSize);
        result += PizzaStore.DRINKS_MAP.get(drink);

        return result;

    }
}
