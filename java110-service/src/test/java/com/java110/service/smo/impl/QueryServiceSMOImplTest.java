package com.java110.service.smo.impl;


import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import junit.framework.TestCase;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class QueryServiceSMOImplTest extends TestCase {


    public void testJava() throws CannotCompileException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String javaCode = "public static void testJava2() {        System.out.println(\"123213\");\n}\n";
                String    javaCode2 ="public static void testJava1() {     testJava2();   System.out.println(\"223213\");\n}";
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.makeClass("com.java110.service.smo.WuxwTest");
        CtMethod helloM = CtNewMethod.make(javaCode, ctClass);
        ctClass.addMethod(helloM);

        CtMethod helloM1 = CtNewMethod.make(javaCode2, ctClass);
        ctClass.addMethod(helloM1);
        Class pc=ctClass.toClass();
        Method move= pc.getMethod("testJava1",new Class[]{});

        Constructor<?> con=pc.getConstructor(new Class[]{});

        move.invoke(con);
    }
}