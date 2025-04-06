package snagicky.collector.api.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import snagicky.collector.api.model.Card;
import snagicky.collector.api.repo.CardRepo;

@RestController()
@RequestMapping("/api/card")
public class CardControler {
    @Autowired
    CardRepo cr;

    @GetMapping("/all/")
    public Iterable<Card> GetAll() {
        return cr.findAll();
    }
}
