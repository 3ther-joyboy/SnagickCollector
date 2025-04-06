package snagicky.collector.api.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import snagicky.collector.api.model.Token;
import snagicky.collector.api.model.User;
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

    // creates unverivied user (guest user - 0)
    @PutMapping("/{name}/{password}")
    public ResponseEntity<UUID> CreateUser(@PathVariable("name") String name, @PathVariable("password") String password, @RequestParam(value = "email", defaultValue = "", required = false) String email) {
        try {
            User us = new User();
            us.Name = name;

            User CreatedUser = ur.save(us);
            CreatedUser.Password = CreatedUser.Salt(password);

            if (!email.isEmpty()) {
                // TODO sends email with verivication
                CreatedUser.Email = email;
            }

            return ResponseEntity.ok(UserLoginToken(ur.save(CreatedUser)).Code);
        } catch (Exception e) {
            return ResponseEntity.ofNullable(UUID.randomUUID());
        }
    }

    // Links login token to given user
    public Token UserLoginToken(User usr) {
        Token ver = new Token();
        ver.User = usr;
        return tr.save(ver);
    }
}
