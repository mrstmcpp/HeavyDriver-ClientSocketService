package org.mrstm.uberclientsocketservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverNewLocationDto {
    private Long driverId;
    private Double latitude;
    private Double longitude;
}
