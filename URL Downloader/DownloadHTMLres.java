import java.io.File;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DownloadHTMLres {
    public String downloadIgmLinks (URL url, File path, String pattern_str, String inputLine, String directory, String SearchType) // method for download html-resources: any files.
    // pattern_str - patter for search tags, inputLine - string, directory - path to save, searchtype - (for example: src or href)
    {
        Pattern pattern = Pattern.compile(pattern_str);
        Matcher matcher = pattern.matcher(inputLine);
        String newSRC="";
        while (matcher.find())
        {
            GetInfoURL giURL = new GetInfoURL(url,path);
            String founded = matcher.group();
            String urlF = founded.substring(founded.indexOf(SearchType));
            urlF = urlF.substring(urlF.indexOf((char)34)+1);                               // from first ' " ' to last ' " '
            urlF = urlF.substring(0,urlF.indexOf((char)34));
            if (!urlF.contains("http") && !urlF.contains("https"))urlF="http:"+urlF;
            String args[]={urlF,directory};
            newSRC = inputLine.replace(urlF,directory+giURL.getNameURL());      // rewrite srs/href
            URL_Downloader ud = new URL_Downloader(args);                              // download to 'filename'+_files
        }
        return newSRC;                                                                      // return string with new filenames (src/href)
    }
}
