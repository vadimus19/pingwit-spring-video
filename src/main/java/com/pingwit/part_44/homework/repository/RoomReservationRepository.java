package com.pingwit.part_44.homework.repository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RoomReservationRepository {

    private String hotelRoomSchema;

    @PostConstruct

    private void init() throws IOException {
        List<String> hotelRoomSchemaLines = Files.readAllLines(Path.of("src/main/resources/homework_44/hotelRooms.txt"));
        hotelRoomSchema = String.join("\n", hotelRoomSchemaLines);
    }

    public String getHotelRoomSchema() {

        return hotelRoomSchema;
    }

    public String save(String hotelRoomSchema) {
        this.hotelRoomSchema = hotelRoomSchema;
        return getHotelRoomSchema();
    }
}
