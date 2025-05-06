package snagicky.collector.api.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import snagicky.collector.api.model.Card;
import snagicky.collector.api.model.Token;
import snagicky.collector.api.model.User;
import snagicky.collector.api.repo.CardRepo;
import snagicky.collector.api.repo.TokenRepo;
import snagicky.collector.api.repo.UserRepo;

import java.util.List;
import java.util.Set;
import java.util.UUID;

// For not that messy saving cards
enum CardAction {
    Save, Own;
}

@RestController()
@RequestMapping("/api/user")
public class UserControler {
    @Autowired
    UserRepo ur;
    @Autowired
    TokenRepo tr;
    @Autowired
    CardRepo cr;

    @GetMapping("/{id}")
    public User FindUser(@PathVariable("id") long id){
        return ur.findById(id).get();
    }

    @GetMapping("/")
    public User FindUser(
            @RequestParam(value = "name",required = false) String name,
            @RequestParam(value = "id",required = false) long id,
            @RequestParam(value = "bio",required = false) String bio,
            @RequestParam(value = "card_owned",required = false) List<Long> card
    ){
        return new User(); // TODO everything
    }

    @PostMapping("/create/{name}")
    public ResponseEntity<UUID> CreateUser(
            @PathVariable("name") String name,
            @RequestHeader("password") String password, // HEADER!!
            @RequestHeader(value = "email", defaultValue = "", required = false) String email // HEADER!!
    ) {
        User us = new User();
        us.Name = name;

        User CreatedUser = ur.save(us);
        CreatedUser.Password = CreatedUser.Salt(password);

        if (!email.isEmpty()) {
            // TODO sends email with verivication
            CreatedUser.Email = email;
        }

        return ResponseEntity.ok(UserLoginToken(ur.save(CreatedUser)).Code);
    }
    @PutMapping("/password/")
    public ResponseEntity.BodyBuilder ResetPassword(
            @RequestHeader("token") UUID token,
            @RequestHeader("password") String password
    ) {
        try {
            Token t = tr.findById(token).get();
            t.User.Password = t.User.Salt(password);
            ur.save(t.User);
            return ResponseEntity.status(200);
        } catch (Exception e) {
            return ResponseEntity.status(500);
        }
    }
    @DeleteMapping("/delete/{uuid}")
    @GetMapping("/delete/{uuid}")
    public ResponseEntity.BodyBuilder DeleteUser( @RequestHeader("uuid") UUID token ) {
        if (tr.TokenExists(token) == 1 && tr.findById(token).get().DeleteSelf) {
            ur.deleteById(tr.findById(token).get().User.Id);
            return ResponseEntity.status(200);
        }
        return ResponseEntity.status(500);
    }

    @PostMapping("/delete/{id}")
    public UUID RequestDeleteUser(
            @RequestHeader("token") UUID token,
            @PathVariable("id") Long id
    ){
        if (tr.TokenExists(token) == 1) {
            User u = ur.findById(id).get();
            Token t = new Token();
            t.DeleteSelf = true;
            t.User = u;
            if(u.Id == id) {
                t = tr.save(t);
                if (u.Email != null) {
                    return null; // TODO send email with it
                } else
                    return t.Code;
            } else if (tr.findById(token).get().DeleteLower && u.Perrmission < tr.findById(token).get().User.Perrmission) {
                t = tr.save(t);
                return t.Code;
            }
            return null;
        }
        return null;
    }
    @PostMapping("/password/")
    public UUID ResetPasswordToken(
            @RequestHeader("token") UUID token,
            @RequestHeader(required = false,value = "password") String password
    ) {
        User u = tr.findById(token).get().User;
        if (password != null){return null;} // TODO send email with password reset token
        if(u.Password == u.Salt(password)){
            Token t = new Token();
            t.ChangePassword = true;
            t.User = u;
            return tr.save(t).Code;
        }
        return null;
    }
    // TODO allow admins edit bunch of stuff here
    @GetMapping("/card/{action}/{id}") // Favs/Owned of a user
    public Set<Card> GetSavedCards(
            @RequestHeader("action") CardAction action,
            @RequestParam("id") Long user
    ) {
        try {
            User u = ur.findById(user).get();
            if(action == CardAction.Own)
                return u.OwnedCards;
            else
                return u.SavedCards;
        } catch (Exception e) {
            return null;
        }
    }
    @PostMapping("/card/{action}/{id}") // make a favorite/owned for a user
    public ResponseEntity.BodyBuilder CardSave(
            @RequestHeader("token") UUID t,
            @RequestHeader("action") CardAction action,
            @RequestParam("id") Long card
    ){
        try{
            User u = tr.findById(t).get().User;
            // :D
            (action == CardAction.Own ? u.OwnedCards : u.SavedCards ).add(cr.findById(card).get());
            ur.save(u);

            return ResponseEntity.status(200);
        } catch (Exception e) {
            return ResponseEntity.status(500);
        }
    }
    @DeleteMapping("/card/{action}/{id}") // removes from favorites/owned for a user
    public ResponseEntity.BodyBuilder CardUnSave(
            @RequestHeader("token") UUID t,
            @RequestHeader("action") CardAction action,
            @RequestParam("id") Long card
    ){
        try{
            User u = tr.findById(t).get().User;
            if(action == CardAction.Own)
                u.OwnedCards.remove(cr.findById(card).get());
            else
                u.SavedCards.remove(cr.findById(card).get());
            ur.save(u);

            return ResponseEntity.status(200);
        } catch (Exception e) {
            return ResponseEntity.status(500);
        }
    }

    @PutMapping("/edit/")
    public ResponseEntity.BodyBuilder EditBio(
            @RequestHeader("token") UUID t,
            @RequestHeader(value = "bio",required = false) String bio,
            @RequestHeader(value = "name",required = false) String name,
            @RequestHeader(value = "user_id",required = false) Long id
    ){
        try {
            Token token = tr.findById(t).get();
            if (id == null || !token.EditLower)
                id = token.User.Id;
            if (token.EditSelf || token.EditLower) {

                User usr = ur.findById(id).get();
                if (bio != null) usr.Bio = bio;
                if (name != null) usr.Name = name;

                ur.save(usr);
                return ResponseEntity.status(200);
            } else return ResponseEntity.status(403);
        } catch (Exception e) {
            return ResponseEntity.status(500);
        }
    }
    @DeleteMapping("/quit/")
    public void DeleteAllTokens(
            @RequestHeader("token") UUID t,
            @RequestHeader(required = false, value = "id") Long Id
    ){
        if(tr.TokenExists(t) == 1)
            if (Id != null && tr.findById(t).get().EditLower && ur.findById(Id).get().Perrmission < tr.findById(t).get().User.Perrmission)
                tr.LogOutAll(Id);
            else
                tr.LogOutAll(tr.findById(t).get().User.Id);
    }
    @DeleteMapping("/logout/")
    public void DeleteTokens( @RequestHeader("token") UUID t){
        tr.delete(tr.findById(t).get());
    }
    // Verifies user from token
    public ResponseEntity.BodyBuilder Verify(UUID code) {
        try {
            User usr = tr.findById(code).get().User;
            if (usr.Perrmission == 0) usr.Perrmission = 1;
            ur.save(usr);
            return ResponseEntity.status(200);
        } catch (Exception e) {
            return ResponseEntity.status(500);
        }
    }
    // Links login token to given user
    public Token UserLoginToken(User usr) {
    /*
        1 verify_self
        2 verify_other
        3 edit_self
        4 delete_self (Grants seperet token just for deleting your acc)
        5 edit_lower
        6 delete_lower
        7 create_cards
        8 create_test_cards
        9 edit_cards
        10 change_permission (up to [not included] your level)
        11 change password

        visitor  = 0
        User     = 1
        Admin    = 2
        Root     = 3 (have to be created through database)

        */
        final boolean x = false;
        final boolean o = true;
        final boolean[][] PerrmissionsLvl = {
        //      {1,2,3,4,5,6,7,8,9,0,1}
                {x,x,o,x,x,x,x,x,x,x,x},
                {x,x,o,x,x,x,x,o,x,x,x},
                {x,o,o,x,x,o,o,o,o,x,x},
                {x,o,o,x,o,o,o,o,o,o,x},
        };
        Token ver = new Token();
        ver.VerifySelf = PerrmissionsLvl[usr.Perrmission][0];
        ver.VerifyOther = PerrmissionsLvl[usr.Perrmission][1];
        ver.EditSelf = PerrmissionsLvl[usr.Perrmission][2];
        ver.EditLower = PerrmissionsLvl[usr.Perrmission][4];
        ver.DeleteLower = PerrmissionsLvl[usr.Perrmission][5];
        ver.CreateCards = PerrmissionsLvl[usr.Perrmission][6];
        ver.CreateTestCards = PerrmissionsLvl[usr.Perrmission][7];
        ver.EditCards = PerrmissionsLvl[usr.Perrmission][8];
        ver.ChangePermission = PerrmissionsLvl[usr.Perrmission][9];

        ver.ChangePassword = PerrmissionsLvl[usr.Perrmission][10];

        return tr.save(ver);
    }
}
