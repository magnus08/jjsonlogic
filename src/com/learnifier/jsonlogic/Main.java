package com.learnifier.jsonlogic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class Main {

    private static final String rule = "{\">=\": [ { \"var\" : \"now\" }, {\"+\": [ { \"var\" : \"course-start-date\" }, 3 ]}]}";
//    private static final String rule = "{\">=\": [ { \"var\" : \"now\" }, { \"var\" : \"course-start-date\" }]}";
//    private static final String rule = "{\">=\": [ { \"var\": \"x\" }, 1 ]}";

    public static void main(String[] args) throws IOException, ParseException {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        final ImmutableMap<String, ? extends Serializable> vals = ImmutableMap.of(
                "now", new Date(),
                "x", 42,
                "course-start-date", df.parse("2017-05-06")
        );

        ObjectMapper mapper = new ObjectMapper();

        final Map<String, Object> tree = mapper.readValue(rule, new TypeReference<Map<String, Object>>() {
        });

        JsonEval jsonEval = new JsonEval(vals);
        System.out.println("Res: " + jsonEval.eval(tree));

    }

}
