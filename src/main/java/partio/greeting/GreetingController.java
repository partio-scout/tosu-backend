package partio.greeting;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {
    @Autowired
    private GreetingRepository greetingRepo;

    @RequestMapping("/greeting")
    public Greeting greeting() {
        Greeting g = new Greeting("hello, i have a random number for you:" + new Random().nextInt());
        greetingRepo.save(g);
        return g;
    }
    
    @RequestMapping("/")
    public List<Greeting> show(@RequestParam(value = "name", defaultValue = "World") String name) {
        return greetingRepo.findAll();
    }
}
