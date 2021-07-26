package com.example.demo;

import com.example.demo.actions.stateActions.*;
import com.example.demo.actions.transitionActions.*;
import com.example.demo.guards.*;
import com.example.demo.task.TaskEvent;
import com.example.demo.task.TaskRepo;
import com.example.demo.task.TaskState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;


@Configuration
@EnableStateMachineFactory
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<TaskState, TaskEvent> {
    @Autowired
    public FailedAction failedAction;
    @Autowired
    public CancelAction cancelAction;
    @Autowired
    FinishedAction finishedAction;


    @Autowired
    public PrepareAction prepareAction;
    @Autowired
    public PrepareErrAction prepareErrAction;
    @Autowired
    public SplitAction splitAction;
    @Autowired
    public FailedSplitAction failedSplitAction;
    @Autowired
    EnterWaitingSplitAction enterWaitingSplitAction;
    @Autowired
    DoTaskAction doTaskAction;
    @Autowired
    DoTaskErrAction doTaskErrAction;
    @Autowired
    EnterReadyAction enterReadyAction;
    @Autowired
    EnterRetryAction enterRetryAction;
    @Autowired
    EnterWaitingAction enterWaitingAction;


    @Autowired
    public TaskRepo taskRepo;


    @Autowired
    public SplitGuard splitGuard;
    @Autowired
    public NoGuard noGuard;
    @Autowired
    SyncTaskGuard syncTaskGuard;
    @Autowired
    TaskReadyGuard taskReadyGuard;
    @Autowired
    SubTaskAllFinishedGuard subTaskAllFinishedGuard;
    @Autowired
    RetryGuard retryGuard;


    @Override
    public void configure(StateMachineStateConfigurer<TaskState, TaskEvent> states) throws Exception {
        /*
          CREATED,
          WAITING_SPLIT,
          READY,
          FAILED,
          FINISHED
        * */
        states.withStates()
                .initial(TaskState.CREATED)
                .choice(TaskState.PREPARED)
                .state(TaskState.WAITING_SPLIT, enterWaitingSplitAction, null)
                .state(TaskState.WAITING, enterWaitingAction)
                .choice(TaskState.READY)
                .state(TaskState.READY_SYNC, enterReadyAction)
                .state(TaskState.READY_ASYNC, enterReadyAction)
                .state(TaskState.PENDING)
                .choice(TaskState.RETRY)
                .state(TaskState.FAILED, failedAction, null)
                .state(TaskState.FINISHED, finishedAction, null)
                .state(TaskState.CANCELED, cancelAction, null)
                .end(TaskState.FAILED)
                .end(TaskState.FINISHED)
                .end(TaskState.CANCELED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<TaskState, TaskEvent> transitions) throws Exception {
        transitions.withExternal().source(TaskState.CREATED).target(TaskState.PREPARED).event(TaskEvent.PREPARE).action(prepareAction, prepareErrAction)
                .and().withExternal().source(TaskState.CREATED).target(TaskState.FAILED).event(TaskEvent.PREPARE_ERR)
                .and().withExternal().source(TaskState.CREATED).target(TaskState.FINISHED).event(TaskEvent.USING_CACHED_RESULT)
                .and().withChoice().source(TaskState.PREPARED).first(TaskState.WAITING_SPLIT, splitGuard)
                                                                    .last(TaskState.READY)
                .and().withExternal().source(TaskState.WAITING_SPLIT).target(TaskState.WAITING).event(TaskEvent.SPLIT).action(splitAction, failedSplitAction)
                .and().withExternal().source(TaskState.WAITING_SPLIT).target(TaskState.FAILED).event(TaskEvent.SPLIT_ERR)
                .and().withExternal().source(TaskState.WAITING).target(TaskState.READY).event(TaskEvent.READY).guard(subTaskAllFinishedGuard)
                .and().withChoice().source(TaskState.READY).first(TaskState.WAITING_SPLIT, taskReadyGuard)
                                                            .then(TaskState.READY_SYNC, syncTaskGuard)
                                                            .last(TaskState.READY_ASYNC)
                .and().withExternal().source(TaskState.READY_SYNC).target(TaskState.FINISHED).event(TaskEvent.DO_TASK).action(doTaskAction, doTaskErrAction)
                .and().withExternal().source(TaskState.READY_SYNC).target(TaskState.RETRY).event(TaskEvent.DO_TASK_ERR)
                .and().withExternal().source(TaskState.READY_ASYNC).target(TaskState.PENDING).event(TaskEvent.DO_TASK).action(doTaskAction, doTaskErrAction)
                .and().withExternal().source(TaskState.READY_ASYNC).target(TaskState.RETRY).event(TaskEvent.DO_TASK_ERR)
                .and().withExternal().source(TaskState.PENDING).target(TaskState.FINISHED).event(TaskEvent.ASYNC_TASK_FINISHED)
                .and().withExternal().source(TaskState.PENDING).target(TaskState.RETRY).event(TaskEvent.ASYNC_TASK_ERR)
                .and().withChoice().source(TaskState.RETRY).first(TaskState.READY, retryGuard, enterRetryAction)
                                                            .last(TaskState.FAILED)
                .and().withExternal().source(TaskState.CREATED).target(TaskState.CANCELED).event(TaskEvent.CANCEL)
                .and().withExternal().source(TaskState.WAITING_SPLIT).target(TaskState.CANCELED).event(TaskEvent.CANCEL)
                .and().withExternal().source(TaskState.WAITING).target(TaskState.CANCELED).event(TaskEvent.CANCEL)
                .and().withExternal().source(TaskState.READY_SYNC).target(TaskState.CANCELED).event(TaskEvent.CANCEL)
                .and().withExternal().source(TaskState.READY_ASYNC).target(TaskState.CANCELED).event(TaskEvent.CANCEL)
                .and().withExternal().source(TaskState.PENDING).target(TaskState.CANCELED).event(TaskEvent.CANCEL);
    }

}
