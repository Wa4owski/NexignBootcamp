package org.example;

import org.example.model.Call;
import org.example.model.CallType;
import org.example.model.Customer;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class App 
{

    static SimpleDateFormat sdt = new SimpleDateFormat("yyyyMMddHH24mmss");
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
            LocalDateTime start = LocalDateTime.ofInstant(sdt.parse(arg[2]).toInstant(), ZoneId.systemDefault());
            LocalDateTime end = LocalDateTime.ofInstant(sdt.parse(arg[3]).toInstant(), ZoneId.systemDefault());
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
    }
}
