package com.pearmilk.legian.taglib;

import org.w3c.dom.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 9/9/13
 * Time: 6:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class TagLibraryBuilder {

    Map<String, TagLibrary> namespaces = new HashMap<String, TagLibrary>();

    public TagLibrary build(Document doc) throws TagLibraryException {

        Element root = doc.getDocumentElement();
        if (!root.getTagName().toLowerCase().equals("taglib"))
            throw new TagLibraryException("Invalid tag library file");
        if (root.getAttribute("namespace").isEmpty())
            throw new TagLibraryException("A namespace must be specified for a tag library");

        TagLibrary tl = new TagLibrary(root.getAttribute("namespace"));

        NodeList children = root.getChildNodes();
        for (int ctr=0; ctr<children.getLength(); ctr++)
        {
            Node node = children.item(ctr);
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Tag t = processTag((Element) node);
                tl.addTag(t);
            }
        }

        return tl;
    }

    Tag processTag(Element e) throws TagLibraryException {

        if (!e.getTagName().toLowerCase().equals("tag"))
            throw new TagLibraryException("Invalid tag: " + e.getTagName());
        if (e.getAttribute("name").isEmpty())
            throw new TagLibraryException("<tag> must have a name");

        Tag t = new Tag(e.getAttribute("name"));

        NamedNodeMap attrs = e.getAttributes();
        for (int ctr=0;ctr<attrs.getLength();ctr++) {
            Attr attr = (Attr) attrs.item(ctr);
            if (attr.getName().toLowerCase().equals("name"))
                continue;
            t.addParameter(attr.getName().toLowerCase(), attr.getValue());
        }

        if (e.getFirstChild() == null)
            throw new TagLibraryException("No code provided for <tag> " + t.getName());

        t.setCode(e.getFirstChild().getNodeValue());

        return t;
    }

}
