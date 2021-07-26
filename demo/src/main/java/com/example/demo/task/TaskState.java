package com.example.demo.task;

public enum TaskState {
    CREATED,
    PREPARED,
    WAITING_SPLIT,
    WAITING,
    READY,
    READY_SYNC,
    READY_ASYNC,
    PENDING,
    RETRY,
    FAILED,
    FINISHED,
    CANCELED
}
