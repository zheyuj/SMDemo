package com.example.demo.task;

import org.springframework.statemachine.StateMachine;

public class MergeTask extends Task{

    public MergeTask(int id, int parentId, StateMachine<TaskState, TaskEvent> stateMachine, TaskRepo taskRepo, String hash) {
        super(id, parentId, stateMachine, taskRepo, hash);
    }

    @Override
    public void prepare() {

    }

    @Override
    public void split() {

    }

    @Override
    public void doTask() {

    }

    @Override
    public boolean needSplit() {
        return false;
    }

    @Override
    public boolean isSyncTask() {
        return false;
    }
}
