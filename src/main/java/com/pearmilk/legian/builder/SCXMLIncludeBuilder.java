package com.pearmilk.legian.builder;

import com.pearmilk.legian.model.Model;
import com.pearmilk.legian.model.RootStateModel;
import com.pearmilk.legian.model.StateChartModel;
import com.pearmilk.legian.model.StateModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 9/9/13
 * Time: 2:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class SCXMLIncludeBuilder implements SCXMLItemBuilder {
    @Override
    public String[] getTagNames() {
        return new String[] {"include"};
    }

    @Override
    public Model build(Element element, Model parent, SCXMLBuilder parentBuilder) throws SCXMLElementException {

        if (!(parent instanceof StateModel))
            throw new SCXMLElementException("The <include> tag must be a child of a <state> tag");
        if (parent instanceof RootStateModel)
            throw new SCXMLElementException("The <include> tag must be a child of a <state> tag");
        if (element.getAttribute("src").isEmpty())
            throw new SCXMLElementException("An src attribute must be specified in the <include> statement");

        Document doc;

        try {

            DocumentBuilderFactory xmlFactory = DocumentBuilderFactory.newInstance();
            xmlFactory.setCoalescing(true);
            DocumentBuilder builder = xmlFactory.newDocumentBuilder();

          //  InputStream content = new FileInputStream(element.getAttribute("src"));
          //  doc =  builder.parse(content);

            SCXMLBuilder scxmlBuilder = new SCXMLBuilder();
            StateChartModel scm = scxmlBuilder.build(
                    parentBuilder.getCurrentPath() != null ?
                            new File(parentBuilder.getCurrentPath().toFile(), element.getAttribute("src")) :
                            new File(element.getAttribute("src"))
            );
            List<StateModel> children = scm.getRootModel().getChildren(new StateModel[0]);

            for (StateModel m : children) {
                parent.appendChild(m);
            }

            for (Map.Entry<String,String> e : scxmlBuilder.getIncludes().entrySet()) {
                parentBuilder.addInclude(e.getKey(), e.getValue());
            }

            parentBuilder.getModules().addAll(scxmlBuilder.getModules());

            return null;

        }
        catch(Exception e){
            throw new SCXMLElementException("Error when processing file: " + element.getAttribute("src"));
        }
    }

    @Override
    public void complete(Model m) throws SCXMLElementException {

    }
}
