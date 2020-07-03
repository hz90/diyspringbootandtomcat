package springandtomcat.tomcat;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class SATResponse {
    private OutputStream outputStream;

    public SATResponse(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void write(String content) throws IOException {
        StringBuffer httpresponse = new StringBuffer();
        httpresponse.append("HTTP/1.1 200 OK\n")
                .append("Content-Type:  text/html\n")
                .append("\r\n")
                .append("<html><body>")
                .append(content)
                .append("</body></html>");
        outputStream.write(httpresponse.toString().getBytes());
        outputStream.close();
    }

    public void write(File file) {
        //处理html文件
    }
}
