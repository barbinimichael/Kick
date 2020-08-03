package com.Kick.Kick;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class LoadData {

    private static final String localPath = System.getProperty("user.dir");

    public static List<String> getFirstNames(int length) throws FileNotFoundException {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            list.add(choose(new File(localPath +"/src/main/resources/FirstNames.txt")));
        }
        return list;
    }

    public static List<String> getLastNames(int length) throws FileNotFoundException {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            list.add(choose(new File(localPath +"/src/main/resources/LastNames.txt")));
        }
        return list;
    }

    public static List<String> getCities(int length) throws FileNotFoundException {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            list.add(choose(new File(localPath +"/src/main/resources/City.txt")));
        }
        return list;
    }

    public static List<String> getCountries(int length) throws FileNotFoundException {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            list.add(choose(new File(localPath +"/src/main/resources/Country.txt")));
        }
        return list;
    }

    public static List<String> getBiographies(int length) throws FileNotFoundException {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            StringBuilder sentence = new StringBuilder();
            for (int j = 0; j < 5; j++) {
                sentence.append(choose(new File(localPath + "/src/main/resources/Words.txt"))).append(" ");
            }
            list.add(sentence.toString());
        }
        return list;
    }

    private static String choose(File f) throws FileNotFoundException
    {
        String result = null;
        Random rand = new Random();
        int n = 100;
        Scanner sc = new Scanner(f);
        while ( sc.hasNextLine() )
        {
            ++n;
            String line = sc.nextLine();
            if(rand.nextInt(n) == 0) {
                result = line;
                return result;
            }
        }

        return "blah blah";
    }
}
