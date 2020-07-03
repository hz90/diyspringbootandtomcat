package satdemo.controller;


import satdemo.service.UserService;
import springandtomcat.spring.annotation.SATController;
import springandtomcat.spring.annotation.SATQualifier;
import springandtomcat.spring.annotation.SATRequestMapping;

@SATController("userController")
@SATRequestMapping("/user")
public class UserController {
    @SATQualifier("userServiceImpl")
    private UserService userService;
    @SATRequestMapping("/insert")
    public void insert(){
        userService.insert();
    }

}
