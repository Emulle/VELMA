package com.embaradj.velma;

import com.embaradj.velma.models.DataModel;

import java.io.File;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.Formatter;
import java.util.HashMap;

/**
 * Keyword analyser for topics extracted with LDA.
 * The percentage for each topic is calculated in regard to possible max outcome, which is when;
 * all words of a topic is found at least once in every document of the dataset. The percentage for
 * each keyword is in regard to the total number of hits of its topic.
 */
public class Analyser {

    private HashMap<String, String> jobs = new HashMap<>();
    private HashMap<String, String> hves = new HashMap<>();
    private HashMap<String, String> hveAim = new HashMap<>();
    private HashMap<String, String> hveCourses = new HashMap<>();
    private HashMap<String, String> topics;
    private final boolean[] settings;
    private DataModel model;

    /**
     * If class is run by itself it will use some fake topics
     * @param args
     */
    public static void main(String[] args) {
        new Analyser(new DataModel(), createTestTopics());
    }

    /**
     * Constructor, parses the global settings and starts the analysing
     * @param topics Topics to analyse
     */
    public Analyser(DataModel model, HashMap<String, String> topics) {
        this.model = model;
        this.topics = topics;
        settings = Settings.getAnalyserSelection();
        String jobsPath = Settings.rawdataPath + "/job";
        String hvePath = Settings.rawdataPath + "/hve";

        if (settings[0]) readFiles(jobs, jobsPath + "/swe");           // Job ads Swe
        if (settings[1]) readFiles(jobs, jobsPath + "/eng");           // Job ads Eng
        if (settings[2]) readFiles(hves, hvePath + "/full");           // HVE full
        if (settings[3]) readFiles(hveAim, hvePath + "/aim");          // HVE aims
        if (settings[4]) readFiles(hveCourses, hvePath + "/courses");  // HVE courses

        doAnalyse();
    }

    /**
     * Read all files located in the supplied path, and put the content of each file as a value in the HashMap
     * @param map HashMap to populate with file input
     * @param path Path pointing to the folder with the files to read
     */
    private void readFiles(HashMap<String, String> map, String path) {

        try {
            File folder = new File(path);
            File[] files = folder.listFiles();

            for (File file : files) {
                if (!file.isFile()) continue;
//                System.out.println("Reading file " + file.getName());
                map.put(file.getName(), Files.readString(file.toPath()));
            }

        } catch (Exception e) {
            System.out.println("Problem reading files in " + path);
            e.printStackTrace();
        }
    }

    /**
     * Creates some dummy topics for testing
     * @return topics
     */
    private static HashMap<String, String> createTestTopics() {
        HashMap<String, String> testTopics = new HashMap<>();
        testTopics.put("TOPIC 0", "utveckling, hos, kunder, erfarenhet, arbeta, projekt, se");
        testTopics.put("TOPIC 1", "experience, team, work, development, working, software, skills");
        testTopics.put("TOPIC 2", "personalexpressen, konsulter, uppdrag, kunder, offentliga, gentemot, tid");
        testTopics.put("TOPIC 3", "erfarenhet, ansökan, arbeta, arbete, arbetar, se, utveckling");
        testTopics.put("TOPIC 4", "erfarenhet, arbeta, hos, kunder, söker, team, tjänsten");
        return testTopics;
    }

    /**
     * Runs the analysing process
     */
    protected String doAnalyse() {
        System.out.println("Analyser running..\nProgress 0 %");

        // Contains number of hits [0] jobs, [1] HVEs
        HashMap<String, Integer[]> wordsNum = new HashMap<>();

        int progress = 0;
        int totalTopics = topics.values().size();
        int topicSize = getTopicSize();

        for (String topic : topics.values()) {              // Each topic

            String[] words = topic.split(", ");

            // Count number of occurrences of each word
            for (int i = 0; i < words.length; i++) {

                // If word already exists it has already been counted. No purpose of counting it again.
                if (!wordsNum.containsKey(words[i])) {
                    int jh = count(words[i], jobs);
                    int hh = 0;

                    // [Jobs Swe, Jobs Eng, HVE Full, HVE goals, HVE Courses]
                    if (settings[2]) hh = count(words[i], hves);
                    else if (settings[3] && settings[4]) hh = count(words[i], hveAim, hveCourses);
                    else if (settings[3]) hh = count (words[i], hveAim);
                    else if (settings[4]) hh = count (words[i], hveCourses);

                    wordsNum.put(words[i], new Integer[]{jh, hh});
                }

                progress++;
                System.out.println("Progress " + 100 * progress / (totalTopics * topicSize) + " %");
            }
        }

        return generateResults(wordsNum);
    }

    /**
     * Counts in how many files a word is present
     * @param keyword the word
     * @param dataset the Map (key is file, value is content)
     * @return Number of files
     */
    private int count(String keyword, HashMap<String, String> dataset) {
        int counter = 0;

        for (String key : dataset.keySet()) {
            if (wordInFile(keyword, dataset.get(key))) counter++;
        }

        return counter;
    }

    /**
     * Counts in how many pair of files (max 1 / pair) a word is present
     * @param keyword the word
     * @param dataset1 1st dataset where file is key
     * @param dataset2 2nd dataset where file is key
     * @return Number of pairs
     */
    private int count(String keyword, HashMap<String, String> dataset1, HashMap<String, String> dataset2) {
        int counter = 0;

        for (String key1 : dataset1.keySet()) {
            if (wordInFile(keyword, dataset1.get(key1))) counter++;
            else {
                // find corresponding key in second map
                for (String key2 : dataset2.keySet()) {
                    if (key2.split("_")[1].equals(key1.split("_")[1])) {
                        if (wordInFile(keyword, dataset2.get(key2))) {
                            counter++;
                            break;
                        }
                    }
                }
            }
        }

        return counter;
    }

    /**
     * Checks if a word is present in a text
     * @param keyword the word
     * @param file the text
     * @return whether present
     */
    private boolean wordInFile(String keyword, String file) {
        String[] words = file.split("[\s\n\\.\\,\\!]+");
        for (int i = 0; i < words.length; i++) {
            if (keyword.toLowerCase().equals(words[i].toLowerCase())) return true;
        }
        return false;
    }

    private String generateResults(HashMap<String, Integer[]> wordsNum) {

        StringBuilder sb = new StringBuilder();

        int totalHves = (settings[2])? hves.size() : hveAim.size();
        int topicSize = getTopicSize();
        final int margin = 30;

        sb.append("\nDone analysing " +
                ((settings[0])?"\n Swedish jobs":"" ) +
                ((settings[1])?"\n English jobs":"" ) +
                ((settings[2])?"\n Full HVE's":"" ) +
                ((settings[3])?"\n HVE aims":"" ) +
                ((settings[4])?"\n HVE courses":"" ));

        sb.append("\n\nTotal number of jobs: " + jobs.size() + "\n");
        sb.append("Total number of HVEs: " + totalHves + "\n");

        // PRINT HEADER
        printSpaces(margin, sb);
        sb.append("Jobs");
        printSpaces(11,sb);
        sb.append("HVEs\n");

        final DecimalFormat df = new DecimalFormat("0.0");

        topics.forEach((topicName, topic) -> {            // Each topic
            String[] words = topic.split(", ");
            int[] total = sum(words, wordsNum);
            sb.append(topicName);
            printSpaces(margin - topicName.length(), sb);
            String jSumString = total[0] + " (" + df.format(100d * total[0] / (jobs.size() * topicSize)) + "%)";
            sb.append(jSumString);
            printSpaces(15 - jSumString.length(), sb);
            sb.append(total[1] + " (" + df.format(100d * total[1] / (totalHves * topicSize)) + "%)\n");


            for (int i = 0; i < words.length; i++) {    // Each word in the topic

                Integer sum[] = wordsNum.get(words[i]);

                String jobString = sum[0] + " (" + df.format(100d * sum[0] / total[0]) + "%)";
                String hveString = sum[1] + " (" + df.format(100d * sum[1] / total[1]) + "%)\n";

                printSpaces(3, sb);
                sb.append(words[i]);
                printSpaces(margin-(3 + words[i].length()), sb);

                sb.append(jobString);
                printSpaces(15 - jobString.length(), sb);
                sb.append(hveString);
            }

        });

        String results = sb.toString();
        System.out.println(results);
        model.setAnalyserResults(results);

        return results;
    }

    /**
     * Calculates the sum of an array of words
     * @param words an array of words
     * @param map the map containing the number of words
     * @return the sum
     */
    private int[] sum(String[] words, HashMap<String, Integer[]> map) {

        int j = 0;
        int h = 0;

        for (int i = 0; i < words.length; i++) {
            j += map.get(words[i])[0];
            h += map.get(words[i])[1];
        }

        return new int[]{j,h};
    }

    private void printSpaces(int spaces, StringBuilder sb){
        for (int i = 0; i < spaces; i++) sb.append(" ");
    }

    private int getTopicSize() {
        int size = 0;

        for (String topic : topics.values()) {
            int newSize = topic.split(", ").length;
            if (size == 0) size = newSize;
            else if (size != newSize) System.out.println("Warning: inconsistent topic size!");
        }

        return size;
    }

    public void setTopics(HashMap<String, String> topics) {
        this.topics = topics;
    }

}
