package com.roadjava.schedule.service;

import java.util.List;

import com.roadjava.schedule.beans.TaskInfo;
import com.roadjava.schedule.vo.ResVo;
import com.roadjava.schedule.vo.TableGridVo;

public interface TaskInfoService {

	ResVo addTask(TaskInfo task);

	TableGridVo getTasks();

	ResVo queryTaskById(String taskId);

	ResVo updateTask(TaskInfo task);

	void taskJoinInSchedule(String taskIds);

	void taskRemoveFromSchedule(String taskIds);

	 List<TaskInfo> getAvailableTasks();

}
