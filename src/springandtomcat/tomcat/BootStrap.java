package springandtomcat.tomcat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class BootStrap {
    private int port=8080;
    private Map<String,SATServlet> mapservlet=new HashMap<String,SATServlet>();
    public BootStrap(int port) {
        this.port = port;
    }
    public void start(SATServletInit satServletInit){
        servletinit(satServletInit);
        ServerSocket serverSocket=null;
        try {
            serverSocket=new ServerSocket(port);
            System.out.println("Tomcat server is start::port="+port);
            while(true){
                Socket socket=serverSocket.accept();
                InputStream inputStream=socket.getInputStream();
                OutputStream outputStream=socket.getOutputStream();
                SATRequest satRequest=new SATRequest(inputStream);
                SATResponse satResponse=new SATResponse(outputStream);
                dispatcher(satServletInit,satRequest,satResponse);
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(serverSocket!=null){
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void dispatcher(SATServletInit satServletInit,SATRequest satRequest, SATResponse satResponse) {
        SATServlet satServlet=mapservlet.get(satServletInit.getMainServlet());
        satServlet.service(satRequest, satResponse);
    }

    private void servletinit(SATServletInit satServletInit) {
        SATServlet satServlet=null;
        try {
            Class<SATServlet> clazz= (Class<SATServlet>) Class.forName(satServletInit.getMainServlet());
            satServlet=clazz.newInstance();
            satServlet.setSatServletInit(satServletInit);
            satServlet.init();
            mapservlet.put(satServletInit.getMainServlet(),satServlet);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
