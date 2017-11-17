package com.lenovo.highavailablecommon;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.ObjectArraySerializer;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by root on 2017/11/1.
 */
public class Sentinel implements Runnable{

    Map<String,String> mapForRabbitMQ = null;

    public Sentinel(Object _ThreadLockFlag,Map<String,String> mapForRabbitMQ){
        super();
        this._ThreadLockFlag = _ThreadLockFlag;
        this.mapForRabbitMQ = mapForRabbitMQ;
    }

    public Sentinel(){
        super();
    }


    static {
        CommonTools.InitLog4jConfig();
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(Sentinel.class);


    public static JSONArray _JSONArrayMasterKV;

    public static JSONArray _JSONArrayMasterETCDClusterNode;

    private Object _ThreadLockFlag;

    @Override
    public void run() {

//        synchronized(_ThreadLockFlag){
            new HighAvailableManager(_ThreadLockFlag,mapForRabbitMQ).fire(_JSONArrayMasterKV,_JSONArrayMasterETCDClusterNode);
//        }

    }



    public static String callOneThreadToLiveETCDSentinel(JSONObject _JSONObjectParameter,
                                                         String patch_ETCDPropertisFile,
                                                         Object _ThreadLockFlag,
                                                         Map<String,String> mapForRabbitMQ){

        Thread.currentThread().setName("ThreadNameETCDBFC");

        Thread.currentThread().getId();

        LOGGER.info("Thread.currentThread().getName()--->>>"+Thread.currentThread().getName());

        String result = "success";

        if(null == _JSONObjectParameter || _JSONObjectParameter.size()<=0){
            //
            if(null != patch_ETCDPropertisFile && patch_ETCDPropertisFile.trim().length()>0){

                CommonTools _CommonTools = new CommonTools();
                //_JSONArrayMasterKV     _JSONArrayMasterETCDClusterNode
                //String[] keyStringArray = {"_JSONArrayMasterKV","_JSONArrayMasterETCDClusterNode"};
                List<String> keyStringArray = new ArrayList<String>();
                //etcdcontext
                keyStringArray.add("etcdcontext");

                JSONArray _JSONArrayETCDContext = _CommonTools.getValueByKeyFromPropertis(patch_ETCDPropertisFile,keyStringArray);

                JSONObject _JSONObjectETCDContext = _JSONArrayETCDContext.getJSONObject(0);

                LOGGER.info("_JSONObjectETCDContext.toJSONString()--->>>>\n"+_JSONObjectETCDContext.toJSONString());

                //etcdcontext
                String value__etcdcontext = _JSONObjectETCDContext.getString("etcdcontext");

                JSONObject JSONObject_value__etcdcontext = (JSONObject) JSONObject.parse(value__etcdcontext);

                LOGGER.info("JSONObject_value__etcdcontext.toJSONString()--->>>"+JSONObject_value__etcdcontext.toJSONString());

                _JSONArrayMasterKV = JSONObject_value__etcdcontext.getJSONArray("_JSONArrayMasterKV");

                _JSONArrayMasterETCDClusterNode = JSONObject_value__etcdcontext.getJSONArray("_JSONArrayMasterETCDClusterNode");

            }

        }

        (new Thread(new Sentinel(_ThreadLockFlag,mapForRabbitMQ))).start();

        return result;
    }

    public static void start(){
        System.out.println("-------->>>>>>>>start()");
    }

    public static void stop(){
        System.out.println("-------->>>>>>>>stop()");

    }
    public static void main(String args[]) {

//        HighAvailableManager _HighAvailableManager = new HighAvailableManager();
//
//        JSONObject _JSONObjectDataSource = _HighAvailableManager.generateData();

        Object _ThreadLockFlag = new Object();



        new Sentinel(_ThreadLockFlag,null).
                callOneThreadToLiveETCDSentinel(
                        null,
                        "D:\\lenovoWorkSpace\\iotairmedia\\etcd\\etcdBFG\\src\\main\\resources\\etcd.conf",null,null);

        System.out.println("already run this instance!!!!!!!!");


    }


}
