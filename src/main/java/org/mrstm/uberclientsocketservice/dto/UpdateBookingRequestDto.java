package org.mrstm.uberclientsocketservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookingRequestDto {
    private Long driverId;
    private String bookingStatus;
}
