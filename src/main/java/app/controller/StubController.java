package app.controller;

import app.domain.StubEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StubController {

    @GetMapping("*")
    public StubEvent stub() {
        return new StubEvent();
    }
}
