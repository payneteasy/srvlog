package com.payneteasy.srvlog.wicket.component;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.AbstractChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.util.value.IValueMap;

import java.util.Collection;
import java.util.List;

/**
 * Component for integration with multi-select-dropdown.js functionality
 */
public class MultiSelectDropdown<T> extends AbstractChoice<Collection<T>, T> {

    public MultiSelectDropdown(final String id,
                               final List<? extends T> choices,
                               final IChoiceRenderer<? super T> renderer) {

        super(id, choices, renderer);
    }

    @Override
    protected boolean isSelected(T choice, int index, String selected) {
        return false;
    }

    @Override
    protected void onComponentTag(ComponentTag tag) {
        checkComponentTag(tag, "select");
        super.onComponentTag(tag);
        IValueMap attrs = tag.getAttributes();
        attrs.put("multiple", null);
        attrs.put("data-multi-select", null);
    }
}
