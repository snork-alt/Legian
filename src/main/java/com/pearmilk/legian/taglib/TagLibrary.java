package com.pearmilk.legian.taglib;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 9/9/13
 * Time: 6:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class TagLibrary {

    String namespace;
    Map<String,Tag> tags = new HashMap<String, Tag>();

    public TagLibrary(String namespace) {
        this.namespace = namespace;
    }

    public void addTag(Tag value) {
        tags.put(value.getName(), value);
    }

    public String getNamespace() {
        return namespace;
    }

    public Tag getTag(String key) {
        return tags.get(key);
    }

    public Collection<Tag> getTags() {
        return tags.values();
    }
}
