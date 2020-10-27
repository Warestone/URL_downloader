import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class GetInfoURL {
    private static URL url;
    private static File path;
    GetInfoURL(URL urlIn, File pathIn)
    {
        url = urlIn;
        path = pathIn;
    }

    public String getCharsetHTML() throws IOException
    {
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null)
        {
            if (inputLine.contains("charset"))
            {
                String charset = inputLine.substring(inputLine.indexOf("charset"));
                try
                {
                    charset = charset.substring(charset.indexOf((char)34)+1,charset.indexOf(">")-1);
                }
                catch (Exception ex){return "utf-8";}
                return charset;
            }
        }
        in.close();
        return "utf-8";
    }

    public String getNameURL()                                                               //get filename with extension
    {
        String name = url.toString().substring(url.toString().lastIndexOf('/')+1);
        if (name.equals(""))name = "index.html";                                                           // If domain
        else if (name.contains("?"))name = name.substring(name.lastIndexOf('/')+1,name.indexOf('?'));   // If has parameters
        if (!name.contains(".")) name+=".html";                                                            // If HTML
        if (name.contains("-") && name.contains(".html"))name = name.substring(name.lastIndexOf('-')+1);
        return name;
    }
    public String pathToString()    // get full filename string. If ith havent path (args[1]) then return filename, else - full filename with extension
    {
        if (path==null)return getNameURL();
        if (path.toString().contains("."))return path.toString();
        return path.toString()+"\\"+getNameURL();
    }
}
