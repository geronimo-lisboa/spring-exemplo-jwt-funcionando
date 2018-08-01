package don.geronimo.testejwt.controller;

import don.geronimo.testejwt.model.BugDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BugController {
    @GetMapping("/bug/{id}")
    public BugDTO getBug(String id){
        return  null;
    }
}
