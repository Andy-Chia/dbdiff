package com.andycoder.dbdiff;

import com.andycoder.dbdiff.service.DatabaseComparator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DbdiffApplicationTests {
    @Autowired
    DatabaseComparator databaseComparator;

    @Test
    void contextLoads() {
        databaseComparator.compare();
    }

}
