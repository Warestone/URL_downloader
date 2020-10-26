import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URL_Downloader {
    private static URL url;
    private static File path;
    private static String filename;
    private static boolean execute = false;

    public URL_Downloader(String[] args)
    {
        if (args.length >= 1) {
            try {url = new URL(args[0]);}
            catch (MalformedURLException e) {e.printStackTrace();}
        }
        if (args.length >= 2)path = new File(args[1]);                  // URL + path
        if (args.length == 3)execute = true;                            // URL + path + run
        else execute = false;
        if (args.length == 0)throw new InputMismatchException();        // NO ARGS
        if (args[0].equals(""))throw new InputMismatchException();      // Empty URL
        filename = getNameURL(args[0]);
        if (path != null)pathCheck();
        try
        {
            if (filename.contains(".html")) saveHTML();                 // if its html
            else saveFile();                                            // if its file
        }
        catch (Exception e) {e.printStackTrace();}
        if (execute)                                                    // run it
        {
            if (Desktop.isDesktopSupported()) {
                try
                {
                    Desktop desktop = Desktop.getDesktop();
                    File myFile = new File(pathToString());
                    desktop.open(myFile);
                } catch (IOException ex) {}
            }
        }
    }

    private static void pathCheck()                                     // This method checks a path of file
    {
        if (path.exists())                                              // If file exists, need accept for rewrite
        {
            if (path.isFile())
            {
                System.out.println("File '" + path.toString() + "' already exists.\nRewrite? (y - yes, n - no)\n");
                String input = new Scanner(System.in).next();
                if (input.equals("y"))
                {
                    path.delete();
                    pathCheck();
                }
                if (input.equals("n")) {
                    System.out.println("\nPlease, enter new filename (with extension): ");
                    String newName = new Scanner(System.in).nextLine();
                    path = new File(path.toString().substring(0, path.toString().lastIndexOf('\\')) + "\\"+newName);
                    pathCheck();
                }
                if (!input.equals("y") && !input.equals("n"))pathCheck();
            }
        }
        else                                                            // If not exist, create a directories (without filename)
        {
            if (path.toString().contains(".")) new File(path.toString().substring(0,path.toString().lastIndexOf('\\'))).mkdirs();
            else path.mkdirs();
        }
    }

    private static void saveFile() throws Exception                    // method for save file with any extension, but html
    {
        InputStream in = new BufferedInputStream(url.openStream());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int n = 0;
        while (-1!=(n=in.read(buf)))out.write(buf, 0, n);
        out.close();
        in.close();
        byte[] response = out.toByteArray();
        new FileOutputStream(pathToString()).write(response);
    }

    private static void saveHTML() throws Exception                    // method for save html with recursive save all files for html site working (with rewrite paths)
    {
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), getCharsetHTML()));
        FileWriter out = new FileWriter(pathToString(), false);
        String directory = pathToString().replace('.','_')+"_files\\";
        String inputLine;
        while ((inputLine = in.readLine()) != null)
        {
            if (inputLine.contains("<img")) inputLine = downloadIgmLinks("<img.+?\\/>",inputLine,directory,"src");                      // if url contains img
            if (inputLine.contains("<link")) inputLine = downloadIgmLinks("<link.+?\\/>",inputLine,directory,"href");                   // if url contains link
            out.write(inputLine);
        }
        out.close();
        in.close();
    }

    private static String getCharsetHTML() throws Exception
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

    private static String downloadIgmLinks (String pattern_str, String inputLine, String directory, String SearchType) // method for download html-resources: any files.
                                            // pattern_str - patter for search tags, inputLine - string, directory - path to save, searchtype - (for example: src or href)
    {
        Pattern pattern = Pattern.compile(pattern_str);
        Matcher matcher = pattern.matcher(inputLine);
        String newSRC="";
        while (matcher.find())
        {
            String founded = matcher.group();
            String urlF = founded.substring(founded.indexOf(SearchType));
            urlF = urlF.substring(urlF.indexOf((char)34)+1);                        // from first ' " ' to last ' " '
            urlF = urlF.substring(0,urlF.indexOf((char)34));
            if (!urlF.contains("http") && !urlF.contains("https"))urlF="http:"+urlF;
            String args[]={urlF,directory};
            newSRC = inputLine.replace(urlF,directory+getNameURL(urlF));      // rewrite srs/href
            try
            {
                URL_Downloader ud = new URL_Downloader(args);                           // download to 'filename'+_files
            }
            catch (Exception ex){};
        }
        return newSRC;                                                                  // return string with new filenames (src/href)
    }

    private static String getNameURL(String text_url)                                                       //get filename with extension
    {
        String name = text_url.substring(text_url.lastIndexOf('/')+1);
        if (name.equals(""))name = "index.html";                                                           // If domain
        else if (name.contains("?"))name = name.substring(name.lastIndexOf('/')+1,name.indexOf('?'));   // If has parameters
        if (!name.contains(".")) name+=".html";                                                            // If HTML
        if (name.contains("-") && name.contains(".html"))name = name.substring(name.lastIndexOf('-')+1);
        return name;
    }

    private static String pathToString()                                                 // get full filename string. If ith havent path (args[1]) then return filename, else - full filename with extension
    {
        if (path==null)return filename;
        if (path.toString().contains("."))
        {
            return path.toString();
        }
        else
        {
            return path.toString()+"\\"+filename;
        }
    }
}
