package com.vsu.events_spring.entity;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Profile {
    private Long id_profile;
    private String full_name;
    private String username;
    private String password;
    private String email;
    private LocalDate date_of_birth;
    private String profile_coordinates;
    private Integer balance;
}
