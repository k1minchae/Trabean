package com.trabean.travel.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TargetAmountListResponseDto {

    private Long targetAmount;
    private Double amount;
    private List<MemberInfo> memberList;

    @Getter
    @Builder
    public static class MemberInfo {
        private Long userId;
        private String userName;
        private String role;
        private Double amount;
        private Long mainAccountId;
        private String mainAccountNumber;
    }

}
