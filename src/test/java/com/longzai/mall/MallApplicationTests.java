package com.longzai.mall;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;

@SpringBootTest
class MallApplicationTests {

    @Test
    void contextLoads() {
        Integer i=222;
        Double i2=222.0;
        boolean equals = i.equals(i2);
        System.out.println(equals);
    }

}
