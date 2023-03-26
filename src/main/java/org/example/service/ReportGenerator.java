package org.example.service;

import lombok.AllArgsConstructor;
import org.example.model.Call;
import org.example.model.CallReport;
import org.example.model.CallType;
import org.example.model.Customer;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Comparator;

@AllArgsConstructor
public class ReportGenerator {
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
    static void generate(Customer customer) throws IOException {
        FileOutputStream outputStream = new FileOutputStream("reports/" + customer.getNumber() + ".txt");
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
        bufferedWriter.write(String.format("Tariff index: %d\n", customer.getRateCode()));
        bufferedWriter.write("----------------------------------------------------------------------------\n");
        bufferedWriter.write(String.format("Report for phone number: %s\n", customer.getNumber()));
        bufferedWriter.write("----------------------------------------------------------------------------\n");
        bufferedWriter.write("| Call Type |   Start Time        |     End Time        | Duration | Cost  |");
        bufferedWriter.write("----------------------------------------------------------------------------\n");
        customer.getCalls().sort(Comparator.comparingInt(call -> call.getEnded().getNano()));
        double sum = 0d;
        for(Call call : customer.getCalls()){
            CallReport report = customer.getRate().resolveCall(call);
            bufferedWriter.write(String.format("|     %s    | %s | %s | %s |  %f.2 |",
                    report.getCallType().equals(CallType.OUTGOING) ? "01" : "02",
                    sdf.format(report.getStarted()), sdf.format(report.getEnded()),
                    sdf2.format(report.getDuration()), report.getCost()));
            sum += report.getCost();
        }
    }
}
