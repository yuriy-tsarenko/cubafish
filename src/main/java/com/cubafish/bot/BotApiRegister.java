package com.cubafish.bot;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

@Service
public class BotApiRegister extends TelegramBotsApi {

    private static final Logger log = Logger.getLogger(BotApiRegister.class);

    @Autowired
    public BotApiRegister(CubafishBot bot) {
        try {
            registerBot(bot);
        } catch (TelegramApiRequestException e) {
            log.error(e);
        }
    }
}
