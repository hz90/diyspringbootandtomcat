package springandtomcat.spring.servlet;

import springandtomcat.spring.annotation.*;
import springandtomcat.tomcat.SATRequest;
import springandtomcat.tomcat.SATResponse;
import springandtomcat.tomcat.SATServlet;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SATDispatcherServlet extends SATServlet {
    //扫描基包
    private String basePackage = "";
    private List<String> packageNames = new ArrayList<>();
    //注解实例化
    private Map<String, Object> instanceMap = new HashMap<>();
    //包路径名称；注解名字
    private Map<String, String> nameMap = new HashMap<>();
    //URL地址：方法映射
    private Map<String, Method> urlMethodMap = new HashMap<>();
    //方法和类名 反射用
    private Map<Method, String> methodPackageMap = new HashMap<>();

    @Override
    public void init() {
        basePackage=getSatServletInit().getBaseScanpackage();
        scanBasePackage(basePackage);
        instance(packageNames);
        IOC();
        handlerUrlMethodMap();

    }

    private void handlerUrlMethodMap() {
        if (packageNames.size() < 1) {
            return;
        }
        try {
            for (String string : packageNames) {
                Class c = Class.forName(string);
                if (c.isAnnotationPresent(SATController.class)) {
                    Method[] methods = c.getMethods();
                    StringBuffer baseurl = new StringBuffer();
                    if (c.isAnnotationPresent(SATRequestMapping.class)) {
                        SATRequestMapping requestMapping = (SATRequestMapping) c.getAnnotation(SATRequestMapping.class);
                        baseurl.append(requestMapping.value());
                    }
                    for (Method method : methods) {
                        if (method.isAnnotationPresent(SATRequestMapping.class)) {
                            SATRequestMapping requestMapping = method.getAnnotation(SATRequestMapping.class);
                            baseurl.append(requestMapping.value());
                            urlMethodMap.put(baseurl.toString(), method);
                            methodPackageMap.put(method, string);
                        }
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void IOC() {
        try {
            for (Map.Entry<String, Object> entry : instanceMap.entrySet()) {
                Field[] fields = entry.getValue().getClass().getDeclaredFields();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(SATQualifier.class)) {
                        String name = field.getAnnotation(SATQualifier.class).value();
                        field.setAccessible(true);
                        field.set(entry.getValue(), instanceMap.get(name));
                    }
                }
            }
        }catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void instance(List<String> packageNames) {
        if (packageNames.size() < 1) {
            return;
        }
        try {
            for (String string : packageNames) {
                Class c = Class.forName(string);
                if (c.isAnnotationPresent(SATController.class)) {
                    SATController controller = (SATController) c.getAnnotation(SATController.class);
                    String controllerName = controller.value();
                    instanceMap.put(controllerName, c.newInstance());
                    nameMap.put(string, controllerName);
                    System.out.println("controller;" + string + ",value;" + controller.value());
                } else if (c.isAnnotationPresent(SATService.class)) {
                    SATService service = (SATService) c.getAnnotation(SATService.class);
                    String servicename = service.value();
                    instanceMap.put(servicename, c.newInstance());
                    nameMap.put(string, servicename);
                } else if (c.isAnnotationPresent(SATRepository.class)) {
                    SATRepository repository = (SATRepository) c.getAnnotation(SATRepository.class);
                    String repositoryName = repository.value();
                    instanceMap.put(repositoryName, c.newInstance());
                    nameMap.put(string, repositoryName);
                }

            }
        }catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void scanBasePackage(String basePackage) {
        URL url = this.getClass().getClassLoader().getResource(basePackage.replaceAll("\\.", "/"));
        File basePackageFile = new File(url.getPath());
        System.out.println(basePackageFile);
        File[] childFiles = basePackageFile.listFiles();
        for (File file : childFiles) {
            if (file.isDirectory()) {
                scanBasePackage(basePackage + "." + file.getName());
            } else if (file.isFile()) {
                packageNames.add(basePackage + "." + file.getName().split("\\.")[0]);
            }
        }

    }

    @Override
    public void doPost(SATRequest satRequest, SATResponse satResponse) {
        String uri = satRequest.getUrl();
        // String contextPath = req.getContextPath();
        //String path = uri.replaceAll(contextPath, "");
        String path = uri;

        Method method = urlMethodMap.get(path);
        if (method != null) {
            String packagename = methodPackageMap.get(method);
            String controllerName = nameMap.get(packagename);

            Object controller = instanceMap.get(controllerName);
            try {
                method.setAccessible(true);
                method.invoke(controller);
                satResponse.write("123");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void doGet(SATRequest satRequest, SATResponse satResponse) {
        doPost(satRequest, satResponse);
    }
}
