package snagicky.collector.api.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import snagicky.collector.api.model.Edition;
import snagicky.collector.api.repo.EditionRepo;
import snagicky.collector.api.repo.TokenRepo;

import java.util.UUID;

@RestController()
@RequestMapping("/api/edition")
public class EditionControler {

    @Autowired
    TokenRepo tr;
    @Autowired
    EditionRepo er;

    @PostMapping("/")
    public ResponseEntity.BodyBuilder CreateEdition(@RequestBody() Edition obj, @RequestParam UUID code) {
        if (tr.TokenFromUUID(code).CreateCards) {
            er.save(obj);
            return ResponseEntity.status(200);
        } else
            return ResponseEntity.status(403);
    }
}

