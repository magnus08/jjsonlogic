package com.learnifier.jsonlogic;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;
/**
 * @author Magnus Andersson (magnus.andersson@learnifier.com)
 */
class JsonLogicTest {

    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:MM");

    @Test
    void varInteger()  throws ParseException, IOException {
        final Integer res = (Integer)JsonLogic.eval(Environment.from(ImmutableMap.of(
                "cool", 42
        )), "{ \"var\" : [ \"cool\" ] }");

        assertTrue(res == 42);
    }

    @Test
    void varDate()  throws ParseException, IOException {
        final Date res = (Date)JsonLogic.eval(Environment.from(ImmutableMap.of(
                "coolDate", df.parse("2042-05-08 11:01")
        )), "{ \"var\" : [ \"coolDate\" ] }");

        assertTrue(res.equals(df.parse("2042-05-08 11:01")));
    }

    @Test
    void dateArithmetic1() throws ParseException, IOException {
        final Boolean res = (Boolean)JsonLogic.eval(Environment.from(ImmutableMap.of(
                "now", df.parse("2017-05-08 11:01"),
                "course-start-date", df.parse("2017-05-06 11:00")
        )), "{\">\": [ { \"var\" : [ \"now\" ] }, {\"+\": [ { \"var\" : [ \"course-start-date\" ] }, 2 ]}]}");

        assertTrue(res);
    }

    @Test
    void or() throws ParseException, IOException {
        final Boolean res = (Boolean)JsonLogic.eval(Environment.from(ImmutableMap.of(
                "now", df.parse("2017-05-08 11:01"),
                "course-start-date", df.parse("2017-05-06 11:00")
        )), "{ \"||\": [{\">\": [ { \"var\" : [ \"now\" ] }, {\"+\": [ { \"var\" : [ \"course-start-date\" ] }, 2 ]}]}, false]}");

        assertTrue(res);
    }

    @Test
    void and() throws ParseException, IOException {
        final Boolean res = (Boolean)JsonLogic.eval(Environment.from(ImmutableMap.of(
                "now", df.parse("2017-05-08 11:01"),
                "course-start-date", df.parse("2017-05-06 11:00")
        )), "{ \"&&\": [{\">\": [ { \"var\" : [ \"now\" ] }, {\"+\": [ { \"var\" : [ \"course-start-date\" ] }, 2 ]}]}, true]}");

        assertTrue(res);
    }

    @Test
    void dateArithmetic2() throws ParseException, IOException {
        final Boolean res = (Boolean)JsonLogic.eval(Environment.from(ImmutableMap.of(
                "now", df.parse("2017-05-08 10:59"),
                "course-start-date", df.parse("2017-05-06 11:00")
        )), "{\">\": [ { \"var\" : [ \"now\" ] }, {\"+\": [ { \"var\" : [ \"course-start-date\" ] }, 2 ]}]}");

        assertTrue(res);
    }

    @Test
    void intArithmetic1() throws ParseException, IOException {
        final Integer res = (Integer)JsonLogic.eval(Environment.from(ImmutableMap.of(
                "cool", 42
        )), "{\"+\": [ { \"var\" : [ \"cool\" ] }, 2 ]}]}");

        assertTrue(res == 44);
    }

}