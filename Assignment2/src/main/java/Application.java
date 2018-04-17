import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import repository.user.UserRepository;

@SpringBootApplication
@EntityScan("model")
@EnableJpaRepositories({"repository.user","repository.sale","repository.book"})
@ComponentScan({"model","repository.user","repository.book","repository.sale","service.book","service.sale","service.user","controller"})
public class Application {

    public static void main(String []args)
    {
        SpringApplication.run(Application.class, args);
    }
}
