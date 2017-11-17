package com.lenovo.highavailablecommon;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * Created by root on 2017/11/16.
 */
public class Subscripter {


public void start(){
    System.out.print("startstartstartstartstartstartstart");
}

    public void stop(){
        System.out.print("stopstopstopstopstopstopstopstop");
    }


public String runSubscripterBussiness(Object _ThreadLockFlag,Map<String,String> mapForRabbitMQ){

    //synchronized (_ThreadLockFlag){

        System.out.println("--->>>>_ThreadLockFlag.toString()"+_ThreadLockFlag.toString());

        if(null != mapForRabbitMQ.get("ThreadLetter")){
            JSONObject JSONObjectThreadLetter = (JSONObject) JSONObject.parse(mapForRabbitMQ.get("ThreadLetter"));
            System.out.println("JSONObjectThreadLetter.toJSONString()--->>>>>"+JSONObjectThreadLetter.toJSONString());
        }else {
            System.err.println("null is mapForRabbitMQ.get(\"ThreadLetter\")");
        }
    //}


return "success";

}


}
