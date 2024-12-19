package com.pingwit.part_41;

import com.pingwit.part_41.entity.User;
import com.pingwit.part_41.repository.UserRepository;
import com.pingwit.part_41.service.DatabaseConnectorService;

import java.util.List;

public class UserRepositoryExample {
    public static void main(String[] args) {
        DatabaseConnectorService databaseConnectorService = new DatabaseConnectorService();

        UserRepository userRepository = new UserRepository(databaseConnectorService);

        List<User> users = userRepository.findAll();
        users.forEach(System.out::println);
    }
}
