package com.lenovo.highavailablecommon;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by root on 2017/11/1.
 */
public class LuaManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(LuaManager.class);

    public static void main(String[] args){

        //copy D:\test\robin.typ d:\test\test2\
        //String commandResult = executeShellByLua("python -V");
        //String commandResult = executeShellByLua("ps -ef |grep docker");

       // System.out.println("commandResult--->>>"+commandResult);

        String luaFilepath = "/opt/sourcode/etcdBFG/src/main/java/com/lenovo/highavailablecommon/lua/login.lua";

        String luaMethodName = "heartbeatjppsubscripter";

        String result = executeShellByLuaDetail(luaFilepath,luaMethodName,null);

        System.out.println("result--->>>"+result);



    }

    //role is master
    public static String executeShellByLua(String command){

//        if(null == command || command.trim().length()<=0){
//            return null;
//        }

        //如果需要查找到 pid 之后 kill 掉该进程，还可以使用 pkill：
        //pkill -f name
        LOGGER.info("executeShellByLua command is --->>>"+command);

        String result = "";

        StringBuffer sb_luaScriptFolder = new StringBuffer();

        String currentPath = System.getProperty("user.dir");

       // sb_luaScriptFolder.append(currentPath);

        //linux
        //sb_luaScriptFolder.append("/opt/mydesktop/login.lua");
        sb_luaScriptFolder.append("/opt/sourcode/etcdBFG/src/main/java/com/lenovo/highavailablecommon/lua/login.lua");
        //sb_luaScriptFolder.append("D:\\lenovoWorkSpace\\iotairmedia\\etcd\\etcdBFG\\src\\main\\resources\\login.lua");

        Globals globals = JsePlatform.standardGlobals();

        //加载脚本文件login.lua，并编译
        globals.loadfile(sb_luaScriptFolder.toString()).call();

        //获取带参函数test
        //LuaValue javaluaexecuteshell = globals.get(LuaValue.valueOf("javaluaexecuteshell"));
        LuaValue javaluaexecuteshell = globals.get(LuaValue.valueOf("javaluaexecuteshell"));
        //LuaValue javaluaexecuteshell = globals.get(LuaValue.valueOf("javaluaexecuteshellnoresultreturn"));

        //执行test方法,传入String类型的参数参数

        LuaString _LuaStringCommand = LuaValue.valueOf(command);

        String commandResult = javaluaexecuteshell.call(_LuaStringCommand).toString();

        LOGGER.debug("commandResult is --->>>>>"+commandResult);

        //System.out.println("commandResult is --->>>>>"+commandResult);

        return commandResult;
    }


    /**
     *
     * @param luaFilepath   lua脚本 全路径
     * @param luaMethodName 欲调用lua方法名称
     * @param command       lua
     * @return
     */
    //role is master
    public static String executeShellByLuaDetail(String luaFilepath ,String luaMethodName, String command){

        if(null == command || command.trim().length()<=0){

        }

        if(null == luaFilepath || luaFilepath.trim().length()<=0){
            return null;
        }

        if(null == luaMethodName || luaMethodName.trim().length()<=0){
            return null;
        }



        //如果需要查找到 pid 之后 kill 掉该进程，还可以使用 pkill：
        //pkill -f name
        LOGGER.info("executeShellByLua command is --->>>"+command);

        String result = "";

        //StringBuffer sb_luaScriptFolder = new StringBuffer();

        //String currentPath = System.getProperty("user.dir");

        // sb_luaScriptFolder.append(currentPath);

        //linux
        //sb_luaScriptFolder.append("/opt/mydesktop/login.lua");
        //sb_luaScriptFolder.append("/opt/sourcode/etcdBFG/src/main/java/com/lenovo/highavailablecommon/lua/login.lua");
        //sb_luaScriptFolder.append("D:\\lenovoWorkSpace\\iotairmedia\\etcd\\etcdBFG\\src\\main\\resources\\login.lua");

        Globals globals = JsePlatform.standardGlobals();

        //加载脚本文件login.lua，并编译
        globals.loadfile(luaFilepath).call();

        //获取带参函数test
        //LuaValue javaluaexecuteshell = globals.get(LuaValue.valueOf("javaluaexecuteshell"));
        LuaValue javaluaexecuteshell = globals.get(LuaValue.valueOf(luaMethodName));
        //LuaValue javaluaexecuteshell = globals.get(LuaValue.valueOf("javaluaexecuteshellnoresultreturn"));

        String commandResult = "";

        //执行test方法,传入String类型的参数参数
        if(null != command && command.trim().length()>0){

            LuaString _LuaStringCommand = LuaValue.valueOf(command);

            commandResult = javaluaexecuteshell.call(_LuaStringCommand).toString();

        }else {
            commandResult = javaluaexecuteshell.call().toString();
        }



        LOGGER.debug("commandResult is --->>>>>"+commandResult);

        //System.out.println("commandResult is --->>>>>"+commandResult);

        return commandResult;
    }


}
