package com.sunyard;

import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.forecast.service.FittingService;
import com.sunyard.dap.forecast.service.impl.FittingServiceImpl;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
/**
 * @program: SunDAP
 * @description: 测试类
 * @author: yey.he
 * @create: 2020-09-11 14:01
 **/
@SpringBootTest
public class ForecastTests {
    @Test
    public void testLineFits(){
        FittingService service = new FittingServiceImpl();
        String param = "{\n" +
                "    \"X-AXIS\":[3,4,5,6,7,8,9],\n" +
                "    \"Y-AXIS\":[3,4,5,6,7,8,9]\n" +
                "}";
        ReturnT<List> stringReturnT = service.lineFitting(param);
        System.out.println(stringReturnT.toString());
    }

    @Test
    public void testPolyFits(){
        FittingService service = new FittingServiceImpl();
        String param = "{\n" +
                "    \"TIMES\":3,\n" +
                "    \"X-AXIS\":[3,4,5,6,7,8,9],\n" +
                "    \"Y-AXIS\":[3,4,5,6,7,8,9]\n" +
                "}";
        ReturnT<List> stringReturnT = service.polyFitting(param);
        System.out.println(stringReturnT.toString());
    }

    @Test
    public void testStandardDeviation(){
        FittingService service = new FittingServiceImpl();
        String param = "{\n" +
                "    \"VALUES\":[3,4,5,6,7,8]\n" +
                "}\n";
        ReturnT<String> stringReturnT = service.standardDeviation(param);
        System.out.println(stringReturnT.toString());
    }
}
