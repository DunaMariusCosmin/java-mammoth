package org.zwobble.mammoth.tests.documents;

import com.natpryce.makeiteasy.Instantiator;
import com.natpryce.makeiteasy.Property;
import org.zwobble.mammoth.documents.*;

import java.util.List;
import java.util.Optional;

import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;
import static com.natpryce.makeiteasy.MakeItEasy.with;
import static com.natpryce.makeiteasy.Property.newProperty;
import static org.zwobble.mammoth.util.MammothLists.list;

public class DocumentElementMakers {
    public static final Property<HasChildren, Optional<Style>> STYLE = newProperty();
    public static final Property<HasChildren, Optional<NumberingLevel>> NUMBERING = newProperty();
    public static final Property<HasChildren, Boolean> BOLD = newProperty();
    public static final Property<HasChildren, Boolean> ITALIC = newProperty();
    public static final Property<HasChildren, Boolean> UNDERLINE = newProperty();
    public static final Property<HasChildren, Boolean> STRIKETHROUGH = newProperty();
    public static final Property<HasChildren, VerticalAlignment> VERTICAL_ALIGNMENT = newProperty();
    public static final Property<HasChildren, List<DocumentElement>> CHILDREN = newProperty();

    public static final Instantiator<Paragraph> PARAGRAPH =
        propertyLookup -> new Paragraph(
            propertyLookup.valueOf(STYLE, Optional.empty()),
            propertyLookup.valueOf(NUMBERING, Optional.empty()),
            propertyLookup.valueOf(CHILDREN, list()));

    public static final Instantiator<Run> RUN =
        propertyLookup -> new Run(
            propertyLookup.valueOf(BOLD, false),
            propertyLookup.valueOf(ITALIC, false),
            propertyLookup.valueOf(UNDERLINE, false),
            propertyLookup.valueOf(STRIKETHROUGH, false),
            propertyLookup.valueOf(VERTICAL_ALIGNMENT, VerticalAlignment.BASELINE),
            Optional.empty(),
            propertyLookup.valueOf(CHILDREN, list()));

    public static Paragraph paragraphWithText(String text) {
        return make(a(PARAGRAPH, with(CHILDREN, list(runWithText(text)))));
    }

    public static Run runWithText(String text) {
        return make(a(RUN, with(CHILDREN, list(new Text(text)))));
    }
}
