package com.qiein.jupiter;

/**
 * @author: yyx
 * @Date: 2018-9-6
 */
public class Demo {
    public TestInterface getInterfacre(){
        return new TestInterface() {
            @Override
            public void add() {

            }
        };
    }
}
