package NamesStatistics;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NamesStatistics {

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

    public int countSubstringInNames(String substring){
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

        return count;
    }
}
