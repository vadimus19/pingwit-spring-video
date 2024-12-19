package com.pingwit.part_41;

import com.pingwit.part_41.entity.User;
import com.pingwit.part_41.repository.UserRepository;
import com.pingwit.part_41.service.DatabaseConnectorService;

public class PreparedStatementInsertTransactionExample {
    public static void main(String[] args) {
        DatabaseConnectorService databaseConnectorService = new DatabaseConnectorService();

        UserRepository userRepository = new UserRepository(databaseConnectorService);

        User user = new User();
        user.setName("Oliwer");
        user.setAge(32);
        user.setRating(5.0);
        user.setPayment(177.0);
        user.setActive(true);
        user.setCountry("USA");
        user.setCurrency("usd");
        user.setBio("Oliwer is here!");

        userRepository.insert(user, false);
    }
}
