package com.pearmilk.legian.builder;

import com.pearmilk.legian.model.*;
import com.pearmilk.legian.taglib.Tag;
import com.pearmilk.legian.taglib.TagLibrary;
import com.pearmilk.legian.taglib.TagLibraryBuilder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 25/7/13
 * Time: 6:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class SCXMLBuilder {

    private Map<String, SCXMLItemBuilder> builders = new HashMap<String, SCXMLItemBuilder>();
    private Map<String, TagLibrary> taglibs = new HashMap<String, TagLibrary>();
    private Map<String,String> includes = new HashMap<String,String>();
    private Set<String> modules = new HashSet<String>();
    private Path currentPath;

    public SCXMLBuilder() {

        addBuilder(new SCXMLStateBuilder());
        addBuilder(new SCXMLTransitionBuilder());
        addBuilder(new SCXMLScriptBuilder());
        addBuilder(new SCXMLEntryExitBuilder());
        addBuilder(new SCXMLLogBuilder());
        addBuilder(new SCXMLIncludeBuilder());
        addBuilder(new SCXMLExecuteBuilder());
        addBuilder(new SCXMLAssignBuilder());
        addBuilder(new SCXMLConditionBuilder());
        addBuilder(new SCXMLParamBuilder());
        addBuilder(new SCXMLEventBuilder());

    }

    public void addTagLibrary(TagLibrary tl) {
        taglibs.put(tl.getNamespace(), tl);
    }

    public Path getCurrentPath() {
        return currentPath;
    }

    public void addInclude(String moduleName, String fileName) throws SCXMLElementException {

        String fn = includes.get(moduleName);
        if (fn != null && (!fn.equals(fileName)))
            throw new SCXMLElementException(moduleName + " has already been decalred by " + fn);
        includes.put(moduleName, fileName);
        modules.add(moduleName);
    }

    public Map<String,String> getIncludes() {
        return includes;
    }

    public Set<String> getModules() {
        return modules;
    }

    private Path getAbsolutePath(Path parent, String child) throws IOException {
        if (parent == null)
            parent = FileSystems.getDefault().getPath(System.getProperty("user.dir"));

        Path apath = FileSystems.getDefault().getPath(child);
        if (apath.isAbsolute())
            apath = apath.toAbsolutePath().toRealPath();
        else
            apath = parent.toAbsolutePath().resolve(apath).toAbsolutePath().toRealPath();
        return apath;
    }

    private void scanTagLibraries(Element root) throws SCXMLElementException, IOException {

        NodeList children = root.getChildNodes();
        for (int ctr=0; ctr<children.getLength(); ctr++)
        {
            Node node = children.item(ctr);
            if (node.getNodeType() == Node.ELEMENT_NODE && ((Element)node).getTagName().toLowerCase().equals("taglib"))
            {
                if (((Element)node).getAttribute("src").isEmpty())
                    throw new SCXMLElementException("Missing src attribute in <taglib> tag");
                String srcfile = ((Element)node).getAttribute("src");
                node.getParentNode().removeChild(node);
                processTagLibFile(getAbsolutePath(currentPath, srcfile).toAbsolutePath().toString());
            }
        }
    }

    private void processTagLibFile(String filename) throws SCXMLElementException {

        try {

            DocumentBuilderFactory xmlFactory = DocumentBuilderFactory.newInstance();
            xmlFactory.setCoalescing(true);
            DocumentBuilder builder = xmlFactory.newDocumentBuilder();

            InputStream content = new FileInputStream(filename);
            Document doc =  builder.parse(content);

            TagLibrary tl = new TagLibraryBuilder().build(doc);
            if (!taglibs.containsKey(tl.getNamespace())) {
                taglibs.put(tl.getNamespace(), tl);
            }
            else {
                for (Tag tag : tl.getTags()) {
                    taglibs.get(tl.getNamespace()).addTag(tag);
                }
            }
        }
        catch(Exception e) {
            throw new SCXMLElementException("Unable to process file " + filename, e);
        }

    }

    public void addBuilder(SCXMLItemBuilder builder){

        for (String tag : builder.getTagNames())
            builders.put(tag, builder);

    }

    public StateChartModel build(File file)
            throws SCXMLElementException, ParserConfigurationException, SAXException, IOException {

        currentPath = Paths.get(file.getAbsolutePath()).getParent();

        DocumentBuilderFactory xmlFactory = DocumentBuilderFactory.newInstance();
        xmlFactory.setCoalescing(true);
        DocumentBuilder builder = xmlFactory.newDocumentBuilder();

        InputStream content = new FileInputStream(file);
        Document doc =  builder.parse(content);
        return build(doc);
    }

    public StateChartModel build(Document doc) throws SCXMLElementException, IOException
    {
        Element root = doc.getDocumentElement();

        scanTagLibraries(root);

        Model m = processElement(root, null);
        return new StateChartModel((StateModel) m, modules);
    }

    private Model processCustomElement(String namespace, String tagName, Element e, Model parent)
            throws SCXMLElementException {

     //   try {

            if (!taglibs.containsKey(namespace))
                throw new SCXMLElementException("Unable to find namespace definition " + namespace);
            if (taglibs.get(namespace).getTag(tagName) == null)
                throw new SCXMLElementException("Unable to find definition of tag " + tagName);

            Tag t = taglibs.get(namespace).getTag(tagName);

            Map<String,String> vals = new HashMap<String,String>();

            NamedNodeMap attrs = e.getAttributes();
            for (int ctr=0;ctr<attrs.getLength();ctr++) {
                Attr attr = (Attr) attrs.item(ctr);
                if (attr.getName().toLowerCase().equals("name"))
                    continue;
                vals.put(attr.getName().toLowerCase(), attr.getValue());
            }

            Model m = new StackableRawCodeProvicerModel(t.generateCode(vals));
          //  parent.appendChild(m);

            return m;
     //   }
     //   catch (InvalidModelException ex) {
     //       throw new SCXMLElementException(ex);
     //   }
    }

    private Model processElement(Element e, Model parent)
            throws SCXMLElementException {

        if (!e.getTagName().contains(":")) {

            SCXMLItemBuilder builder = builders.get(e.getTagName().toLowerCase());
            if (builder == null)
                throw new SCXMLElementException(e.getTagName().toLowerCase());

            Model m = builder.build(e, parent, this);
            processChildren(e, m);
            builder.complete(m);
            return m;
        }
        else {
            String[] ftn = e.getTagName().split(":");
            Model m = processCustomElement(ftn[0], ftn[1], e, parent);
            processChildren(e, m);
            return m;
            //return null;
        }
    }

    private void processChildren(Element e, Model parent) throws SCXMLElementException {

        try
        {
            NodeList children = e.getChildNodes();
            for (int ctr=0; ctr<children.getLength(); ctr++)
            {
                Node node = children.item(ctr);
                if (node.getNodeType() == Node.ELEMENT_NODE)
                {
                    Model m = processElement((Element)node, parent);
                    if (m != null)
                        parent.appendChild(m);
                }
            }
        }
        catch(InvalidModelException me){
            throw new SCXMLElementException(me.getMessage());
        }
    }

}
