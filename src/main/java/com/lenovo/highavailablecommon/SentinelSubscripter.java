package com.lenovo.highavailablecommon;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * Created by root on 2017/11/15.
 */
public class SentinelSubscripter implements Runnable{

    public Object _ThreadLockFlag;

    public Map<String,String> mapForRabbitMQ = null;



    static {
        CommonTools.InitLog4jConfig();
    }

    public SentinelSubscripter(Object _ThreadLockFlag,Map<String,String> mapForRabbitMQ){
        this._ThreadLockFlag = _ThreadLockFlag;
        this.mapForRabbitMQ = mapForRabbitMQ;
    }


    @Override
    public void run() {

        synchronized(_ThreadLockFlag){

            while (true){

                String _ThreadLetter = mapForRabbitMQ.get("ThreadLetter");

                if(_ThreadLetter == null || _ThreadLetter.equals("")) {

                    try {

                        _ThreadLockFlag.wait();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }else{

                    System.out.println("_ThreadLetter--->>>>>>>>>>"+_ThreadLetter);

                    mapForRabbitMQ.put("ThreadLetter",null);
                    JSONObject _JSONObject_ThreadLetter = (JSONObject) JSONObject.parse(_ThreadLetter);
                    String role = _JSONObject_ThreadLetter.getString("role");

                    Subscripter _Subscripter = new Subscripter();

                    if(null != role && role.equals("master")){
                        _Subscripter.start();
                    }

                    if(null != role && role.equals("standby")){
                        _Subscripter.stop();
                    }





                }

                System.out.println("wait success----->>>>>.");
            }





//            if(null !=_ThreadLetter){
//                for(int i = 0 ; i < 100 ; i++){
//                    System.out.println("_ThreadLetter--->>>>>"+_ThreadLetter);
//                }
//            }
        }


    }


//    public String callOneThreadToLive__SentinelSubscripter(){
//
//        (new Thread(new SentinelSubscripter(_ThreadLockFlag,mapForRabbitMQ))).start();
//
//        System.out.println("--->>>>already callOneThreadToLive__SentinelSubscripter");
//
//        return "success";
//    }







//    public String callOneThreadToLiveETCDSentinel(){
//        return "true";
//    }
}
