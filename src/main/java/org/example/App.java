package org.example;

import org.example.model.Call;
import org.example.model.CallType;
import org.example.model.Customer;
import org.example.service.ReportGenerator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class App 
{

    static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    static Map<String, Customer> customers = new HashMap<>();
    static void parseCDR(String line) {
        String[] arg = line.split(", ");
        if(arg.length != 5){
            System.out.println("Wrong line! skip...");
            return;
        }
        Customer customer = customers.get(arg[1]);
        if(customer == null){
            int rateCode = Integer.parseInt(arg[4]);
            try {
                customer = new Customer(arg[1], rateCode);
            }
            catch (IllegalArgumentException | IllegalAccessException ex){
                System.out.println(ex.getMessage());
                return;
            }
        }
        try {
            LocalDateTime start = LocalDateTime.parse(arg[2], dtf);
            LocalDateTime end = LocalDateTime.parse(arg[3], dtf);
            if(! (arg[0].equals("01") || arg[0].equals("02") ) ){
                throw new IllegalAccessException("No such type of call");
            }
            CallType type = (arg[0].equals("01") ? CallType.OUTGOING : CallType.INCOMING);
            customer.addCall(new Call(type, start, end));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        customers.put(arg[1], customer);
    }
    public static void main( String[] args ) throws IOException {
        FileInputStream inputStream = new FileInputStream("src/main/resources/cdr.txt");
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        while (bufferedReader.ready()){
            parseCDR(bufferedReader.readLine());
        }
        for(Customer customer : customers.values()) {
            ReportGenerator.generate(customer);
        }
        System.out.println("Reports were successfully generated!");
    }
}
