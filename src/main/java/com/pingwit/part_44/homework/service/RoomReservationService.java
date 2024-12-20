package com.pingwit.part_44.homework.service;

import com.pingwit.part_44.homework.repository.RoomReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomReservationService {
    private static final String ROOM_FREE = "r%df";
    private static final String ROOM_OCCUPIED = "r%dnf";

    private final RoomReservationRepository repository;

    public String getRoomHotelSchema() {

        return repository.getHotelRoomSchema();
    }

    public String reserveRoom(int roomNumber) {
        String hotelRoomSchema = repository.getHotelRoomSchema();
        String freeRoomInfo = String.format(ROOM_FREE, roomNumber);
        String occupiedRoomInfo = String.format(ROOM_OCCUPIED, roomNumber);

        if (hotelRoomSchema.contains(occupiedRoomInfo)) {
            return "This room is already occupied,pleas booking another one.Danke\n" + hotelRoomSchema;
        }

        hotelRoomSchema = hotelRoomSchema.replace(freeRoomInfo, occupiedRoomInfo);
        return repository.save(hotelRoomSchema);

    }

    public String cancelReservationRoom(int roomNumber) {
        String hotelRoomSchema = repository.getHotelRoomSchema();
        String freeRoomInfo = String.format(ROOM_FREE, roomNumber);
        String occupiedRoomInfo = String.format(ROOM_OCCUPIED, roomNumber);

        if (hotelRoomSchema.contains(freeRoomInfo)) {
            return "This room is already free to reserve\n" + hotelRoomSchema;
        }

        hotelRoomSchema = hotelRoomSchema.replace(occupiedRoomInfo, freeRoomInfo);
        return repository.save(hotelRoomSchema);
    }
}