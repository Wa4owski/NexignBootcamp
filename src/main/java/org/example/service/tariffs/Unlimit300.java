package org.example.service.tariffs;

import org.example.model.Call;
import org.example.model.CallReport;
import org.example.model.CallType;
import org.example.service.RateStrategy;


public class Unlimit300 extends AbstractUnlimited implements RateStrategy {

    private int freeMinutesLeft = 300;
    private final double minutePrice = 1d;

    public Unlimit300() {
        this.tariffPrice = 100;
    }

    public double getTariffPrice(){
        return tariffPrice;
    }

    @Override
    public CallReport resolveCall(Call call) {
        CallReport report = new CallReport(call.getCallType(), call.getStarted(), call.getEnded());
        long durationMinutes = report.calcAndSetDuration();
        if(report.getCallType().equals(CallType.INCOMING)){
            report.setCost(0d);
            return report;
        }
        if(freeMinutesLeft >= durationMinutes){
            freeMinutesLeft -= durationMinutes;
            report.setCost(0d);
            return report;
        }
        report.setCost((durationMinutes - freeMinutesLeft) * minutePrice);
        freeMinutesLeft = 0;
        return report;
    }
}
