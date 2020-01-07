package com.gdz.mybatisxml.utils;

import com.gdz.mybatisxml.mapper.InterfaceMethodInfo;
import com.gdz.mybatisxml.mapper.MapperBean;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Author: guandezhi
 * @Date: 2019/1/29 9:49
 * 读取xml配置文件，初始化mapperBean对象
 */
@Slf4j
public class XmlParseUtils {

    public static MapperBean loadXml(String path) {

        InputStream is = getXmlPathInputStream(path);
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(is);
            Element rootElement = document.getRootElement();
            String namespace = rootElement.attributeValue("namespace").trim();
            List<InterfaceMethodInfo> interfaceMethodInfoList = parseXml(rootElement);
            return getMapperBean(namespace, interfaceMethodInfoList);

        } catch (Exception e) {
            log.error("解析xml配置文件异常，{}", e.getMessage());
            return null;
        }
    }

    /**
     * mapperBean赋值
     * @param namespace
     * @param interfaceMethodInfoList
     * @return
     */
    private static MapperBean getMapperBean(String namespace, List<InterfaceMethodInfo> interfaceMethodInfoList) {
        MapperBean mapperBean = new MapperBean();
        mapperBean.setInterfaceName(namespace);
        mapperBean.setInterfaceMethodInfoList(interfaceMethodInfoList);
        return mapperBean;
    }


    /**
     * 解析xml配置文件
     * @param rootElement
     * @return
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private static List<InterfaceMethodInfo> parseXml(Element rootElement) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        List<InterfaceMethodInfo> interfaceMethodInfoList = new ArrayList<>();

        Iterator iterator = rootElement.elementIterator();

        while (iterator.hasNext()) {
            InterfaceMethodInfo interfaceMethodInfo = new InterfaceMethodInfo();
            Element element = (Element) iterator.next();
            String sqlType = element.getName().trim();
            String methodName = element.attributeValue("id").trim();
            String sql = element.getText().trim();
            String resultType = element.attributeValue("resultType").trim();
            //  String paramterType = element.attributeValue("parameterType").trim();
            interfaceMethodInfo.setSqlType(sqlType);
            interfaceMethodInfo.setMethodName(methodName);
            interfaceMethodInfo.setSql(sql);

            // interfaceMethodInfo.setParamterType(paramterType);
            interfaceMethodInfoList.add(interfaceMethodInfo);

            Object object = Class.forName(resultType).newInstance();
            interfaceMethodInfo.setResultType(object);
        }
        return interfaceMethodInfoList;
    }


    /**
     * 读取xml配置文件
     * @param path
     * @return
     */
    private static InputStream getXmlPathInputStream(String path) {
        return XmlParseUtils.class.getClassLoader().getResourceAsStream(path);
    }
}
