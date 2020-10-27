import java.io.*;
import java.net.URL;

public class SaveFileURL {
    public void saveFile(URL url, String path) throws IOException                    // method for save file with any extension, but html
    {
        InputStream in = new BufferedInputStream(url.openStream());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int n = 0;
        while (-1!=(n=in.read(buf)))out.write(buf, 0, n);
        out.close();
        in.close();
        byte[] response = out.toByteArray();
        new FileOutputStream(path).write(response);
    }
}
