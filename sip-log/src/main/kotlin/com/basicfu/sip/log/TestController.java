package com.basicfu.sip.log;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author basicfu
 * @date 2018/5/24
 */
@RestController
public class TestController {
    @GetMapping("/test")
    public void test(){
        System.out.println("123");
    }
}
