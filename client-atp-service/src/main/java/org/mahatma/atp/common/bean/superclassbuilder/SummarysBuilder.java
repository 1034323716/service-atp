//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.mahatma.atp.common.bean.superclassbuilder;

import com.feinno.superpojo.Builder;
import com.feinno.superpojo.UnknownField;
import com.feinno.superpojo.UnknownFieldSet;
import com.feinno.superpojo.io.CodedInputStream;
import com.feinno.superpojo.io.CodedOutputStream;
import com.feinno.superpojo.io.JsonInputStream;
import com.feinno.superpojo.io.XmlInputStream;
import com.feinno.superpojo.io.XmlOutputStream;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.mahatma.atp.common.bean.Summary;
import org.mahatma.atp.common.bean.Summarys;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.stream.XMLStreamException;

public class SummarysBuilder extends Builder<Summarys> {
    private int memoizedSerializedSize = -1;

    public SummarysBuilder(Summarys var1) {
        super(var1);
    }

    public void parsePbFrom(CodedInputStream var1) throws IOException {
        while(true) {
            int var2 = var1.readTag();
            switch(var2) {
            case 0:
                return;
            case 10:
                ((Summarys)this.data).putSerializationFieldTag(1);
                Object var3 = ((Summarys)this.data).getSummary();
                if (var3 == null) {
                    var3 = new ArrayList();
                    ((Summarys)this.data).setSummary((List)var3);
                }

                Summary var4 = null;
                var4 = new Summary();
                SummaryBuilder var5 = new SummaryBuilder(var4);
                var1.readMessage(var5);
                if (var4 != null) {
                    ((List)var3).add(var4);
                }
                break;
            default:
                ((Summarys)this.data).getUnknownFields().parseUnknownField(var2, var1);
            }
        }
    }

    public void writePbTo(CodedOutputStream var1) throws IOException {
        if (!this.isInitialized()) {
            throw new RuntimeException("required field is null,so stop write.");
        } else {
            this.getSerializedSize();
            if (((Summarys)this.data).getSummary() != null) {
                Iterator var2 = ((Summarys)this.data).getSummary().iterator();

                while(var2.hasNext()) {
                    Summary var3 = (Summary)var2.next();
                    if (var3 != null) {
                        var1.writeMessage(1, new SummaryBuilder(var3));
                    }
                }
            }

            ((Summarys)this.data).getUnknownFields().writeUnknownField(var1);
        }
    }

    public int getSerializedSize() {
        int var1 = this.memoizedSerializedSize;
        if (var1 != -1) {
            return var1;
        } else {
            var1 = 0;
            if (((Summarys)this.data).getSummary() != null) {
                Iterator var2 = ((Summarys)this.data).getSummary().iterator();

                while(var2.hasNext()) {
                    Summary var3 = (Summary)var2.next();
                    if (var3 != null) {
                        var1 += CodedOutputStream.computeMessageSize(1, new SummaryBuilder(var3));
                    }
                }
            }

            var1 = (int)((long)var1 + ((Summarys)this.data).getUnknownFields().getSerializedSize());
            this.memoizedSerializedSize = var1;
            return var1;
        }
    }

    public JsonObject toJsonObject() {
        JsonObject var1 = new JsonObject();
        Iterator var3;
        if (((Summarys)this.data).getSummary() != null) {
            JsonArray var2 = new JsonArray();
            var3 = ((Summarys)this.data).getSummary().iterator();

            while(var3.hasNext()) {
                Summary var4 = (Summary)var3.next();
                if (var4 != null) {
                    var2.add(var4.toJsonObject());
                }
            }

            var1.add("summary", var2);
        }

        if (this.getData() != null && ((Summarys)this.getData()).getUnknownFields() != null && ((Summarys)this.getData()).getUnknownFields().getNumbers() != null) {
            UnknownFieldSet var10 = ((Summarys)this.getData()).getUnknownFields();
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
        this.data = var1.read(((Summarys)this.getData()).getClass());
    }

    public boolean isInitialized() {
        return true;
    }

    public void writeXmlTo(XmlOutputStream var1) throws XMLStreamException {
//        var1.writeStartRoot("summarys");
//        if (((Summarys)this.data).getSummary() != null) {
//            Iterator var2 = ((Summarys)this.data).getSummary().iterator();
//
//            while(var2.hasNext()) {
//                Summary var3 = (Summary)var2.next();
//                if (var3 != null) {
//                    var1.writeStartElement("summary");
//                    var1.write(var3);
//                    var1.writeEndElement("summary");
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
        var1.moveStartRoot("summarys");
        int var3 = var1.getCurrentSeq();

        String var4;
        while(var1.hasAttributeNext()) {
            var1.nextAttribute();
            var4 = var1.readName();
            if (var4 == null) {
                break;
            }

            String var5 = var1.readString();
            System.err.println(String.format("Not found [%s] attribute.skip value [%s]", var4, var5));
        }

        while(var1.hasNodeNext()) {
            var1.nextEvent();
            if (var1.getCurrentEvent().isCharacters()) {
                var4 = var1.getCurrentEvent().asCharacters().getData();
                ((Summarys)this.data).getUnknownFields().putStringAnyNode(var4);
            }

            var4 = var1.readName(var3);
            if (var4 == null) {
                break;
            }

            if (var4.equals("summary")) {
                Object var7 = ((Summarys)this.data).getSummary();
                if (var7 == null) {
                    var7 = new ArrayList();
                    ((Summarys)this.data).setSummary((List)var7);
                }

                Summary var6 = null;
                Summary summary = new Summary();
                SummaryBuilder summaryBuilder = new SummaryBuilder(summary);
                summaryBuilder.parseXmlFrom(var1);
                var6 = summaryBuilder.getData();
                if (var6 != null) {
                    ((List)var7).add(var6);
                }
            } else {
                ((Summarys)this.data).getUnknownFields().parseAnyNode(var4, var1);
                System.err.println(String.format("Not found [%s] node.", var4));
            }
        }

    }
}
