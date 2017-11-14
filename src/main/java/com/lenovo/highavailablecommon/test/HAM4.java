package com.lenovo.highavailablecommon.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.coreos.jetcd.Client;
import com.coreos.jetcd.KV;
import com.coreos.jetcd.data.ByteSequence;
import com.coreos.jetcd.kv.GetResponse;
import com.lenovo.highavailablecommon.IAMConsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by root on 2017/10/31.
 */
public class HAM4 {


    public static void main(String[] args){

//        String byteCode = "MTkyLjE2OC4xLjA3OjgwODA=";
//
//        String point = new String(byteCode.getBytes());
//
//        System.out.println(point);

//        int val = (int)(Math.random()*100+1);
//
//        Long v2 = Long.valueOf(val);
//
//        System.out.print(val);
//
//
//
//        if(true){
//            return;
//        }


       //List<String> stringArrayAlianame = IAMConsumer.getAllETCDKeyAlianame(null,"D:\\lenovoWorkSpace\\iotairmedia\\etcd\\etcdBFG\\src\\main\\resources\\etcd.conf");

      //  System.out.println("stringArrayAlianame:::-->>>>"+stringArrayAlianame);

       List<JSONObject> value11 = IAMConsumer.getValueByKeyFromETCD(null,"D:\\lenovoWorkSpace\\iotairmedia\\etcd\\etcdBFG\\src\\main\\resources\\etcd.conf","foo",null);


        if(null != value11 && value11.size()>0) {
            System.out.println("value11--->>>>" + value11.get(0).getString("value_"));
        }

        List<JSONObject> value22 = IAMConsumer.getValueByKeyFromETCD(null,"D:\\lenovoWorkSpace\\iotairmedia\\etcd\\etcdBFG\\src\\main\\resources\\etcd.conf","foo2",null);

        if(null != value22 && value22.size()>0){

            System.out.println("value22--->>>>"+value22.get(0).getString("value_"));

        }




        if(true){
            return;
        }


        Client client = Client.builder().endpoints("http://27.cloudcpp.com:2379").build();

        KV _KVClient = client.getKVClient();

        String value1 = getValueByKey("foo",_KVClient);
        //String value11 = getValueByKey("foo",_KVClient);

        String value2 = getValueByKey("foo2",_KVClient);

        System.out.println("value1------>>>>>"+value1);

        System.out.println("value2------>>>>>"+value2);


        /*JSONObject _JSONObjectDataSource = generateData();

        JSONArray _JSONArrayMasterKV = _JSONObjectDataSource.getJSONArray("_JSONArrayMasterKV");

        JSONArray _JSONArrayMasterETCDClusterNode = _JSONObjectDataSource.getJSONArray("_JSONArrayMasterETCDClusterNode");

        HighAvailableManager _HighAvailableManager = new HighAvailableManager(_JSONArrayMasterKV,_JSONArrayMasterETCDClusterNode);

        String foo = _HighAvailableManager.getValueByKey("foo");
        String fooz = _HighAvailableManager.getValueByKey("foo2");

        System.out.println("foo--->>>"+foo);
        System.out.println("fooz--->>>"+fooz);*/

    }


    public static  String getValueByKey(String key, KV _KVClient){

        //String

        GetResponse _GetResponse__Check = null;
        try {
            _GetResponse__Check = _KVClient.get(ByteSequence.fromString(key)).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        return _GetResponse__Check.toString();
    }




    public  static JSONObject generateData(){

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
        list4Value1.add("192.168.1.07@@@@topname/device/iot1");

        _JSONObject4urladdressVAL1.put("valueArray",list4Value1);

        _JSONObject4urladdressVAL1.put("selectedIndex",3);//-1 全部

        _JSONObject_MasterKV1.put("urladdress",_JSONObject4urladdressVAL1);


        _JSONObject_MasterKV1.put("ttl","20");
        _JSONObject_MasterKV1.put("ttlinterval","10");
        _JSONObject_MasterKV1.put("keyRangStart","foo");
        _JSONObject_MasterKV1.put("keyRangEnd","fooz");

        JSONObject _JSONObject_MasterKV2 = new JSONObject();

        _JSONObject_MasterKV2.put("alianamekey","foo2");
        //_JSONObject_MasterKV2.put("urladdress","192.168.1.08:9090");


        JSONObject _JSONObject4urladdressVAL2 = new JSONObject();
        List<String> list4Value2 = new ArrayList<String>();
        list4Value2.add("192.168.2.77");
        list4Value2.add("8080");
        list4Value2.add("topname/devicetslt/iot2");
        list4Value2.add("192.168.1.77@@@@topname/device/iot2");

        _JSONObject4urladdressVAL2.put("valueArray",list4Value2);

        _JSONObject4urladdressVAL2.put("selectedIndex",3);

        _JSONObject_MasterKV2.put("urladdress",_JSONObject4urladdressVAL2);


        _JSONObject_MasterKV2.put("ttl","20");
        _JSONObject_MasterKV2.put("ttlinterval","10");
        _JSONObject_MasterKV2.put("keyRangStart","foo");
        _JSONObject_MasterKV2.put("keyRangEnd","fooz");



        _JSONArrayMasterKV.add(_JSONObject_MasterKV1);
        _JSONArrayMasterKV.add(_JSONObject_MasterKV2);


        JSONArray _JSONArrayMasterETCDClusterNode = new JSONArray();
        JSONObject _JSONObject__ETCDNode = new JSONObject();
        _JSONObject__ETCDNode.put("etcipporturl","http://27.cloudcpp.com:2379");

        _JSONArrayMasterETCDClusterNode.add(_JSONObject__ETCDNode);

        _JSONObjectResult.put("_JSONArrayMasterKV",_JSONArrayMasterKV);
        _JSONObjectResult.put("_JSONArrayMasterETCDClusterNode",_JSONArrayMasterETCDClusterNode);

        return _JSONObjectResult;
    }


}
