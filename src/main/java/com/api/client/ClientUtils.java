package com.api.client;

import javax.swing.text.Element;
import javax.swing.text.ElementIterator;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import java.io.Serializable;

public class ClientUtils implements Serializable {

    public static String getAttributeOfElement(HTMLDocument doc, String elementName, HTML.Attribute knownAttribute, String knownValue, HTML.Attribute desiredAttribute) {
        ElementIterator it = new ElementIterator(doc);
        Element elem;

        while ((elem = it.next()) != null) {
            if (elem.getName().equals(elementName)) {
                String name = (String) elem.getAttributes().getAttribute(knownAttribute);
                if (name == null) continue;

                if (name.equals(knownValue))
                    return (String) elem.getAttributes().getAttribute(desiredAttribute);
            }
        }
        return "";
    }

    public static String getAttributeOfElement(HTMLDocument doc, String elementName, HTML.Attribute desiredAttribute) {
        ElementIterator it = new ElementIterator(doc);
        Element elem;

        while ((elem = it.next()) != null) {
            if (elem.getName().equals(elementName))
                return (String) elem.getAttributes().getAttribute(desiredAttribute);
        }
        return "";
    }

    public static boolean hasAttributeValue(HTMLDocument doc, String elementName, HTML.Attribute attribute, String value) {
        ElementIterator it = new ElementIterator(doc);
        Element elem;

        while ((elem = it.next()) != null) {
            if (elem.getName().equals(elementName)) {
                String name = (String) elem.getAttributes().getAttribute(attribute);
                if (name == null) continue;

                if (name.equals(value))
                    return true;
            }
        }
        return false;
    }
}