package ru.tokarev.ddbot.bot;

import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.tokarev.ddbot.keyboards.Keyboards;
import ru.tokarev.ddbot.util.Utils;

import java.time.Instant;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Bot extends TelegramLongPollingBot {

    @Value("${bot.username}")
    private String botUsername;
    @Value("${bot.token}")
    private String botToken;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Long userId;

        if (update.hasMessage()) {
            try {
                userId = update.getMessage().getFrom().getId();
                String text = update.getMessage().getText();

                if (text.matches("([0-9]+[чмс]\s*)+")) {
                    reminder(userId, String.format("Время прошло: %s", text), Utils.parseStringTimeToSec(text));
                    sendMsg(userId, "Таймер успешно заведен");
                } else if (!text.equals("/start")) {
                    sendMsg(userId, "Введен неверный формат времени");
                }

                sendMainMenu(userId);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }

        } else if (update.hasCallbackQuery()) {
            userId = update.getCallbackQuery().getFrom().getId();
            String data = update.getCallbackQuery().getData();

            AnswerCallbackQuery close = AnswerCallbackQuery.builder()
                    .callbackQueryId(update.getCallbackQuery().getId())
                    .build();

            switch (data) {
                case "1min" -> reminder(userId, "Прошла 1 минута", 60L);
                case "30sec" -> reminder(userId, "Прошло 30 секунд", 30L);
            }

            try {
                execute(close);
                sendMsg(userId, "Таймер успешно заведен");
                sendMainMenu(userId);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sendMsg(Long userId, String text) throws TelegramApiException {
        execute(SendMessage.builder()
                .chatId(userId)
                .text(text)
                .build());
    }

    public void reminder(Long userId, String text, Long deltaSeconds) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    sendMsg(userId, text);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }, Date.from(Instant.now().plusSeconds(deltaSeconds)));
    }

    public void sendMainMenu(Long userId) throws TelegramApiException {
        execute(
                SendMessage.builder()
                        .chatId(userId)
                        .text("Выберите время, либо введите самостоятельно, используя символы: с - секунды, " +
                                "м - минуты, ч - часы. Пример ввода: 2ч 35м 33с; 35c; 6м; 89с 66м.")
                        .replyMarkup(Keyboards.mainMenu)
                        .build()
        );
    }
}
