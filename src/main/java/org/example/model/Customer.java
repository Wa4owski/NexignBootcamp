package org.example.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.service.ByMinuteRate;
import org.example.service.DefaultRate;
import org.example.service.RateStrategy;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class Customer {
    private String number;
    private List<Call> calls = new ArrayList<>();
    private RateStrategy rate;

    private int rateCode;

    public Customer(String number, int rateCode) throws IllegalAccessException {
        this.number = number;
        this.rateCode = rateCode;
        switch (rateCode){
            case 3:
                rate = new ByMinuteRate();
                break;
            case 6:
                //rate = n
                break;
            case 11:
                rate = new DefaultRate();
                break;
            default:
                throw new IllegalAccessException("No such rate");
        };
    }

    public void addCall(Call call){
        calls.add(call);
    }
}
