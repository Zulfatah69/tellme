package com.tellme.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tellme.model.Status;
import com.tellme.repository.StatusRepository;

@RestController
@RequestMapping("/api/status")
public class StatusController {

    @Autowired
    private StatusRepository statusRepository;

    @GetMapping
    public List<Status> getAll() {
        return statusRepository.findAll();
    }

    @PostMapping
    public Status create(@RequestBody Status s) {
        return statusRepository.save(s);
    }

    @PutMapping("/{id}")
    public Status update(@PathVariable Long id, @RequestBody Status s) {
        Status data = statusRepository.findById(id).orElseThrow();
        data.setNamaStatus(s.getNamaStatus());
        return statusRepository.save(data);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        statusRepository.deleteById(id);
    }
}