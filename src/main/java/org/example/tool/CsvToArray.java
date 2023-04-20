package org.example.tool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;

public class CsvToArray {
    public static ArrayList<String[]> catchCsv(String file) {
        BufferedReader reader = null;
        String line = "";
        String[] row;
        ArrayList<String[]> col = new ArrayList<>();

        try {
            reader = new BufferedReader(new FileReader(file));
            while ((line = reader.readLine()) != null) {
                row = line.split(",");

                col.add(row);
            }
            return col;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert reader != null;
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
