package com.example.demo.guards;

import com.example.demo.task.Task;
import com.example.demo.task.TaskEvent;
import com.example.demo.task.TaskRepo;
import com.example.demo.task.TaskState;
import lombok.AllArgsConstructor;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RetryGuard implements Guard<TaskState, TaskEvent> {
    TaskRepo taskRepo;

    @Override
    public boolean evaluate(StateContext<TaskState, TaskEvent> stateContext) {
        var sm = stateContext.getStateMachine();
        int id = Integer.valueOf(sm.getId());
        Task t = taskRepo.getTask(id);
        return t.canRetry();
    }
}
