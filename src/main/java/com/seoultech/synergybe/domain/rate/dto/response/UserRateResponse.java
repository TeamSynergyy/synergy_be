package com.seoultech.synergybe.domain.rate.dto.response;

import com.seoultech.synergybe.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.text.DecimalFormat;

@AllArgsConstructor
@Getter
public class UserRateResponse {
    private String userId;
    private Double rateNumber;
    private Double temperature;

    public static UserRateResponse from(User user, double rateNumber, double temperature) {
        DecimalFormat df = new DecimalFormat("#.#");
        return new UserRateResponse(user.getUserId(), rateNumber, Double.parseDouble(df.format(temperature)));
    }
}
