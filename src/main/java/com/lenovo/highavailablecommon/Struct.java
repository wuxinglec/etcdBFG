package com.lenovo.highavailablecommon;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 2017/11/7.
 */
public class Struct {

    public static void main(String[] args){


        generateData();





    }




    public static JSONObject generateData(){

        JSONObject _JSONObjectResult = new JSONObject();

        JSONArray _JSONArrayMasterKV = new JSONArray();

        JSONObject _JSONObject_MasterKV1 = new JSONObject();

        _JSONObject_MasterKV1.put("alianamekey","foo");
        //_JSONObject_MasterKV1.put("urladdress","192.168.1.07:8080");

        JSONObject _JSONObject4urladdressVAL1 = new JSONObject();
        List<String> list4Value1 = new ArrayList<String>();
        list4Value1.add("192.168.1.07");
        list4Value1.add("8080");
        list4Value1.add("topname/device/iot1");
        list4Value1.add("$MOC/device/test_deviceid_06");

        _JSONObject4urladdressVAL1.put("valueArray",list4Value1);

        _JSONObject4urladdressVAL1.put("selectedIndex",3);//-1 全部

        _JSONObject_MasterKV1.put("urladdress",_JSONObject4urladdressVAL1);

        _JSONObject_MasterKV1.put("ttl","20");
        _JSONObject_MasterKV1.put("ttlinterval","10");
        _JSONObject_MasterKV1.put("keyRangStart","foo");
        _JSONObject_MasterKV1.put("keyRangEnd","fooz");
        _JSONObject_MasterKV1.put("masterinvokeshell","/data/mqttsubscriber/start.sh -t ");
        _JSONObject_MasterKV1.put("standbyinvokeshell","pkill -f MqttSubscriber ");
        //String luaFilepath ,String luaMethodName, String command
        JSONObject _JSONObject__heartbeattestshell1 = new JSONObject();
        _JSONObject__heartbeattestshell1.put("luaFilepath","/opt/sourcode/etcdBFG/src/main/java/com/lenovo/highavailablecommon/lua/login.lua");
        _JSONObject__heartbeattestshell1.put("luaMethodName","heartbeatjppsubscripter");
        _JSONObject__heartbeattestshell1.put("command","");
        _JSONObject_MasterKV1.put("heartbeattestshell",_JSONObject__heartbeattestshell1);

        JSONObject _JSONObject_MasterKV2 = new JSONObject();

        _JSONObject_MasterKV2.put("alianamekey","foo2");

        //_JSONObject_MasterKV2.put("urladdress","192.168.1.08:9090");
        JSONObject _JSONObject4urladdressVAL2 = new JSONObject();
        List<String> list4Value2 = new ArrayList<String>();
        list4Value2.add("192.168.2.77");
        list4Value2.add("8080");
        list4Value2.add("topname/devicetslt/iot2");
        list4Value2.add("$MOC/device/test_deviceid_06");

        _JSONObject4urladdressVAL2.put("valueArray",list4Value2);

        _JSONObject4urladdressVAL2.put("selectedIndex",3);

        _JSONObject_MasterKV2.put("urladdress",_JSONObject4urladdressVAL2);

        _JSONObject_MasterKV2.put("ttl","20");
        _JSONObject_MasterKV2.put("ttlinterval","10");
        _JSONObject_MasterKV2.put("keyRangStart","foo");
        _JSONObject_MasterKV2.put("keyRangEnd","fooz");
        _JSONObject_MasterKV2.put("masterinvokeshell","/data/mqttsubscriber/start.sh -t ");
        _JSONObject_MasterKV2.put("standbyinvokeshell","pkill -f MqttSubscriber ");
        //String luaFilepath ,String luaMethodName, String command
        JSONObject _JSONObject__heartbeattestshell2 = new JSONObject();
        _JSONObject__heartbeattestshell1.put("luaFilepath","/opt/sourcode/etcdBFG/src/main/java/com/lenovo/highavailablecommon/lua/login.lua");
        _JSONObject__heartbeattestshell1.put("luaMethodName","heartbeatjppsubscripter");
        _JSONObject__heartbeattestshell1.put("command","");
        _JSONObject_MasterKV2.put("heartbeattestshell",_JSONObject__heartbeattestshell2);



        _JSONArrayMasterKV.add(_JSONObject_MasterKV1);
        _JSONArrayMasterKV.add(_JSONObject_MasterKV2);


        JSONArray _JSONArrayMasterETCDClusterNode = new JSONArray();
        JSONObject _JSONObject__ETCDNode = new JSONObject();
        _JSONObject__ETCDNode.put("etcipporturl","http://27.cloudcpp.com:2379");

        _JSONArrayMasterETCDClusterNode.add(_JSONObject__ETCDNode);

        _JSONObjectResult.put("_JSONArrayMasterKV",_JSONArrayMasterKV);
        _JSONObjectResult.put("_JSONArrayMasterETCDClusterNode",_JSONArrayMasterETCDClusterNode);

        System.out.println("_JSONObjectResult--->>>>"+_JSONObjectResult.toJSONString());

        return _JSONObjectResult;
    }







}
