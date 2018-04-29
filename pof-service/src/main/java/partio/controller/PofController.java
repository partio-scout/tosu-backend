package partio.controller;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import partio.service.FilledPof;
import partio.service.PofService;

@RestController
@Transactional
public class PofController {

    @Autowired
    private PofService pofService;

    //return raw version of pof which is identical to the original
    @GetMapping("/pofdata")
    public Object getLe() throws IOException {
        return pofService.getPof();
    }
    //return objectnode containing treestructure of a single agegroup filled with all data 
    @GetMapping("/filledpof/{ageGroup}")
    public Object getFilledPofActivities(@PathVariable String ageGroup) throws IOException {
        try {//tarppo, tarppodev registered
            return pofService.getFilledPof().getAgeGroup(ageGroup, pofService.getPof());
        } catch (IllegalArgumentException e) {// too lazy to type url correctly
            return pofService.getFilledPof().getAgeGroup(FilledPof.TARPPODEV, pofService.getPof());
        }
    }
    //forces to reload pof and filledpof in it, for development currently
    //if needed in production this should be secured
    @GetMapping("reloadPof")
    public Object forceupdateAllOfPof() throws IOException {
       pofService.updatePofData(true);
       return "reloaded";
    }
}
