package satdemo.service.impl;

import satdemo.dao.UserDao;
import satdemo.service.UserService;
import springandtomcat.spring.annotation.SATQualifier;
import springandtomcat.spring.annotation.SATService;

@SATService("userServiceImpl")
public class UserServiceImpl implements UserService {
    @SATQualifier("userDaoImpl")
    private UserDao userDao;

    @Override
    public void insert() {
        System.out.println("start");
        userDao.insert();
        System.out.println("end");
    }
}
