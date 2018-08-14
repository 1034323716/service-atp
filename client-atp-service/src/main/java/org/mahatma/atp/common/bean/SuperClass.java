package org.mahatma.atp.common.bean;

import com.feinno.superpojo.SuperPojoManager;
import com.feinno.superpojo.UnknownFieldSet;
import com.google.gson.JsonObject;

import java.util.BitSet;

/**
 * Created by JiYunfei on 17-11-9.
 */
public class SuperClass {
    /**
     * 由于某种原因导致流中{@link }序号不在当前的JAVA对象中，则将此序号和数据存储在此区域
     */
    private UnknownFieldSet unknownFieldSet = new UnknownFieldSet();

    /**
     * 用于装载反序列化的字段名称，通过此集合中的内容，可以知道哪些字段值被反序列化到当前对象中
     */
    private BitSet serializationFieldSet = new BitSet();

    public String toXmlString() {
        byte[] buffer = toXmlByteArray();
        return new String(buffer);
    }

    public byte[] toXmlByteArray() {
        return SuperPojoManager.toXmlByteArray(this);
    }

    @Override
    public String toString() {
        return this.toXmlString();
    }

    public JsonObject toJsonObject() {
        return SuperPojoManager.toJsonObject(this);
    }

    public boolean hasValue(int tag) {
        return serializationFieldSet.get(tag);
    }

    public UnknownFieldSet getUnknownFields() {
        return unknownFieldSet;
    }

    public void putSerializationFieldTag(int tag) {
        serializationFieldSet.set(tag);
    }
}
