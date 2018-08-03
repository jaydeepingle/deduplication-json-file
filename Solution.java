import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.gson.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

/**
 * @author jaydeep
 */
public class Solution {

    private static final String dateFormat = "yyyy-MM-dd'T'HH:mm:ss+00:00";
    private static final String id = "_id";
    private static final String email = "email";
    private static final String firstName = "firstName";
    private static final String lastName = "lastName";
    private static final String address = "address";
    private static final String entryDate = "entryDate";
    private static final String leadsConstant = "leads";
    private static final int elements = 6;

    public static void main(String[] args) {
        // Adding json objects to ArrayList of JSON Objects
        ArrayList<Lead> list = getObjectList(args[0]);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = null;

        JSONObject transitions = new JSONObject();
        PrintWriter logs = null;
        try {
            logs = new PrintWriter("logs.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Iterating over list to remove objects with similar Id
        LinkedHashMap<String, Lead> hashMap = new LinkedHashMap<String, Lead>();
        for (Lead lead : list) {
            if (hashMap.containsKey(lead.getId())) {
                printLogs(hashMap.get(lead.getId()), lead, logs);
                Date one = hashMap.get(lead.getId()).getDate();
                Date two = lead.getDate();
                if (one.compareTo(two) >= 0) {
                    hashMap.remove(lead.getId());
                    hashMap.put(lead.getId(), lead);
                }
            } else {
                hashMap.put(lead.getId(), lead);
            }
        }

        list = getMapList(list, hashMap);

        // Iterating over list to remove objects with similar email Id
        hashMap = new LinkedHashMap<String, Lead>();
        for (Lead lead : list) {
            if (hashMap.containsKey(lead.getEmail())) {
                printLogs(hashMap.get(lead.getEmail()), lead, logs);
                Date one = hashMap.get(lead.getEmail()).getDate();
                Date two = lead.getDate();
                if (one.compareTo(two) >= 0) {
                    hashMap.remove(lead.getEmail());
                    hashMap.put(lead.getEmail(), lead);
                }
            } else {
                hashMap.put(lead.getEmail(), lead);
            }
        }
        logs.close();

        // following code writes json object to an output file
        writeToFile(gson, jsonParser, jsonElement, hashMap, simpleDateFormat);
    }

    /**
     * To get the object list
     * @param fileName
     * @return
     */
    public static ArrayList<Lead> getObjectList(String fileName) {
        JSONProcessor jsonProcessor = new JSONProcessor();
        JSONArray jsonArray = jsonProcessor.parseData(fileName);

        ArrayList<Lead> list = new ArrayList<Lead>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        for (Object jsonObject : jsonArray) {
            try {
                Date date = simpleDateFormat.parse(((JSONObject) jsonObject).get(entryDate).toString());
                list.add(new Lead(
                        (String) ((JSONObject) jsonObject).get(id),
                        (String) ((JSONObject) jsonObject).get(email),
                        (String) ((JSONObject) jsonObject).get(firstName),
                        (String) ((JSONObject) jsonObject).get(lastName),
                        (String) ((JSONObject) jsonObject).get(address),
                        date));
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * Prints the lead object
     * @param lead
     * @param logs
     * @param type
     */
    @SuppressWarnings("unchecked")
    public static void printLeadObject(Lead lead, PrintWriter logs, String type) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jsonParser = new JsonParser();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>(elements);
        map.put(id, lead.getId());
        map.put(email, lead.getEmail());
        map.put(firstName, lead.getFirstName());
        map.put(lastName, lead.getLastName());
        map.put(address, lead.getAddress());
        map.put(entryDate, simpleDateFormat.format(lead.getDate()));
        JSONObject o = new JSONObject();
        o.put(type, map);

        JsonElement jsonElement = jsonParser.parse(o.toJSONString());
        logs.write(gson.toJson(jsonElement));
        logs.write("\n");
        logs.flush();
    }

    /**
     * Prints the log line changes
     * @param first
     * @param second
     * @param logs
     */
    public static void printLogLine(String first, String second, PrintWriter logs) {
        if(!first.equals(second)) {
            logs.write(first + " => " + second + " " + "\n");
            logs.flush();
        }
    }

    /**
     * Prints the log line changes of Date
     * @param first
     * @param second
     * @param logs
     */
    public static void printLogLine(Date first, Date second, PrintWriter logs) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        if(first.compareTo(second) != 0) {
            logs.write(simpleDateFormat.format(first) + " => " + simpleDateFormat.format(second) + " " + "\n");
            logs.flush();
        }
    }

    /**
     * Print logs to the file
     * @param l1
     * @param l2
     * @param logs
     */
    public static void printLogs(Lead l1, Lead l2, PrintWriter logs) {
        printLeadObject(l1, logs, "source record");
        printLeadObject(l2, logs, "output record");
        logs.write("Individual Field Changes\n");
        printLogLine(l1.getId(), l2.getId(), logs);
        printLogLine(l1.getEmail(), l2.getEmail(), logs);
        printLogLine(l1.getFirstName(), l2.getFirstName(), logs);
        printLogLine(l1.getLastName(), l2.getLastName(), logs);
        printLogLine(l1.getAddress(),  l2.getAddress(), logs);
        printLogLine(l1.getDate(), l2.getDate(), logs);
    }

    /**
     * Get the list after de-duplicating the List using HashMap
     * @param list
     * @param hashMap
     * @return
     */
    public static ArrayList<Lead> getMapList(ArrayList<Lead> list, HashMap<String, Lead> hashMap) {
        list = new ArrayList<Lead>();
        for (Map.Entry<String, Lead> entry : hashMap.entrySet()) {
            list.add(entry.getValue());
        }
        return list;
    }

    /**
     * Write Results to file
     * @param gson
     * @param jsonParser
     * @param jsonElement
     * @param hashMap
     * @param simpleDateFormat
     */
    @SuppressWarnings("unchecked")
    public static void writeToFile(Gson gson, JsonParser jsonParser, JsonElement jsonElement, HashMap<String, Lead> hashMap, SimpleDateFormat simpleDateFormat) {
        PrintWriter printWriter = null;
        LinkedHashMap<String, String> map = null;
        try {
            gson = new GsonBuilder().setPrettyPrinting().create();
            jsonParser = new JsonParser();

            JSONArray leads = new JSONArray();
            JSONObject lead = new JSONObject();

            printWriter = new PrintWriter("output.json");
            for (Map.Entry<String, Lead> entry : hashMap.entrySet()) {
                map = new LinkedHashMap<String, String>(elements);
                map.put(id, entry.getValue().getId());
                map.put(email, entry.getValue().getEmail());
                map.put(firstName, entry.getValue().getFirstName());
                map.put(lastName, entry.getValue().getLastName());
                map.put(address, entry.getValue().getAddress());
                map.put(entryDate, simpleDateFormat.format(entry.getValue().getDate()));

                leads.add(map);
            }
            lead.put(leadsConstant, leads);

            jsonElement = jsonParser.parse(lead.toJSONString());
            printWriter.write(gson.toJson(jsonElement));

            printWriter.flush();
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
