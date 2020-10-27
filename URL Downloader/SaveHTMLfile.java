import java.io.*;
import java.net.URL;

public class SaveHTMLfile {
    public void saveHTML(URL url, File path) throws IOException                    // method for save html with recursive save all files for html site working (with rewrite paths)
    {
        GetInfoURL giURL = new GetInfoURL(url,path);
        DownloadHTMLres dHTML = new DownloadHTMLres();
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), giURL.getCharsetHTML()));
        FileWriter out = new FileWriter(giURL.pathToString(), false);
        String directory = giURL.pathToString().replace('.','_')+"_files\\";
        String inputLine;
        while ((inputLine = in.readLine()) != null)
        {
            if (inputLine.contains("<img")) inputLine = dHTML.downloadIgmLinks(url, path,"<img.+?\\/>",inputLine,directory,"src");                      // if url contains img
            if (inputLine.contains("<link")) inputLine = dHTML.downloadIgmLinks(url, path,"<link.+?\\/>",inputLine,directory,"href");                   // if url contains link
            out.write(inputLine);
        }
        out.close();
        in.close();
    }
}
