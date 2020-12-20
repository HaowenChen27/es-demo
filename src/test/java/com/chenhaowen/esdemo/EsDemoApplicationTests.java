package com.chenhaowen.esdemo;

import com.chenhaowen.esdemo.zklock.ExtLock;
import com.chenhaowen.esdemo.zklock.ZookeeperDistributeLock;
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

    @Test
    public void testLock() {
        ExtLock lock = new ZookeeperDistributeLock();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    lock.getLock();
                    System.out.println("获取到锁，我开始处理了");
                    Thread.sleep(5 * 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unLock();
                }
            }
        });
        thread.start();
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
