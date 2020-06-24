package com.geek45.exampleall.js;

import com.alibaba.fastjson.JSON;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class JsDemo {

    private static final Logger log = LoggerFactory.getLogger(JsDemo.class);

    /**
     * 调用外部js进行执行某个函数，获取返回值
     * @param args
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        String jsName = "D:\\test\\hmac-sha1.js";
        String jsName2 = "D:\\test\\md5.js";
        String jsName3 = "D:\\test\\tripledes.js";
        //读取js
        FileReader fileReader = new FileReader(jsName);
        FileReader fileReader2 = new FileReader(jsName2);
        FileReader fileReader3 = new FileReader(jsName3);
        //执行指定脚本

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");

        try{
            engine.eval(fileReader);
            engine.eval(fileReader2);
            engine.eval(fileReader3);
            engine.eval("function test(password,key,iv){" +
                    "return  CryptoJS.DES.encrypt(CryptoJS.MD5(password).toString(), key, {" +
                    "    iv: iv," +
                    "   mode: CryptoJS.mode.CBC," +
                    "    padding: CryptoJS.pad.Pkcs7" +
                    "}).toString();" +
                    "}");
            if (engine instanceof Invocable) {
                Invocable in = (Invocable) engine;
                System.out.println(in.invokeFunction("test", "fdsaf", "dsa", "fdsa"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    static String jsName = "D:\\test\\hmac-sha1.js";
    static String jsName2 = "D:\\test\\md5.js";
    static String jsName3 = "D:\\test\\tripledes.js";

    public static ScriptObjectMirror crypt(String str){
        ScriptObjectMirror crypt = null;
        try {
            FileReader fileReader = new FileReader(jsName);
            FileReader fileReader2 = new FileReader(jsName2);
            FileReader fileReader3 = new FileReader(jsName3);
            //执行指定脚本
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("javascript");
            try {
                engine.eval(fileReader);
                engine.eval(fileReader2);
                engine.eval(fileReader3);
                engine.eval("function test(a){" +
                        "return CryptoJS.enc.Utf8.parse(a);" +
                        "}");
                if (engine instanceof Invocable) {
                    Invocable in = (Invocable) engine;
//                    System.out.println(in.invokeFunction("test", "423432"));
                    crypt = (ScriptObjectMirror) in.invokeFunction("test", str);
                    System.err.println(crypt);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            log.error(JSON.toJSONString(e.getStackTrace()));
        }
        return crypt;
    }

    public static String hashPass(String pass,ScriptObjectMirror key,ScriptObjectMirror iv){
        String crypt = "";
        try {
            //读取js
            FileReader fileReader = new FileReader(jsName);
            FileReader fileReader2 = new FileReader(jsName2);
            FileReader fileReader3 = new FileReader(jsName3);
            //执行指定脚本

            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("javascript");

            try {
                engine.eval(fileReader);
                engine.eval(fileReader2);
                engine.eval(fileReader3);
                engine.eval("function test(password,key,iv){" +
                        "return  CryptoJS.DES.encrypt(CryptoJS.MD5(password).toString(), key, {" +
                        "    iv: iv," +
                        "   mode: CryptoJS.mode.CBC," +
                        "    padding: CryptoJS.pad.Pkcs7" +
                        "}).toString();" +
                        "}");
                if (engine instanceof Invocable) {
                    Invocable in = (Invocable) engine;
//                    System.out.println(in.invokeFunction("test", "423432"));
                    crypt = in.invokeFunction("test", pass, key, iv).toString();
                    System.err.println(crypt);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            log.error(JSON.toJSONString(e.getStackTrace()));
        }
        return crypt;
    }
}
