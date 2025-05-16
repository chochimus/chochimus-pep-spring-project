package com.example.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@Controller
public class SocialMediaController {
    private AccountService accountService;
    private MessageService messageService;

    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("/register")
    public @ResponseBody ResponseEntity<Account> registerUser(@RequestBody Account newAccount){
        Account saved = accountService.registerUser(newAccount);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/login")
    public @ResponseBody ResponseEntity<Account> loginUser(@RequestBody Account account) {
        Account loggedIn = accountService.login(account);
        return ResponseEntity.ok(loggedIn);
    }

    @PostMapping("/messages")
    public @ResponseBody ResponseEntity<Message> createMessage(@RequestBody Message newMessage) {
        Message saved = messageService.createMessage(newMessage);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/messages")
    public @ResponseBody ResponseEntity<List<Message>> getMessages() {
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/messages/{message_id}")
    public @ResponseBody ResponseEntity<Message> getMessageById(@PathVariable Integer message_id){
        Optional<Message> message = messageService.getMessageById(message_id);
        
        if(message.isPresent()) {
            return ResponseEntity.ok(message.get());
        } else {
            return ResponseEntity.ok().build();
        }
    }

    @DeleteMapping("/messages/{message_id}")
    public @ResponseBody ResponseEntity<Integer> deleteMessageById(@PathVariable Integer message_id){
        boolean deleted = messageService.deleteMessageById(message_id);

        if(deleted) {
            return ResponseEntity.ok(1);
        } else {
            return ResponseEntity.ok().build();
        }
    }

    @PatchMapping("/messages/{message_id}")
    public @ResponseBody ResponseEntity<Integer> updateMessage(@PathVariable Integer message_id, @RequestBody Message messageUpdate) {
        int rowsUpdated = messageService.updateMessage(message_id, messageUpdate.getMessageText());

        return ResponseEntity.ok(rowsUpdated);
    }

    @GetMapping("/accounts/{account_id}/messages")
    public @ResponseBody ResponseEntity<List<Message>> getAllMessagesFromUser(@PathVariable Integer account_id) {
        List<Message> messages = messageService.getMessagesByAccountId(account_id);
        return ResponseEntity.ok(messages);
    }
}
