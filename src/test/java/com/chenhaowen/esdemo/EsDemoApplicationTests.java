package com.chenhaowen.esdemo;

import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EsDemoApplicationTests {

    @Autowired
    private JestClient jestClient;

    @Test
    public void test() throws IOException {
        String query = "{\n" +
                "  \"query\": {\n" +
                "    \"match_phrase\": {\n" +
                "      \"name\": \"operation red sea\"\n" +
                "    }\n" +
                "  }\n" +
                "}";
        Search search = new Search.Builder(query)
                .addIndex("movie_index")
                .addType("movie")
                .build();
        SearchResult result = jestClient.execute(search);
        List<SearchResult.Hit<HashMap, Void>> hits = result.getHits(HashMap.class);
        for (SearchResult.Hit<HashMap, Void> hit : hits) {
            HashMap source = hit.source;
            System.out.println("source=" + source);
        }

    }

}
