package com.trabean.travel.callApi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExchangeRateOpenApiResponseDto {

    private Integer result;
    private String cur_unit;
    private String ttb;
    private String tts;
    private String deal_bas_r;
    private String bkpr;
    private String yy_efee_r;
    private String ten_dd_efee_r;
    private String kftc_bkpr;
    private String kftc_deal_bas_r;
    private String cur_nm;

}
