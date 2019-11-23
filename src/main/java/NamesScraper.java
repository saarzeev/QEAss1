import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.Jsoup;

import org.json.simple.JSONObject;

public class NamesScraper {

    static List<String> names = new ArrayList<String>();

    public static void main (String[] args){
        int numberOfPagesInSite = 14;

        for (int i = 0; i < numberOfPagesInSite; i++)
        {
            ExtractNamesFromPage(i);
        }
        
        ConvertToJsonFile();
    }

    private static void ConvertToJsonFile()
    {
        JSONObject obj = new JSONObject();
        obj.put("list", names);

        try
        {
            Files.write(Paths.get("namesList.json"), obj.toJSONString().getBytes());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static void ExtractNamesFromPage(int i)
    {
        String searchUrl = "https://www.behindthename.com/names/usage/english/" + i;

        try
        {
            Document doc = Jsoup.connect(searchUrl).get();
            Elements elements = doc.select(".listname");

            for (Element elem: elements)
            {
                String name = elem.child(0).select("a").first().text();
                if(name.contains(" ("))
                {
                    name = name.substring(0, name.indexOf(" ("));
                }
                names.add(name);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
