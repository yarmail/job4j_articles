package ru.job4j.articles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.articles.service.SimpleArticleService;
import ru.job4j.articles.service.generator.RandomArticleGenerator;
import ru.job4j.articles.store.ArticleStore;
import ru.job4j.articles.store.WordStore;
import java.io.InputStream;
import java.util.Properties;

/**
 * Задание
 * Запустить программу и убедиться, что она падает на ошибке OutOfMemoryException.
 * Исправить код программы таким образом, чтобы этой ошибки не возникало.
 * --------------
 * ВНИМАНИЕ
 * НЕ СОХРАНЯТЬ И НЕ КОМИТИТЬ ФАЙЛЫ DB - DIR
 * articles.lck
 * articles.log
 * articles.properties
 * ------------
 * Наблюдения и соображения.
 * 1. Ошибка происходит на генерации 198 тыс статей.
 * 2. Если TARGET_COUNT маленький (например 5000) - то заданние проходит
 * 3. В сервисе зачем-то статьи заносятся в список а потом в файл - вероятно это лишнее
 * Пробуем внести изменения в файл SimpleArticleService
 */
public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class.getSimpleName());

    public static final int TARGET_COUNT = 1_000_000;

    public static void main(String[] args) {
        var properties = loadProperties();
        var wordStore = new WordStore(properties);
        var articleStore = new ArticleStore(properties);
        var articleGenerator = new RandomArticleGenerator();
        var articleService = new SimpleArticleService(articleGenerator);
        articleService.generate(wordStore, TARGET_COUNT, articleStore);
    }

    private static Properties loadProperties() {
        LOGGER.info("Загрузка настроек приложения");
        var properties = new Properties();
        try (InputStream in = Application.class.getClassLoader().getResourceAsStream("application.properties")) {
            properties.load(in);
        } catch (Exception e) {
            LOGGER.error("Не удалось загрузить настройки. { }", e.getCause());
            throw new IllegalStateException();
        }
        return properties;
    }
}