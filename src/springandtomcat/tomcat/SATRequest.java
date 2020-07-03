package springandtomcat.tomcat;

import java.io.IOException;
import java.io.InputStream;

public class SATRequest {
    private String url;
    private String method;

    public SATRequest(InputStream inputStream) throws IOException {
        String httpRequest = "";
        //GET请求一般长度为1024，
        byte[] requestInformationBytes = new byte[1024];
        int length = 0;
        if ((length = inputStream.read(requestInformationBytes)) > 0) {
            httpRequest = new String(requestInformationBytes);
        }
        //解析请求信息
        dealHeader(httpRequest);
        System.out.println(this+"*****url:"+url+"******method:"+method);
    }

    /**
     *
     */
    private void dealHeader(String httpRequest) {
        String httpHead = httpRequest.split("\n")[0];
        String url = httpHead.split("\\s")[1];
        method = httpHead.split("\\s")[0];
        if (url.startsWith("/") && !url.startsWith("/favicon.ico")
                && ("GET".equalsIgnoreCase(method) || "POST".equalsIgnoreCase(method))) {
        } else {
            String[] strhead = httpRequest.split("\n");
            int broswer = 0;
            for (String string : strhead) {
                if (string.startsWith("User-Agent:") && string.toLowerCase().indexOf("chrome") > 0) {
                    //this is chrome browser
                    broswer = 1;
                }
                if (broswer == 1) {
                    if (string.toLowerCase().startsWith("referer")) {
                        url = string.substring(string.lastIndexOf("/")).trim();
                    }
                }
                if (string.toLowerCase().startsWith("get ") || string.toLowerCase().startsWith("post ")) {
                    method = string.substring(0, 4).trim();
                }

            }
        }
        setUrl(url);
        setMethod(method);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
