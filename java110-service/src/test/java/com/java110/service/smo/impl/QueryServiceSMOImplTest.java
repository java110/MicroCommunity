package com.java110.service.smo.impl;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import javassist.util.HotSwapper;
import junit.framework.TestCase;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class QueryServiceSMOImplTest extends TestCase {


    public void testJava() throws CannotCompileException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, NotFoundException, IOException, ClassNotFoundException {
        String javaCode = "public static void testJava2() {       DataQuery dataQuery = new DataQuery();\n dataQuery.setServiceCode(\"服务编码\");  System.out.println(dataQuery.getServiceCode());\n}\n";
                String    javaCode2 ="public static void testJava1() {     testJava2(); ServiceSql serviceSql = new ServiceSql();  System.out.println(\"623213\");\n}";
                String    javaCode3 ="public static void testJava3() {     ServiceSql serviceSql = new ServiceSql();  System.out.println(\"723213\");\n}";
        ClassPool classPool = ClassPool.getDefault();
        classPool.importPackage("com.java110.entity.service.DataQuery");
        classPool.importPackage("com.java110.entity.service.ServiceSql");
        CtClass ctClass = classPool.makeClass("com.java110.service.smo.WuxwTest3");
        CtMethod helloM = CtNewMethod.make(javaCode, ctClass);
        ctClass.addMethod(helloM);

        CtMethod helloM1 = CtNewMethod.make(javaCode2, ctClass);
        ctClass.addMethod(helloM1);
        Class<?> pc=ctClass.toClass();
        //ctClass.writeFile("./1111");
        Method move= pc.getMethod("testJava1",new Class[]{});

        Constructor<?> con=pc.getConstructor(new Class[]{});

        move.invoke(con);

        CtClass ctClass2 = classPool.get("com.java110.service.smo.WuxwTest3");
        CtMethod helloM3 = CtNewMethod.make(javaCode3, ctClass2);
        ctClass2.addMethod(helloM3);
        //ctClass.detach();

       // CtClass ctClass3 = classPool.get("com.java110.service.smo.WuxwTest1");

        /*HotSwapper swap = new HotSwapper(8000);
        swap.reload("com.java110.service.smo.WuxwTest3",ctClass2.toBytecode());
        Class ctClass1 = Class.forName("com.java110.service.smo.WuxwTest3");


         move= ctClass1.getMethod("testJava3",new Class[]{});

         con=ctClass1.getConstructor(new Class[]{});

        move.invoke(con);*/







    }



    public void testExistsJavaClass() throws Exception{
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.get("com.java110.core.javassist.Java110CoreTemplateJavassist");



        String javaCode = "public static void testJava2() {        System.out.println(\"123213\");\n}\n";
        String    javaCode2 ="public static void testJava1() {     testJava2();   System.out.println(\"223213\");\n}";
        CtMethod helloM = CtNewMethod.make(javaCode, ctClass);
        ctClass.addMethod(helloM);

        CtMethod helloM1 = CtNewMethod.make(javaCode2, ctClass);
        ctClass.addMethod(helloM1);
        //ctClass
        ctClass.writeFile("E:\\project\\HC\\MicroCommunity\\11");
    }
}