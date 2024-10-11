package com.trabean.notification.api.feign.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserKeyReq {
    private Long userId;
}
