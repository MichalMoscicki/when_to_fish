package com.moscicki.user_service.controller;


import com.moscicki.user_service.dto.user.UserDTO;
import com.moscicki.user_service.entities.user.User;
import com.moscicki.user_service.manager.UserManager;
import com.moscicki.user_service.translator.UserTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/users")
@RestController
public class UserController {

    @Autowired
    UserManager userManager;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTOParam) {
        User user = userManager.createUser(UserTranslator.translate(userDTOParam));
        return new ResponseEntity<>(UserTranslator.translate(user), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String id) {
        User user = userManager.getUser(id);
        return new ResponseEntity<>(UserTranslator.translate(user), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String id, @RequestBody UserDTO userDTOParam) {
        User user = userManager.updateUser(id, UserTranslator.translate(userDTOParam));
        return new ResponseEntity<>(UserTranslator.translate(user), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userManager.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}