package com.example.demo.listener;

import com.example.demo.task.Task;
import com.example.demo.task.TaskEvent;
import com.example.demo.task.TaskRepo;
import com.example.demo.task.TaskState;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

@AllArgsConstructor
public class StateMachineEventListener extends StateMachineListenerAdapter<TaskState, TaskEvent> {
    Task t;


    public StateMachineEventListener() {
        super();
    }

    @Override
    public void stateChanged(State<TaskState, TaskEvent> from, State<TaskState, TaskEvent> to) {
        super.stateChanged(from, to);
        t.logs.add(String.format("State changed from %s to %s", from == null ? "null" : from.getId().toString(),
                to == null ? "null" : to.getId().toString()));
    }

    @Override
    public void stateEntered(State<TaskState, TaskEvent> state) {
        super.stateEntered(state);
        t.logs.add(String.format("Enter state: %s", state == null ? "null" : state.getId().toString()));
    }

    @Override
    public void stateExited(State<TaskState, TaskEvent> state) {
        super.stateExited(state);
        t.logs.add(String.format("Exit state: %s", state == null ? "null" : state.getId().toString()));
    }

    @Override
    public void eventNotAccepted(Message<TaskEvent> event) {
        super.eventNotAccepted(event);
    }

    @Override
    public void transition(Transition<TaskState, TaskEvent> transition) {
        super.transition(transition);
        State<TaskState, TaskEvent> from = transition.getSource();
        State<TaskState, TaskEvent> to = transition.getTarget();
        t.logs.add(String.format("transitions: event: %s, source: %s, target: %s",
                transition.getTrigger().getEvent().toString(),
                from == null ? "null" : from.getId().toString(),
                to == null ? "null" : to.getId().toString()));

    }

    @Override
    public void transitionStarted(Transition<TaskState, TaskEvent> transition) {
        super.transitionStarted(transition);
        State<TaskState, TaskEvent> from = transition.getSource();
        State<TaskState, TaskEvent> to = transition.getTarget();
        t.logs.add(String.format("transitions START: event: %s, source: %s, target: %s",
                transition.getTrigger().getEvent().toString(),
                from == null ? "null" : from.getId().toString(),
                to == null ? "null" : to.getId().toString()));
    }

    @Override
    public void transitionEnded(Transition<TaskState, TaskEvent> transition) {
        super.transitionEnded(transition);
        State<TaskState, TaskEvent> from = transition.getSource();
        State<TaskState, TaskEvent> to = transition.getTarget();
        t.logs.add(String.format("transitions END: event: %s, source: %s, target: %s",
                transition.getTrigger().getEvent().toString(),
                from == null ? "null" : from.getId().toString(),
                to == null ? "null" : to.getId().toString()));
    }

    @Override
    public void stateMachineStarted(StateMachine<TaskState, TaskEvent> stateMachine) {
        super.stateMachineStarted(stateMachine);
    }

    @Override
    public void stateMachineStopped(StateMachine<TaskState, TaskEvent> stateMachine) {
        super.stateMachineStopped(stateMachine);
    }

    @Override
    public void stateMachineError(StateMachine<TaskState, TaskEvent> stateMachine, Exception exception) {
        super.stateMachineError(stateMachine, exception);
    }

    @Override
    public void extendedStateChanged(Object key, Object value) {
        super.extendedStateChanged(key, value);
    }

    @Override
    public void stateContext(StateContext<TaskState, TaskEvent> stateContext) {
        super.stateContext(stateContext);
    }
}
