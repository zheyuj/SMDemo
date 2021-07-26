package com.example.demo.controller;

import com.example.demo.task.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoController {
    @Autowired
    TaskRepo taskRepo;

    @RequestMapping(path = "/hello", method = RequestMethod.GET)
    public String hello(){
        return "hello";
    }


    @RequestMapping(path = "/new/{hash}", method = RequestMethod.GET)
    public String newTask(@PathVariable String hash){

        Task t = taskRepo.createTask(-1, VideoExportTask.class, hash);
        t.start();
        return taskRepo.taskMap.size() + "";
    }

    @RequestMapping(path = "/info", method = RequestMethod.GET)
    public String info(){
        StringBuilder sb = new StringBuilder();
        for(Task t: taskRepo.taskMap.values()){
            sb.append(t.toString());
            sb.append("<br/>");
        }
        return sb.toString();
    }

    @RequestMapping(path ="/fail/{id}", method = RequestMethod.GET)
    public String fail(@PathVariable String id){
        int taskId = Integer.parseInt(id);
        Task t = taskRepo.getTask(taskId);
        if(!t.isSyncTask() && t.stateMachine.getState().getId() == TaskState.PENDING){
            t.stateMachine.sendEvent(TaskEvent.ASYNC_TASK_ERR);
            return id + " ASYNC_TASK_ERR SEND";
        }

        return id + " ASYNC_TASK_ERR NOT SEND";
    }

    @RequestMapping(path ="/finish/{id}", method = RequestMethod.GET)
    public String finish(@PathVariable String id){
        int taskId = Integer.parseInt(id);
        Task t = taskRepo.getTask(taskId);
        if(!t.isSyncTask() && t.stateMachine.getState().getId() == TaskState.PENDING){
            t.stateMachine.sendEvent(TaskEvent.ASYNC_TASK_FINISHED);
            return id + " ASYNC_TASK_FINISHED SEND";
        }

        return id + " ASYNC_TASK_FINISHED NOT SEND";
    }

    @RequestMapping(path ="/cancel/{id}", method = RequestMethod.GET)
    public String cancel(@PathVariable String id){
        int taskId = Integer.parseInt(id);
        Task t = taskRepo.getTask(taskId);
        if(t.parentId == -1 && !t.stateMachine.isComplete()){
            t.stateMachine.sendEvent(TaskEvent.CANCEL);
            return id + " CANCEL SEND";
        }

        return id + " CANCEL NOT SEND";
    }
}
