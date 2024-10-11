package com.trabean.travel.callApi.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberInfoApiResponseDto {

    private Long userId;
    private Long memberCount;
    private List<MemberDetail> members;

    @Getter
    public static class MemberDetail {
        private Long userId;
        private String userName;
        private String role;
    }

}
