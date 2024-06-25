package ru.yaga;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TriangleDrawingBot extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return "NumerologyBot";
    }

    @Override
    public String getBotToken() {
        return "5656643323:AAE8bEZ5kI1wfSf-dNAdHqqjU_2WQXFIjhM";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            // Пример обработки команды для рисования треугольника
            if (messageText.startsWith("/drawtriangle")) {
                String[] parts = messageText.split(" ");
                if (parts.length == 4) {
                    try {
                        int day = Integer.parseInt(parts[1]);
                        int month = Integer.parseInt(parts[2]);
                        int year = Integer.parseInt(parts[3]);

                        BufferedImage image = drawEsotericImage(day, month, year);
                        sendImage(chatId, image);
                    } catch (NumberFormatException | IOException | TelegramApiException e) {
                        e.printStackTrace();
                        sendMessage(chatId, "Произошла ошибка при обработке вашей команды. Убедитесь, что вы ввели правильный формат даты.");
                    }
                } else {
                    sendMessage(chatId, "Неверный формат команды. Используйте /drawtriangle day month year");
                }
            }
        }
    }

    private int applyMinus22Rule(int number) {
        while (number > 22) {
            number -= 22;
        }
        return number;
    }

    private int sumOfDigits(int number) {
        int sum = 0;
        while (number != 0) {
            sum += number % 10;
            number /= 10;
        }
        return applyMinus22Rule(sum);
    }

    private int calculateSoulKey(int value1, int value2) {
        int sum = value1 + value2;
        return applyMinus22Rule(sum);
    }

    private int calculateMask(int value1, int value2, int value3) {
        int sum = value1 + value2 + value3;
        return applyMinus22Rule(sum);
    }

    private int calculateShadow(int value1, int value2, int value3) {
        int sum = value1 + value2 + value3;
        return applyMinus22Rule(sum);
    }

    // Метод для расчета типажа с учетом правила вычитания 22
    public int calculateTypage(int shadow1, int shadow2, int shadow3) {
        int sum = calculateShadowsSum(shadow1, shadow2, shadow3);
        return applyMinus22Rule(sum);
    }

    // Метод для вычисления суммы теней
    private int calculateShadowsSum(int shadow1, int shadow2, int shadow3) {
        return shadow1 + shadow2 + shadow3;
    }

    private BufferedImage drawEsotericImage(int day, int month, int year) throws IOException {
        int width = 1000;
        int height = 1000;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // Рисуем фон
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        // Настройки пера
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));

        // Расчет значений вершин
        int alterEgo = applyMinus22Rule(day);  // Альтер-Эго: применяем закон минус 22 к дню рождения
        int destinyKey = month;  // Ключ реализации Предназначения: месяц рождения
        int talentKey = sumOfDigits(year);  // Ключ реализации Таланта: сумма цифр года рождения с применением закона минус 22

        // Расчет значений для центров
        int centerPersonality = calculateSoulKey(alterEgo, talentKey);
        int centerDestiny = calculateSoulKey(alterEgo, destinyKey);
        int centerFamilyPrograms = calculateSoulKey(talentKey, destinyKey);

        // Расчет значений для масок
        int maskLoveScenario = calculateSoulKey(centerPersonality, alterEgo);
        int maskTalentRealization = calculateSoulKey(centerPersonality, talentKey);
        int maskKarmicTask = calculateSoulKey(alterEgo, centerDestiny);
        int maskHealingLoveScenario = calculateSoulKey(centerDestiny, centerFamilyPrograms);
        int maskKarmicDestiny = calculateSoulKey(centerDestiny, destinyKey);
        int maskFinancialHealing = calculateSoulKey(centerFamilyPrograms, destinyKey);
        int maskHeartLine = calculateSoulKey(centerFamilyPrograms, centerPersonality);
        int maskLoveTransmission = calculateSoulKey(centerPersonality, centerDestiny);
        int maskScenarioTransmission = calculateSoulKey(talentKey, centerFamilyPrograms);

        // Новые расчеты для теней и типажа
        int shadow1 = calculateShadow(maskLoveScenario, maskKarmicTask, maskLoveTransmission);
        int shadow2 = calculateShadow(maskTalentRealization, maskHeartLine, maskScenarioTransmission);
        int shadow3 = calculateShadow(maskHealingLoveScenario, maskKarmicDestiny, maskFinancialHealing);
        int typage = calculateTypage(shadow1, shadow2, shadow3);

        // Логика рисования треугольников и других элементов
        drawTrianglesAndElements(g2d, alterEgo, destinyKey, talentKey, centerPersonality, centerDestiny, centerFamilyPrograms,
                maskLoveScenario, maskTalentRealization, maskKarmicTask, maskHealingLoveScenario, maskKarmicDestiny, maskFinancialHealing, maskHeartLine, maskLoveTransmission, maskScenarioTransmission,
                shadow1, shadow2, shadow3, typage);

        // Добавление маленьких треугольников и подписей
        drawSmallTrianglesAndText(g2d, shadow1, shadow2, shadow3, typage);

        g2d.dispose();
        return image;
    }

    private void drawTrianglesAndElements(Graphics2D g2d, int alterEgo, int destinyKey, int talentKey, int centerPersonality, int centerDestiny, int centerFamilyPrograms,
                                          int maskLoveScenario, int maskTalentRealization, int maskKarmicTask, int maskHealingLoveScenario, int maskKarmicDestiny, int maskFinancialHealing, int maskHeartLine, int maskLoveTransmission, int maskScenarioTransmission,
                                          int shadow1, int shadow2, int shadow3, int typage) {
        // Координаты точек треугольника
        int[] xPoints = {400, 100, 700};
        int[] yPoints = {100, 700, 700};

        // Рисуем большой внешний треугольник
        drawTriangle(g2d, xPoints[0], yPoints[0], xPoints[1], yPoints[1], xPoints[2], yPoints[2], Color.GRAY, 2);

        // Внутренние треугольники
        drawTriangle(g2d, 400, 100, 250, 400, 550, 400, new Color(255, 0, 255), 2); // Верхний треугольник (ярко-розовый)
        drawTriangle(g2d, 250, 400, 100, 700, 400, 700, new Color(0, 255, 0), 2); // Левый нижний треугольник (ярко-зеленый)
        drawTriangle(g2d, 550, 400, 400, 700, 700, 700, new Color(128, 0, 128), 2); // Правый нижний треугольник (ярко-фиолетовый)
        drawTriangle(g2d, 400, 700, 250, 400, 550, 400, Color.WHITE, 2); // Центральный треугольник (белый)

        // Добавление текста и номеров
        drawText(g2d, Integer.toString(alterEgo), 385, 85, new Font("Arial", Font.BOLD, 20), Color.BLACK);  // Верхняя точка
        drawText(g2d, Integer.toString(destinyKey), 705, 700, new Font("Arial", Font.BOLD, 20), Color.BLACK); // Правая нижняя точка
        drawText(g2d, Integer.toString(talentKey), 70, 700, new Font("Arial", Font.BOLD, 20), Color.BLACK); // Левая нижняя точка

        // Добавление значений центров
        drawText(g2d, Integer.toString(centerPersonality), 230, 400, new Font("Arial", Font.BOLD, 20), Color.BLACK); // Центр Личности
        drawText(g2d, Integer.toString(centerDestiny), 560, 400, new Font("Arial", Font.BOLD, 20), Color.BLACK); // Центр Предназначения
        drawText(g2d, Integer.toString(centerFamilyPrograms), 390, 720, new Font("Arial", Font.BOLD, 20), Color.BLACK); // Центр Родовых Программ

        // Добавление значений масок
        drawText(g2d, Integer.toString(maskLoveScenario), 300, 240, new Font("Arial", Font.BOLD, 20), Color.BLACK); // Маска Любовного сценария
        drawText(g2d, Integer.toString(maskTalentRealization), 150, 540, new Font("Arial", Font.BOLD, 20), Color.BLACK); // Маска реализации Таланта
        drawText(g2d, Integer.toString(maskKarmicTask), 480, 240, new Font("Arial", Font.BOLD, 20), Color.BLACK); // Маска Кармической задачи
        drawText(g2d, Integer.toString(maskHealingLoveScenario), 450, 540, new Font("Arial", Font.BOLD, 20), Color.BLACK); // Маска Исцеления любовного сценария
        drawText(g2d, Integer.toString(maskKarmicDestiny), 640, 540, new Font("Arial", Font.BOLD, 20), Color.BLACK); // Маска Кармического Предназначения
        drawText(g2d, Integer.toString(maskFinancialHealing), 540, 680, new Font("Arial", Font.BOLD, 20), Color.BLACK); // Маска Исцеления денежного сценария
        drawText(g2d, Integer.toString(maskHeartLine), 300, 540, new Font("Arial", Font.BOLD, 20), Color.BLACK); // Маска линии Сердца
        drawText(g2d, Integer.toString(maskLoveTransmission), 390, 380, new Font("Arial", Font.BOLD, 20), Color.BLACK); // Маска передачи любви
        drawText(g2d, Integer.toString(maskScenarioTransmission), 240, 680, new Font("Arial", Font.BOLD, 20), Color.BLACK); // Маска передачи сценария

        // Подписи
        drawText(g2d, "Альтер-Эго", 370, 65, new Font("Arial", Font.PLAIN, 12), Color.BLACK);
        drawText(g2d, "Ключ реализации", 690, 720, new Font("Arial", Font.PLAIN, 12), Color.BLACK);
        drawText(g2d, "Предназначения", 690, 735, new Font("Arial", Font.PLAIN, 12), Color.BLACK);
        drawText(g2d, "Ключ реализации Таланта", 70, 720, new Font("Arial", Font.PLAIN, 12), Color.BLACK);

        // Подписи для центров
        drawText(g2d, "Центр Личности", 140, 410, new Font("Arial", Font.PLAIN, 12), Color.BLACK);
        drawText(g2d, "Центр Предназначения", 560, 415, new Font("Arial", Font.PLAIN, 12), Color.BLACK);
        drawText(g2d, "Центр Родовых Программ", 390, 735, new Font("Arial", Font.PLAIN, 12), Color.BLACK);

        // Подписи для масок
        drawText(g2d, "Маска Любовного сценария", 150, 255, new Font("Arial", Font.PLAIN, 12), Color.BLACK);
        drawText(g2d, "Маска реализации Таланта", 40, 520, new Font("Arial", Font.PLAIN, 12), Color.BLACK);
        drawText(g2d, "Маска Кармической задачи", 490, 255, new Font("Arial", Font.PLAIN, 12), Color.BLACK);
        drawText(g2d, "Маска Исцеления любовного сценария", 380, 560, new Font("Arial", Font.PLAIN, 12), Color.BLACK);
        drawText(g2d, "Маска Кармического Предназначения", 630, 560, new Font("Arial", Font.PLAIN, 12), Color.BLACK);
        drawText(g2d, "Маска Исцеления денежного сценария", 250, 520, new Font("Arial", Font.PLAIN, 12), Color.BLACK);
        drawText(g2d, "Маска линии Сердца", 500, 695, new Font("Arial", Font.PLAIN, 12), Color.BLACK);
        drawText(g2d, "Маска передачи любви", 350, 393, new Font("Arial", Font.PLAIN, 12), Color.BLACK);
        drawText(g2d, "Маска передачи сценария", 190, 695, new Font("Arial", Font.PLAIN, 12), Color.BLACK);

        // Добавление значений теней
        drawText(g2d, "1-я Тень: " + shadow1, 50, 50, new Font("Arial", Font.BOLD, 20), Color.RED);
        drawText(g2d, "2-я Тень: " + shadow2, 50, 80, new Font("Arial", Font.BOLD, 20), Color.RED);
        drawText(g2d, "3-я Тень: " + shadow3, 50, 110, new Font("Arial", Font.BOLD, 20), Color.RED);

        // Добавление значения типажа
        drawText(g2d, "Типаж: " + typage, 50, 140, new Font("Arial", Font.BOLD, 20), Color.RED);
    }

    private void drawSmallTrianglesAndText(Graphics2D g2d, int shadow1, int shadow2, int shadow3, int typage) {
        // Позиции и размеры маленьких треугольников
        int triangleSize = 20;
        int startX = 50;
        int startY = 750;

        // Рисуем маленькие треугольники и добавляем текст рядом
        drawSmallTriangle(g2d, startX, startY, triangleSize, new Color(255, 0, 255)); // Розовый треугольник
        drawText(g2d, "Любовь/Отношения/Коммуникации", startX + 30, startY + 15, new Font("Arial", Font.PLAIN, 14), Color.BLACK);
        drawText(g2d, "1-я Тень - " + shadow1, startX + 30, startY + 30, new Font("Arial", Font.PLAIN, 14), Color.BLACK);

        startY += 60;
        drawSmallTriangle(g2d, startX, startY, triangleSize, new Color(0, 255, 0)); // Зеленый треугольник
        drawText(g2d, "Деньги/Карьера", startX + 30, startY + 15, new Font("Arial", Font.PLAIN, 14), Color.BLACK);
        drawText(g2d, "2-я Тень - " + shadow2, startX + 30, startY + 30, new Font("Arial", Font.PLAIN, 14), Color.BLACK);

        startY += 60;
        drawSmallTriangle(g2d, startX, startY, triangleSize, new Color(128, 0, 128)); // Фиолетовый треугольник
        drawText(g2d, "Предназначение/Кармическая задача", startX + 30, startY + 15, new Font("Arial", Font.PLAIN, 14), Color.BLACK);
        drawText(g2d, "3-я Тень - " + shadow3, startX + 30, startY + 30, new Font("Arial", Font.PLAIN, 14), Color.BLACK);

        startY += 60;
        drawSmallTriangle(g2d, startX + 300, startY - 180, triangleSize, Color.WHITE); // Белый треугольник правее и выше
        drawText(g2d, "Типаж - " + typage, startX + 320, startY - 165, new Font("Arial", Font.PLAIN, 14), Color.BLACK); // Текст сдвинут вместе с треугольником
    }

    private void drawSmallTriangle(Graphics2D g2d, int x, int y, int size, Color color) {
        g2d.setColor(color);
        int[] xPoints = {x, x + size / 2, x - size / 2};
        int[] yPoints = {y, y + size, y + size};
        g2d.fillPolygon(xPoints, yPoints, 3);
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawPolygon(xPoints, yPoints, 3);
    }

    private void drawTriangle(Graphics2D g2d, int x1, int y1, int x2, int y2, int x3, int y3, Color color, int strokeWidth) {
        g2d.setColor(color);
        int[] xPoints = {x1, x2, x3};
        int[] yPoints = {y1, y2, y3};
        g2d.fillPolygon(xPoints, yPoints, 3);
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(strokeWidth));
        g2d.drawPolygon(xPoints, yPoints, 3);
    }

    private void drawText(Graphics2D g2d, String text, int x, int y, Font font, Color color) {
        g2d.setColor(color);
        g2d.setFont(font);
        g2d.drawString(text, x, y);
    }

    private void sendImage(long chatId, BufferedImage image) throws IOException, TelegramApiException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        byte[] imageBytes = baos.toByteArray();

        try (ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes)) {
            InputFile inputFile = new InputFile(bais, "esoteric_calculation.jpg");
            SendPhoto msg = new SendPhoto();
            msg.setChatId(String.valueOf(chatId));
            msg.setPhoto(inputFile);
            execute(msg);
        }
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new TriangleDrawingBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}











