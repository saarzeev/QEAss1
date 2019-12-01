import org.javatuples.Pair;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import org.json.simple.JSONObject;

public class Main {
    public static void main (String[] args) {
        NamesStatistics stats = new NamesStatistics();
        CLIExecuter extractor = new CLIExecuter(stats, args);
        extractor.executeFromArgs();
    }
}

class NamesStatistics {

    private List<String> names = new ArrayList<>();

    public NamesStatistics(){
        JSONObject a = null;
        JSONParser parser = new JSONParser();
        Object obj  = null;

        try {
            obj = parser.parse(new FileReader("namesList.json"));
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONArray array = new JSONArray();
        array.add(obj);


        for (Object o : array)
        {
            JSONObject list = (JSONObject) o;
            JSONArray namesArray = (JSONArray) list.values().toArray()[0];

            for (int i = 0; i < namesArray.size(); i++) {
                String name = (String)namesArray.get(i);
                names.add(name);
            }
        }
    }

    public void countSubstringInNames(String substring){
        int count = 0;

        for (String name : names)
        {
            int lastIndex = 0;

            while(lastIndex != -1){

                lastIndex = name.indexOf(substring,lastIndex);

                if(lastIndex != -1){ //found substring
                    count++;
                    lastIndex += substring.length(); //accumulate position within name
                }
            }
        }

        System.out.println(count);
    }

    public void countNGramsInNames(int n){
        if (n < 1){
            return;
        }

        Map<String, Integer> map = getNGramsHistogram(n);

        for (String key: map.keySet())
        {
            System.out.println(key + ":" + map.get(key));
        }
    }

    private  Map<String, Integer> getNGramsHistogram(int n) {
        Map<String, Integer> map = new HashMap<>();

        for (String name: names)
        {
            for (int i = 0; i < name.length() - n + 1; i++) {
                String substr = name.substring(i, i + n);
                if(!map.containsKey(substr)){
                    map.put(substr, 1);
                }
                else
                {
                    map.put(substr, map.get(substr) + 1);
                }
            }
        }
        return  map;
    }

    public void getMostPopularNGramInNames(int n){
        boolean isCaseSensitive = false;

        if (n < 1){
            return;
        }

        if(!isCaseSensitive){
            setNamesToLowerCase();
        }

        Map<String, Integer> map = getNGramsHistogram(n);

        if(!isCaseSensitive){
            setNamesCapitalLetter();
        }

        int maxAppearances = getMaxAppearancesOfNgram(map);

        for (String ngram: map.keySet())
        {
            if(map.get(ngram) == maxAppearances){
                System.out.println(ngram);
            }
        }
    }

    private int getMaxAppearancesOfNgram(Map<String, Integer> map) {
        int maxAppearances = 0;

        for (String ngram: map.keySet())
        {
            if(map.get(ngram) > maxAppearances){
                maxAppearances = map.get(ngram);
            }
        }

        return maxAppearances;
    }

    private void setNamesCapitalLetter() {
        List<String> tmp = new ArrayList<>(names);
        names.clear();
        for (String name: tmp)
        {
            String res = name.toLowerCase();
            res = (Character.toString(res.charAt(0))).toUpperCase() + res.substring(1, res.length());
            names.add(res);
        }
    }

    private void setNamesToLowerCase() {
        List<String> tmp = new ArrayList<>(names);
        names.clear();
        for (String name: tmp)
        {
            names.add(name.toLowerCase());
        }
    }

    public void printNamesFoundInString(String str){
        boolean isCaseSensitive = false;
        str = isCaseSensitive ? str : str.toLowerCase();

        for (String name: names)
        {
            String nameToCheck = isCaseSensitive ? name : name.toLowerCase();

            if(str.contains(nameToCheck)){
                System.out.println(nameToCheck);
            }
        }
    }

    public Map<Character, Map<Character, Float>> createProbabilityMap(){
        Map<Character, Map<Character, Float>> map = new Hashtable<>();
        Map<Character, Integer> totalAppearances = new Hashtable<>();

        createNextCharHistogram(map, totalAppearances);
        turnNextCharHistogramToProbabilities(map, totalAppearances);

        return map;
    }

    private void turnNextCharHistogramToProbabilities(Map<Character, Map<Character, Float>> map, Map<Character, Integer> totalAppearances) {
        for (char c: map.keySet())
        {
            Map<Character, Float> nextCharAppearances = map.get(c);

            for (char nextChar: nextCharAppearances.keySet())
            {
                nextCharAppearances.put(nextChar, nextCharAppearances.get(nextChar) / (float)totalAppearances.get(c));
            }
        }
    }

    private void createNextCharHistogram(Map<Character, Map<Character, Float>> map, Map<Character, Integer> totalAppearances)
    {
        for (String name: names)
        {
            for (int i = 0; i < name.toCharArray().length; i++) {
                char c = name.toCharArray()[i];

                if(!map.containsKey(c)){
                    map.put(c, new Hashtable<Character, Float>());
                    totalAppearances.put(c, 1);
                }
                else{
                    totalAppearances.put(c, totalAppearances.get(c) + 1);
                }

                if(i + 1 < name.toCharArray().length){
                    char nextChar = name.toCharArray()[i + 1];

                    if(!map.get(c).containsKey(nextChar)){
                        map.get(c).put(nextChar, new Float(0));
                    }

                    map.get(c).put(nextChar, map.get(c).get(nextChar) + 1);
                }
            }
        }
    }

    private String getFirstLetter(){
        Map<String, Integer> mapOfCharAndAmount = getNGramsHistogram(1);
        Pair<String, Integer> maxAppearances = new Pair<String, Integer>("",0);
        for (Map.Entry<String,Integer> entry : mapOfCharAndAmount.entrySet()) {
            if(Character.isUpperCase(entry.getKey().charAt(0))){
                int appearances = entry.getValue();
                maxAppearances = maxAppearances.getValue1() > appearances ? maxAppearances : new Pair(entry.getKey(), appearances);
            }
        }
        return maxAppearances.getValue0();
    }

    private int getRandomDoubleBetweenRange(int min, int max){
        double randomNum = Math.random();
        int randomInRange = (int) ((randomNum * ((max - min) + 1)) + min);
        return randomInRange;
    }

    private Character getCharWithMaxProbability(Map<Character, Float> charAndProbability){
        charAndProbability.entrySet().removeIf(entry -> Character.isUpperCase(entry.getKey()));
        Map.Entry<Character, Float> maxEntry = Collections.max(charAndProbability.entrySet(), Comparator.comparing(Map.Entry::getValue));
        return maxEntry.getKey();
    }

    public void generateName(){
        int nameLength = getRandomDoubleBetweenRange(2,15);
        Map<Character, Map<Character, Float>> probabilities = createProbabilityMap();

        String name = getFirstLetter();
        for(int i = 1 ; i <= nameLength ; i++){
            name += getCharWithMaxProbability(probabilities.get(name.charAt(name.length() - 1)));
        }
        System.out.println(name);
    }
}


class CLIExecuter {

    private final int MAX_LENGTH_OF_ARGS = 2;
    private final NamesStatistics namesStatistics;

    private String arg;
    private String param;
    private int numOfArgs;
    private String[] args;

    public CLIExecuter(NamesStatistics namesStatistics, String[] args){
        this.namesStatistics = namesStatistics;
        this.args = args;
    }

    public void executeFromArgs()
    {
        numOfArgs = args.length;

        boolean isValidNumberOfArgs = (numOfArgs >= 1 && numOfArgs <= MAX_LENGTH_OF_ARGS);

        if(isValidNumberOfArgs){
            parseArgumentAndParameter();
            executeRelevantMethod();
        }


    }

    private void parseArgumentAndParameter()
    {
        if(args.length >= 1){
            arg = args[0];
        }

        if (args.length >= 2){
            param = args[1];
        }
    }


    private void executeRelevantMethod()
    {
        int n;
        switch (arg)
        {
            case "GenerateName":
                namesStatistics.generateName();
                break;

            case "CountSpecificString":
                namesStatistics.countSubstringInNames(param);
                break;

            case "CountAllStrings":
                if(numOfArgs != 2)
                {
                    break;
                }

                n = -1;

                try
                {
                    n = Integer.parseInt(param);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                namesStatistics.countNGramsInNames(n);
                break;

            case "CountMaxString":
                if(numOfArgs != 2)
                {
                    break;
                }

                n = -1;

                try
                {
                    n = Integer.parseInt(param);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                namesStatistics.getMostPopularNGramInNames(n);
                break;

            case "AllIncludesString":
                namesStatistics.printNamesFoundInString(param);
        }
    }
}

