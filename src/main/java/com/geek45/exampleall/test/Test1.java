package com.geek45.exampleall.test;

import com.alibaba.fastjson.JSONObject;
import com.geek45.commons.util.HttpClientUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 地铁接口调用报错，必要参数缺失，自己写一个main方法，测试一下，看看为什么会报错
 */
public class Test1 {

    public static void main(String[] args) {
        /**
         * curl -X POST
         * "http://localhost:9999/supply/api/queryVoucherStatus
         * ?orderNo=QY1840403350482429
         * &sign=e4b7e0b320193a57b9bc4a620c784b11
         * &supplyId=31
         * &timestamp=1592963831010
         * &voucherId=20200622001159283475078600000000
         * &voucherId=20200622001159283475083600000000
         * &voucherId=20200622001159283475123700000001
         * &voucherId=20200622001159283475123700000000
         * " -H "accept:
         */
        String url = "http://localhost:9999/supply/api/queryVoucherStatus";
        String orderNo = "QY1840403350482429";
        String sign = "e4b7e0b320193a57b9bc4a620c784b11";
        Integer supplyId = 31;
        String timestamp = "1592963831010";
        String[] voucherId = {"20200622001159283475078600000000", "20200622001159283475083600000000", "20200622001159283475123700000001", "20200622001159283475123700000000"};

        JSONObject data = new JSONObject();
        data.put("orderNo", orderNo);
        data.put("sign", sign);
        data.put("supplyId", supplyId);
        data.put("timestamp", timestamp);
        data.put("voucherId", voucherId);
        try {
            System.out.println(url);
            System.out.println(data.toJSONString());
            Map<String, String> head = new HashMap<>();
            head.put("Content-Type", "application/x-www-form-urlencoded");
            Map map = HttpClientUtil.doPost(url,head, data, 6000, 6000, "utf-8", false);
            String result = map.get("result").toString();
            System.out.println(result);

        } catch (Exception e) {

        }

    }

}
