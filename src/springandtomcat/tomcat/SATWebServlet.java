package springandtomcat.tomcat;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SATWebServlet {
    String value();
}
