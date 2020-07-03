package springandtomcat.tomcat;

public abstract class SATServlet {
    private SATServletInit satServletInit;
    public abstract void doPost(SATRequest satRequest, SATResponse satResponse);

    public abstract void doGet(SATRequest satRequest, SATResponse satResponse);
    public void init(){}
    public void service(SATRequest satRequest, SATResponse satResponse) {
        if ("POST".equalsIgnoreCase(satRequest.getMethod())) {
            doPost(satRequest, satResponse);
        } else if ("GET".equalsIgnoreCase(satRequest.getMethod())) {
            doGet(satRequest, satResponse);
        }
    }
    public SATServletInit getSatServletInit() {
        return satServletInit;
    }

    public void setSatServletInit(SATServletInit satServletInit) {
        this.satServletInit = satServletInit;
    }
}
