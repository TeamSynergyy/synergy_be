package com.seoultech.synergybe.domain.rate.dto.response;

import com.seoultech.synergybe.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.text.DecimalFormat;

@Builder
public record UserRateResponse(
        String userId,
        Double rateNumber,
        Double temperature
) {

//    public static UserRateResponse from(User user, double rateNumber, double temperature) {
//        DecimalFormat df = new DecimalFormat("#.#");
//        return new UserRateResponse(user.getUserId(), rateNumber, Double.parseDouble(df.format(temperature)));
//    }
}
