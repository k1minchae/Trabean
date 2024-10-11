package com.trabean.travel.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InvitaionRequestDto {

    private String email;
    private Long accountId;

    public static InvitaionRequestDto from(String email, Long accountId) {
        return new InvitaionRequestDto(email, accountId);
    }

}
