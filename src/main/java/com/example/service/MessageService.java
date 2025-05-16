package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.exception.InvalidMessageException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {   
    private MessageRepository messageRepository;
    private AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public Message createMessage(Message message) {
        if(message.getMessageText() == null || message.getMessageText().isBlank()) {
            throw new InvalidMessageException("Message text cannot be blank.");
        }
        if(message.getMessageText().length() > 255) {
            throw new InvalidMessageException("Message text cannot exceed 255 characters.");
        }
        if(message.getPostedBy() == null || !accountRepository.existsById(message.getPostedBy())) {
            throw new InvalidMessageException("PostedBy must refer to an existing user.");
        }

        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Optional<Message> getMessageById(Integer id) {
        return messageRepository.findById(id);
    }

    public boolean deleteMessageById(Integer id) {
        if(messageRepository.existsById(id)) {
            messageRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public int updateMessage(Integer id, String newMessageText) {
        if(newMessageText == null || newMessageText.isBlank()) {
            throw new InvalidMessageException("Message text cannot be blank.");
        }
        if(newMessageText.length() > 255) {
            throw new InvalidMessageException("Message text cannot exceed 255 characters.");
        }

        Message toUpdate = messageRepository.findById(id)
                        .orElseThrow(() -> new InvalidMessageException("Message with ID " + id + " does not exists."));
        
        toUpdate.setMessageText(newMessageText);

        messageRepository.save(toUpdate);

        return 1;
    }

    public List<Message> getMessagesByAccountId(Integer id) {
        return messageRepository.findByPostedBy(id);
    }
}
