package snagicky.collector.api.controler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/test")
public class TestRestControler {

    @GetMapping(value = "/")
    public String Index(){
        return "RestApi is working";
    }

}
