import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.*;
import java.util.Random;

public class txtGenerator
{
    private HashMap<String, List<String>> transitions;

    public txtGenerator(Path filename, int l) throws IOException {
        String txt = this.trimFile(filename);
        String[] words = txt.split("[\\r\\n\\s ]+");
        this.transitions = new HashMap<>();
        for(int i = l; i < words.length - l; i++) {
            StringBuilder key = new StringBuilder();
            for(int k = i - l; k < i; k++) {
                key.append(words[k]).append(" ");
            }
            String skey = key.toString().trim();
            if(!transitions.containsKey(skey)) {
                ArrayList<String> list = new ArrayList<>();
                list.add(words[i]);
                transitions.put(skey, list);
            } else {
                transitions.get(skey).add(words[i]);
            }
        }
    }

    public txtGenerator(String title, int l) throws Exception
    {
        String txt = txtFetcher.fetchText(title);
        if(txt != "Nie znaleziono tytułu. Spróbuj ponownie.")
        {
            try
            {
                txt = this.trimFile(txt);
            } catch (Exception e)
            {
            } // nie udało sie obciąć tekstu licencji i innych śmieci z tekstu książki - trudno, jedziemy dalej
            String[] words = txt.split("[\\r\\n\\s ]+");
            this.transitions = new HashMap<>();
            for (int i = l; i < words.length - l; i++)
            {
                StringBuilder key = new StringBuilder();
                for (int k = i - l; k < i; k++)
                {
                    key.append(words[k]).append(" ");
                }
                String skey = key.toString().trim();
                if (!transitions.containsKey(skey))
                {
                    ArrayList<String> list = new ArrayList<>();
                    list.add(words[i]);
                    transitions.put(skey, list);
                } else
                {
                    transitions.get(skey).add(words[i]);
                }
            }
        }
        else {
            throw new Exception("Nie znaleziono tytułu. Spróbuj ponownie.");
        }
    }

    public txtGenerator(){}

    public String generateText(int l) {
        List<String> kList = new ArrayList<String>(transitions.keySet());
        Random r = new Random();
        String startKey = kList.get(r.nextInt(kList.size()));
        StringBuilder result = new StringBuilder();
        result.append(startKey).append(" ");
        for(int i = 0; i < l; i++) {
            String w = transitions.get(startKey).get(r.nextInt(transitions.get(startKey).size()));
            result.append(w).append(" ");
            String[] parts = startKey.split(" +");
            StringBuilder k = new StringBuilder();
            for(int j = 1; j < parts.length; j++) {
                k.append(parts[j]).append(" ");
            }
            k.append(w).append(" ");
            startKey = k.toString().trim();
        }
        return result.toString();
    }

    public String trimFile(Path fileName) throws IOException {
        String content = Files.readString(fileName);
        String[] parts = content.split("\\*\\*\\*[A-Z ]+\\*\\*\\*.*|\\n*\\*\\*\\*[A-Z ]+\\*\\*\\*");
        return parts[1].trim();
    }

    public String trimFile(String content) {
        String s;
        String[] parts = content.split("\\*\\*\\*[A-Z ]+\\*\\*\\*.*|\\n*\\*\\*\\*[A-Z ]+\\*\\*\\*");
        return parts[1].trim();
    }
}
