package snagicky.collector.api.controler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import snagicky.collector.api.model.*;

import java.util.HashSet;

@RestController()
@RequestMapping("/test")
public class TestRestControler {
    @GetMapping("/")
    public String test() {
        return "working!";
    }
    @GetMapping("/edition/")
    public Edition e(){return getedition(1L);}
    public Edition getedition(Long i){
        Edition e = new Edition();
        e.Id=i;
        e.Name= "Humorous " + i;
        return e;
    }
    @GetMapping("/subtype/")
    public SubType getsubtype(){
        SubType t = new SubType();
        t.Id=1L;
        t.Name="Bytosti";
        t.Description="Bla bla mohou útočit";
        return t;
    }
    @GetMapping("/type/")
    public Type gettype(){
        Type t = new Type();
        t.Id=1L;
        t.Name="Žouželi";
        t.subType = getsubtype();
        return t;
    }
    @GetMapping("/card/")
    public Card getcard(){
        Card c = new Card();
        c.Id = 1L;
        c.Attack = 1;
        c.Defense = 1;
        c.Name = "Divoký žoužel";
        c.Description = "Je fakt divoký";
        c.Rarity="c";
        c.Story=":3";

        c.type = gettype();

        c.Editions=new HashSet<>();
        c.Editions.add(getedition(1L));
        c.ByUser=getuser();

        return c;
    }
    @GetMapping("/user/")
    public User getuser(){
        User u = new User();
        u.Perrmission=3;
        u.Bio="I love playing snagicky";
        u.name="Cipísek";

        return u;
    }
}
