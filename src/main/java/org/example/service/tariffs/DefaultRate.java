package org.example.service.tariffs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.model.Call;
import org.example.model.CallReport;
import org.example.model.CallType;
import org.example.service.RateStrategy;

@NoArgsConstructor
public class DefaultRate implements RateStrategy {
    private int cheapMinutesLeft = 100;
    private final double cheapMinutePrice = 0.5;
    private final double expensiveMinutePrice = 1.5;

    @Override
    public CallReport resolveCall(Call call) {
        CallReport report = new CallReport(call.getCallType(), call.getStarted(), call.getEnded());
        long durationMinutes = report.calcAndSetDuration();
        if(report.getCallType().equals(CallType.INCOMING)){
            report.setCost(0d);
            return report;
        }
        if(cheapMinutesLeft >= durationMinutes){
            cheapMinutesLeft -= durationMinutes;
            report.setCost(durationMinutes * cheapMinutePrice);
            return report;
        }
        report.setCost(cheapMinutesLeft * cheapMinutePrice + (durationMinutes - cheapMinutesLeft) * expensiveMinutePrice);
        cheapMinutesLeft = 0;
        return report;
    }
}
