//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.mahatma.atp.common.bean.superClassBuilder;

import com.feinno.superpojo.Builder;
import com.feinno.superpojo.SuperPojo;
import com.feinno.superpojo.UnknownField;
import com.feinno.superpojo.UnknownFieldSet;
import com.feinno.superpojo.io.CodedInputStream;
import com.feinno.superpojo.io.CodedOutputStream;
import com.feinno.superpojo.io.JsonInputStream;
import com.feinno.superpojo.io.XmlInputStream;
import com.feinno.superpojo.io.XmlOutputStream;
import com.feinno.superpojo.util.SuperPojoUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.Iterator;
import javax.xml.stream.XMLStreamException;
import org.mahatma.atp.common.bean.PkgCfgArgs.TestCaseCfgArgs;

public class TestCaseCfgArgsBuilder extends Builder<TestCaseCfgArgs> {
    private int memoizedSerializedSize = -1;

    public TestCaseCfgArgsBuilder(TestCaseCfgArgs var1) {
        super(var1);
    }

    public void parsePbFrom(CodedInputStream var1) throws IOException {
        while(true) {
            int var2 = var1.readTag();
            switch(var2) {
            case 0:
                return;
            case 8:
                ((TestCaseCfgArgs)this.data).putSerializationFieldTag(1);
                ((TestCaseCfgArgs)this.data).setTcId(var1.readInt64());
                break;
            case 16:
                ((TestCaseCfgArgs)this.data).putSerializationFieldTag(2);
                ((TestCaseCfgArgs)this.data).setCfgId(var1.readInt64());
                break;
            default:
                ((TestCaseCfgArgs)this.data).getUnknownFields().parseUnknownField(var2, var1);
            }
        }
    }

    public void writePbTo(CodedOutputStream var1) throws IOException {
        if (!this.isInitialized()) {
            throw new RuntimeException("required field is null,so stop write.");
        } else {
            this.getSerializedSize();
            if (((TestCaseCfgArgs)this.data).getTcId() != 0L || ((TestCaseCfgArgs)this.data).hasValue(1)) {
                var1.writeInt64(1, ((TestCaseCfgArgs)this.data).getTcId());
            }

            if (((TestCaseCfgArgs)this.data).getCfgId() != 0L || ((TestCaseCfgArgs)this.data).hasValue(2)) {
                var1.writeInt64(2, ((TestCaseCfgArgs)this.data).getCfgId());
            }

            ((TestCaseCfgArgs)this.data).getUnknownFields().writeUnknownField(var1);
        }
    }

    public int getSerializedSize() {
        int var1 = this.memoizedSerializedSize;
        if (var1 != -1) {
            return var1;
        } else {
            var1 = 0;
            if (((TestCaseCfgArgs)this.data).getTcId() != 0L || ((TestCaseCfgArgs)this.data).hasValue(1)) {
                var1 += CodedOutputStream.computeInt64Size(1, ((TestCaseCfgArgs)this.data).getTcId());
            }

            if (((TestCaseCfgArgs)this.data).getCfgId() != 0L || ((TestCaseCfgArgs)this.data).hasValue(2)) {
                var1 += CodedOutputStream.computeInt64Size(2, ((TestCaseCfgArgs)this.data).getCfgId());
            }

            var1 = (int)((long)var1 + ((TestCaseCfgArgs)this.data).getUnknownFields().getSerializedSize());
            this.memoizedSerializedSize = var1;
            return var1;
        }
    }

    public JsonObject toJsonObject() {
        JsonObject var1 = new JsonObject();
        var1.addProperty("tcId", ((TestCaseCfgArgs)this.data).getTcId());
        var1.addProperty("cfgId", ((TestCaseCfgArgs)this.data).getCfgId());
        if (this.getData() != null && ((TestCaseCfgArgs)this.getData()).getUnknownFields() != null && ((TestCaseCfgArgs)this.getData()).getUnknownFields().getNumbers() != null) {
            UnknownFieldSet var2 = ((TestCaseCfgArgs)this.getData()).getUnknownFields();
            Iterator var3 = var2.getNumbers();
            JsonObject var4 = new JsonObject();

            while(true) {
                JsonArray var5;
                Integer var6;
                Iterator var7;
                do {
                    if (!var3.hasNext()) {
                        JsonObject var10 = new JsonObject();
                        var10.add("fieldMap", var4);
                        var1.add("unknownFieldSet", var10);
                        return var1;
                    }

                    var5 = new JsonArray();
                    var6 = (Integer)var3.next();
                    var7 = var2.getUnknowFields(var6.intValue());
                } while(var7 == null);

                while(var7.hasNext()) {
                    UnknownField var8 = (UnknownField)var7.next();
                    JsonObject var9 = new JsonObject();
                    var9.addProperty("t", var8.getData().toString());
                    var9.addProperty("wireFormat", var8.getWireFormat());
                    var5.add(var9);
                }

                var4.add(String.valueOf(var6), var5);
            }
        } else {
            return var1;
        }
    }

    public void parseJsonFrom(JsonInputStream var1) {
        this.data = var1.read(((TestCaseCfgArgs)this.getData()).getClass());
    }

    public boolean isInitialized() {
        return true;
    }

    public void writeXmlTo(XmlOutputStream var1) throws XMLStreamException {
//        var1.writeStartRoot("TestCaseCfgArgs");
//        var1.writeAttribute("id", ((TestCaseCfgArgs)this.data).getTcId());
//        var1.writeAttribute("config", ((TestCaseCfgArgs)this.data).getCfgId());
//        String var2 = SuperPojoUtils.getStringAnyNode((SuperPojo)this.data);
//        if (var2 != null) {
//            var1.write(var2);
//        }
//
//        if (SuperPojoUtils.getAnyNode((SuperPojo)this.data) != null) {
//            SuperPojoUtils.getAnyNode((SuperPojo)this.data).toXmlByteArray(var1);
//        }

    }

    public void parseXmlFrom(XmlInputStream var1) throws XMLStreamException {
        String var2 = "";
        var1.moveStartRoot("TestCaseCfgArgs");
        int var3 = var1.getCurrentSeq();

        String var4;
        while(var1.hasAttributeNext()) {
            var1.nextAttribute();
            var4 = var1.readName();
            if (var4 == null) {
                break;
            }

            Long var5;
            if (var4.equals("id")) {
                var5 = var1.readLong();
                if (var5 != null) {
                    ((TestCaseCfgArgs)this.data).setTcId(var5.longValue());
                }
            } else if (var4.equals("config")) {
                var5 = var1.readLong();
                if (var5 != null) {
                    ((TestCaseCfgArgs)this.data).setCfgId(var5.longValue());
                }
            } else {
                String var6 = var1.readString();
                System.err.println(String.format("Not found [%s] attribute.skip value [%s]", var4, var6));
            }
        }

        while(var1.hasNodeNext()) {
            var1.nextEvent();
            if (var1.getCurrentEvent().isCharacters()) {
                var4 = var1.getCurrentEvent().asCharacters().getData();
                ((TestCaseCfgArgs)this.data).getUnknownFields().putStringAnyNode(var4);
            }

            var4 = var1.readName(var3);
            if (var4 == null) {
                break;
            }

            ((TestCaseCfgArgs)this.data).getUnknownFields().parseAnyNode(var4, var1);
            System.err.println(String.format("Not found [%s] node.", var4));
        }

    }
}
