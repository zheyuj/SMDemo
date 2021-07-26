package com.example.demo.task;

import org.springframework.statemachine.StateMachine;

import java.util.Random;

public class VideoExportTask extends Task{

    static Random r = new Random(System.currentTimeMillis());
    boolean needSceneSubTask = true;
    boolean needMergeSubTask = true;

    public VideoExportTask(int id, int parentId, StateMachine<TaskState, TaskEvent> stateMachine, TaskRepo taskRepo, String hash) {
        super(id, parentId, stateMachine, taskRepo, hash);
    }

    @Override
    public void prepare() {
        int a = 1;
        if (a > 0){
            System.out.println("Prepare Succeed");
        } else {
            throw new RuntimeException("Prepare Err");
        }
    }

    @Override
    public void split() {
        if (needSceneSubTask){
            int num = 1;
            for(int i = 0; i < num; i++){
                Task t = this.taskRepo.createTask(this.id, SceneRasterizeTask.class, this.hash + "_" + i);
                this.subTasks.put(t.id, t);
                t.start();
            }
            needSceneSubTask = false;
        } else if(needMergeSubTask){
            Task t = this.taskRepo.createTask(this.id, MergeTask.class, this.hash + "_merge");
            this.subTasks.put(t.id, t);
            t.start();
            needMergeSubTask = false;
        }
    }

    @Override
    public void doTask() {

    }

    @Override
    public boolean needSplit() {
        return needSceneSubTask || needMergeSubTask;
    }

    @Override
    public boolean isSyncTask() {
        return true;
    }
}
