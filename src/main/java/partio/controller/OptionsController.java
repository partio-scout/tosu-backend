
package partio.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

//handles preflight requests made by browsers
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class OptionsController {
     @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity handle() {
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public ResponseEntity handlePreflight() {
        return new ResponseEntity(HttpStatus.OK);
    }
   
    
}
