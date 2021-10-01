package com.github.frtu.kotlin.utils.io

import com.github.frtu.serdes.avro.DummyBeanData
import org.apache.avro.util.Utf8
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class AvroBeanHelperTest {
    @Test
    fun `jsonFileToBean method with full constructor`() {
        //--------------------------------------
        // 1. Constructor only call once
        //--------------------------------------
        val avroBeanHelper = AvroBeanHelper(DummyBeanData.getClassSchema(), DummyBeanData::class.java)
        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val fileToBean = avroBeanHelper.jsonFileToBean("classpath:dummy-bean.json")!!
        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        assertThat(fileToBean.getName()).isEqualTo(Utf8("bean_name"))
        assertThat(fileToBean.getValue()).isEqualTo(Utf8("bean_value"))
    }

    @Test
    fun `jsonFileToBean full method for multiple Avro classes`() {
        //--------------------------------------
        // 1. Constructor only call once
        //--------------------------------------
        val avroBeanHelper = AvroBeanHelper<Any>()
        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val fileToBean =
            avroBeanHelper.jsonFileToBean(
                DummyBeanData.getClassSchema(),
                DummyBeanData::class.java,
                "classpath:dummy-bean.json"
            )!!
        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        assertThat(fileToBean.getName()).isEqualTo(Utf8("bean_name"))
        assertThat(fileToBean.getValue()).isEqualTo(Utf8("bean_value"))
    }

}