package org.pizza.bot.strategy;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Strategy {

    SendMessage getResponse(Update update);
}
