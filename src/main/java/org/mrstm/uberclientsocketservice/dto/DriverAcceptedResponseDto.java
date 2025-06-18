package org.mrstm.uberclientsocketservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverAcceptedResponseDto {
    private Boolean response;
    private Long bookingId;
}
