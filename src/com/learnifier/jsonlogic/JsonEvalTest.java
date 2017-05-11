package com.learnifier.jsonlogic;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.assertTrue;
/**
 * @author Magnus Andersson (magnus.andersson@learnifier.com)
 */
class JsonEvalTest {

    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:MM");

    @Test
    void varInteger()  throws ParseException, IOException {
        final Object res = new JsonEval(ImmutableMap.of(
                "cool", 42
        )).evalSt("{ \"var\" : \"cool\" }");

        assertTrue(res instanceof Integer);
        assertTrue((int)res == 42);
    }

    @Test
    void dateArithmetic1()  throws ParseException, IOException {
        final Object res = new JsonEval(ImmutableMap.of(
                "now", df.parse("2017-05-08 11:01"),
                "course-start-date", df.parse("2017-05-06 11:00")
        )).evalSt("{\">=\": [ { \"var\" : \"now\" }, {\"+\": [ { \"var\" : \"course-start-date\" }, 2 ]}]}");

        assertTrue(res instanceof Boolean);
        assertTrue((Boolean)res);
    }

    @Test
    void dateArithmetic2()  throws ParseException, IOException {
        final Object res = new JsonEval(ImmutableMap.of(
                "now", df.parse("2017-05-08 10:59"),
                "course-start-date", df.parse("2017-05-06 11:00")
        )).evalSt("{\">=\": [ { \"var\" : \"now\" }, {\"+\": [ { \"var\" : \"course-start-date\" }, 2 ]}]}");

        assertTrue(res instanceof Boolean);
        assertTrue((Boolean)res);
    }
}