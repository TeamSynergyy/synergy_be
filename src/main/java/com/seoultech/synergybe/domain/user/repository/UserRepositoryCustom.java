package com.seoultech.synergybe.domain.user.repository;

import com.seoultech.synergybe.domain.user.dto.response.GetUserAccountResponse;

public interface UserRepositoryCustom {
    GetUserAccountResponse findUserAccountByEmail(String email);
}
