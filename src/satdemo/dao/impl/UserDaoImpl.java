package satdemo.dao.impl;


import satdemo.dao.UserDao;
import springandtomcat.spring.annotation.SATRepository;

@SATRepository("userDaoImpl")
public class UserDaoImpl implements UserDao {
    @Override
    public void insert() {
        System.out.println("execute userdaoImpl");
    }
}
