package com.theopus.parser.obj.ident;

import com.theopus.parser.exceptions.IllegalParserFileException;
import com.theopus.parser.obj.AnchorType;
import com.theopus.parser.obj.FileSheet;
import com.theopus.parser.obj.Patterns;
import com.theopus.parser.obj.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class AnchorIdentifier {

    private static final Logger LOG = LoggerFactory.getLogger(AnchorIdentifier.class);

    private final FileSheet parser;
    private Map<AnchorType, Sheet> typeSheetMap;

    public AnchorIdentifier(Map<AnchorType, Sheet> typeSheetMap) {
        this.typeSheetMap = typeSheetMap;
        this.parser = FileSheet.create().delimiter(Patterns.Sheet.SHEET_DELIMITER).build();
    }

    public AnchorType identifyAnchor(String text) {
        for (Map.Entry<AnchorType, Sheet> kv : typeSheetMap.entrySet()) {
            Sheet sheet = kv.getValue();
            parser.child(sheet).prepare(text).parse();
            try {
                sheet.parseAnchor();
                return kv.getKey();
            } catch (IllegalParserFileException e) {
                LOG.warn("Not a {} anchored file. \n {}", kv.getKey(), e);
            }
        }
        throw new RuntimeException("Can not find matching type for text.");
    }
}
