package com.github.frtu.sample.temporal.activitydsl

import com.github.frtu.sample.temporal.activitydsl.model.ActResult
import com.github.frtu.sample.temporal.activitydsl.model.Customer
import io.temporal.activity.Activity

class DslActivitiesImpl : DslActivities {
    override fun checkCustomerInfo(customer: Customer): ActResult? {
        return try {
            ActResult(Activity.getExecutionContext().info.activityType, "invoked")
        } catch (e: Exception) {
            null
        }
    }

    override fun updateApplicationInfo(customer: Customer): ActResult? {
        return try {
            ActResult(Activity.getExecutionContext().info.activityType, "invoked")
        } catch (e: Exception) {
            null
        }
    }

    override fun approveApplication(customer: Customer): ActResult? {
        return try {
            ActResult("decision", "APPROVED")
        } catch (e: Exception) {
            null
        }
    }

    override fun rejectApplication(customer: Customer): ActResult? {
        return try {
            ActResult("decision-" + customer.name, "DENIED")
        } catch (e: Exception) {
            null
        }
    }

    override fun invokeBankingService(customer: Customer): ActResult? {
        return try {
            ActResult(Activity.getExecutionContext().info.activityType, "invoked")
        } catch (e: Exception) {
            null
        }
    }
}
