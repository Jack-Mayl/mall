package com.longzai.mall;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;

@SpringBootTest
class MallApplicationTests {

    @Test
    void contextLoads() {
        int dayOfWeek = Calendar.DAY_OF_MONTH;
        System.out.println(dayOfWeek);
    }

}
