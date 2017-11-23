

import java.io.*;

import java.net.URLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;


public class Http {
    //    private File textFile = new File("");
    private String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
    private URLConnection connections;



    public void sendPhotoTo(File binaryFile, String chat_id) {
        {
            try {
                String url = "https://api.telegram.org/bot476871764:AAEE_jxroAgUuU2mjC14lq3desSLPy7xDec/sendPhoto?chat_id=" + chat_id;
                connections = new URL(url).openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        connections.setDoOutput(true);
        connections.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        String charset = "UTF-8";
        try (
                OutputStream output = connections.getOutputStream();
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);
        ) {
            // Send binary file.
            String CRLF = "\r\n";
            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"photo\"; filename=\"" + binaryFile.getName() + "\"").append(CRLF);
            writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(binaryFile.getName())).append(CRLF);
            writer.append("Content-Transfer-Encoding: binary").append(CRLF);
            writer.append(CRLF).flush();
            Files.copy(binaryFile.toPath(), output);
            output.flush(); // Important before continuing with writer!
            writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.

            // End of multipart/form-data.
            writer.append("--" + boundary + "--").append(CRLF).flush();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            InputStream response = connections.getInputStream();
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
