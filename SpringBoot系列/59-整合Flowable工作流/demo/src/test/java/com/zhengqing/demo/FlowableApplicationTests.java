package com.zhengqing.demo;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p> Flowable流程测试 </p>
 *
 * @author zhengqingya
 * @description 参考 https://mp.weixin.qq.com/s/Pd0MEkZu12vdmGGBT9ZV-w
 * @date 2022/5/10 17:51
 */
@Slf4j
@SpringBootTest
class FlowableApplicationTests {

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    String staffId = "1000";
    String zuzhangId = "90";
    String managerId = "100";

    /**
     * 列出所有待执行的任务
     */
    @Test
    void allTasks() {
        List<Task> list = this.taskService.createTaskQuery().orderByTaskId().desc().list();
        for (Task task : list) {
            log.info("任务 ID：{}；任务处理人：{}；任务是否挂起：{}", task.getId(), task.getAssignee(), task.isSuspended());
        }
    }

    /**
     * 开启一个流程
     */
    @Test
    void askForLeave() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("leaveTask", this.staffId);
        ProcessInstance processInstance = this.runtimeService.startProcessInstanceByKey("ask_for_leave", map);
        this.runtimeService.setVariable(processInstance.getId(), "name", "javaboy");
        this.runtimeService.setVariable(processInstance.getId(), "reason", "休息一下");
        this.runtimeService.setVariable(processInstance.getId(), "days", 10);
        log.info("创建请假流程 processId：{}", processInstance.getId());
    }

    /**
     * 提交给组长审批
     */
    @Test
    void submitToZuzhang() {
        //员工查找到自己的任务，然后提交给组长审批
        List<Task> list = this.taskService.createTaskQuery().taskAssignee(this.staffId).orderByTaskId().desc().list();
        for (Task task : list) {
            log.info("任务 ID：{}；任务处理人：{}；任务是否挂起：{}", task.getId(), task.getAssignee(), task.isSuspended());
            Map<String, Object> map = new HashMap<>();
            //提交给组长的时候，需要指定组长的 id
            map.put("zuzhangTask", this.zuzhangId);
            this.taskService.complete(task.getId(), map);
        }
    }

    /**
     * 组长查看自己的任务
     */
    @Test
    void zuzhangTaskList() {
        List<Task> list = this.taskService.createTaskQuery().taskAssignee(this.zuzhangId).orderByTaskId().desc().list();
        for (Task task : list) {
            Map<String, Object> info = this.taskService.getVariables(task.getId());
            log.info("请假人：{};请假时间：{} 天；请假理由：{}", info.get("name"), info.get("days"), info.get("reason"));
            log.info("任务 ID：{}；任务处理人：{}；任务是否挂起：{}", task.getId(), task.getAssignee(), task.isSuspended());
        }
    }

    /**
     * 组长审批-批准
     */
    @Test
    void zuZhangApprove() {
        List<Task> list = this.taskService.createTaskQuery().taskAssignee(this.zuzhangId).orderByTaskId().desc().list();
        for (Task task : list) {
            log.info("组长 {} 在审批 {} 任务", task.getAssignee(), task.getId());
            Map<String, Object> map = new HashMap<>();
            //组长审批的时候，如果是同意，需要指定经理的 id
            map.put("managerTask", this.managerId);
            map.put("checkResult", "通过");
            this.taskService.complete(task.getId(), map);
        }
    }

    /**
     * 组长审批-拒绝
     */
    @Test
    void zuZhangReject() {
        List<Task> list = this.taskService.createTaskQuery().taskAssignee(this.zuzhangId).orderByTaskId().desc().list();
        for (Task task : list) {
            log.info("组长 {} 在审批 {} 任务", task.getAssignee(), task.getId());
            Map<String, Object> map = new HashMap<>();
            //组长审批的时候，如果是拒绝，就不需要指定经理的 id
            map.put("checkResult", "拒绝");
            this.taskService.complete(task.getId(), map);
        }
    }

    /**
     * 经理查看自己的任务
     */
    @Test
    void managerTaskList() {
        List<Task> list = this.taskService.createTaskQuery().taskAssignee(this.managerId).orderByTaskId().desc().list();
        for (Task task : list) {
            log.info("经理 {} 待审批的任务 ID：{}", task.getAssignee(), task.getId());
        }
    }

    /**
     * 经理审批自己的任务-批准
     */
    @Test
    void managerApprove() {
        List<Task> list = this.taskService.createTaskQuery().taskAssignee(this.managerId).orderByTaskId().desc().list();
        for (Task task : list) {
            log.info("经理 {} 在审批 {} 任务", task.getAssignee(), task.getId());
            Map<String, Object> map = new HashMap<>();
            map.put("checkResult", "通过");
            this.taskService.complete(task.getId(), map);
        }
    }


    /**
     * 经理审批自己的任务-拒绝
     */
    @Test
    void managerReject() {
        List<Task> list = this.taskService.createTaskQuery().taskAssignee(this.managerId).orderByTaskId().desc().list();
        for (Task task : list) {
            log.info("经理 {} 在审批 {} 任务", task.getAssignee(), task.getId());
            Map<String, Object> map = new HashMap<>();
            map.put("checkResult", "拒绝");
            this.taskService.complete(task.getId(), map);
        }
    }

}
