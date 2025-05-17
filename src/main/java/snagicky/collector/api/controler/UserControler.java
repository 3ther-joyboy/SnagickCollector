package snagicky.collector.api.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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


@RestController()
@RequestMapping("/api/user")
public class UserControler {
    @Autowired
    UserRepo ur;
    @Autowired
    TokenRepo tr;
    @Autowired
    CardRepo cr;

    //Email
    @Autowired
    JavaMailSender emailSender;

    @GetMapping("/{id}")
    public User FindUser(@PathVariable("id") long id){
        return ur.findById(id).get();
    }

    @GetMapping("/")
    public Iterable<User> FindUser(){
        return ur.findAll();
    }

    @PostMapping("/login/{name}")
    public UUID LoginUser(
        @PathVariable("name") String name,
        @RequestHeader("password") String password
    ) {
        if(ur.existsByName(name)) {
            User u = ur.findByName(name).get(0);
            if (u.CheckPassword(password))
                return UserLoginToken(u).Code;
        }
       return null;
    }
    @PostMapping("/create/{name}")
    public UUID CreateUser(
            @PathVariable("name") String name,
            @RequestHeader("password") String password, // HEADER!!
            @RequestHeader(value = "email", defaultValue = "", required = false) String email // HEADER!!
    ) {
        User us = new User();
        us.name = name;

        User CreatedUser = ur.save(us);
        CreatedUser.Password = CreatedUser.Salt(password);

        if (!email.isEmpty())
            CreatedUser.Email = email;

        CreatedUser = ur.save(CreatedUser);

        if (!email.isEmpty())
            SendVerifiEmail(CreatedUser);

        return UserLoginToken(CreatedUser).Code;
    }
    private void SendVerifiEmail(User u){
        Token t = new Token();
        t.VerifySelf = true;
        t.User = u;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreplay@gmail.com");
        message.setTo(u.Email);
        message.setSubject("Verification Email for Snagick database");

        message.setText("http://3ther.org:8080/api/user/verifi/" + tr.save(t).Code);
        emailSender.send(message);
    }
    @GetMapping("/verifi/{token}")
    public boolean VerifiUser(@PathVariable("token") UUID t) {
        if(tr.existsById(t) && tr.findById(t).get().VerifySelf) {
            User u = tr.findById(t).get().User;
            if(u.Perrmission == 0) {
                u.Perrmission = 1;
                ur.save(u);
                DeleteAllTokens(t,null);

                return true;
            }
        }
        return false;
    }
    @DeleteMapping("/delete/{token}")
    public void DeleteUser( @PathVariable("token") UUID token ) {
        if (tr.existsById(token) && tr.findById(token).get().DeleteSelf)
            ur.deleteById(tr.findById(token).get().User.Id);
    }

    @PostMapping("/delete/{id}")
    public UUID RequestDeleteUser(
            @RequestHeader("token") UUID token,
            @PathVariable("id") Long id
    ){
        if (tr.existsById(token)) {
            User u = ur.findById(id).get();
            Token t = new Token();
            t.DeleteSelf = true;
            t.User = u;
            if(u.Id == id) {
                t = tr.save(t);
                if (u.Email != null) {
                    SendDeletionEmail(u);
                    return null;
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
    private void SendDeletionEmail(User u){
        Token t = new Token();
        t.DeleteSelf = true;
        t.User = u;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreplay@gmail.com");
        message.setTo(u.Email);
        message.setSubject("Deletion Email for Snagick database");

        message.setText("http://3ther.org:8080/api/user/delete/" + tr.save(t).Code);
        emailSender.send(message);
    }
    @PutMapping("/changepassword/")
    public boolean ResetPassword(
            @RequestHeader("token") UUID token,
            @RequestHeader(value = "password") String password
    ){
        if(tr.existsById(token) && tr.findById(token).get().ChangePassword) {
            User u = tr.findById(token).get().User;
            u.Password = u.Salt(password);
            ur.save(u);
            tr.deleteById(token);
            return true;
        }
        return false;
    }
    @PostMapping("/password/")
    public UUID ResetPasswordToken(
            @RequestHeader("token") UUID token,
            @RequestHeader(required = false,value = "password") String password
    ) {
        User u = tr.findById(token).get().User;
        if (password != null){return null;} // TODO send email with password reset token
        if(u.CheckPassword(password)){
            Token t = new Token();
            t.ChangePassword = true;
            t.User = u;
            return tr.save(t).Code;
        }
        return null;
    }
    @GetMapping("/card/{action}/{id}") // Favs/Owned of a user
    public Set<Card> GetSavedCards(
            @RequestParam("action") String action,
            @RequestParam("id") Long user
    ) {
        try {
            User u = ur.findById(user).get();
            if(action == "own")
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
            @RequestParam("action") String action,
            @RequestParam("id") Long card
    ){
        try{
            User u = tr.findById(t).get().User;
            // :D
            (action == "own" ? u.OwnedCards : u.SavedCards ).add(cr.findById(card).get());
            ur.save(u);

            return ResponseEntity.status(200);
        } catch (Exception e) {
            return ResponseEntity.status(500);
        }
    }
    @DeleteMapping("/card/{action}/{id}") // removes from favorites/owned for a user
    public ResponseEntity.BodyBuilder CardUnSave(
            @RequestHeader("token") UUID t,
            @RequestParam("action") String action,
            @RequestParam("id") Long card
    ){
        try{
            User u = tr.findById(t).get().User;
            if(action == "own")
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
    public User EditBio(
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
                if (name != null) usr.name = name;

                return ur.save(usr);
            } else return new User();
        } catch (Exception e) {
            return null;
        }
    }
    @DeleteMapping("/quit/")
    public void DeleteAllTokens(
            @RequestHeader("token") UUID t,
            @RequestHeader(required = false, value = "id") Long Id
    ){
        if(tr.existsById(t))
            if (Id != null && tr.findById(t).get().EditLower && ur.findById(Id).get().Perrmission < tr.findById(t).get().User.Perrmission)
                tr.LogOutAll(Id);
            else
                tr.LogOutAll(tr.findById(t).get().User.Id);
    }
    @DeleteMapping("/logout/")
    public void DeleteTokens( @RequestHeader("token") UUID t){
        tr.deleteById(t);
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
        Token ver = new Token(31);
        ver.User = usr;

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
