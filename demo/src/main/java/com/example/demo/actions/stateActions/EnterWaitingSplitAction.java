package com.example.demo.actions.stateActions;

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
public class EnterWaitingSplitAction implements Action<TaskState, TaskEvent> {
    TaskRepo taskRepo;

    @Override
    public void execute(StateContext<TaskState, TaskEvent> stateContext) {
        var sm = stateContext.getStateMachine();
        sm.sendEvent(TaskEvent.SPLIT);
    }
}
