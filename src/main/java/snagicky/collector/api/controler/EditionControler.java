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

    @PostMapping("/{Name}/")
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
    @PutMapping("/{id}/{name}/")
    public ResponseEntity.BodyBuilder DeleteEdition(
            @PathVariable("id") Long id,
            @PathVariable("name") String name,
            @RequestHeader() UUID code
    ) {
        if (tr.TokenFromUUID(code).CreateCards) {
            Edition e = er.findById(id).get();
            e.Name = name;
            er.save(e);
            return ResponseEntity.status(200);
        } else
            return ResponseEntity.status(403);
    }
    @DeleteMapping("/{id}/")
    public ResponseEntity.BodyBuilder DeleteEdition(
            @PathVariable("id") Long id,
            @RequestHeader() UUID code
    ) {
        if (tr.TokenFromUUID(code).CreateCards) {
            tr.deleteById(id);
            return ResponseEntity.status(200);
        } else
            return ResponseEntity.status(403);
    }
}

