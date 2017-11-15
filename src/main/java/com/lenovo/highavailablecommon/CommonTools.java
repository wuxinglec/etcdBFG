package com.lenovo.highavailablecommon;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.coreos.jetcd.Client;
import com.coreos.jetcd.KV;
import com.coreos.jetcd.Lease;
import com.coreos.jetcd.data.ByteSequence;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by root on 2017/11/2.
 */
public class CommonTools {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonTools.class);


    public JSONArray getValueByKeyFromPropertis(String path_PropertisFile, List<String> list_Key){

        JSONArray _JSONArrayResult = new JSONArray();


        if(null == path_PropertisFile || path_PropertisFile.trim().length()<=0){
            return null;
        }

        if(null == list_Key || list_Key.size()<=0){
            return null;
        }


        try {
            FileInputStream is = new FileInputStream(path_PropertisFile);

            Properties _Properties = new Properties();

            _Properties.load(is);

            for(int i = 0 ; i < list_Key.size() ; i++){

                String currentKey = list_Key.get(i);

                String currentValueByKey = _Properties.getProperty(currentKey);

                JSONObject _JSONObject_KeyValue = new JSONObject();

                _JSONObject_KeyValue.put(currentKey,currentValueByKey);

                _JSONArrayResult.add(_JSONObject_KeyValue);


            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return _JSONArrayResult;
    }


    public   static  void InitLog4jConfig() {

        Properties props = null;

        FileInputStream fis = null;

        try {
            // 从配置文件dbinfo.properties中读取配置信息
            props = new Properties();

            String currentPath = System.getProperty("user.dir");

            fis = new FileInputStream(currentPath+"\\src\\main\\resources\\log4j.properties");

            props.load(fis);

            PropertyConfigurator.configure(props);//装入log4j配置信息

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null)
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            fis = null;
        }
    }


    public static JSONObject getETCDDClient(JSONArray _JSONArrayMasterETCDClusterNode){

        JSONObject _JSONObjectResult = new JSONObject();

        List<String> listStr_endpoints = new ArrayList<String>();


        if(null == _JSONArrayMasterETCDClusterNode || _JSONArrayMasterETCDClusterNode.size()<=0){
            _JSONObjectResult.put("actionFlag",false);
            _JSONObjectResult.put("message","error:null == _JSONArrayMasterETCDClusterNode || _JSONArrayMasterETCDClusterNode.size()<=0");
            LOGGER.error("error:null == _JSONArrayMasterETCDClusterNode || _JSONArrayMasterETCDClusterNode.size()<=0");
            return _JSONObjectResult;
        }



        for(int i = 0 ; i < _JSONArrayMasterETCDClusterNode.size(); i++){

            JSONObject _JSONObject__ETCDNode = _JSONArrayMasterETCDClusterNode.getJSONObject(i);

            String etcipporturl =  _JSONObject__ETCDNode.getString("etcipporturl");
            if(null == etcipporturl || etcipporturl.trim().length()<=0){
                LOGGER.error("null == etcipporturl || etcipporturl.trim().length()<=0");
                continue;
            }else {

                listStr_endpoints.add(etcipporturl);

            }
        }

        Client client = Client.builder().endpoints(listStr_endpoints).build();

        KV kvClient = client.getKVClient();

        Lease leaseClient = client.getLeaseClient();

        _JSONObjectResult.put("client",client);
        _JSONObjectResult.put("kvClient",kvClient);
        _JSONObjectResult.put("leaseClient",leaseClient);

        return _JSONObjectResult;
    }


    public static  JSONObject check_fire_init(JSONArray _JSONArrayMasterKV, JSONArray _JSONArrayMasterETCDClusterNode){


        if(null == _JSONArrayMasterKV || _JSONArrayMasterKV.size()<=0){
            LOGGER.error("error:null == _JSONArrayMasterKV || _JSONArrayMasterKV.size()<=0");
            return null;
        }

        if(null == _JSONArrayMasterETCDClusterNode || _JSONArrayMasterETCDClusterNode.size()<=0){
            LOGGER.error("null == _JSONArrayMasterETCDClusterNode || _JSONArrayMasterETCDClusterNode.size()<=0");
            return null;
        }


        JSONObject JSONObject_ClientAll = getETCDDClient(_JSONArrayMasterETCDClusterNode);

        if(null == JSONObject_ClientAll){
            LOGGER.error("error:"+"null == JSONObject_ClientAll");
            return null;
        }

        return JSONObject_ClientAll;

    }





}
