package snagicky.collector.api.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import snagicky.collector.api.model.Card;
import snagicky.collector.api.model.Token;
import snagicky.collector.api.model.User;
import snagicky.collector.api.repo.CardRepo;
import snagicky.collector.api.repo.EditionRepo;
import snagicky.collector.api.repo.TokenRepo;

import java.net.http.HttpResponse;
import java.util.Set;
import java.util.UUID;

@RestController()
@RequestMapping("/api/card")
public class CardControler {
    @Autowired
    CardRepo cr;
    @Autowired
    EditionRepo er;
    @Autowired
    TokenRepo tr;

    @GetMapping("/{state}/{id}")
    public Set<User> CardOwnedUsers(
            @RequestParam("state") CardAction action,
            @RequestParam("id") Long card_id
    ){
        if(action == CardAction.Own)
            return cr.findById(card_id).get().OwnedBy;
        else
            return cr.findById(card_id).get().SavedBy;
    }
    @GetMapping("/all/")
    public Iterable<Card> GetAll() {
        return cr.findAll();
    }

    @PostMapping("/create/")
    public ResponseEntity.BodyBuilder AddCard(
            @RequestHeader("token") UUID token,
            @RequestParam(name = "editionId",required = false) Long edition,
            @RequestBody() Card card
    ){

        if(tr.TokenExists(token)==1) {
            Token t = tr.TokenFromUUID(token);
            if (t.CreateCards || t.CreateTestCards) {
                card.Id = null;

                // Edition stuff
                if(edition == null || !t.CreateCards)
                    card.Editions.add(er.findById(1L).get());
                else
                    card.Editions.add(er.findById(edition).get());

                card.ByUser = t.User;

                return ResponseEntity.status(200);
            }
        }
        return ResponseEntity.status(403);
    }

    @PostMapping("/")
    public ResponseEntity.BodyBuilder CreateCard() {

        return ResponseEntity.status(200);
    }
}
