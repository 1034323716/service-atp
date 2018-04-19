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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.stream.XMLStreamException;

import org.mahatma.atp.common.bean.PkgCfgArgs;
import org.mahatma.atp.common.bean.PkgCfgArgs.TestCaseCfgArgs;
import org.mahatma.atp.common.bean.Summary;

public class PkgCfgArgsBuilder extends Builder<PkgCfgArgs> {
    private int memoizedSerializedSize = -1;

    public PkgCfgArgsBuilder(PkgCfgArgs var1) {
        super(var1);
    }

    public void parsePbFrom(CodedInputStream var1) throws IOException {
        while(true) {
            int var2 = var1.readTag();
            switch(var2) {
            case 0:
                return;
            case 8:
                ((PkgCfgArgs)this.data).putSerializationFieldTag(1);
                ((PkgCfgArgs)this.data).setPkgId(var1.readInt64());
                break;
            case 16:
                ((PkgCfgArgs)this.data).putSerializationFieldTag(2);
                ((PkgCfgArgs)this.data).setCfgId(var1.readInt64());
                break;
            case 26:
                ((PkgCfgArgs)this.data).putSerializationFieldTag(3);
                Object var3 = ((PkgCfgArgs)this.data).getTestCaseCfgArgs();
                if (var3 == null) {
                    var3 = new ArrayList();
                    ((PkgCfgArgs)this.data).setTestCaseCfgArgs((List)var3);
                }

                TestCaseCfgArgs var4 = null;
                var4 = new TestCaseCfgArgs();
                TestCaseCfgArgsBuilder var5 = new TestCaseCfgArgsBuilder(var4);
                var1.readMessage(var5);
                if (var4 != null) {
                    ((List)var3).add(var4);
                }
                break;
            default:
                ((PkgCfgArgs)this.data).getUnknownFields().parseUnknownField(var2, var1);
            }
        }
    }

    public void writePbTo(CodedOutputStream var1) throws IOException {
        if (!this.isInitialized()) {
            throw new RuntimeException("required field is null,so stop write.");
        } else {
            this.getSerializedSize();
            if (((PkgCfgArgs)this.data).getPkgId() != 0L || ((PkgCfgArgs)this.data).hasValue(1)) {
                var1.writeInt64(1, ((PkgCfgArgs)this.data).getPkgId());
            }

            if (((PkgCfgArgs)this.data).getCfgId() != 0L || ((PkgCfgArgs)this.data).hasValue(2)) {
                var1.writeInt64(2, ((PkgCfgArgs)this.data).getCfgId());
            }

            if (((PkgCfgArgs)this.data).getTestCaseCfgArgs() != null) {
                Iterator var2 = ((PkgCfgArgs)this.data).getTestCaseCfgArgs().iterator();

                while(var2.hasNext()) {
                    TestCaseCfgArgs var3 = (TestCaseCfgArgs)var2.next();
                    if (var3 != null) {
                        var1.writeMessage(3, new TestCaseCfgArgsBuilder(var3));
                    }
                }
            }

            ((PkgCfgArgs)this.data).getUnknownFields().writeUnknownField(var1);
        }
    }

    public int getSerializedSize() {
        int var1 = this.memoizedSerializedSize;
        if (var1 != -1) {
            return var1;
        } else {
            var1 = 0;
            if (((PkgCfgArgs)this.data).getPkgId() != 0L || ((PkgCfgArgs)this.data).hasValue(1)) {
                var1 += CodedOutputStream.computeInt64Size(1, ((PkgCfgArgs)this.data).getPkgId());
            }

            if (((PkgCfgArgs)this.data).getCfgId() != 0L || ((PkgCfgArgs)this.data).hasValue(2)) {
                var1 += CodedOutputStream.computeInt64Size(2, ((PkgCfgArgs)this.data).getCfgId());
            }

            if (((PkgCfgArgs)this.data).getTestCaseCfgArgs() != null) {
                Iterator var2 = ((PkgCfgArgs)this.data).getTestCaseCfgArgs().iterator();

                while(var2.hasNext()) {
                    TestCaseCfgArgs var3 = (TestCaseCfgArgs)var2.next();
                    if (var3 != null) {
                        var1 += CodedOutputStream.computeMessageSize(3, new TestCaseCfgArgsBuilder(var3));
                    }
                }
            }

            var1 = (int)((long)var1 + ((PkgCfgArgs)this.data).getUnknownFields().getSerializedSize());
            this.memoizedSerializedSize = var1;
            return var1;
        }
    }

    public JsonObject toJsonObject() {
        JsonObject var1 = new JsonObject();
        var1.addProperty("pkgId", ((PkgCfgArgs)this.data).getPkgId());
        var1.addProperty("cfgId", ((PkgCfgArgs)this.data).getCfgId());
        Iterator var3;
        if (((PkgCfgArgs)this.data).getTestCaseCfgArgs() != null) {
            JsonArray var2 = new JsonArray();
            var3 = ((PkgCfgArgs)this.data).getTestCaseCfgArgs().iterator();

            while(var3.hasNext()) {
                TestCaseCfgArgs var4 = (TestCaseCfgArgs)var3.next();
                if (var4 != null) {
                    var2.add(var4.toJsonObject());
                }
            }

            var1.add("testCaseCfgArgs", var2);
        }

        if (this.getData() != null && ((PkgCfgArgs)this.getData()).getUnknownFields() != null && ((PkgCfgArgs)this.getData()).getUnknownFields().getNumbers() != null) {
            UnknownFieldSet var10 = ((PkgCfgArgs)this.getData()).getUnknownFields();
            var3 = var10.getNumbers();
            JsonObject var11 = new JsonObject();

            while(true) {
                JsonArray var5;
                Integer var6;
                Iterator var7;
                do {
                    if (!var3.hasNext()) {
                        JsonObject var12 = new JsonObject();
                        var12.add("fieldMap", var11);
                        var1.add("unknownFieldSet", var12);
                        return var1;
                    }

                    var5 = new JsonArray();
                    var6 = (Integer)var3.next();
                    var7 = var10.getUnknowFields(var6.intValue());
                } while(var7 == null);

                while(var7.hasNext()) {
                    UnknownField var8 = (UnknownField)var7.next();
                    JsonObject var9 = new JsonObject();
                    var9.addProperty("t", var8.getData().toString());
                    var9.addProperty("wireFormat", var8.getWireFormat());
                    var5.add(var9);
                }

                var11.add(String.valueOf(var6), var5);
            }
        } else {
            return var1;
        }
    }

    public void parseJsonFrom(JsonInputStream var1) {
        this.data = var1.read(((PkgCfgArgs)this.getData()).getClass());
    }

    public boolean isInitialized() {
        return true;
    }

    public void writeXmlTo(XmlOutputStream var1) throws XMLStreamException {
//        var1.writeStartRoot("package");
//        var1.writeAttribute("id", ((PkgCfgArgs)this.data).getPkgId());
//        var1.writeAttribute("config", ((PkgCfgArgs)this.data).getCfgId());
//        if (((PkgCfgArgs)this.data).getTestCaseCfgArgs() != null) {
//            Iterator var2 = ((PkgCfgArgs)this.data).getTestCaseCfgArgs().iterator();
//
//            while(var2.hasNext()) {
//                TestCaseCfgArgs var3 = (TestCaseCfgArgs)var2.next();
//                if (var3 != null) {
//                    var1.writeStartElement("testCase");
//                    var1.write(var3);
//                    var1.writeEndElement("testCase");
//                }
//            }
//        }
//
//        String var4 = SuperPojoUtils.getStringAnyNode((SuperPojo)this.data);
//        if (var4 != null) {
//            var1.write(var4);
//        }
//
//        if (SuperPojoUtils.getAnyNode((SuperPojo)this.data) != null) {
//            SuperPojoUtils.getAnyNode((SuperPojo)this.data).toXmlByteArray(var1);
//        }

    }

    public void parseXmlFrom(XmlInputStream var1) throws XMLStreamException {
        String var2 = "";
        var1.moveStartRoot("package");
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
                    ((PkgCfgArgs)this.data).setPkgId(var5.longValue());
                }
            } else if (var4.equals("config")) {
                var5 = var1.readLong();
                if (var5 != null) {
                    ((PkgCfgArgs)this.data).setCfgId(var5.longValue());
                }
            } else {
                String var7 = var1.readString();
                System.err.println(String.format("Not found [%s] attribute.skip value [%s]", var4, var7));
            }
        }

        while(var1.hasNodeNext()) {
            var1.nextEvent();
            if (var1.getCurrentEvent().isCharacters()) {
                var4 = var1.getCurrentEvent().asCharacters().getData();
                ((PkgCfgArgs)this.data).getUnknownFields().putStringAnyNode(var4);
            }

            var4 = var1.readName(var3);
            if (var4 == null) {
                break;
            }

            if (var4.equals("testCase")) {
                Object var8 = ((PkgCfgArgs)this.data).getTestCaseCfgArgs();
                if (var8 == null) {
                    var8 = new ArrayList();
                    ((PkgCfgArgs)this.data).setTestCaseCfgArgs((List)var8);
                }

                TestCaseCfgArgs var6 = null;
                TestCaseCfgArgs testCaseCfgArgs = new TestCaseCfgArgs();
                TestCaseCfgArgsBuilder testCaseCfgArgsBuilder = new TestCaseCfgArgsBuilder(testCaseCfgArgs);
                testCaseCfgArgsBuilder.parseXmlFrom(var1);
                var6 = testCaseCfgArgsBuilder.getData();
                if (var6 != null) {
                    ((List)var8).add(var6);
                }
            } else {
                ((PkgCfgArgs)this.data).getUnknownFields().parseAnyNode(var4, var1);
                System.err.println(String.format("Not found [%s] node.", var4));
            }
        }

    }
}
