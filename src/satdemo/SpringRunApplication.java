package satdemo;

import springandtomcat.tomcat.BootStrap;
import springandtomcat.tomcat.SATServletInit;

public class SpringRunApplication {


    public static void main(String[] args){
        SATServletInit satServletInit=new SATServletInit();
        satServletInit.setBaseScanpackage("satdemo");
        satServletInit.setMainServlet("springandtomcat.spring.servlet.SATDispatcherServlet");
       new BootStrap(8080).start(satServletInit);

    }
}
