package springandtomcat.tomcat;

public class SATServletInit {
    private String mainServlet;
    private String baseScanpackage;

    public String getBaseScanpackage() {
        return baseScanpackage;
    }

    public void setBaseScanpackage(String baseScanpackage) {
        this.baseScanpackage = baseScanpackage;
    }

    public String getMainServlet() {
        return mainServlet;
    }

    public void setMainServlet(String mainServlet) {
        this.mainServlet = mainServlet;
    }
}
