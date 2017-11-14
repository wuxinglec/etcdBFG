--无参函数
function hello()
print 'hello'
end
--带参函数
function test(str)
print('data from java is:'..str)
return 'haha'
end


function javaluaexecuteshell(shellfilewholefolder)
    --handle = io.popen(shellfilewholefolder)
    local handle = io.popen(shellfilewholefolder)
    local result = handle:read("*a")
    --print("result--inner-method->>>>>"..result)
    --print(handle)
    --return "ok"
    handle:close()
    return "true"
end



function javaluaexecuteshellnoresultreturn(shellfilewholefolder)
    print("aaaaaaaaaaaaaaaaa")
      local commandResult = os.execute(shellfilewholefolder)
        print(commandResult)


    return commandResult
end


function heartbeatjppsubscripter()
    command = "jps"
    local logicResult = true
    local handle = io.popen(command)
    local result = handle:read("*a")
    print("result--->>>>"..result)
    handle:close()

    local indexArray = string.find(result,"RemoteMavenServer")

    if indexArray > 0 then
        logicResult = true
    else
        logicResult = false
    end

    return logicResult
end