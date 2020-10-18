package se.coolaganget.friendzip.model;

import java.time.Duration;
import java.time.LocalDateTime;

public interface Calendar {
    boolean isAvaliableDuringPeriod(LocalDateTime startTime, Duration toSchedule);
}
