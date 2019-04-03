package com.jellyfish.sell.common.api.bean;

public class TaskEnum {

	public enum NewUserTaskEnum {
		USER_REGIST(1, 1) {
		},
		SUBSCRIBE_NUMBER(3,1){

		},
		BING_MOBILE(5,1){

		};

		private Integer taskId;
		private Integer type;

		NewUserTaskEnum(Integer taskId, Integer type) {
			this.taskId = taskId;
			this.type = type;
		}

		public Integer getTaskId() {
			return taskId;
		}

		public void setTaskId(Integer taskId) {
			this.taskId = taskId;
		}

		public Integer getType() {
			return type;
		}

		public void setType(Integer type) {
			this.type = type;
		}
	}


	public enum DayUserTask {
		DAY_INVITE(21, 2,"INVITE_") {
		},
		DAY_LOTTO(22,2,"DAYTASK_LOTTO_"){
		},
		DAY_SIGN(23,2,"DAYTASK_SIGN_"){
		},
		DAY_SHAREB(24,2,"DAYTASK_SHAREB_"){
		},
		DAY_AWARD(25,2,"DAYTASK_AWARD_"){
		},
		DAY_PAY(26,2,"DAYTASK_PAY_"){},
		DAY_CHAT(27,2,"DAYTASK_CHAT_"){};

		private Integer taskId;
		private Integer type;
		private String key;

		DayUserTask(Integer taskId, Integer type,String key) {
			this.taskId = taskId;
			this.type = type;
			this.key=key;
		}

		public  static  DayUserTask getDayTaskEnum(Integer taskId){
			for (DayUserTask dayUserTask:DayUserTask.values()){
				if (dayUserTask.getTaskId().intValue()==taskId.intValue()){
					return dayUserTask;
				}
			}
			return null;
		}

		public Integer getTaskId() {
			return taskId;
		}

		public void setTaskId(Integer taskId) {
			this.taskId = taskId;
		}

		public Integer getType() {
			return type;
		}

		public void setType(Integer type) {
			this.type = type;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}
	}
	
}
