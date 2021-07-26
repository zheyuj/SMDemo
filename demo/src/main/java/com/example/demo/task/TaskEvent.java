package com.example.demo.task;

public enum TaskEvent {
    PREPARE,
    PREPARE_ERR,
    SPLIT,
    SPLIT_ERR,
    READY,
    DO_TASK,
    DO_TASK_ERR,
    CANCEL,
    ASYNC_TASK_FINISHED,
    ASYNC_TASK_ERR,
    USING_CACHED_RESULT,
}
