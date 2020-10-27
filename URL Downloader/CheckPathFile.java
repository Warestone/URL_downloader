import java.io.File;
import java.util.Scanner;

public class CheckPathFile {
    public File CheckPath(File path)
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
                    CheckPath(path);
                }
                if (input.equals("n")) {
                    System.out.println("\nPlease, enter new filename (with extension): ");
                    String newName = new Scanner(System.in).nextLine();
                    path = new File(path.toString().substring(0, path.toString().lastIndexOf('\\')) + "\\"+newName);
                    CheckPath(path);
                }
                if (!input.equals("y") && !input.equals("n"))CheckPath(path);
            }
        }
        else                                                            // If not exist, create a directories (without filename)
        {
            if (path.toString().contains(".")) new File(path.toString().substring(0,path.toString().lastIndexOf('\\'))).mkdirs();
            else path.mkdirs();
            return path;
        }
        return path;
    }
}
