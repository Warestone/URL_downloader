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
        if (args.length == 0)throw new InputMismatchException();        // NO ARGS
        if (args[0].equals(""))throw new InputMismatchException();      // Empty URL
        try {url = new URL(args[0]);}
        catch (MalformedURLException e) {
            System.out.println("\n\nIncorrect URL '"+url.toString()+"'");
            System.exit(-1);
        }
        if (args.length >= 2)path = new File(args[1]);                  // URL + path
        if (path != null) path = new CheckPathFile().CheckPath(path);
        boolean execute = args.length == 3;                             // URL + path + run
        GetInfoURL giURL = new GetInfoURL(url,path);
        String filename = giURL.getNameURL();
        File fileExec = new File(giURL.pathToString());
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
                    desktop.open(fileExec);
                } catch (IOException ex) {
                    System.out.println("\n\nCan't open the file "+filename+"'.");
                }
            }
        }
    }
}
