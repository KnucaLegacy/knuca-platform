package com.theopus.parser.obj;


import java.util.List;

//can be represented as PDF file, raw String, etc.
public interface FullFile {

    List<GroupSheet> parseGroupSheet();
}
