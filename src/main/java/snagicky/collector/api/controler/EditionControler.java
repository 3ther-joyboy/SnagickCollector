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

    @GetMapping("/")
    public Iterable<Edition> Get(
            @RequestParam(required = false,name = "id") Long id,
            @RequestParam(required = false,name = "name") String name,
            @RequestParam(required = false,name = "description") String des,
            @RequestParam(required = false,name = "page",defaultValue = "0") Integer page,
            @RequestParam(required = false,name = "scroll",defaultValue = "25") Integer scroll
    ){
        return er.FindEdition(id,name,des,page,scroll);
    }
    @PostMapping("/{Name}/")
    public Edition CreateEdition(
            @PathVariable("Name") String name,
            @RequestHeader("token") UUID code
    ) {
        if (tr.findById(code).get().CreateCards) {
            Edition obj = new Edition();
            obj.Name = name;
            return er.save(obj);
        } else
            return null;
    }
    @PutMapping("/{id}/{name}/")
    public Edition RenameEdition(
            @PathVariable("id") Long id,
            @PathVariable("name") String name,
            @RequestHeader("token") UUID code,
            @RequestBody String dis
    ) {
        if (tr.findById(code).get().CreateCards) {
            Edition e = er.findById(id).get();
            e.Name = name;
            e.Description = dis;
            return er.save(e);
        } else
            return null;
    }
    @DeleteMapping("/{id}/")
    public boolean DeleteEdition(
            @PathVariable("id") Long id,
            @RequestHeader("token") UUID code
    ) {
        if (tr.findById(code).get().CreateCards) {
            er.deleteById(id);
            return true;
        } else
            return false;
    }
}

