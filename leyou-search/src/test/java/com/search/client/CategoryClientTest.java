package com.search.client;

import com.search.LeyouSearchApplication;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LeyouSearchApplication.class)
public class CategoryClientTest {

    @Autowired
    private CategoryClient categoryClient;

    public void testQueryCategories() {
        List<String> names = this.categoryClient.queryNamesById(Arrays.asList(1L, 2L, 3L));
        names.forEach(System.out::println);
        for( String name : names ) {
            System.out.println(name);
        }
    }

}