# SpringBoot 动态定时任务

定时任务执行: [ScheduleTask.java](src/main/java/com/zhengqing/demo/task/ScheduleTask.java)

- CronTrigger：适用于复杂的定时任务，可以定义非常详细的时间规则。
- PeriodicTrigger：适用于简单的定时任务，只需定义固定的时间间隔即可。

选择哪种触发器取决于你的具体需求。
如果你需要复杂的定时规则，建议使用 CronTrigger；
如果你只需要简单的固定时间间隔，可以选择 PeriodicTrigger。