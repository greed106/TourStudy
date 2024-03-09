package com.ymj.tourstudy;

import com.alibaba.fastjson.JSON;
import com.ymj.tourstudy.utils.HuffmanResult;
import com.ymj.tourstudy.utils.HuffmanUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TourStudyApplicationTests {


    @Test
    void contextLoads() {
        String str = "i like like like java do you like a java";
        HuffmanResult result = HuffmanUtils.encode(str);
        String s = JSON.toJSONString(result);
        HuffmanResult huffmanResult = JSON.parseObject(s, HuffmanResult.class);
        String decode = HuffmanUtils.decode(huffmanResult);
        System.out.println("解码后的字符串：" + decode);

    }

}
