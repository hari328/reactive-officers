package com.harish.officers.controller;

import com.harish.officers.dao.OfficerRepository;
import com.harish.officers.entity.Officer;
import com.harish.officers.entity.Rank;
import com.sun.tools.javac.util.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

//todo: read - you actually dont need to point to the controller class here. https://docs.spring.io/spring/docs/current/spring-framework-reference/pdf/testing-webtestclient.pdf

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OfficersControllerTest {

  @Autowired
  private WebTestClient testClient;

  @Autowired
  private OfficerRepository repository;

  @Before
  public void setUp() {
    repository.deleteAll()
      .thenMany(Flux.just(new Officer(Rank.ADMIRAL, "harish", "bla"), new Officer(Rank.ADMIRAL, "Anurag", "samak")))
      .flatMap(repository::save)
      .then();
  }

  @Test
  public void getAllOfficers() {
    testClient.get().uri("/officers")
      .exchange()
      .expectStatus().isOk()
      .expectBodyList(Officer.class)
      .hasSize(2)
      .consumeWith(System.out::println);
  }

  @Test
  public void getOfficerById() {
    final String firstName = "girish";
    final Officer createdOffier = repository.save(new Officer(Rank.ADMIRAL, firstName, "venkatachaliah")).block();

    final EntityExchangeResult<Officer> returnedValue = testClient.get().uri("/officers" + "/" + createdOffier.getId())
      .exchange()
      .expectStatus().isOk()
      .expectBody(Officer.class)
      .returnResult();

    assertEquals(firstName, returnedValue.getResponseBody().getFirst());
  }


  @Test
  public void insertOfficerBy() {
    final String firstName = "girish";
    final String lastName = "venkatachaliah";

    testClient.post().uri("/officers")
      .header("x-id", "445")
      .bodyValue(new Officer(Rank.ADMIRAL, firstName, lastName))
      .exchange()
      .expectStatus().isCreated()
      .expectBody()
      .consumeWith(System.out::println);
  }

}