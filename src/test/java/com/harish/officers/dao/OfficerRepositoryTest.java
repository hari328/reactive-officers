package com.harish.officers.dao;

import com.harish.officers.entity.Officer;
import com.harish.officers.entity.Rank;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OfficerRepositoryTest {

  @Autowired
  private OfficerRepository officerRepository;

  private List<Officer> officers = Arrays.asList(
    new Officer(Rank.ADMIRAL, "James", "Krik"),
    new Officer(Rank.ADMIRAL, "Benjamin", "Franklin"),
    new Officer(Rank.ADMIRAL, "Ross", "Ross"),
    new Officer(Rank.ADMIRAL, "Sandor", "Clagane"),
    new Officer(Rank.ADMIRAL, "James", "Lanister")
    );


  @Before
  public void setUp() {
    officerRepository.deleteAll()
      .thenMany(Flux.fromIterable(officers))
      .flatMap(officerRepository::save)
      .then()
      .block();
  }

  @Test
  public void saveOfficer() {
    Officer harish = new Officer(Rank.CAPTAIN, "harish", "Sarapalle");
    StepVerifier.create(officerRepository.save(harish))
      .expectNextMatches(officer -> !officer.getId().equals(""))
      .verifyComplete();
  }

  @Test
  public void findAllOfficers(){
      StepVerifier.create(officerRepository.findAll())
        .expectNextCount(5L);
  }

  @Test
  public void findByRank() {
    Officer harish = new Officer(Rank.CAPTAIN, "harish", "Sarapalle");
    StepVerifier.create(officerRepository.save(harish))
      .expectNextMatches(officer -> !officer.getId().equals(""))
      .verifyComplete();

    StepVerifier.create(officerRepository.findByRank(Rank.CAPTAIN))
      .expectNextCount(1L);
  }
}