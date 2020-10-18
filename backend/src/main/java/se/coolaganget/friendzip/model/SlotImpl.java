package se.coolaganget.friendzip.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class SlotImpl implements Slot {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    public SlotImpl(LocalDateTime startTime, Duration duration) {
        this.startTime = startTime;
        this.endTime = startTime.plus(duration);
    }

    @Override
    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public Duration getDuration() {
        return Duration.between(startTime, endTime);
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return "SlotImpl{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
