package snagicky.collector.api.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import snagicky.collector.api.model.Card;
import snagicky.collector.api.model.Edition;
import snagicky.collector.api.model.Token;
import snagicky.collector.api.model.User;
import snagicky.collector.api.repo.*;

import java.util.*;

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
    TypeRepo Tr;
    @Autowired
    TokenRepo tr;

    @GetMapping("/")
    public Iterable<Card> FindCard(
            @RequestParam Map<String,String> allParams
    ){
        return cr.findCardAdvanced(allParams);
    }
    @GetMapping("/{state}/{id}")
    public Set<User> CardOwnedUsers(
            @PathVariable("state") String action,
            @PathVariable("id") Long card_id
    ){
        if(action == "own")
            return cr.findById(card_id).get().OwnedBy;
        else
            return cr.findById(card_id).get().SavedBy;
    }

    @DeleteMapping("/delete/{id}")
    public boolean RemoveCard(
            @RequestHeader("token") UUID token,
            @PathVariable("id") Long card
            ) {
        if(tr.existsById(token)) {
            Token t = tr.findById(token).get();
            if (t.EditCards) {
                cr.findById(card);
                return true;
            }
        }
        return false;
    }
    @PutMapping("/ownership/")
    public boolean EditCard(
            @RequestHeader("token") UUID token,
            @RequestParam("user") Long user,
            @RequestParam("card") Long card
    ){

        if(tr.existsById(token)) {
            Token t = tr.findById(token).get();
            if (t.EditCards) {
                Card c = cr.findById(card).get();
                c.ByUser = ur.findById(user).get();
                cr.save(c);
                return true;
            }
        }
        return false;
    }
    @PostMapping("/create/")
    public Card AddCard(
            @RequestHeader("token") UUID token,
            @RequestHeader(name = "edition_id",required = false) Long edition,
            @RequestHeader(name = "type_id") Long type,
            @RequestBody() Card card
    ){

        if(tr.existsById(token)) {
            Token t = tr.findById(token).get();
            if (t.CreateCards || t.CreateTestCards) {
                card.Id = null;
                card.ByUser = t.User;
                card.type = Tr.findById(type).get();

                // Edition stuff
                if(edition != null && t.CreateCards) {
                    card.Editions = new HashSet<>();
                    card.Editions.add(er.findById(edition).get());
                }


                return cr.save(card);
            }
        }
        return null;
    }
    @DeleteMapping("/edition/{card_id}/{edition_id}")
    public Card RemoveFromEdition(
            @RequestHeader("token") UUID token,
            @PathVariable("edition_id") Long edition,
            @PathVariable("card_id") Long card
    ) {

        if(tr.existsById(token)) {
            Token t = tr.findById(token).get();
            if (t.EditCards) {
                Edition e = er.findById(edition).get();
                Card c = cr.findById(card).get();
                e.Cards.remove(c);
                er.save(e);
                return cr.findById(card).get();
            }
        }
        return null;
    }
    @PostMapping("/edition/{card_id}/{edition_id}")
    public Card AddToEdition(
            @RequestHeader("token") UUID token,
            @PathVariable("edition_id") Long edition,
            @PathVariable("card_id") Long card
    ) {

        if(tr.existsById(token)) {
            Token t = tr.findById(token).get();
            if (t.EditCards) {
                Edition e = er.findById(edition).get();
                Card c = cr.findById(card).get();
                e.Cards.add(c);
                er.save(e);
                return cr.findById(card).get();
            }
        }
        return null;
    }
}
