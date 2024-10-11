package com.trabean.account.dto.request;

import com.trabean.account.domain.UserAccountRelation;
import lombok.Getter;

@Getter
public class UpdateUserRoleRequestDTO {
    private Long userId;
    private Long accountId;
    private UserAccountRelation.UserRole userRole;
}
