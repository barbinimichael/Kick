package com.Kick.Kick;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LoadData {

    private String path = "/";

    public List<String> getFirstNames(int length) throws IOException {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            list.add(choose(path + "FirstNames.txt"));
        }
        return list;
    }

    public List<String> getLastNames(int length) throws IOException {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            list.add(choose(path + "LastNames.txt"));
        }
        return list;
    }

    public List<String> getCities(int length) throws IOException {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            list.add(choose(path + "City.txt"));
        }
        return list;
    }

    public List<String> getCountries(int length) throws IOException {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            list.add(choose(path + "Country.txt"));
        }
        return list;
    }

    public List<String> getBiographies(int length) throws IOException {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            StringBuilder sentence = new StringBuilder();
            for (int j = 0; j < 5; j++) {
                sentence.append(choose(path + "Words.txt")).append(" ");
            }
            list.add(sentence.toString());
        }
        return list;
    }

    private String choose(String name) throws IOException {
        InputStream inputStream = getClass().getResourceAsStream(name);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        Random rand = new Random();
        int n = 100;
        String line;
        while ((line = br.readLine()) != null) {
            if(rand.nextInt(n) == 0) {
                return line;
            }
        }

        return "blah blah";
    }
}
