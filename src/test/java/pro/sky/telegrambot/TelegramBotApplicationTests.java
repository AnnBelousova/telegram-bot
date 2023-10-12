package pro.sky.telegrambot;

import org.junit.jupiter.api.Test;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import pro.sky.telegrambot.listener.TelegramBotUpdatesListener;

@WebMvcTest(TelegramBotUpdatesListener.class)
class TelegramBotApplicationTests {
	@Test
	void contextLoads() {
	}
}
