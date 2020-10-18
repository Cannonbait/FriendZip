package se.coolaganget.friendzip.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class SlotImpl implements Slot {
    private LocalDateTime startTime;
    private Duration duration;
    public SlotImpl(LocalDateTime startTime, Duration duration) {
        this.startTime = startTime;
        this.duration = duration;
    }

    @Override
    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return "Slot:\n" + startTime.toString() + "\n" + "Duration:\n" + duration.toString() + "\n";
    }
}
