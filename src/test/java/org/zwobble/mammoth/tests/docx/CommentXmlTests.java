package org.zwobble.mammoth.tests.docx;

import org.junit.jupiter.api.Test;
import org.zwobble.mammoth.internal.documents.Comment;
import org.zwobble.mammoth.internal.docx.CommentXmlReader;
import org.zwobble.mammoth.internal.results.InternalResult;
import org.zwobble.mammoth.internal.xml.XmlNode;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.isA;
import static org.zwobble.mammoth.internal.util.Lists.list;
import static org.zwobble.mammoth.internal.util.Maps.map;
import static org.zwobble.mammoth.internal.xml.XmlNodes.element;
import static org.zwobble.mammoth.tests.DeepReflectionMatcher.deepEquals;
import static org.zwobble.mammoth.tests.ResultMatchers.isInternalSuccess;
import static org.zwobble.mammoth.tests.documents.DocumentElementMakers.paragraph;
import static org.zwobble.mammoth.tests.docx.BodyXmlReaderMakers.bodyReader;

public class CommentXmlTests {
    @Test
    public void idAndBodyOfCommentIsRead() {
        List<XmlNode> body = list(element("w:p"));
        CommentXmlReader reader = new CommentXmlReader(bodyReader());
        InternalResult<List<Comment>> result = reader.readElement(element("w:comments", list(
            element("w:comment", map("w:id", "1"), body)
        )));
        assertThat(
            result,
            isInternalSuccess(contains(
                allOf(
                    isA(Comment.class),
                    hasProperty("commentId", equalTo("1")),
                    hasProperty("body", deepEquals(list(paragraph())))
                )
            ))
        );
    }

    @Test
    public void whenOptionalAttributesOfCommentAreMissingThenTheyAreReadAsNone() {
        CommentXmlReader reader = new CommentXmlReader(bodyReader());
        InternalResult<List<Comment>> result = reader.readElement(element("w:comments", list(
            element("w:comment", map("w:id", "1"))
        )));
        assertThat(
            result,
            isInternalSuccess(contains(
                allOf(
                    isA(Comment.class),
                    hasProperty("authorName", equalTo(Optional.empty())),
                    hasProperty("authorInitials", equalTo(Optional.empty()))
                )
            ))
        );
    }

    @Test
    public void whenOptionalAttributesOfCommentAreBlankThenTheyAreReadAsNone() {
        CommentXmlReader reader = new CommentXmlReader(bodyReader());
        InternalResult<List<Comment>> result = reader.readElement(element("w:comments", list(
            element("w:comment", map("w:id", "1", "w:author", " ", "w:initials", " "))
        )));
        assertThat(
            result,
            isInternalSuccess(contains(
                allOf(
                    isA(Comment.class),
                    hasProperty("authorName", equalTo(Optional.empty())),
                    hasProperty("authorInitials", equalTo(Optional.empty()))
                )
            ))
        );
    }

    @Test
    public void whenOptionalAttributesOfCommentAreNotBlankThenTheyAreRead() {
        CommentXmlReader reader = new CommentXmlReader(bodyReader());
        InternalResult<List<Comment>> result = reader.readElement(element("w:comments", list(
            element("w:comment", map("w:id", "1", "w:author", "The Piemaker", "w:initials", "TP"))
        )));
        assertThat(
            result,
            isInternalSuccess(contains(
                allOf(
                    isA(Comment.class),
                    hasProperty("authorName", equalTo(Optional.of("The Piemaker"))),
                    hasProperty("authorInitials", equalTo(Optional.of("TP")))
                )
            ))
        );
    }
}
