package se.coolaganget.friendzip.model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StupidCalendar implements Calendar{
    private List<Slot> unavaliable;

    public StupidCalendar() {
        unavaliable = new ArrayList<>();
    }
    public StupidCalendar(List<Slot> busyTimes) {
        unavaliable = busyTimes;
    }
    public void addNewBusyTime(Slot s) {
        unavaliable.add(s);
    }

    @Override
    public boolean isAvaliableDuringPeriod(LocalDateTime startTime, Duration duration) {
        for (Slot busy : unavaliable) {
            LocalDateTime startOfBusy = busy.getStartTime();
            Duration busyDuration = busy.getDuration();
            // Two cases are allowed in time: [startBusy, endBusy, Start, End]
            boolean wantedIsBeforeBusy = startTime.isBefore(startOfBusy) && (startTime.plus(duration).isBefore(startOfBusy));
            // Two cases are allowed in time: [Start, End, startBusy, endBusy]
            boolean wantedIsAfterBusy = startTime.isAfter(startOfBusy.plus(busyDuration));
            if (!(wantedIsAfterBusy || wantedIsBeforeBusy)) { // if both are false we have a missmatch
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        String header = "Calendar that is unavaliable at these slots:\n";
        String rest = "";
        for (Slot s: unavaliable) {
            rest += s.toString();
        }
        return header + rest;
    }
}
