package com.qiein.jupiter.util;

import net.sf.cglib.beans.BeanGenerator;
import net.sf.cglib.beans.BeanMap;

import java.util.Map;

public class DynamicBean {

    /**
     * 目标对象
     */
    private Object object;

    /*
    * 属性的集合
    */
    private BeanMap beanMap;

    /**
     *
     * @param clazz  要添加字段的对象
     * @param properties  要添加的字段
     */
    public DynamicBean(Class clazz, Map<String,Object> properties){
        this.object = generateBean(clazz,properties);
        this.beanMap = BeanMap.create(this.object);
    }


    /**
     * 添加属性和值
     */
    public void setValue(String property, Object value) {
        beanMap.put(property, value);
    }

    /**
     * 获取属性值
     */
    public Object getValue(String property) {
        return beanMap.get(property);
    }

    /**
     * 获取对象
     *
     * @return
     */
    public Object getObject() {
        return this.object;
    }


    //生成新的对象
    private Object generateBean(Class clazz, Map<String, Object> properties) {
        BeanGenerator beanGenerator = new BeanGenerator();
      /*  if(clazz != null){
            beanGenerator.setSuperclass(clazz);
        }*/
        //添加属性
        BeanGenerator.addProperties(beanGenerator,properties);
        return beanGenerator.create();
    }


}
