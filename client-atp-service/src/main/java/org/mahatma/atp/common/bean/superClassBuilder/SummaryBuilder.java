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
import org.mahatma.atp.common.bean.PkgCfgArgs;
import org.mahatma.atp.common.bean.Summary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.stream.XMLStreamException;

public class SummaryBuilder extends Builder<Summary> {
    private int memoizedSerializedSize = -1;

    public SummaryBuilder(Summary var1) {
        super(var1);
    }

    public void parsePbFrom(CodedInputStream var1) throws IOException {
        while(true) {
            int var2 = var1.readTag();
            switch(var2) {
            case 0:
                return;
            case 8:
                ((Summary)this.data).putSerializationFieldTag(1);
                ((Summary)this.data).setType(var1.readInt32());
                break;
            case 16:
                ((Summary)this.data).putSerializationFieldTag(2);
                ((Summary)this.data).setId(var1.readInt64());
                break;
            case 26:
                ((Summary)this.data).putSerializationFieldTag(3);
                Object var3 = ((Summary)this.data).getPkgCfgArgsList();
                if (var3 == null) {
                    var3 = new ArrayList();
                    ((Summary)this.data).setPkgCfgArgsList((List)var3);
                }

                PkgCfgArgs var4 = null;
                var4 = new PkgCfgArgs();
                PkgCfgArgsBuilder var5 = new PkgCfgArgsBuilder(var4);
                var1.readMessage(var5);
                if (var4 != null) {
                    ((List)var3).add(var4);
                }
                break;
            default:
                ((Summary)this.data).getUnknownFields().parseUnknownField(var2, var1);
            }
        }
    }

    public void writePbTo(CodedOutputStream var1) throws IOException {
        if (!this.isInitialized()) {
            throw new RuntimeException("required field is null,so stop write.");
        } else {
            this.getSerializedSize();
            if (((Summary)this.data).getType() != 0 || ((Summary)this.data).hasValue(1)) {
                var1.writeInt32(1, ((Summary)this.data).getType());
            }

            if (((Summary)this.data).getId() != 0L || ((Summary)this.data).hasValue(2)) {
                var1.writeInt64(2, ((Summary)this.data).getId());
            }

            if (((Summary)this.data).getPkgCfgArgsList() != null) {
                Iterator var2 = ((Summary)this.data).getPkgCfgArgsList().iterator();

                while(var2.hasNext()) {
                    PkgCfgArgs var3 = (PkgCfgArgs)var2.next();
                    if (var3 != null) {
                        var1.writeMessage(3, new PkgCfgArgsBuilder(var3));
                    }
                }
            }

            ((Summary)this.data).getUnknownFields().writeUnknownField(var1);
        }
    }

    public int getSerializedSize() {
        int var1 = this.memoizedSerializedSize;
        if (var1 != -1) {
            return var1;
        } else {
            var1 = 0;
            if (((Summary)this.data).getType() != 0 || ((Summary)this.data).hasValue(1)) {
                var1 += CodedOutputStream.computeInt32Size(1, ((Summary)this.data).getType());
            }

            if (((Summary)this.data).getId() != 0L || ((Summary)this.data).hasValue(2)) {
                var1 += CodedOutputStream.computeInt64Size(2, ((Summary)this.data).getId());
            }

            if (((Summary)this.data).getPkgCfgArgsList() != null) {
                Iterator var2 = ((Summary)this.data).getPkgCfgArgsList().iterator();

                while(var2.hasNext()) {
                    PkgCfgArgs var3 = (PkgCfgArgs)var2.next();
                    if (var3 != null) {
                        var1 += CodedOutputStream.computeMessageSize(3, new PkgCfgArgsBuilder(var3));
                    }
                }
            }

            var1 = (int)((long)var1 + ((Summary)this.data).getUnknownFields().getSerializedSize());
            this.memoizedSerializedSize = var1;
            return var1;
        }
    }

    public JsonObject toJsonObject() {
        JsonObject var1 = new JsonObject();
        var1.addProperty("type", ((Summary)this.data).getType());
        var1.addProperty("id", ((Summary)this.data).getId());
        Iterator var3;
        if (((Summary)this.data).getPkgCfgArgsList() != null) {
            JsonArray var2 = new JsonArray();
            var3 = ((Summary)this.data).getPkgCfgArgsList().iterator();

            while(var3.hasNext()) {
                PkgCfgArgs var4 = (PkgCfgArgs)var3.next();
                if (var4 != null) {
                    var2.add(var4.toJsonObject());
                }
            }

            var1.add("pkgCfgArgsList", var2);
        }

        if (this.getData() != null && ((Summary)this.getData()).getUnknownFields() != null && ((Summary)this.getData()).getUnknownFields().getNumbers() != null) {
            UnknownFieldSet var10 = ((Summary)this.getData()).getUnknownFields();
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
        this.data = var1.read(((Summary)this.getData()).getClass());
    }

    public boolean isInitialized() {
        return true;
    }

    public void writeXmlTo(XmlOutputStream var1) throws XMLStreamException {
//        var1.writeStartRoot("summary");
//        var1.writeAttribute("type", ((Summary)this.data).getType());
//        var1.writeAttribute("id", ((Summary)this.data).getId());
//        if (((Summary)this.data).getPkgCfgArgsList() != null) {
//            Iterator var2 = ((Summary)this.data).getPkgCfgArgsList().iterator();
//
//            while(var2.hasNext()) {
//                PkgCfgArgs var3 = (PkgCfgArgs)var2.next();
//                if (var3 != null) {
//                    var1.writeStartElement("package");
//                    var1.write(var3);
//                    var1.writeEndElement("package");
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
        var1.moveStartRoot("summary");
        int var3 = var1.getCurrentSeq();

        String var4;
        while(var1.hasAttributeNext()) {
            var1.nextAttribute();
            var4 = var1.readName();
            if (var4 == null) {
                break;
            }

            if (var4.equals("type")) {
                Integer var5 = var1.readInt();
                if (var5 != null) {
                    ((Summary)this.data).setType(var5.intValue());
                }
            } else if (var4.equals("id")) {
                Long var7 = var1.readLong();
                if (var7 != null) {
                    ((Summary)this.data).setId(var7.longValue());
                }
            } else {
                String var8 = var1.readString();
                System.err.println(String.format("Not found [%s] attribute.skip value [%s]", var4, var8));
            }
        }

        while(var1.hasNodeNext()) {
            var1.nextEvent();
            if (var1.getCurrentEvent().isCharacters()) {
                var4 = var1.getCurrentEvent().asCharacters().getData();
                ((Summary)this.data).getUnknownFields().putStringAnyNode(var4);
            }

            var4 = var1.readName(var3);
            if (var4 == null) {
                break;
            }

            if (var4.equals("package")) {
                Object var9 = ((Summary)this.data).getPkgCfgArgsList();
                if (var9 == null) {
                    var9 = new ArrayList();
                    ((Summary)this.data).setPkgCfgArgsList((List)var9);
                }

                PkgCfgArgs var6 = null;
                PkgCfgArgs pkgCfgArgs = new PkgCfgArgs();
                PkgCfgArgsBuilder pkgCfgArgsBuilder = new PkgCfgArgsBuilder(pkgCfgArgs);
                pkgCfgArgsBuilder.parseXmlFrom(var1);
                var6 = pkgCfgArgsBuilder.getData();
                if (var6 != null) {
                    ((List)var9).add(var6);
                }
            } else {
                ((Summary)this.data).getUnknownFields().parseAnyNode(var4, var1);
                System.err.println(String.format("Not found [%s] node.", var4));
            }
        }

    }
}
