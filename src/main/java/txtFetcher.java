import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Scanner;

public final class txtFetcher
{
    public static String fetchText(String title) throws IOException
    {
        String url_string = getURL(title);
        if(url_string != "Nie znaleziono tytułu. Spróbuj ponownie.") {
            URL url = new URL(url_string);
            Scanner c = new Scanner(url.openStream(), "UTF-8").useDelimiter("\\Z");
            String content = c.next();
            //System.out.println(content.nextLine());
            return content.toString();
        }
        return url_string;
    }

    private static String getURL(String title) {
        String sql = "select `Text#`, Title from titles where Title == \"" + title + "\" collate nocase;";
        //String sql = "select `Text#`, Title from titles where cast(`Text#` as integer) between 200 and 210;";
        System.out.println(sql);
        String id;
        try (Connection conn = connect();
             PreparedStatement stmt  = conn.prepareStatement(sql);)
        {
            ResultSet rs = stmt.executeQuery();
            System.out.println(stmt.toString());
            if(rs.next())
            {
               id = rs.getString("Text#");
               System.out.println(id);
               String result = "https://www.gutenberg.org/files/" + id + "/" + id + "-0.txt";
               System.out.println(result);
               return result;

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return "Nie znaleziono tytułu. Spróbuj ponownie.";
    }

    private static Connection connect() {
        String url = "jdbc:sqlite:titles.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        //System.out.println("Conn established");
        return conn;
    }
}
