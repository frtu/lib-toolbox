package com.github.frtu.kotlin.utils

import com.github.frtu.serdes.avro.DummyBeanData
import org.apache.avro.util.Utf8
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class AvroBeanHelperTest {
    @Test
    fun `fileToBean method with full constructor`() {
        val avroBeanHelper = AvroBeanHelper(DummyBeanData.getClassSchema(), DummyBeanData::class.java)
        val fileToBean = avroBeanHelper.fileToBean("classpath:dummy-bean.json")!!
        println(fileToBean.getName().javaClass)
        assertThat(fileToBean.getName()).isEqualTo(Utf8("bean_name"))
        assertThat(fileToBean.getValue()).isEqualTo(Utf8("bean_value"))
    }

    @Test
    fun `fileToBean full method for multiple Avro classes`() {
        val avroBeanHelper = AvroBeanHelper<Any>()
        val fileToBean =
            avroBeanHelper.fileToBean(
                DummyBeanData.getClassSchema(),
                DummyBeanData::class.java,
                "classpath:dummy-bean.json"
            )!!
        assertThat(fileToBean.getName()).isEqualTo(Utf8("bean_name"))
        assertThat(fileToBean.getValue()).isEqualTo(Utf8("bean_value"))
    }

}