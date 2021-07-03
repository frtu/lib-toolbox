package com.github.frtu.dot.utils;

import com.github.frtu.dot.utils.IdUtil;
import org.junit.Assert;
import org.junit.Test;

public class IdUtilTest {


    @Test
    public void formatId() {
        //--------------------------------------
        // 1. Prepare data
        //--------------------------------------
        final String id = "tests.pojo.complex.ComplexStructureParent";
        //--------------------------------------
        // 2. Run tests
        //--------------------------------------
        final String formatId = IdUtil.formatId(id);
        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        Assert.assertTrue(IdUtil.assertFormatId(formatId));
    }}