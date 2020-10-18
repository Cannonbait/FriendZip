package se.coolaganget.friendzip.model;

import java.time.Duration;
import java.time.LocalDateTime;

public interface Slot {
    LocalDateTime getStartTime();

    Duration getDuration();

    LocalDateTime getEndTime();
}
