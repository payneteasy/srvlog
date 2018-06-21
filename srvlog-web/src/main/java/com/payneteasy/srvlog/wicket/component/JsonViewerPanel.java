package com.payneteasy.srvlog.wicket.component;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;


public class JsonViewerPanel extends Panel {

    public JsonViewerPanel(String id, String aJson) {
        super(id, new Model<>(aJson));

        add(new Label("json", aJson));
    }

}
