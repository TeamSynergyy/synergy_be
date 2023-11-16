package com.seoultech.synergybe.domain.rate.dto.response;

import com.seoultech.synergybe.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserRateResponse {
    private String userId;
    private Double rateNumber;
    private Double temperature;

    public static UserRateResponse from(User user, double rateNumber) {
        return new UserRateResponse(user.getUserId(), rateNumber, user.getTemperature());
    }
}
