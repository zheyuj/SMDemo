package com.example.demo.actions.transitionActions;

import com.example.demo.task.Task;
import com.example.demo.task.TaskEvent;
import com.example.demo.task.TaskRepo;
import com.example.demo.task.TaskState;
import lombok.AllArgsConstructor;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PrepareAction implements Action<TaskState, TaskEvent> {
    TaskRepo taskRepo;

    @Override
    public void execute(StateContext<TaskState, TaskEvent> stateContext) {
        var sm = stateContext.getStateMachine();
        int id = Integer.valueOf(sm.getId());
        Task t = taskRepo.getTask(id);
        t.prepare();
    }
}
