package myPack;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

/** Main class to do I/O task. Read input file line by line
    and output medianvals_by_zip line by line. After reading,
    output medianvals_by_date.
 **/
public class Main {
    // Two hashmaps to store entries catagorized by Date or Zip
    private static HashMap<String, HashMap<String, MedianList>> medianByDate = new HashMap<>();
    private static HashMap<String, HashMap<String, MedianHeap>> medianByZip = new HashMap<>();

    public static void main(String[] args) throws IOException{
        // Initialize scanner and input path
        String filePath = "../input/itcont.txt";
        Scanner scanner = new Scanner(new java.io.File(filePath), "UTF-8");

        // Initialize output path and writer
        Path fileZip = Paths.get("../output/medianvals_by_zip.txt");
        Path fileDate = Paths.get("../output/medianvals_by_date.txt");
        Charset charset = Charset.forName("UTF-8");

        // Make sure the directories exist
        Files.createDirectories(fileZip.getParent());
        BufferedWriter writerZip = Files.newBufferedWriter(fileZip, charset, StandardOpenOption.CREATE);
        BufferedWriter writerDate = Files.newBufferedWriter(fileDate, charset, StandardOpenOption.CREATE);


        // Read input line by line, for each line do something
        while (scanner.hasNextLine()){
            String str = scanner.nextLine();
            String[] raw = str.split("\\|");

            // if raw data is invalid, skip it; otherwise parse to get info
            if (!isValid(raw)) continue;
            String[] parsed = preprocess(raw);

            // if date is valid, record by date
            if (parsed[2].length() == 8) recordByDate(parsed);

            // if zip is valid, record by zip
            if (parsed[1].length() >= 5) {
                String entry = recordByZip(parsed) + "\n";
                writerZip.append(entry);
            }
        }
        writerZip.close();

        // In order to sort ID and date, we put entries into a TreeMap
        TreeMap<String, TreeMap<String, Integer[]>> sortedMedianByDate = new TreeMap<>();
        for (String id: medianByDate.keySet()){
            for (String date: medianByDate.get(id).keySet()){
                if (!sortedMedianByDate.containsKey(id)){
                    sortedMedianByDate.put(id, new TreeMap<>());
                }
                if (!sortedMedianByDate.get(id).containsKey(date)){
                    sortedMedianByDate.get(id).put(date, new Integer[3]);
                }
                sortedMedianByDate.get(id).get(date)[0] = medianByDate.get(id).get(date).findMedian();
                sortedMedianByDate.get(id).get(date)[1] = medianByDate.get(id).get(date).getCount();
                sortedMedianByDate.get(id).get(date)[2] = medianByDate.get(id).get(date).getSum();
            }
        }

        // Output entries from a TreeMap so that it is sorted
        for (String id: sortedMedianByDate.keySet()){
            for (String date: sortedMedianByDate.get(id).keySet()){
              // flip date string back to MM/DD/YYYY inorder to be valid
                String flipDate = date.substring(4, 8) + date.substring(0, 4);
                int median = sortedMedianByDate.get(id).get(date)[0];
                int count = sortedMedianByDate.get(id).get(date)[1];
                int sum = sortedMedianByDate.get(id).get(date)[2];
                String curr = id+"|"+flipDate+"|"+median+"|"+count+"|"+sum+"\n";
                writerDate.append(curr);
            }
        }
        writerDate.close();

    }

    // check if this line is valid, or skip it
    public static boolean isValid(String[] s) {
        if (!s[0].isEmpty() && !s[14].isEmpty() && s[15].isEmpty()) return true;
        return false;
    }

    // preprocess raw data, only get what we want
    public static String[] preprocess(String[] raw){
        String[] res = new String[4];
        res[0] = raw[0];    //id
        res[1] = raw[10];   //zip
        res[2] = raw[13];   //date
        res[3] = raw[14];   //val
        return res;
    }

    // in order to calculate median by date, we need to store id, date and
    // a List to calculate median.
    public static void recordByDate(String[] parsed) {
        String id = parsed[0];
        // flip date string to YYYY/MM/DD inorder to be sorted
        String date = parsed[2].substring(4, 8)+ parsed[2].substring(0, 4);
        double val = Double.valueOf(parsed[3]);

        if (!medianByDate.containsKey(id)) {
            medianByDate.put(id, new HashMap<>());
        }
        if (!medianByDate.get(id).containsKey(date)) {
            medianByDate.get(id).put(date, new MedianList());
        }
        medianByDate.get(id).get(date).addNum(val);
    }

    // in order to calculate median by zip, we need to store id, zip and
    // two Heaps to calculate running median.
    public static String recordByZip(String[] parsed) {
        String id = parsed[0];
        String zip = parsed[1].substring(0, 5);
        double val = Double.valueOf(parsed[3]);

        if (!medianByZip.containsKey(id)) {
            medianByZip.put(id, new HashMap<String, MedianHeap>());
        }
        if (!medianByZip.get(id).containsKey(zip)) {
            medianByZip.get(id).put(zip, new MedianHeap());
        }
        medianByZip.get(id).get(zip).addNum(val);
        int median = medianByZip.get(id).get(zip).findMedian();
        int count = medianByZip.get(id).get(zip).getCount();
        int sum = medianByZip.get(id).get(zip).getSum();
        return id+"|"+zip+"|"+median+"|"+count+"|"+sum;
    }
}
