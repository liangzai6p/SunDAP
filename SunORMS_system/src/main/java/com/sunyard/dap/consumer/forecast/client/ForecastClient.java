package com.sunyard.dap.consumer.forecast.client;

import com.sunyard.dap.common.model.ReturnT;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

@Component
@FeignClient("sundap-forecast")
public interface ForecastClient {
    /**
     * 拟合运算
     **/
    @PostMapping("/fitting/lineFitting")
    ReturnT<List> getFittingLineFitting(String params);

    @PostMapping("/fitting/polyFitting")
    ReturnT<List> getFittingPolyFitting(String params);

    @PostMapping("/fitting/contourFitting")
    ReturnT<Map> getFittingContourFitting(String params);

    @PostMapping("/fitting/onLineFitting")
    ReturnT<Map> getFittingOnLineFitting(String params);

    @PostMapping("/fitting/annualLineFitting")
    ReturnT<Map> getFittingAnnualLineFitting(String params);

    @PostMapping("/fitting/standardDeviation")
    ReturnT<String> getFittingStandardDeviation(String params);



}
