package org.pizza.bot.strategy;

import org.pizza.bot.CommonMessages;
import org.pizza.bot.MongoDB;
import org.pizza.bot.OrderState;
import org.pizza.bot.PizzaStore;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CallbackStrategy implements Strategy{
    @Override
    public SendMessage getResponse(Update update) {
        String chatId = String.valueOf(update.getCallbackQuery().getMessage().getChatId());

        SendMessage response = new SendMessage();
        response.setChatId(chatId);
        response.setText(CommonMessages.UNKNOWN_RESPONSE);

        String callBackResponse = update.getCallbackQuery().getData();

        if(PizzaStore.PIZZA_TYPE_LIST.contains(callBackResponse)){
            MongoDB.updateField(MongoDB.PIZZA_TYPE,callBackResponse,chatId);

            //Setting message response
            response.setText("Select the pizza size you'd like to have");

            // First create the keyboard
            List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

            //Then we create the buttons' row
            List<InlineKeyboardButton> buttonsRow = new ArrayList<>();

            for(Map.Entry<String,Double> set:PizzaStore.PIZZA_SIZES_MAP.entrySet()){
                InlineKeyboardButton button = new InlineKeyboardButton();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(set.getKey()).append(": $").append(set.getValue());
                button.setText(stringBuilder.toString());
                button.setCallbackData(set.getKey());
                buttonsRow.add(button);

            }

            keyboard.add(buttonsRow);

            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            inlineKeyboardMarkup.setKeyboard(keyboard);

            response.setReplyMarkup(inlineKeyboardMarkup);
        }

        if(PizzaStore.PIZZA_SIZES_MAP.containsKey(callBackResponse)){
            MongoDB.updateField(MongoDB.PIZZA_SIZE,callBackResponse,chatId);

            response.setText("Please select the drink you'd like to have:");

            // First create the keyboard
            List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

            //Then we create the buttons' row
            List<InlineKeyboardButton> buttonsRow = new ArrayList<>();

            for(Map.Entry<String,Double> set:PizzaStore.DRINKS_MAP.entrySet()){
                InlineKeyboardButton button = new InlineKeyboardButton();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(set.getKey()).append(": $").append(set.getValue());
                button.setText(stringBuilder.toString());
                button.setCallbackData(set.getKey());
                buttonsRow.add(button);

            }

            keyboard.add(buttonsRow);

            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            inlineKeyboardMarkup.setKeyboard(keyboard);

            response.setReplyMarkup(inlineKeyboardMarkup);

        }

        if(PizzaStore.DRINKS_MAP.containsKey(callBackResponse)){
            MongoDB.updateField(MongoDB.DRINK,callBackResponse,chatId);
            MongoDB.updateField(MongoDB.ORDER_STATE, OrderState.ADDRESS.toString(),chatId);

            response.setText("Please type the address to which the delivery of the pizza will go to.");

        }

        return response;
    }
}
