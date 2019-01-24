package com.theopus.upload.handling;

import com.theopus.parser.obj.AnchorType;
import com.theopus.parser.obj.FileSheet;
import com.theopus.parser.obj.ident.AnchorIdentifier;

import java.util.Map;


//TODO just do it
public class ParserDistributor {

    private final Map<AnchorType, FileSheet<?>> parserMap;
    private final Map<AnchorType, FindAndRemoveStrategy<?>> removeMap;
    private final AnchorIdentifier anchorIdentifier;

    public ParserDistributor(Map<AnchorType, FileSheet<?>> parserMap,
                             Map<AnchorType, FindAndRemoveStrategy<?>> removeMap,
                             AnchorIdentifier anchorIdentifier) {
        this.parserMap = parserMap;
        this.removeMap = removeMap;
        this.anchorIdentifier = anchorIdentifier;
    }

    public FindAndRemoveStrategy removeStrategy(String content) {
        return removeMap.get(parseAnchor(content));
    }

    public FindAndRemoveStrategy removeStrategy(AnchorType type) {
        return removeMap.get(type);
    }

    public FileSheet parser(String content) {
        return parserMap.get(parseAnchor(content));
    }

    public FileSheet parser(AnchorType type) {
        return parserMap.get(type);
    }

    public AnchorType parseAnchor(String content) {
        return anchorIdentifier.identifyAnchor(content);
    }
}
