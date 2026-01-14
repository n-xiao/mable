package code.backend.data;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CountdownTests {
    @Test
    void testDaysUntilDue() {
        final var LOCAL_DATE_NOW = LocalDate.now();
        var cdNormal = new Countdown("normal", LOCAL_DATE_NOW.plusYears(1));
        var cdNext = new Countdown("nextDay", LOCAL_DATE_NOW.plusDays(1));
        var cdPrev = new Countdown("prevDay", LOCAL_DATE_NOW.minusDays(1));
        var cdNow = new Countdown("today", LOCAL_DATE_NOW);

        assertTrue(cdNormal.daysUntilDue(LOCAL_DATE_NOW) == 365);
        assertTrue(cdNext.daysUntilDue(LOCAL_DATE_NOW) + cdPrev.daysUntilDue(LOCAL_DATE_NOW) == 0);
        assertTrue(cdNow.isDueToday(LOCAL_DATE_NOW) && cdNow.daysUntilDue(LOCAL_DATE_NOW) == 0);
    }
}
