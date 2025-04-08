package snagicky.collector.api.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import snagicky.collector.api.model.Card;
import snagicky.collector.api.repo.CardRepo;

import java.net.http.HttpResponse;

@RestController()
@RequestMapping("/api/card")
public class CardControler {
    @Autowired
    CardRepo cr;

    @GetMapping("/all/")
    public Iterable<Card> GetAll() {
        return cr.findAll();
    }

    @PostMapping("/")
    public ResponseEntity.BodyBuilder CreateCard() {

        return ResponseEntity.status(200);
    }
}
