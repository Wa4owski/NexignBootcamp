package org.example.service;

import lombok.AllArgsConstructor;
import org.example.model.Call;
import org.example.model.CallReport;
import org.example.model.CallType;
import org.example.model.Customer;
import org.example.service.tariffs.AbstractUnlimited;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

@AllArgsConstructor
public class ReportGenerator {
    static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    static DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("HH:mm:ss");
    static public void generate(Customer customer) throws IOException {
        File reportFile = new File("reports/" + customer.getNumber() + ".txt");
        reportFile.createNewFile();
        FileOutputStream outputStream = new FileOutputStream(reportFile, false);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
        bufferedWriter.write(String.format("Tariff index: %d\n", customer.getRateCode()));
        bufferedWriter.write("----------------------------------------------------------------------------\n");
        bufferedWriter.write(String.format("Report for phone number: %s\n", customer.getNumber()));
        bufferedWriter.write("----------------------------------------------------------------------------\n");
        bufferedWriter.write("| Call Type |   Start Time        |     End Time        | Duration | Cost  |\n");
        bufferedWriter.write("----------------------------------------------------------------------------\n");
        customer.getCalls().sort(Comparator.comparing(Call::getEnded));
        double sum = 0d;
        for(Call call : customer.getCalls()){
            CallReport report = customer.getRate().resolveCall(call);
            bufferedWriter.write(String.format("|     %s    | %s | %s | %s |  %.2f |\n",
                    report.getCallType().equals(CallType.OUTGOING) ? "01" : "02",
                    dtf.format(report.getStarted()), dtf.format(report.getEnded()),
                    dtf2.format(report.getDuration()), report.getCost()));
            sum += report.getCost();
        }
        if(customer.getRate() instanceof AbstractUnlimited){
            sum += ((AbstractUnlimited) customer.getRate()).getTariffPrice();
        }
        bufferedWriter.write("----------------------------------------------------------------------------\n");
        bufferedWriter.write(String.format("|                                           Total Cost: |    %.2f rubles |\n", sum));
        bufferedWriter.write("----------------------------------------------------------------------------\n");
        bufferedWriter.close();
    }
}
