package com.lenovo.highavailablecommon;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.coreos.jetcd.Client;
import com.coreos.jetcd.KV;
import com.coreos.jetcd.Lease;
import com.coreos.jetcd.Watch;
import com.coreos.jetcd.data.ByteSequence;
import com.coreos.jetcd.data.KeyValue;
import com.coreos.jetcd.kv.GetResponse;
import com.coreos.jetcd.kv.PutResponse;
import com.coreos.jetcd.options.PutOption;
import com.coreos.jetcd.options.WatchOption;
import com.coreos.jetcd.watch.WatchEvent;
import com.coreos.jetcd.watch.WatchResponse;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by root on 2017/10/30.
 */
public class HighAvailableManager {


    private static final Logger LOGGER = LoggerFactory.getLogger(HighAvailableManager.class);

    private Object _ThreadLockFlag = null;

    private Map<String,String> mapForRabbitMQ = null;

    public HighAvailableManager(JSONArray _JSONArrayMasterKV, JSONArray _JSONArrayMasterETCDClusterNode){
        JSONObject_ClientAll = check_fire_init(_JSONArrayMasterKV,_JSONArrayMasterETCDClusterNode);
    }

    public HighAvailableManager(){
        super();
    }

    public HighAvailableManager(Object _ThreadLockFlag,Map<String,String> mapForRabbitMQ){
        super();
        this._ThreadLockFlag = _ThreadLockFlag;
        this.mapForRabbitMQ = mapForRabbitMQ;
    }


    //Object _ThreadLockFlag
//    static {
//        InitLog4jConfig();
//
//    }

    JSONObject JSONObject_ClientAll = null;

    private static JSONArray _JSONArrayMasterKV = null;

    private static JSONArray _JSONArrayMasterETCDClusterNode = null;



        /**
         * role :
         *          1:citizen default
         *          2:master
         *          3:standby
         *
         *
         */
        public  String myRole = "citizen";

        public static void main(String[] args){


//            HighAvailableManager _HighAvailableManager = new HighAvailableManager();
//
//            JSONObject _JSONObjectDataSource = _HighAvailableManager.generateData();
//
//             _JSONArrayMasterKV = _JSONObjectDataSource.getJSONArray("_JSONArrayMasterKV");
//
//            _JSONArrayMasterETCDClusterNode = _JSONObjectDataSource.getJSONArray("_JSONArrayMasterETCDClusterNode");
            if(null == args || args.length<=0){
                LOGGER.error("error:null == args || args.length<=0");
                return;
            }

            String etcdContextPath = args[0];

            if(null == etcdContextPath || etcdContextPath.trim().length()<=0){
                LOGGER.error("null == etcdContextPath || etcdContextPath.trim().length()<=0");
                return;
            }


            CommonTools _CommonTools = new CommonTools();

            //_JSONArrayMasterKV     _JSONArrayMasterETCDClusterNode
            //String[] keyStringArray = {"_JSONArrayMasterKV","_JSONArrayMasterETCDClusterNode"};
            List<String> keyStringArray = new ArrayList<String>();

            //etcdcontext
            keyStringArray.add("etcdcontext");

            System.out.println("etcdContextPath--->>>"+etcdContextPath);

            JSONArray _JSONArrayETCDContext = _CommonTools.getValueByKeyFromPropertis(etcdContextPath,keyStringArray);

            JSONObject _JSONObjectETCDContext = _JSONArrayETCDContext.getJSONObject(0);

            LOGGER.info("_JSONObjectETCDContext.toJSONString()--->>>>\n"+_JSONObjectETCDContext.toJSONString());

            //etcdcontext
            String value__etcdcontext = _JSONObjectETCDContext.getString("etcdcontext");

            JSONObject JSONObject_value__etcdcontext = (JSONObject) JSONObject.parse(value__etcdcontext);

            LOGGER.info("JSONObject_value__etcdcontext.toJSONString()--->>>"+JSONObject_value__etcdcontext.toJSONString());

            _JSONArrayMasterKV = JSONObject_value__etcdcontext.getJSONArray("_JSONArrayMasterKV");

            _JSONArrayMasterETCDClusterNode = JSONObject_value__etcdcontext.getJSONArray("_JSONArrayMasterETCDClusterNode");

            new HighAvailableManager().fire(_JSONArrayMasterKV,_JSONArrayMasterETCDClusterNode);
        }


    public  JSONObject getETCDDClient(JSONArray _JSONArrayMasterETCDClusterNode){

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




    public  String createThreadForHighAvailable(){
        return "";
    }


    //step_1: get the masterKeyList

    //step_2: 根据 masterKey 查询 etcd集群 ，是否 有此key 及 对应的 值
    //        bussiness_1:如果碰到任何一个key未找到  将此key put到 etcd 中 及对应的值，将自己的 role 设定为  master ，且循环 为此 key 进行  申请生命周期
    //        bussiness_2:如果循环所有的masterKey在etcd中全部已经 存在  且  有 值
    //                    则 将 自己的 的角色 设定为 standby 且 wathc 此  逻辑的  masterKey Range
    public  String fire(JSONArray _JSONArrayMasterKV,JSONArray _JSONArrayMasterETCDClusterNode){

            JSONObject_ClientAll = check_fire_init(_JSONArrayMasterKV,_JSONArrayMasterETCDClusterNode);

            if(null == JSONObject_ClientAll){
                LOGGER.error("error:null == JSONObject_ClientAll");
                return null;
            }

            this._JSONArrayMasterKV = _JSONArrayMasterKV;

            this._JSONArrayMasterETCDClusterNode = _JSONArrayMasterETCDClusterNode;

            Client _Client = JSONObject_ClientAll.getObject("client",Client.class);

            KV _KVClient = JSONObject_ClientAll.getObject("kvClient",KV.class);

            Lease leaseClient = JSONObject_ClientAll.getObject("leaseClient",Lease.class);



            lableA:
            for(int i = 0 ; i<_JSONArrayMasterKV.size() ; i++){

                JSONObject _JSONObject_MasterKV =  _JSONArrayMasterKV.getJSONObject(i);

                String current_alianamekey = _JSONObject_MasterKV.getString("alianamekey");
                String current_urladdress = _JSONObject_MasterKV.getString("urladdress");
                String keyRangStart = _JSONObject_MasterKV.getString("keyRangStart");
                String keyRangEnd = _JSONObject_MasterKV.getString("keyRangEnd");
                int ttl = Integer.valueOf(_JSONObject_MasterKV.getString("ttl"));
                int ttlinterval = Integer.valueOf(_JSONObject_MasterKV.getString("ttlinterval"));

                String masterinvokeshell = _JSONObject_MasterKV.getString("masterinvokeshell");//如果role is master should run shellfile

                String standbyinvokeshell = _JSONObject_MasterKV.getString("standbyinvokeshell");// if role is standby should run shellfile

                String heartbeattestshellJSONObjectStr =_JSONObject_MasterKV.getString("heartbeattestshell");

                //心跳检测shell逻辑 lua文件 位置
                JSONObject heartbeattestshellJSONObject = null;

                if(null !=heartbeattestshellJSONObjectStr && heartbeattestshellJSONObjectStr.trim().length()>0){
                    heartbeattestshellJSONObject = (JSONObject) JSONObject.parse(heartbeattestshellJSONObjectStr);
                }


                if(null != current_alianamekey &&
                        current_alianamekey.trim().length()>0 &&
                        null != current_urladdress &&
                        current_urladdress.trim().length()>0
                        ){

                    ByteSequence KEYParameter_ByteSequence =ByteSequence.fromString(current_alianamekey);

                    ByteSequence VALUEParameter_ByteSequence =ByteSequence.fromString(current_urladdress);

                    try {

                        long countForSearchByKey = _KVClient.get(KEYParameter_ByteSequence).get().getCount();
                        GetResponse resultForSearchByKey = _KVClient.get(KEYParameter_ByteSequence).get();
                        LOGGER.error("countForSearchByKey--->>>"+current_alianamekey+":"+countForSearchByKey+"\n ---the result is:"+resultForSearchByKey);

                        //如果 etcd 没有此 key 及 对应的 值
                        if(countForSearchByKey <=0){
                            //ttlinterval = 5
                            long leaseID = leaseClient.grant(ttlinterval).get().getID();

                            //put
                            PutResponse _PutResponse = _KVClient.put(   KEYParameter_ByteSequence,
                                    VALUEParameter_ByteSequence,
                                    PutOption.newBuilder().
                                            withLeaseId(leaseID).withPrevKV().
                                            build()).get();


                            KeyValue __KeyValue_getPrevKv = _PutResponse.getPrevKv();

                            LOGGER.info(String.valueOf(__KeyValue_getPrevKv.getKey()));
                            LOGGER.info(String.valueOf(__KeyValue_getPrevKv.getValue()));
                            LOGGER.info(String.valueOf(__KeyValue_getPrevKv.getLease()));

                            System.out.println(__KeyValue_getPrevKv.getCreateRevision());

                            GetResponse _GetResponse__Check = _KVClient.get(KEYParameter_ByteSequence).get();

                            LOGGER.debug("_GetResponse__Check--->>>>"+_GetResponse__Check);

                            List<KeyValue> list_KeyValue =  _GetResponse__Check.getKvs();

                            String list_KeyValue__JSONString = JSONArray.toJSONString(list_KeyValue);

                            LOGGER.debug("_GetResponse__Check.getKvs()--->>>>"+list_KeyValue__JSONString);

                            LOGGER.debug("_GetResponse__Check.getHeader()--->>>>"+_GetResponse__Check.getHeader());

                            long _Count__Check = _GetResponse__Check.getCount();

                            LOGGER.debug("_Count__Check--->>>"+_Count__Check);

                            LOGGER.info("i am a master.........pls fix me.....");

                            myRole="master";

                            //i am a master ,mybe i should run one shell
                            if(null != masterinvokeshell && masterinvokeshell.trim().length()>0){
                            }

                            //生效生命   创建一个新线程
                            callOneThreadToKeepAliveByleaseID(leaseID,
                                    leaseClient,
                                    ttlinterval,
                                    _Client,
                                    KEYParameter_ByteSequence,
                                    masterinvokeshell,
                                    JSONObject_ClientAll,
                                    heartbeattestshellJSONObject
                            );

                        }else if(countForSearchByKey > 0){

                            //?
                            if(i == _JSONArrayMasterKV.size() - 1){

                                myRole = "standby";

                                if(null != standbyinvokeshell && standbyinvokeshell.trim().length()>0){
                                    LOGGER.info("standbyinvokeshell--->will be run >>>"+standbyinvokeshell);
                                    LuaManager _LuaManager = new LuaManager();
                                    String commandResult = _LuaManager.executeShellByLua(standbyinvokeshell);
                                    LOGGER.info("standbyinvokeshell--->>>"+commandResult);
                                }

                                //need to watch
                                watchEtcdByKeyRange(_Client,keyRangStart,keyRangEnd);
                            }
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        fire(_JSONArrayMasterKV,_JSONArrayMasterETCDClusterNode);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                        fire(_JSONArrayMasterKV,_JSONArrayMasterETCDClusterNode);
                    }
                }
            }

            fire(_JSONArrayMasterKV,_JSONArrayMasterETCDClusterNode);

            return myRole;

        }


    public String getValueByKey(String key){

        //String

        KV _KVClient = JSONObject_ClientAll.getObject("kvClient",KV.class);

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



    private JSONObject watchEtcdByKeyRange(Client client,String keyRangStart,String keyRangEnd){

        JSONObject _JSONObjectResult = new JSONObject();

        myRole = "standby";

        Boolean flagToMaster = false;

        LOGGER.info("watchEtcdByKeyRange --->>>> standby , i will go to watch.....");

        Watch watch = client.getWatchClient();

        WatchOption _WatchOption = WatchOption.newBuilder().withRange(ByteSequence.fromString(keyRangEnd)).build();

        Watch.Watcher watcher = watch.watch(ByteSequence.fromString(keyRangStart),_WatchOption);

        String wayFlag = "";

        try{

            lableA:
            for (int i = 0; i < Integer.MAX_VALUE; i++) {

                LOGGER.info("Watching for keyRangStart={}", keyRangStart);

                LOGGER.info("Watching for keyRangEnd={}", keyRangEnd);

//                if(null == response.getEvents()){
//                    watch.watch(ByteSequence.fromString(keyRangStart),_WatchOption);
//                }

                //notify this is a standby letter
                if(null != _ThreadLockFlag && null != mapForRabbitMQ){
                    _ThreadLockFlag.notify();
                    mapForRabbitMQ.put("","");
                }


                WatchResponse response = watcher.listen();

                //Thread.sleep(10/2);

                lableB:
                for (WatchEvent event : response.getEvents()) {

                    WatchEvent.EventType currentEventType = event.getEventType();

                    ByteSequence _ByteSequenceKey= event.getKeyValue().getKey();

                    String _ByteSequenceKeyValueStr = Optional.ofNullable(_ByteSequenceKey).map(ByteSequence::toStringUtf8).orElse("");

                    ByteSequence _ByteSequenceValue = event.getKeyValue().getValue();

                    String _ByteSequenceValueStr =
                            Optional.ofNullable(_ByteSequenceValue).
                                    map(ByteSequence::toStringUtf8).orElse("");

                    LOGGER.info("type={}, key={}, value={}", currentEventType, _ByteSequenceKeyValueStr, _ByteSequenceValueStr);

                    if(currentEventType.name().equals("DELETE")){

                        //find value by key if 1 --->fire else watch  fixme
                        KV _KVClient = client.getKVClient();

                        long countForSearchByKey = _KVClient.get(_ByteSequenceKey).get().getCount();
                        //Thread.sleep();
                        if(countForSearchByKey <= 0){//此key
                            //delete
                            //watcher.close();
                            //
                            LOGGER.debug("watcher get the DELETE Event,and the watcher will be cancel watch!!!!");

                            watcher.close();

                            flagToMaster = true;

                            LOGGER.debug("watcher already canceld wathch watch ,and then will snatch the master role!!!!");
                            wayFlag = "fire";
                            break lableA;
                        }else {//this key already master put
                            watcher.close();
                            wayFlag = "watchEtcdByKeyRange";
                            break lableA;
                            //response.getEvents().removeAll(response.getEvents());
                            //break lableB ;

                            //watchEtcdByKeyRange(client,keyRangStart,keyRangEnd);
                        }

                    }

                    if(currentEventType.equals("put")){
                        //delete
                        //watcher.close();
                        LOGGER.info("watch get put Event ---->>>>>>>currentEventType.equals(\"put\")");
                    }

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        if(wayFlag.equals("fire")){
            //继续抢夺 master
            fire(_JSONArrayMasterKV,_JSONArrayMasterETCDClusterNode);
        }


        if(wayFlag.equals("watchEtcdByKeyRange")){
            //重新watch
            watchEtcdByKeyRange(client,keyRangStart,keyRangEnd);
        }



        return _JSONObjectResult;
    }


    private  JSONObject check_fire_init(JSONArray _JSONArrayMasterKV, JSONArray _JSONArrayMasterETCDClusterNode){


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


    private  void callOneThreadToKeepAliveByleaseID(long leaseID,
                                                          Lease leaseClient,int ttlinterval,Client _Client,
                                                          ByteSequence KEYParameter_ByteSequence,
                                                          String masterinvokeshell,
                                                          JSONObject JSONObject_ClientAll,
                                                          JSONObject heartbeattestshellJSONObject
                                                          ){

        JSONObject _JSONObjectResult = new JSONObject();

        int index = 0;

        lablewhiletrue:
        while (true){

            index = index +1;

            keepAliveleaseID(leaseID,leaseClient,_Client,KEYParameter_ByteSequence);

            //FUCK DEGINE
            if(null != _ThreadLockFlag && null !=mapForRabbitMQ && index == 1){

                String fixedCommandParameter = IAMConsumer.getSelectedIndexParameter(KEYParameter_ByteSequence,JSONObject_ClientAll);

                JSONObject _JSONObjectThreadAmonMesage = new JSONObject();

                _JSONObjectThreadAmonMesage.put("role","master");

                _JSONObjectThreadAmonMesage.put("fixedCommandParameter",fixedCommandParameter);

                String messageStr = _JSONObjectThreadAmonMesage.toJSONString();

                mapForRabbitMQ.put("ThreadLetter",messageStr);

                _ThreadLockFlag.notify();

                LOGGER.info("Thread message is sended..........._ThreadLockFlag.notify()..........the content is--->>>\n"+messageStr);
            }

            try {
                Thread.sleep(ttlinterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(index == 1){

                if(null != masterinvokeshell && masterinvokeshell.trim().length()>0){

                    List<JSONObject> list_JSONObjectKV = IAMConsumer.getValueByKeyFromETCD(null,null,KEYParameter_ByteSequence.toStringUtf8(),JSONObject_ClientAll);

                    JSONObject _JSONObjectKVFixedByIndex = list_JSONObjectKV.get(0);

                    String _StrValue = _JSONObjectKVFixedByIndex.getString("value_");

                    JSONObject _JSONObject__value_ = (JSONObject) JSONObject.parse(_StrValue);

                    //"value_" -> "{"valueArray":["192.168.1.07","8080","topname/device/iot1","192.168.1.07@@@@topname/device/iot1"],"selectedIndex":3}"
                    int selectedIndex = _JSONObject__value_.getInteger("selectedIndex");

                    JSONArray _JSONArrayvalueArray = (JSONArray) _JSONObject__value_.getJSONArray("valueArray");

                    String fixedCommandParameter = (String) _JSONArrayvalueArray.get(selectedIndex);

                    LOGGER.debug(fixedCommandParameter);
                    //System.out.println(fixedCommandParameter);

                    //System.out.println("_JSONObjectValue--->>>"+_StrValue);
                    LOGGER.info("_JSONObjectValue--->>>"+_StrValue);


                    LOGGER.info("masterinvokeshell--->will be run >>>"+masterinvokeshell);

                    masterinvokeshell = masterinvokeshell +" "+fixedCommandParameter;

                    LuaManager _LuaManager = new LuaManager();

                    String commandResult = _LuaManager.executeShellByLua(masterinvokeshell);

                    //commandResult = commandResult+"";

                    LOGGER.info("commandResult--->>>"+commandResult);
                }


            }else{

                //如果配置了  心跳检测shell lua 文件  逻辑
                if(null !=heartbeattestshellJSONObject && heartbeattestshellJSONObject.toJSONString().trim().length()>0){

                    LOGGER.info("heartbeattestshellJSONObject.toJSONString()--->>>"+heartbeattestshellJSONObject.toJSONString());

                    String luaFilepath = heartbeattestshellJSONObject.getString("luaFilepath");

                    String luaMethodName = heartbeattestshellJSONObject.getString("luaMethodName");

                    String checkFlag = LuaManager.executeShellByLuaDetail(luaFilepath,luaMethodName,null);

                    if(null == checkFlag || checkFlag.trim().length()<=0 || checkFlag.equals("false")){
                        LOGGER.error("--->>>>null == checkFlag || checkFlag.trim().length()<=0 || checkFlag.equals(\"false\")");
                        break lablewhiletrue;
                    }
                }
            }
        }

        fire(_JSONArrayMasterKV,_JSONArrayMasterETCDClusterNode);

    }

    public  JSONObject keepAliveleaseID(long leaseID,
                                        Lease leaseClient,
                                        Client _Client,
                                        ByteSequence KEYParameter_ByteSequence){

        JSONObject _JSONObjectResult = new JSONObject();

        //getValueByKey  == this.leaseID

        KV  _KVClient = _Client.getKVClient();

        try {

            GetResponse  resultForSearchByKey = _KVClient.get(KEYParameter_ByteSequence).get();

            List<KeyValue> listKeyValue = resultForSearchByKey.getKvs();

            long nowLease = listKeyValue.get(0).getLease();

            if(leaseID != nowLease){
                LOGGER.warn("leaseID != nowLease ---will invoke >>>> fire(_JSONArrayMasterKV,_JSONArrayMasterETCDClusterNode)");
                fire(_JSONArrayMasterKV,_JSONArrayMasterETCDClusterNode);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
            fire(_JSONArrayMasterKV,_JSONArrayMasterETCDClusterNode);
        } catch (ExecutionException e) {
            e.printStackTrace();
            fire(_JSONArrayMasterKV,_JSONArrayMasterETCDClusterNode);
        }




        Lease.KeepAliveListener _KeepAliveListener = leaseClient.keepAlive(leaseID);

        com.coreos.jetcd.lease.LeaseKeepAliveResponse _LeaseKeepAliveResponse = null;

        try {

            _LeaseKeepAliveResponse = _KeepAliveListener.listen();

            LOGGER.debug("_LeaseKeepAliveResponse---->>>>"+_LeaseKeepAliveResponse);

        } catch (InterruptedException e) {
            e.printStackTrace();
            //如果期间已经被其他 standby节点 put覆盖 ，则此节点 角色 恢复 standby角色
            //.....
            _JSONObjectResult.put("actionFlag",false);
            _JSONObjectResult.put("message","version is not Unified ");//,my role will be change to be standby from master

            fire(_JSONArrayMasterKV,_JSONArrayMasterETCDClusterNode);
        }

        _JSONObjectResult.put("ID",_LeaseKeepAliveResponse.getID());
        _JSONObjectResult.put("TTL",_LeaseKeepAliveResponse.getTTL());

        return _JSONObjectResult;
    }



    private  static  void InitLog4jConfig() {

        Properties props = null;

        FileInputStream fis = null;

        try {
            // 从配置文件dbinfo.properties中读取配置信息
            props = new Properties();

            String currentPath = System.getProperty("user.dir");

            //fis = new FileInputStream(currentPath+"\\src\\main\\resources\\log4j.properties");
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


    private  com.coreos.jetcd.api.WatchResponse cancelWatch(){

        com.coreos.jetcd.api.WatchResponse canceledReponse = com.coreos.jetcd.api.WatchResponse.newBuilder().setCanceled(true).build();

        boolean isFlag = canceledReponse.getCanceled();

        return canceledReponse;
    }




    public   JSONObject generateData(){

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
        _JSONObject_MasterKV1.put("masterinvokeshell","python -V");
        _JSONObject_MasterKV1.put("standbyinvokeshell","python -V");


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
        _JSONObject_MasterKV2.put("masterinvokeshell","python -V");
        _JSONObject_MasterKV2.put("standbyinvokeshell","python -V");


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
