package org.example.service;

import org.example.model.Call;
import org.example.model.CallReport;

public interface RateStrategy {
    CallReport resolveCall(Call call);
}
