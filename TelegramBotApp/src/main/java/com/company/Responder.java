package com.company;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Responder extends TelegramLongPollingBot {

    @Override
    public String getBotToken() {
        return Bot.BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {

        try {

            String response = "I'm sorry, but I haven't understood your message.";

            String chatId = "";

            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(response);

            if (update.hasCallbackQuery() && update.getCallbackQuery().getData() != null && !update.getCallbackQuery().getData().isEmpty()) {
                chatId = String.valueOf(update.getCallbackQuery().getMessage().getChatId());

                String callBackData = update.getCallbackQuery().getData();

                if (callBackData.equalsIgnoreCase(CallBackData.CD_YES.toString())) {
                    LocalDateTime currentTime = LocalDateTime.now();
                    sendMessage.setText(currentTime.toLocalTime().toString());
                }

                if (callBackData.equalsIgnoreCase(CallBackData.CD_NO.toString())) {
                    sendMessage.setText("Fine, thanks.");
                }
            }

            if (update.hasMessage()) {
                chatId = String.valueOf(update.getMessage().getChatId());

                boolean userExists = MongoDB.userExists(chatId);

                MongoDB.insertNewUserId(chatId);

                if (update.getMessage().hasText()) {
                    String userMessage = update.getMessage().getText().trim();

                    if (userMessage.equalsIgnoreCase("Hello")) {
                        if (userExists) {
                            sendMessage.setText("Hello again! How are you? \uD83D\uDE00");
                        } else {
                            sendMessage.setText("How are you? \uD83D\uDE00");
                        }
                    }

                    if (userMessage.equalsIgnoreCase("How are you")) {
                        sendMessage.setText("I'm fine thank you!");
                    }

                    if (userMessage.contains("time")) {

                        sendMessage.setText("Would you like to know the current time?");

                        // First create the keyboard
                        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

                        //Then we create the buttons' row
                        List<InlineKeyboardButton> buttonsRow = new ArrayList<>();

                        //Create yes button
                        InlineKeyboardButton yesButton = new InlineKeyboardButton();
                        yesButton.setText("\uD83D\uDC4D");
                        yesButton.setCallbackData(CallBackData.CD_YES.toString());

                        InlineKeyboardButton noThanksButton = new InlineKeyboardButton();
                        noThanksButton.setText("No thanks");
                        noThanksButton.setCallbackData(CallBackData.CD_NO.toString());

                        //We add the yes button to the buttons row
                        buttonsRow.add(yesButton);
                        buttonsRow.add(noThanksButton);

                        //We add the newly created buttons row that contains the yes button to the keyboard
                        keyboard.add(buttonsRow);

                        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                        inlineKeyboardMarkup.setKeyboard(keyboard);

                        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

                    }

                    if (userMessage.equalsIgnoreCase("/day")) {
                        DayOfWeek todayDayOfTheWeek = LocalDateTime.now().getDayOfWeek();
                        sendMessage.setText(todayDayOfTheWeek.toString());
                    }

                    if (userMessage.contains("contact")) {
                        sendMessage.setText("Can you share your Telegram's phone number with me so our customer service representatives can contact you?");

                        //1- Create KeyboardRow
                        KeyboardRow keyboardRow = new KeyboardRow();

                        //2- Create KeyboardButton
                        KeyboardButton keyboardButton = new KeyboardButton();
                        keyboardButton.setText("Yes, share contact");
                        keyboardButton.setRequestContact(true);

                        //3- Add the keyboardButton to the KeyboardRow
                        keyboardRow.add(keyboardButton);

                        //4- Create list of KeyboardRows
                        List<KeyboardRow> listOfKeyboardRows = new ArrayList<>();

                        //5- Add keyboardRow to the listOfKeyboardRows
                        listOfKeyboardRows.add(keyboardRow);

                        //6- Create Reply KeyboardMarkup and set parameters
                        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                        replyKeyboardMarkup.setKeyboard(listOfKeyboardRows);
                        replyKeyboardMarkup.setOneTimeKeyboard(true);
                        replyKeyboardMarkup.setResizeKeyboard(true);

                        sendMessage.setReplyMarkup(replyKeyboardMarkup);
                    }
                }

                if (update.getMessage().hasContact()) {
                    sendMessage.setText("Thank you for sending us your phone number. We will contact you shortly!");

                    String phoneNumber = update.getMessage().getContact().getPhoneNumber().trim();
                    //You can now encrypt the phone number and store it.

                }
            }


            if (chatId.isEmpty()) {
                throw new IllegalStateException("The chat id couldn't be identified or found.");
            }

            sendMessage.setChatId(chatId);

            sendApiMethod(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        } catch (Exception exception) {
            //Execute logic to handle generic exceptions
            exception.printStackTrace();
        } finally {
            // Here you can make sure to perform any cleanup necessary before the next update is received and processed.
        }
    }

    @Override
    public String getBotUsername() {
        return Bot.USERNAME;
    }
}
