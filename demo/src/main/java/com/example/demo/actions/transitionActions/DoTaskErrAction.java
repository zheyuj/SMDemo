package com.example.demo.actions.transitionActions;

import com.example.demo.task.Task;
import com.example.demo.task.TaskEvent;
import com.example.demo.task.TaskRepo;
import com.example.demo.task.TaskState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Component
public class DoTaskErrAction implements Action<TaskState, TaskEvent> {
    @Autowired TaskRepo taskRepo;

    @Override
    public void execute(StateContext<TaskState, TaskEvent> stateContext) {
        Exception exception = stateContext.getException();
        System.out.println(this.getClass().getName() + ": " + exception.getMessage());
        var sm = stateContext.getStateMachine();
        sm.sendEvent(TaskEvent.DO_TASK_ERR);
    }
}
