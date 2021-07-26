package com.example.demo.task;


import com.example.demo.listener.StateMachineEventListener;
import org.springframework.statemachine.StateMachine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Task {
    public int id;
    public int parentId;
    public StateMachine<TaskState, TaskEvent> stateMachine;
    public Map<Integer, Task> subTasks = new ConcurrentHashMap<>();
    TaskRepo taskRepo;
    private boolean needSplit = false;
    public int retryTime = 0;
    public String result = null;
    public String hash = null;
    public List<String> logs = new ArrayList<>();

    public Task(int id, int parentId, StateMachine<TaskState, TaskEvent> stateMachine, TaskRepo taskRepo, String hash){
        this.id = id;
        this.parentId = parentId;
        this.stateMachine = stateMachine;
        this.taskRepo = taskRepo;
        this.hash = hash;
        this.stateMachine.addStateListener(new StateMachineEventListener(this));
        this.stateMachine.start();
    }


    public abstract void prepare();
    public abstract void split();
    public abstract void doTask();

    public void start(){
        if (this.result == null){
            this.stateMachine.sendEvent(TaskEvent.PREPARE);
        } else {
            this.stateMachine.sendEvent(TaskEvent.USING_CACHED_RESULT);
        }

    }

    public void onFailed(){
        if(parentId != -1){
            Task parentTask = taskRepo.getTask(parentId);
            parentTask.onSubTaskFailed();
        }
    }

    public void onSubTaskFailed(){
        stateMachine.sendEvent(TaskEvent.CANCEL);
    }

    public void onCancel(){
        for (Task t :  subTasks.values()){
            if(!t.stateMachine.isComplete())
                t.stateMachine.sendEvent(TaskEvent.CANCEL);
        }
    }

    public boolean needSplit(){
        return needSplit;
    }

    public boolean isSyncTask(){
        return true;
    }

    public String toString(){
        String s1 = String.format("id: %d, type: %s, subtasks: %s, state: %s", id, this.getClass().getSimpleName(), subTasks.keySet().toString(), stateMachine.getState().getId().toString());
        s1 += "<br/>";
        for(String s : logs){
            s1 += s;
            s1 += "<br/>";
        }
        return s1;
    }

    public boolean isAllSubTaskFinished(){
        for(Task t: subTasks.values()){
            if(t.stateMachine.getState().getId() != TaskState.FINISHED){
                return false;
            }
        }
        return true;
    }

    public void onFinished(){
        if(parentId != -1){
            Task parentTask = taskRepo.getTask(parentId);
            parentTask.onSubTaskFinished();
        }

        this.result = this.hash + "_result";
        taskRepo.taskHashMap.putIfAbsent(hash, this);
    }

    public void onSubTaskFinished(){
        stateMachine.sendEvent(TaskEvent.READY);
    }

    public boolean canRetry() {
        return retryTime < 1;
    }
}
