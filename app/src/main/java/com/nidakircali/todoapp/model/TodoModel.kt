package com.nidakircali.todoapp.model

import java.io.Serializable

data class TodoModel(
    var id: Long,
    var title: String,
    var detail: String,
    var isCompleted: Boolean
) : Serializable
