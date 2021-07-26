package com.example.demo.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TaskRepo {
    public Map<Integer, Task> taskMap = new ConcurrentHashMap<>();
    public Map<String, Task> taskHashMap = new ConcurrentHashMap<>();
    public AtomicInteger currentId = new AtomicInteger(0);
    public StateMachineFactory<TaskState,TaskEvent> factory;

    @Autowired
    public TaskRepo(@Lazy StateMachineFactory<TaskState,TaskEvent> factory) {
        this.factory = factory;
    }

    public Task createTask(int parentId, Class<? extends Task> taskType, String hash){
        int id = currentId.getAndIncrement();
        Task t = null;
        if (VideoExportTask.class.equals(taskType)){
            t = new VideoExportTask(id, parentId, factory.getStateMachine(String.valueOf(id)), this, hash);
        }
        else if (SceneRasterizeTask.class.equals(taskType)){
            t = new SceneRasterizeTask(id, parentId, factory.getStateMachine(String.valueOf(id)), this, hash);
        }
        else if (MergeTask.class.equals(taskType)){
            t = new MergeTask(id, parentId, factory.getStateMachine(String.valueOf(id)), this, hash);
        }

        if(taskHashMap.containsKey(hash)){
            t.result = taskHashMap.get(hash).result;
        }

        if (t != null){
            taskMap.put(id, t);
        }

        return t;
    }


    public Task getTask(int id){
        return taskMap.getOrDefault(id, null);
    }


}
