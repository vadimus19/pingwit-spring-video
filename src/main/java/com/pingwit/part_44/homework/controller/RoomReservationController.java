package com.pingwit.part_44.homework.controller;

import com.pingwit.part_44.homework.service.RoomReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservation/room")
@RequiredArgsConstructor
public class RoomReservationController {

    private final RoomReservationService roomReservationService;

    @GetMapping
    public String getRoomHotelSchema() {

        return roomReservationService.getRoomHotelSchema();
    }

    @PutMapping("/{roomNumber}")
    public String reserveRoom(@PathVariable int roomNumber) {
        return roomReservationService.reserveRoom(roomNumber);
    }

    @PutMapping("/cancel/{roomNumber}")
    public String cancelReservation(@PathVariable int roomNumber) {
        return roomReservationService.cancelReservationRoom(roomNumber);
    }

}
