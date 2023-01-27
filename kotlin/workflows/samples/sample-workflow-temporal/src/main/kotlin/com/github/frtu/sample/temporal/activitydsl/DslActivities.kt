package com.github.frtu.sample.temporal.activitydsl

import com.github.frtu.sample.temporal.activitydsl.model.ActResult
import com.github.frtu.sample.temporal.activitydsl.model.Customer
import io.temporal.activity.ActivityInterface

@ActivityInterface
interface DslActivities {
    fun checkCustomerInfo(customer: Customer): ActResult?
    fun approveApplication(customer: Customer): ActResult?
    fun rejectApplication(customer: Customer): ActResult?
    fun updateApplicationInfo(customer: Customer): ActResult?
    fun invokeBankingService(customer: Customer): ActResult?
}
