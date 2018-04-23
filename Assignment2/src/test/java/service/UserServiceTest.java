package service;

import model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import repository.user.UserRepository;
import service.user.UserService;
import service.user.UserServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class UserServiceTest {

    @Configuration
    static class UserServiceTestConfig
    {
        @Bean
        public UserRepository userRepository()
        {
            return Mockito.mock(UserRepository.class);
        }
        @Autowired
        private UserRepository userRepository;

        @Bean
        public UserService userService()
        {
            return new UserServiceImpl(userRepository);
        }
    }
    @Autowired
    private UserService userService;

    @Test
    public void addUser()
    {
        Assert.assertTrue(userService.addUser("mirel@yahoo.com","parola123!","administrator").getResult());
    }
    @Test
    public void sizeUsers()
    {
        Assert.assertTrue(userService.findAll().size()==0);
    }
}
