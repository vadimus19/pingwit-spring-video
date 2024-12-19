package com.pingwit.part_41;

import com.pingwit.part_41.entity.User;
import com.pingwit.part_41.repository.UserRepository;
import com.pingwit.part_41.service.DatabaseConnectorService;

import java.util.List;

public class PreparedStatementInsertBatchExample {
    public static void main(String[] args) {
        DatabaseConnectorService databaseConnectorService = new DatabaseConnectorService();

        UserRepository userRepository = new UserRepository(databaseConnectorService);

        User jack = new User();
        jack.setName("Jack");
        jack.setAge(32);
        jack.setRating(5.0);
        jack.setPayment(177.0);
        jack.setActive(true);
        jack.setCountry("USA");
        jack.setCurrency("usd");
        jack.setBio("Jack is here!");

        User oliwia = new User();
        oliwia.setName("Oliwia");
        oliwia.setAge(32);
        oliwia.setRating(5.0);
        oliwia.setPayment(177.0);
        oliwia.setActive(true);
        oliwia.setCountry("USA");
        oliwia.setCurrency("usd");
        oliwia.setBio("Oliwia is here!");

        List<User> users = List.of(jack, oliwia);

        userRepository.insert(users);
    }
}
