import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.InputMismatchException;

public class URL_Downloader {
    private static URL url;
    private static File path;

    public URL_Downloader(String[] args)
    {
        if (args.length >= 1) {
            try {url = new URL(args[0]);}
            catch (MalformedURLException e) {
                System.out.println("\n\nIncorrect URL '"+url.toString()+"'");
                System.exit(-1);
            }
        }
        if (args.length >= 2)path = new File(args[1]);                  // URL + path
        if (args.length == 0)throw new InputMismatchException();        // NO ARGS
        if (args[0].equals(""))throw new InputMismatchException();      // Empty URL
        if (path != null) path = new CheckPathFile().CheckPath(path);
        GetInfoURL giURL = new GetInfoURL(url,path);
        String filename = giURL.getNameURL();
        boolean execute = args.length == 3;                              // URL + path + run
        try
        {
            if (filename.contains(".html")) new SaveHTMLfile().saveHTML(url,path);                // if its html
            else new SaveFileURL().saveFile(url, giURL.pathToString());                           // if its file
        }
        catch (IOException e) {
            System.out.println("\n\nUnable to download '"+filename+"'. IOException, bad URL or no access file.");
        }
        if (execute)                                                    // run it
        {
            if (Desktop.isDesktopSupported()) {
                try
                {
                    Desktop desktop = Desktop.getDesktop();
                    File myFile = new File(giURL.pathToString());
                    desktop.open(myFile);
                } catch (IOException ex) {
                    System.out.println("\n\nCan't open the file "+filename+"'.");
                }
            }
        }
    }
}
