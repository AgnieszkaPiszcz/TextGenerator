import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import java.io.*;
import java.util.HashMap;
import static spark.Spark.*;


public class Main
{
    public static void main(String[] arg) throws IOException
    {
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache m = mf.compile("index.mustache");

        get("/", (req, res) -> {
            HashMap<String, String> input = new HashMap<>();
            input.put("text", "");
            StringWriter writer = new StringWriter();
            m.execute(writer, input).flush();
            return writer.toString();
        });

        post("/generate", (request, response) -> {
            String title = request.queryParams("title");
            int n = (int) Integer.parseInt(request.queryParams("n"));
            HashMap<String, String> input = new HashMap<>();
            input.put("text", getText(title, n));
            StringWriter writer = new StringWriter();
            m.execute(writer, input).flush();
            return writer.toString();
        });
    }

    public static String getText(String title, int n) {
        txtGenerator g = null;
        try
        {
            g = new txtGenerator(title, 4);
        } catch (Exception e)
        {
            return e.getMessage();
        }
        return g.generateText(n);

    }
}
