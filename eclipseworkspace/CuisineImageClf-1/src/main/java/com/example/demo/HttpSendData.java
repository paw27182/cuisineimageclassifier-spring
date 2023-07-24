package com.example.demo;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class HttpSendData {
    private final String TWO_HYPHEN = "--";
    private final String EOL = "\r\n";
    private final String BOUNDARY = "*****" + String.format("%x", new Random().hashCode());;

    /**
     * Method: makeMultipartPostData
     * @param params
     * @param name
     * @param filename
     * @param mimetype
     * @param data
     * @return
     * @throws IOException
     */
    protected byte[] makeMultipartPostData(
            String params,    // method parameters
            String name,      // form name
            String filename,  // upload file name
            String mimetype,  // MIME encoding type  image/jpeg text/plain text/csv
            byte[] bImage     // image data
            ) throws IOException {
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            baos.write((TWO_HYPHEN + BOUNDARY + EOL).getBytes());
            // create params-part
            baos.write((TWO_HYPHEN + BOUNDARY + EOL).getBytes());
            baos.write(("Content-Disposition: form-data;").getBytes());
            baos.write(("name= \"params\";").getBytes());  // name= "params"
            baos.write(("filename= \"params\"" + EOL).getBytes());  // filename= "params"
            baos.write(("Content-Type: \"application/json\"" + EOL + EOL).getBytes());  // Content-Type : "application/json"
            baos.write(params.getBytes());
            baos.write((EOL).getBytes());

            // create image-part
            baos.write((TWO_HYPHEN + BOUNDARY + EOL).getBytes());
            baos.write(("Content-Disposition: form-data;").getBytes());
            baos.write(("name=\"" + name + "\";").getBytes());
            baos.write(("filename=\"" + filename + "\"" + EOL).getBytes());
            baos.write(("Content-Type: " + mimetype + EOL + EOL).getBytes());
            if (bImage != null) {
                baos.write(bImage);
            }
            baos.write((EOL).getBytes());

            // add boundary
            baos.write((TWO_HYPHEN + BOUNDARY + TWO_HYPHEN + EOL).getBytes());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                baos.close();
            } catch (Exception e) {
            }
        }
        return baos.toByteArray();
    }

    /**
     * Method: post
     * @param url
     * @param data
     * @return
     */
    protected PostResultPayload post(String strUrl, byte[] data) {
        if (data == null) {
            return null;
        }

        PostResultPayload prp = new PostResultPayload();  // response class
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        HttpURLConnection connection = null;

        try {
            URL url = new URL(strUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            connection.setRequestProperty("Content-Length", String.valueOf(data.length));
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // connect
            connection.connect();

            // send
            OutputStream os = connection.getOutputStream();
            os.write(data);
            os.close();

            // receive
            int responseCode = connection.getResponseCode();
            String contentType = connection.getContentType();
            System.out.println("[HttpSendData] responseCode= " + responseCode);
            System.out.println("[HttpSendData] content-Type= " + contentType);
            
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {  // 200 send succeeded
                // set headersData
                String body = "{\"Results\":" + connection.getHeaderField("Results") + "}";
                prp.setHeadersData(body);

                // set value data or image data
                final InputStream is = connection.getInputStream();
                if (contentType.equals("image/jpg") |
                		contentType.equals("image/png")) {
                    // in case image data (byte array)
                    byte[] buf = new byte[data.length + 1048576];  // set sufficient buffer size (+1M 1_048_576byte)
                    int size;
                    
                    while ((size = is.read(buf)) != -1) {
                        baos.write(buf, 0, size);
                    }
                    
                    prp.setImageData(baos.toByteArray());  // byte[]

                } else if(contentType.equals("application/json"))  {
                    // in case value data(json)
                    final InputStreamReader inReader = new InputStreamReader(is, "utf-8");
                    final BufferedReader bufReader = new BufferedReader(inReader);
                    StringBuffer buf = new StringBuffer();
                    String line = null;

                    // read line by line
                    while ((line = bufReader.readLine()) != null) {
                        buf.append(line);
                    }
                    prp.setValueData(buf.toString());
                    
                    bufReader.close();
                    inReader.close();
                }
                is.close();
            } else {
                // send failed
                System.out.println("statusresponseCode = " + connection.getResponseCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.disconnect();
            } catch (Exception e) {
            }
            try {
                baos.close();
            } catch (Exception e) {
            }
        }

        return prp;  // post request payload
    }
}
