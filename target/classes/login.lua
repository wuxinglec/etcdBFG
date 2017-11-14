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
    handle = io.popen(shellfilewholefolder)
    --local handle = io.popen(shellfilewholefolder)
    local result = handle:read("*a")
    print("result--inner-method->>>>>"..result)
    print(handle)
    --return "ok"
    handle:close()
    return result
end



function javaluaexecuteshellnoresultreturn(shellfilewholefolder)
    print("aaaaaaaaaaaaaaaaa")
      local commandResult = os.execute(shellfilewholefolder)
        print(commandResult)


    return commandResult
end

