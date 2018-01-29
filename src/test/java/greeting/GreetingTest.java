package greeting;


import junit.framework.Assert;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import partio.Application;
import partio.greeting.Greeting;
import partio.greeting.GreetingRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = Application.class)
public class GreetingTest {

    @LocalServerPort
    private int port;

    @Autowired
    private GreetingRepository repo;

    @Test
    public void greetingShouldReturnDefaultMessage() throws Exception {
        Greeting g = new Greeting("asd");
        repo.save(g);
        System.out.println(repo.findAll());
        System.out.println("asdffdß\nefdsv\nfewasdf\nfd");
        Assert.assertFalse(repo.findAll().isEmpty());
    }
}