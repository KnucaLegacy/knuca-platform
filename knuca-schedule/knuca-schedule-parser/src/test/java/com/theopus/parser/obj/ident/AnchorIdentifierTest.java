package com.theopus.parser.obj.ident;

import com.google.common.collect.ImmutableMap;
import com.theopus.entity.schedule.Group;
import com.theopus.parser.ParserUtils;
import com.theopus.parser.obj.AnchorType;
import com.theopus.parser.obj.Sheet;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class AnchorIdentifierTest {

    @Test
    public void groupIdentify() throws Exception {
        Sheet<Group> build = new IdentifierGroupSheet();
        AnchorIdentifier anchorIdentifier = new AnchorIdentifier(ImmutableMap.of(AnchorType.GROUP, build));

        String s = ParserUtils.pdfFileToString(new File("src/test/resources/pdfs/full/small/fait.pdf"));
        AnchorType actual = anchorIdentifier.identifyAnchor(s);
        assertEquals(actual, AnchorType.GROUP);
    }
}