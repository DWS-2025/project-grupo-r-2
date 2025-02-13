package com.spartanwrath.restController;

import com.spartanwrath.exceptions.NoMembership;
import com.spartanwrath.exceptions.NoSuchMem;
import com.spartanwrath.model.Membership;
import com.spartanwrath.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Membership")
public class MembershipRestController {

    @Autowired
    private MembershipService membershipService;

    @GetMapping("")
    public ResponseEntity<List<Membership>> getAllMembership(){
        List<Membership> membership = membershipService.findAll();
        if (membership.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(membership);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Membership> getMembership(@PathVariable long id) throws NoSuchMem {
        try {
            Membership membership = membershipService.findById(id);
            return ResponseEntity.ok().body(membership);
        }catch (NoSuchMem noSuchMem){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/new")
    public ResponseEntity<Membership> newMembership(@RequestBody Membership membership){
        membershipService.save(membership);
        return ResponseEntity.ok().body(membership);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Membership> deleteMem(@PathVariable long id){
        membershipService.delete(id);
        return ResponseEntity.ok().build();
    }

}
