package com.theopus.parser.obj.sheets;

import com.theopus.entity.schedule.Curriculum;
import com.theopus.parser.obj.table.Table;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Created by Oleksandr_Tkachov on 17.12.2017.
 */
public interface Sheet<T> {

    T getAnchor();

    Table getTable();

    List<Curriculum> parse();
}
