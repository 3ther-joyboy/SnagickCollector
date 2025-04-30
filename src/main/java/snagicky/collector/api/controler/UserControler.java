package snagicky.collector.api.controler;

import jakarta.persistence.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import snagicky.collector.api.model.Token;
import snagicky.collector.api.model.User;
import snagicky.collector.api.repo.CardRepo;
import snagicky.collector.api.repo.TokenRepo;
import snagicky.collector.api.repo.UserRepo;

import java.util.Optional;
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

    // creates unverivied user (guest user - 0)

    // Password is in Header
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
    @PutMapping("/edit/")
    public ResponseEntity.BodyBuilder EditBio(
            @RequestHeader("token") UUID t,
            @RequestHeader(value = "bio",required = false) String bio,
            @RequestHeader(value = "name",required = false) String name,
            @RequestHeader(value = "user_id",required = false) Long id
    ){
        Token token = tr.TokenFromUUID(t);
        if (id == null || !token.EditLower)
            id = token.User.Id;
        if(token.EditSelf || token.EditLower){

            User usr = ur.findById(id).get();
            if(bio != null) usr.Bio = bio;
            if(name != null) usr.Name = name;

            ur.save(usr);
            return ResponseEntity.status(200);
        }
        return ResponseEntity.status(500);
    }
    // Verifies user from token
    public ResponseEntity.BodyBuilder Verify(UUID code) {
        try {
            User usr = tr.TokenFromUUID(code).User;
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

        visitor  = 0 (perrmissions as user1 but is deleted after a while, email less)
        user     = 1 (cant add test cards)
        User     = 2
        admin    = 3 (can edit just cards)
        Admin    = 4 (can manage lvl 1 profiles)
        Root     = 5 (have to be created through database)

        */
        final boolean x = false;
        final boolean o = true;
        final boolean[][] PerrmissionsLvl = {
        //      {1,2,3,4,5,6,7,8,9,0}
                {x,x,o,x,x,x,x,x,x,x},
                {x,x,o,x,x,x,x,x,x,x},
                {x,x,o,x,x,x,x,o,x,x},
                {x,x,o,x,x,x,o,o,o,x},
                {x,o,o,x,o,o,o,o,o,o},
                {x,o,o,x,o,o,o,o,o,o},
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

        return tr.save(ver);
    }
}
