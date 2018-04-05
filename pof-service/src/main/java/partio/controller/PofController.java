package partio.controller;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import partio.service.FillPofService;
import partio.service.PofService;

@RestController
@Transactional
public class PofController {

    @Autowired
    private PofService pof;
    @Autowired
    private FillPofService filledPof;

    @GetMapping("/pofdata")
    public Object getLe() throws IOException {
        return pof.getPof();
    }

    //tarppo= all tarppo, tarppoDev= sample from tarppo (faster but less data)
    @GetMapping("/pofdata/{ageGroup}")
    public Object getPofActivities(@PathVariable String ageGroup) throws IOException {
        return pof.getTasks(ageGroup);
    }

    @GetMapping("/filledpof/{ageGroup}")
    public Object getFilledPofActivities(@PathVariable String ageGroup) throws IOException {
        try {//tarppo, tarppodev valid
            return filledPof.getAgeGroup(ageGroup);
        } catch (IllegalArgumentException e) {// too lazy to type url correctly
            return filledPof.getAgeGroup(FillPofService.TARPPODEV);
        }
    }
}
