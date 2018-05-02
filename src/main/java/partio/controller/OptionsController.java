/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package partio.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author kbvalto
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class OptionsController {
    //preflight response to axios, maybe fix?
     @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity handle() {
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public ResponseEntity handle2() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
