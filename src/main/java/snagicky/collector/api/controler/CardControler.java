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
import snagicky.collector.api.repo.UserRepo;

import java.util.Set;
import java.util.UUID;

@RestController()
@RequestMapping("/api/card")
public class CardControler {
    @Autowired
    CardRepo cr;
    @Autowired
    UserRepo ur;
    @Autowired
    EditionRepo er;
    @Autowired
    TokenRepo tr;

    @GetMapping("/{state}/{id}")
    public Set<User> CardOwnedUsers(
            @PathVariable("state") CardAction action,
            @PathVariable("id") Long card_id
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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity.BodyBuilder RemoveCard(
            @RequestHeader("token") UUID token,
            @PathVariable("id") Long card
            ) {
        if(tr.TokenExists(token)==1) {
            Token t = tr.TokenFromUUID(token);
            if (t.EditCards) {
                cr.findById(card);
                return ResponseEntity.status(200);
            }
        }
        return ResponseEntity.status(403);
    }
    @PutMapping("/ownership/")
    public ResponseEntity.BodyBuilder EditCard(
            @RequestHeader("token") UUID token,
            @RequestParam("user") Long user,
            @RequestParam("card") Long card
    ){

        if(tr.TokenExists(token)==1) {
            Token t = tr.TokenFromUUID(token);
            if (t.EditCards) {
                Card c = cr.findById(card).get();
                c.ByUser = ur.findById(user).get();
                cr.save(c);
                return ResponseEntity.status(200);
            }
        }
        return ResponseEntity.status(403);
    }
    @PostMapping("/create/")
    public ResponseEntity.BodyBuilder AddCard(
            @RequestHeader("token") UUID token,
            @RequestParam(name = "edition_id",required = false) Long edition,
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

                cr.save(card);
                return ResponseEntity.status(200);
            }
        }
        return ResponseEntity.status(403);
    }
    @DeleteMapping("/edition/{card_id}/{edition_id}")
    public ResponseEntity.BodyBuilder RemoveFromEdition(
            @RequestHeader("token") UUID token,
            @PathVariable("edition_id") Long edition,
            @PathVariable("card_id") Long card
    ) {

        if(tr.TokenExists(token)==1) {
            Token t = tr.TokenFromUUID(token);
            if (t.EditCards) {
                Card c = cr.findById(card).get();
                c.Editions.remove(er.findById(edition).get());
                cr.save(c);
                return ResponseEntity.status(200);
            }
        }
        return ResponseEntity.status(403);
    }
    @PutMapping("/edition/{card_id}/{edition_id}")
    public ResponseEntity.BodyBuilder AddToEdition(
            @RequestHeader("token") UUID token,
            @PathVariable("edition_id") Long edition,
            @PathVariable("card_id") Long card
    ) {

        if(tr.TokenExists(token)==1) {
            Token t = tr.TokenFromUUID(token);
            if (t.EditCards) {
                Card c = cr.findById(card).get();
                c.Editions.add(er.findById(edition).get());
                cr.save(c);
                return ResponseEntity.status(200);
            }
        }
        return ResponseEntity.status(403);
    }
}
