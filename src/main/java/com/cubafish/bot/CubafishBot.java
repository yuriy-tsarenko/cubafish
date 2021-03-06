package com.cubafish.bot;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Getter
@Setter
public class CubafishBot extends TelegramLongPollingBot {

    @Value("${bot.token}")
    private String botToken;

    @Value("${bot.username}")
    private String botUserName;

    private static final Logger log = Logger.getLogger(CubafishBot.class);

    private final BookingResolver bookingResolver;
    private Long chatId = 0L;
    private Long chatIdSuperAdmin = 0L;
    private Integer totalAmount = 0;
    private Integer itemsAmount = 0;
    private SendMessage uploadMessage = new SendMessage();
    private String customMessage;
    private String superAdminMessage;
    private String bookingMessage;
    private Long[] oldKeys = new Long[1];

    @Override
    public void onUpdateReceived(Update update) {
        Map<String, Object> responseFromGetChatId = getOneChatIdForAdmin(update);
        Runnable informer = () -> {
            List<Long> keys = bookingResolver.keyExtractor();
            Long id = (Long) responseFromGetChatId.get("chatId");
            String securityMessage = (String) responseFromGetChatId.get("customMessage");
            itemsAmount = totalAmount;
            totalAmount = keys.size();
            Long idSuperAdmin = (Long) responseFromGetChatId.get("chatIdSuperAdmin");
            if (totalAmount > itemsAmount) {
                itemsAmount = totalAmount - itemsAmount;

                if (keys.size() > 0) {
                    oldKeys = new Long[keys.size()];
                    for (int p = 0; p < keys.size(); p++) {
                        Long key = keys.get(p);
                        oldKeys[p] = key;
                    }
                }

            } else {
                itemsAmount = totalAmount;
                for (int y = 0; y < keys.size(); y++) {
                    for (int b = 0; b < oldKeys.length; b++) {
                        if (keys.get(y).equals(oldKeys[b])) {
                            itemsAmount--;
                        }
                    }
                }
                oldKeys = new Long[keys.size()];
                for (int u = 0; u < keys.size(); u++) {
                    oldKeys[u] = keys.get(u);
                }
            }

            for (int i = itemsAmount; i > 0; i--) {
                Map<String, Object> responseFromBookingResolverNextStep = bookingResolver.getMessageForAdmin(i);
                bookingMessage = (String) responseFromBookingResolverNextStep.get("bookingMessage");
                if ((bookingMessage != null) && (securityMessage.equals("admin"))) {
                    uploadMessage.setText(bookingMessage);
                    uploadMessage.setChatId(id);
                    try {
                        execute(uploadMessage);
                    } catch (TelegramApiException e) {
                        log.error(e);
                    }

                }
            }
            if ((idSuperAdmin != 0L) && (bookingMessage != null)) {
                if (!bookingMessage.equals(superAdminMessage)) {
                    uploadMessage.setText(bookingMessage);
                    uploadMessage.setChatId(idSuperAdmin);
                    superAdminMessage = bookingMessage;
                    try {
                        execute(uploadMessage);
                    } catch (TelegramApiException e) {
                        log.error(e);
                    }
                }

            }

        };
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleWithFixedDelay(informer, 0, 2, TimeUnit.MINUTES);
        String userName = update.getMessage().getFrom().getFirstName();
        String receivedMessage = update.getMessage().getText();
        if (receivedMessage.equalsIgnoreCase("/start")
                || receivedMessage.equalsIgnoreCase("/start@CUBAFISH_BOT")) {
            customMessage = "Привет!!! " + userName + " я CUBAFISH_BOT. Я твой персональный помощник" + "\n"
                    + "Буду тебе отправлять новые заказы";
        }

        if (customMessage != null) {
            Long id = (Long) responseFromGetChatId.get("chatId");
            String securityMessage = (String) responseFromGetChatId.get("customMessage");
            if (!securityMessage.equals("admin")) {
                uploadMessage.setText(securityMessage);
                uploadMessage.setChatId(update.getMessage().getChatId());
            } else {
                uploadMessage.setText(customMessage);
                uploadMessage.setChatId(id);
            }

            try {
                execute(uploadMessage);
            } catch (TelegramApiException e) {
                log.error(e);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public Map<String, Object> getOneChatIdForAdmin(Update update) {
        String message = "";
        if (chatId.equals(update.getMessage().getChatId())) {
            message = "admin";
        } else if (chatIdSuperAdmin.equals(update.getMessage().getChatId())) {
            message = "admin";

        } else if ((chatId == 0L) || (chatIdSuperAdmin == 0L)) {
            if (chatId != 0L) {
                chatIdSuperAdmin = update.getMessage().getChatId();
                log.info("admin was detected");
            }
            chatId = update.getMessage().getChatId();
            log.info("super admin was detected");
            message = "admin";

        } else if (!chatId.equals(update.getMessage().getChatId())) {
            String userName = update.getMessage().getFrom().getFirstName();
            message = "Привет!!! " + userName + " я CUBAFISH_BOT." + "\n" + " К сожалению у меня уже есть хозяин."
                    + "Может в далеком будущем я тебе помогу. " + "\n" + "До встречи!!!";
            log.warn("an attempt to obtain information was detected");
        }
        return Map.of("chatId", chatId, "chatIdSuperAdmin", chatIdSuperAdmin, "customMessage", message);
    }
}
