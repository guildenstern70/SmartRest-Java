/*
 *  Project SmartREST - Java Edition
 *  Copyright (c) Alessio Saltarin 2022.
 *  This software is licensed under MIT License (see LICENSE)
 */

package net.littlelite.smartrest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class SmartRestTest
{
    @DisplayName("Context should load")
    @Test
    void contextLoads()
    {
    }

    @DisplayName("19 and 23 should be 42")
    @Test
    public void testAdd()
    {
        var sum = Integer.sum(19, 23);
        assertThat(sum).isEqualTo(42);
    }
}
