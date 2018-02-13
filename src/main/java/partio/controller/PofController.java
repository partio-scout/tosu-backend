
package partio.controller;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import partio.service.PofService;

@RestController
@Transactional
public class PofController {
    @Autowired
    private PofService pof;
    
    @GetMapping("/pofdata")
    public Object getLe() throws IOException {
       return pof.getPof();
    }
}
