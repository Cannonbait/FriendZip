package se.coolaganget.friendzip.model;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class Scheduler {
    Duration FIFTHEEN_MINUTES = Duration.ofMinutes(15);

    public List<Slot> schedule(List<Calendar> schedulesToSync,
                               LocalDateTime startOfSchedulingPeriod,
                               LocalDateTime endOfSchedulingPeriod,
                               Duration meetingTimeLength) {
        List<Slot> avaliableSlots = new ArrayList<>();
        LocalDateTime latestPossibleStart = endOfSchedulingPeriod.minus(meetingTimeLength);

        LocalDateTime startTime = startOfSchedulingPeriod;
        while(startTime.isBefore(latestPossibleStart) || startTime.equals(latestPossibleStart)) {
            final LocalDateTime finalStartTime = startTime;
            if (schedulesToSync.stream().allMatch(c -> c.isAvaliableDuringPeriod(finalStartTime, meetingTimeLength))) {
                Slot match = new SlotImpl(startTime, meetingTimeLength);
                avaliableSlots.add(match);
            }
            startTime = startTime.plus(FIFTHEEN_MINUTES);
        }
        return avaliableSlots;
    }
}
