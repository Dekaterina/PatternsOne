package ru.netology.patterns.data;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import java.time.Duration;

public class CardDeliveryTest {

    @BeforeEach
    public void setup() {
        Selenide.open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully plan and replan meeting")
    public void shouldSuccessfullyPlanAndReplanMeeting() {
        ru.netology.patterns.data.DataGenerator DataGenerator = null;
        var validUser = ru.netology.patterns.data.DataGenerator.Registration.generateUser("ru");
        int daysToAddForFirstMeeting = 4;
        String firstMeetingDate = ru.netology.patterns.data.DataGenerator.generateDate(daysToAddForFirstMeeting);
        int daysToAddForSecondMeeting = 7;
        String secondMeetingDate = ru.netology.patterns.data.DataGenerator.generateDate(daysToAddForSecondMeeting);

        Selenide.$("[data-test-id=city] input").setValue(validUser.getCity());
        Selenide.$("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        Selenide.$("[data-test-id=date] input").setValue(firstMeetingDate);
        Selenide.$("[data-test-id=name] input").setValue(validUser.getName());
        Selenide.$("[data-test-id=phone] input").setValue(validUser.getPhone());
        Selenide.$("[data-test-id=agreement]").click();
        Selenide.$(Selectors.byText("Запланировать")).click();
        Selenide.$(Selectors.byText("Успешно!")).shouldBe(Condition.visible, Duration.ofSeconds(15));
        Selenide.$("[data-test-id='success-notification'] .notification__content")
                .shouldHave(Condition.exactText("Встреча успешно запланирована на " + firstMeetingDate))
                .shouldBe(Condition.visible);
        Selenide.$("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        Selenide.$("[data-test-id=date] input").setValue(secondMeetingDate);
        Selenide.$(Selectors.byText("Запланировать")).click();
        Selenide.$("[data-test-id='replan-notification'] .notification__content")
                .shouldHave(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?"))
                .shouldBe(Condition.visible);
        Selenide.$("[data-test-id='replan-notification'] button").click();
        Selenide.$("[data-test-id='success-notification'] .notification__content")
                .shouldHave(Condition.exactText("Встреча успешно запланирована на " + secondMeetingDate))
                .shouldBe(Condition.visible);
    }
}