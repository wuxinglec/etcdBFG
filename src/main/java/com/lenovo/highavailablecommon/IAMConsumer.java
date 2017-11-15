package com.lenovo.highavailablecommon;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.coreos.jetcd.Client;
import com.coreos.jetcd.KV;
import com.coreos.jetcd.Lease;
import com.coreos.jetcd.data.ByteSequence;
import com.coreos.jetcd.data.KeyValue;
import com.coreos.jetcd.kv.GetResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by root on 2017/11/2.
 * java -cp test.jar com.ee2ee.test.PackageTest
 */
public class IAMConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(IAMConsumer.class);


    static {
        CommonTools.InitLog4jConfig();
    }

    public static JSONArray _JSONArrayMasterKV;

    public static JSONArray _JSONArrayMasterETCDClusterNode;

    public static List<JSONObject> getValueByKeyFromETCD(JSONObject _JSONObjectParameter, String patch_ETCDPropertisFile,String key,JSONObject JSONObject_ClientAll){

        if(null == key || key.trim().length()<=0){
            return null;
        }


        String result = "";

        if(null == _JSONObjectParameter || _JSONObjectParameter.size()<=0){
            //

            if(null == JSONObject_ClientAll && null != patch_ETCDPropertisFile && patch_ETCDPropertisFile.trim().length()>0){

                CommonTools _CommonTools = new CommonTools();
                //_JSONArrayMasterKV     _JSONArrayMasterETCDClusterNode
                //String[] keyStringArray = {"_JSONArrayMasterKV","_JSONArrayMasterETCDClusterNode"};
                List<String> keyStringArray = new ArrayList<String>();
                //etcdcontext
                keyStringArray.add("etcdcontext");

                JSONArray _JSONArrayETCDContext = _CommonTools.getValueByKeyFromPropertis(patch_ETCDPropertisFile,keyStringArray);

                JSONObject _JSONObjectETCDContext = _JSONArrayETCDContext.getJSONObject(0);

                System.out.println("_JSONObjectETCDContext.toJSONString()--->>>>\n"+_JSONObjectETCDContext.toJSONString());

                //etcdcontext
                String value__etcdcontext = _JSONObjectETCDContext.getString("etcdcontext");

                JSONObject JSONObject_value__etcdcontext = (JSONObject) JSONObject.parse(value__etcdcontext);

                System.out.println("JSONObject_value__etcdcontext.toJSONString()--->>>"+JSONObject_value__etcdcontext.toJSONString());

                _JSONArrayMasterKV = JSONObject_value__etcdcontext.getJSONArray("_JSONArrayMasterKV");

                _JSONArrayMasterETCDClusterNode = JSONObject_value__etcdcontext.getJSONArray("_JSONArrayMasterETCDClusterNode");

                JSONObject_ClientAll = CommonTools.check_fire_init(_JSONArrayMasterKV,_JSONArrayMasterETCDClusterNode);

            }

        }



        //Client _Client = JSONObject_ClientAll.getObject("client",Client.class);

        KV _KVClient = JSONObject_ClientAll.getObject("kvClient",KV.class);

        //Lease leaseClient = JSONObject_ClientAll.getObject("leaseClient",Lease.class);

        GetResponse _GetResponse__Check = null;
        JSONArray _JSONArraylistKeyValue = null;
        List<JSONObject>  listKeyValueToStringUTF8 = new ArrayList<JSONObject>();

        try {
            _GetResponse__Check = _KVClient.get(ByteSequence.fromString(key)).get();

            //= _KVClient.get(ByteSequence.fromString(key));

            List<KeyValue> listKeyValue = _GetResponse__Check.getKvs();


            for(int i = 0 ; i< listKeyValue.size() ; i++){
                KeyValue currentKeyValue = listKeyValue.get(i);
                String key_ = currentKeyValue.getKey().toStringUtf8();
                String value_ = currentKeyValue.getValue().toStringUtf8();
                long createRevision_ = currentKeyValue.getCreateRevision();
                long lease_ = currentKeyValue.getLease();
                long modRevision_ = currentKeyValue.getModRevision();
                long version_ = currentKeyValue.getVersion();

                JSONObject _JSONObjectCurrent_KeyValue = new JSONObject();

                _JSONObjectCurrent_KeyValue.put("key_",key_);
                _JSONObjectCurrent_KeyValue.put("value_",value_);
                _JSONObjectCurrent_KeyValue.put("createRevision_",createRevision_);
                _JSONObjectCurrent_KeyValue.put("lease_",lease_);
                _JSONObjectCurrent_KeyValue.put("modRevision_",modRevision_);
                _JSONObjectCurrent_KeyValue.put("version_",version_);

                listKeyValueToStringUTF8.add(_JSONObjectCurrent_KeyValue);

            }

            String jsonStringlistKeyValue = JSONArray.toJSONString(listKeyValueToStringUTF8);

            _JSONArraylistKeyValue = (JSONArray) JSONArray.parse(jsonStringlistKeyValue);

            result = _JSONArraylistKeyValue.toJSONString();

            System.out.println("_GetResponse__Check.getKvs()--->>>"+_JSONArraylistKeyValue.toJSONString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return listKeyValueToStringUTF8;
    }


    public static List<String> getAllETCDKeyAlianame(JSONObject _JSONObjectParameter, String patch_ETCDPropertisFile){

        List<String> result = new ArrayList<String>();

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

                System.out.println("_JSONObjectETCDContext.toJSONString()--->>>>\n"+_JSONObjectETCDContext.toJSONString());

                //etcdcontext
                String value__etcdcontext = _JSONObjectETCDContext.getString("etcdcontext");

                JSONObject JSONObject_value__etcdcontext = (JSONObject) JSONObject.parse(value__etcdcontext);

                System.out.println("JSONObject_value__etcdcontext.toJSONString()--->>>"+JSONObject_value__etcdcontext.toJSONString());

                _JSONArrayMasterKV = JSONObject_value__etcdcontext.getJSONArray("_JSONArrayMasterKV");

                _JSONArrayMasterETCDClusterNode = JSONObject_value__etcdcontext.getJSONArray("_JSONArrayMasterETCDClusterNode");

                for(int i = 0 ; i < _JSONArrayMasterKV.size(); i++){

                    JSONObject _JSONObject4EtcdKV = _JSONArrayMasterKV.getJSONObject(i);

                    String alianamekey = _JSONObject4EtcdKV.getString("alianamekey");

                    result.add(alianamekey);

                }

            }

        }

        JSONObject JSONObject_ClientAll = CommonTools.check_fire_init(_JSONArrayMasterKV,_JSONArrayMasterETCDClusterNode);

        return result;

    }

    public static String getSelectedIndexParameter(ByteSequence KEYParameter_ByteSequence,JSONObject JSONObject_ClientAll){

        if(null == KEYParameter_ByteSequence || null ==JSONObject_ClientAll){
            LOGGER.error("null == KEYParameter_ByteSequence || null ==JSONObject_ClientAll");
            return null;
        }


        String fixedCommandParameter = "";

        List<JSONObject> list_JSONObjectKV = IAMConsumer.getValueByKeyFromETCD(
                null,
                null,
                KEYParameter_ByteSequence.toStringUtf8(),
                JSONObject_ClientAll);

        JSONObject _JSONObjectKVFixedByIndex = list_JSONObjectKV.get(0);

        String _StrValue = _JSONObjectKVFixedByIndex.getString("value_");

        JSONObject _JSONObject__value_ = (JSONObject) JSONObject.parse(_StrValue);

        //"value_" -> "{"valueArray":["192.168.1.07","8080","topname/device/iot1","192.168.1.07@@@@topname/device/iot1"],"selectedIndex":3}"
        int selectedIndex = _JSONObject__value_.getInteger("selectedIndex");

        JSONArray _JSONArrayvalueArray = (JSONArray) _JSONObject__value_.getJSONArray("valueArray");

        fixedCommandParameter = (String) _JSONArrayvalueArray.get(selectedIndex);

        LOGGER.debug("fixedCommandParameter-->>>"+fixedCommandParameter);

        return fixedCommandParameter;

    }



}
