package se.coolaganget.friendzip.model;

import java.time.Duration;
import java.time.LocalDateTime;

public interface Rule {
    boolean allowsTime(LocalDateTime finalStartTime, Duration meetingTimeLength);

    static Rule NightRule() {
        return new NightRule();
    }
}
