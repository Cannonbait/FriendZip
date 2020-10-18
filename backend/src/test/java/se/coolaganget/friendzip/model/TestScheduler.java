package se.coolaganget.friendzip.model;

import org.junit.jupiter.api.Test;

import javax.swing.plaf.synth.SynthToolTipUI;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestScheduler {
    @Test
    public void testCreateScheduler() {
        LocalDateTime startTime = LocalDateTime.of(2020, 10, 20, 18, 30);
        Duration ONEHOUR = Duration.ofHours(1);
        Slot slot1 = new SlotImpl(startTime, ONEHOUR);

        StupidCalendar stinas = new StupidCalendar();
        stinas.addNewBusyTime(new SlotImpl(LocalDateTime.of(2020, 10, 19, 12, 0), Duration.ofHours(2)));
        stinas.addNewBusyTime(new SlotImpl(LocalDateTime.of(2020, 10, 19, 17, 0), Duration.ofHours(1)));

        StupidCalendar eriks = new StupidCalendar();
        eriks.addNewBusyTime(new SlotImpl(LocalDateTime.of(2020, 10, 19, 11, 0), Duration.ofHours(2)));
        eriks.addNewBusyTime(new SlotImpl(LocalDateTime.of(2020, 10, 19, 16, 0), Duration.ofHours(1)));

        List<Calendar> cals = new ArrayList<>();
        cals.add(stinas);
        cals.add(eriks);

        List<Rule> rules = new ArrayList<>();
        rules.add(Rule.NightRule());
        Scheduler s = new Scheduler(rules);
        List<Slot> matches =  s.schedule(cals, LocalDateTime.of(2020, 10, 19, 9, 0),
                LocalDateTime.of(2020, 10, 20, 11, 0),
                Duration.ofHours(4));


        System.out.println(matches);
    }
}
