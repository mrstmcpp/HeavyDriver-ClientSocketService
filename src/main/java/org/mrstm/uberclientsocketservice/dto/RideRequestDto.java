package org.mrstm.uberclientsocketservice.dto;

import lombok.*;
import org.mrstm.uberclientsocketservice.models.ExactLocation;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RideRequestDto {
    private Long passengerId;
//    private ExactLocation startLocation;
//    private ExactLocation endLocation;
    private List<Long> driverIds;
    private Long bookingId;
}
