package com.harish.officers.controller;

import com.harish.officers.dao.OfficerRepository;
import com.harish.officers.entity.Officer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/officers")
public class OfficersController {

  @Autowired
  OfficerRepository officerRepository;

  @GetMapping("")
  public Flux<Officer> getAll() {
    return officerRepository.findAll();
  }

  @GetMapping("/{id}")
  public Mono<Officer> getOfficer(@PathVariable String id) {
    return officerRepository.findById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<Officer> createOfficer(Officer officer, @RequestHeader("x-id") int id) {
    System.out.println(id);
    return officerRepository.save(officer);
  }

}
