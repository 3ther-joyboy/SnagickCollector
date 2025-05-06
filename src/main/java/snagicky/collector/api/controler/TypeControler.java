package snagicky.collector.api.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import snagicky.collector.api.model.SubType;
import snagicky.collector.api.model.Token;
import snagicky.collector.api.model.Type;
import snagicky.collector.api.repo.SubTypeRepo;
import snagicky.collector.api.repo.TokenRepo;
import snagicky.collector.api.repo.TypeRepo;

import java.util.UUID;

@RestController()
@RequestMapping("/api/")
public class TypeControler {

    @Autowired
    TokenRepo tr;
    @Autowired
    TypeRepo Tr;
    @Autowired
    SubTypeRepo sr;

    @GetMapping("type/")
    public Iterable<Type> GetType(
        @RequestParam(required = false,name = "id") Long id,
        @RequestParam(required = false,name = "name") String name,
        @RequestParam(required = false,name = "sub_type") Long sub,
        @RequestParam(required = false,name = "page",defaultValue = "0") Integer page,
        @RequestParam(required = false,name = "scroll",defaultValue = "25") Integer scroll
    ) {
       return Tr.FindType(id,name,sub,page,scroll);
    }
    @GetMapping("subtype/")
    public Iterable<SubType> GetSubType(
            @RequestParam(required = false,name = "id") Long id,
            @RequestParam(required = false,name = "name") String name,
            @RequestParam(required = false,name = "description") String des,
            @RequestParam(required = false,name = "page",defaultValue = "0") Integer page,
            @RequestParam(required = false,name = "scroll",defaultValue = "25") Integer scroll
    ) {
        return sr.FindSubType(id,name,des,page,scroll);
    }
    @PostMapping("subtype/{Name}")
    public ResponseEntity.BodyBuilder AddSub(
            @PathVariable("Name") String name,
            @RequestHeader("token") UUID token,
            @RequestHeader("description") String des
    ){
        if(tr.existsById(token)) {
            Token t = tr.findById(token).get();
            if (t.CreateCards) {
                SubType s = new SubType();
                s.Name = name;
                s.Description = des;

                sr.save(s);
                return ResponseEntity.status(200);
            }
        }
        return ResponseEntity.status(500);
    }
    @PostMapping("type/{Name}/{sub}")
    public ResponseEntity.BodyBuilder AddType(
            @PathVariable("Name") String name,
            @RequestHeader("token") UUID token,
            @PathVariable("sub") Long sub
    ){
        if(tr.existsById(token)) {
            Token t = tr.findById(token).get();
            if (t.CreateCards) {
                Type typ = new Type();
                typ.Name = name;
                typ.subType = sr.findById(sub).get();

                Tr.save(typ);
                return ResponseEntity.status(200);
            }
        }
        return ResponseEntity.status(500);
    }
    @DeleteMapping("subtype/{id}")
    public ResponseEntity.BodyBuilder RemoveSub(
            @PathVariable("id") Long id,
            @RequestHeader("token") UUID token
    ){
        if(tr.existsById(token)) {
            Token t = tr.findById(token).get();
            if (t.CreateCards) {

                sr.deleteById(id);
                return ResponseEntity.status(200);
            }
        }
        return ResponseEntity.status(500);
    }
    @DeleteMapping("type/{id}")
    public ResponseEntity.BodyBuilder RemoveType(
            @PathVariable("id") Long id,
            @RequestHeader("token") UUID token
    ){
        if(tr.existsById(token)) {
            Token t = tr.findById(token).get();
            if (t.CreateCards) {
                Tr.deleteById(id);
                return ResponseEntity.status(200);
            }
        }
        return ResponseEntity.status(500);
    }

}
