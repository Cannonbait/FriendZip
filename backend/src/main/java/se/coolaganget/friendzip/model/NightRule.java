package se.coolaganget.friendzip.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class NightRule implements Rule {
    @Override
    public boolean allowsTime(LocalDateTime start, Duration meetingTimeLength) {
        boolean notAllowedStartTime = start.getHour() > 22 || start.getHour() < 7;
        LocalDateTime endTime = start.plus(meetingTimeLength);
        boolean allowedEndTime = endTime.getHour() > 7 || endTime.getHour() < 2;
        return !notAllowedStartTime && allowedEndTime;
    }
}
