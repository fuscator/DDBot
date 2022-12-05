package ru.tokarev.ddbot.keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class Keyboards {

    private static final InlineKeyboardButton oneMin = InlineKeyboardButton.builder()
            .text("1 мин")
            .callbackData("1min")
            .build();

    private static final InlineKeyboardButton thirtySec = InlineKeyboardButton.builder()
            .text("30 сек")
            .callbackData("30sec")
            .build();

    public static InlineKeyboardMarkup mainMenu = InlineKeyboardMarkup.builder()
            .keyboardRow(List.of(oneMin, thirtySec))
            .build();
}
