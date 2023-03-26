package org.example.model;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
public class CallReport extends Call{

    private LocalTime duration;
    private double cost;

    public CallReport(CallType callType, LocalDateTime started, LocalDateTime ended) {
        super(callType, started, ended);
    }

    public long calcAndSetDuration(){
        long secondsDuration = Duration.between(ended, started).getSeconds();
        this.duration = LocalTime.ofSecondOfDay(secondsDuration);
        return (secondsDuration % 60 == 0 ? secondsDuration / 60 : secondsDuration / 60 + 1);
    }



}
