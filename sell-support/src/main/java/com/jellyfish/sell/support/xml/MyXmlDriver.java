package com.jellyfish.sell.support.xml;

import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

import java.io.Writer;

public class MyXmlDriver extends XppDriver {

    @Override
    public HierarchicalStreamWriter createWriter(Writer out) {
        return new PrettyPrintWriter(out) {
            boolean cdata = false;

            @Override
            public void startNode(String name, Class clazz) {
                super.startNode(name, clazz);
                cdata = "value".equalsIgnoreCase(name);
            }

            @Override
            public void setValue(String text) {
                super.setValue(text);
            }

            @Override
            public String encodeNode(String name) {
                return name;
            }

            @Override
            protected void writeText(QuickWriter writer, String text) {
                if (cdata) {
                    writer.write("<![CDATA[");
                    writer.write(text);
                    writer.write("]]>");
                } else {
                    writer.write(text);
                }
            }

        };
    }
}
