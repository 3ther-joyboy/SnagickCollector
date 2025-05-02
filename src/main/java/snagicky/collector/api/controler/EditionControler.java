package snagicky.collector.api.controler;

import jakarta.websocket.server.PathParam;
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

    @PostMapping("/create/{Name}")
    public ResponseEntity.BodyBuilder CreateEdition(
            @PathVariable("Name") String name,
            @RequestHeader() UUID code
    ) {
        if (tr.TokenFromUUID(code).CreateCards) {
            Edition obj = new Edition();
            obj.Name = name;
            er.save(obj);
            return ResponseEntity.status(200);
        } else
            return ResponseEntity.status(403);
    }
}

