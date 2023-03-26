package org.example.service;

import org.example.model.Call;
import org.example.model.CallReport;
import org.example.model.CallType;

public class ByMinuteRate implements RateStrategy{
    private final double minutePrice = 1.5;

    @Override
    public CallReport resolveCall(Call call) {
        CallReport report = new CallReport(call.getCallType(), call.getStarted(), call.getEnded());
        long durationMinutes = report.calcAndSetDuration();
        if(report.getCallType().equals(CallType.INCOMING)){
            report.setCost(0d);
            return report;
        }
        report.setCost(durationMinutes * minutePrice);
        return report;
    }
}
