package org.zwobble.mammoth.tests.docx;

import org.junit.jupiter.api.Test;
import org.zwobble.mammoth.internal.documents.Document;
import org.zwobble.mammoth.internal.documents.Note;
import org.zwobble.mammoth.internal.documents.NoteType;
import org.zwobble.mammoth.internal.documents.Notes;
import org.zwobble.mammoth.internal.docx.DocumentXmlReader;
import org.zwobble.mammoth.internal.results.InternalResult;
import org.zwobble.mammoth.internal.xml.XmlElement;
import org.zwobble.mammoth.internal.xml.XmlNodes;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.zwobble.mammoth.internal.util.Lists.list;
import static org.zwobble.mammoth.internal.xml.XmlNodes.element;
import static org.zwobble.mammoth.tests.DeepReflectionMatcher.deepEquals;
import static org.zwobble.mammoth.tests.ResultMatchers.isInternalSuccess;
import static org.zwobble.mammoth.tests.documents.DocumentElementMakers.document;
import static org.zwobble.mammoth.tests.documents.DocumentElementMakers.paragraphWithText;
import static org.zwobble.mammoth.tests.documents.DocumentElementMakers.withChildren;
import static org.zwobble.mammoth.tests.docx.BodyXmlReaderMakers.bodyReader;

public class DocumentXmlTests {

    @Test
    public void canReadTextWithinDocument() {
        XmlElement documentElement = element("w:document", list(
            element("w:body", list(
                element("w:p", list(
                    element("w:r", list(
                        element("w:t", list(
                            XmlNodes.text("Hello!")))))))))));

        DocumentXmlReader reader = new DocumentXmlReader(bodyReader(), Notes.EMPTY, list());

        assertThat(
            reader.readElement(documentElement),
            isInternalSuccess(document(
                withChildren(paragraphWithText("Hello!"))
            ))
        );
    }

    @Test
    public void notesOfDocumentAreIncludedInDocument() {
        Note note = new Note(NoteType.FOOTNOTE, "4", list(paragraphWithText("Hello")));
        Notes notes = new Notes(list(note));
        DocumentXmlReader reader = new DocumentXmlReader(bodyReader(), notes, list());

        XmlElement documentElement = element("w:document", list(element("w:body")));
        InternalResult<Document> document = reader.readElement(documentElement);

        assertThat(
            document.getValue().getNotes().findNote(NoteType.FOOTNOTE, "4").get(),
            deepEquals(note));
    }
}
