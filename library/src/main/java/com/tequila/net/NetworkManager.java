package com.tequila.net;

import android.os.Handler;
import android.os.Message;
import com.tequila.cache.ResponseMemCache;
import com.tequila.net.task.AsyncTask;
import com.tequila.net.task.BaseTask;
import com.tequila.net.task.CommonTask;
import java.util.Iterator;
import java.util.LinkedList;


public class NetworkManager implements TaskListener {

	private static NetworkManager singleInstance = null;

	public static NetworkManager getInstance() {
		if (singleInstance == null) {
			synchronized (NetworkManager.class) {
				if (singleInstance == null) {
					singleInstance = new NetworkManager();
				}
			}
		}
		return singleInstance;
	}

	private final LinkedList<NetworkTask> listSequence = new LinkedList<NetworkTask>();
	@SuppressWarnings("unchecked")
	private final LinkedList<BaseTask>[] tasks = new LinkedList[IServiceMap.NET_TASKTYPE_ALL];
	/**
	 * 不设置上限
	 */
	private final int maxCount = Integer.MAX_VALUE;

	private NetworkManager() {
		// maxCount = MainConstants.NET_MAXTASK_COUNT;
		tasks[IServiceMap.NET_TASKTYPE_CONTROL] = new LinkedList<BaseTask>();
		tasks[IServiceMap.NET_TASKTYPE_SECURE] = new LinkedList<BaseTask>();
		tasks[IServiceMap.NET_TASKTYPE_POLL] = new LinkedList<BaseTask>();
		tasks[IServiceMap.NET_TASKTYPE_MULTI] = new LinkedList<BaseTask>();
	}

	private boolean addCurrentTask(int taskType, BaseTask task) {
		boolean suc = false;
		LinkedList<BaseTask> taskList = tasks[taskType];
		if (taskList == null) {
			throw new IllegalArgumentException("no task type = " + taskType);
		}
		synchronized (taskList) {
			if (taskList.size() < maxCount) {
				taskList.add(task);
				suc = true;
			}
		}
		return suc;
	}

	public void addTask(NetworkParam param, Handler handler) {
		boolean isRepeat = false;
		NetworkTask task = new NetworkTask(param, handler);
		synchronized (this.listSequence) {
			Iterator<NetworkTask> listSequenceIterator = this.listSequence.iterator();
			while (listSequenceIterator.hasNext()) {
				NetworkTask networkTask = listSequenceIterator.next();
				NetworkParam tmp = networkTask.param;
				if (tmp.equals(param)) {
					isRepeat = true;
				}
			}

			LinkedList<BaseTask> taskList = tasks[param.key.getCode()];
			if (taskList == null) {
				throw new IllegalArgumentException("param.key.getCode() returns not task type");
			}
			synchronized (taskList) {
				for (int i = 0; i < taskList.size(); i++) {
					NetworkParam tmp = taskList.get(i).getNetworkParam();
					if (tmp.equals(param)) {
						isRepeat = true;
					}
				}
			}

			if (isRepeat) {
				return;
			}

			if (task.handler != null) {
				Message m = task.handler.obtainMessage(TaskStatus.START, task.param);
				task.handler.sendMessage(m);
			}

			switch (param.addType) {
				case Request.NET_ADD_ONORDER:
					this.listSequence.add(task);
					break;
				case Request.NET_ADD_INSERT2HEAD:
					this.listSequence.add(0, task);
					break;
				case Request.NET_ADD_CANCELPRE: {
					Iterator<NetworkTask> it = this.listSequence.iterator();
					while (it.hasNext()) {
						NetworkTask nt = it.next();
						if (param.key.getCode() == nt.param.key.getCode() && nt.param.cancelAble) {
							it.remove();
						}
					}

					synchronized (taskList) {
						Iterator<BaseTask> itt = taskList.iterator();
						while (itt.hasNext()) {
							BaseTask bt = itt.next();
							bt.cancelWithType(param.key.getCode());
							itt.remove();
						}
					}

					this.listSequence.add(0, task);
				}
				break;
				case Request.NET_ADD_CANCELSAMET: {

					Iterator<NetworkTask> it = this.listSequence.iterator();
					while (it.hasNext()) {
						NetworkTask nt = it.next();
						if (param.key == nt.param.key && nt.param.cancelAble) {
							it.remove();
						}
					}
					synchronized (taskList) {
						Iterator<BaseTask> itt = taskList.iterator();
						while (itt.hasNext()) {
							BaseTask bt = itt.next();
							if (bt.networkTask.param.key == param.key) {
								bt.cancelWithType(param.key.getCode());
								itt.remove();
							}
						}
					}

					this.listSequence.add(task);

				}
				break;
				default:
					break;
			}
		}
		checkTasks();
	}

	@SuppressWarnings("rawtypes")
	public void cancelTaskByHandler(Handler handler) {
		synchronized (this.listSequence) {
			Iterator<NetworkTask> it = this.listSequence.iterator();
			while (it.hasNext()) {
				NetworkTask task = it.next();
				if (task.handler == handler && task.param.cancelAble) {
					it.remove();
				}
			}
		}
		for (LinkedList<BaseTask> taskList : tasks) {
			synchronized (taskList) {
				Iterator it = taskList.iterator();
				while (it.hasNext()) {
					BaseTask task = (BaseTask) it.next();
					if (task.networkTask.handler == handler) {
						if (task.cancelWithHandler(handler)) {
							it.remove();
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public void cancelTaskByKey(IServiceMap key) {
		synchronized (this.listSequence) {
			Iterator<NetworkTask> it = this.listSequence.iterator();
			while (it.hasNext()) {
				NetworkTask task = it.next();
				if (task.param.key == key && task.param.cancelAble) {
					it.remove();
				}
			}
		}
		for (LinkedList<BaseTask> taskList : tasks) {
			synchronized (taskList) {
				Iterator it = taskList.iterator();
				while (it.hasNext()) {
					BaseTask task = (BaseTask) it.next();
					if (task.networkTask.param.key == key) {
						if (task.cancelWithKey(key)) {
							it.remove();
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public void cancelTaskByType(int type) {

		synchronized (this.listSequence) {
			if (type == IServiceMap.NET_TASKTYPE_ALL) {
				this.listSequence.clear();
			} else {
				Iterator<NetworkTask> it = this.listSequence.iterator();
				while (it.hasNext()) {
					NetworkTask task = it.next();
					if (task.param.key.getCode() == type) {
						it.remove();
					}
				}
			}
		}

		if (type == IServiceMap.NET_TASKTYPE_ALL) {
			for (LinkedList<BaseTask> taskList : tasks) {
				synchronized (taskList) {
					Iterator it = taskList.iterator();
					while (it.hasNext()) {
						BaseTask task = (BaseTask) it.next();
						if (task.cancelWithType(type)) {
							it.remove();
						}
					}
				}
			}
		} else {
			LinkedList<BaseTask> taskList = tasks[type];
			synchronized (taskList) {
				Iterator<BaseTask> it = taskList.iterator();
				while (it.hasNext()) {
					BaseTask task = it.next();
					if (task.cancelWithType(type)) {
						it.remove();
					}
				}
			}
		}
	}

	public void cancelTaskByUrl(String url) {
		synchronized (this.listSequence) {
			Iterator<NetworkTask> it = this.listSequence.iterator();
			while (it.hasNext()) {
				NetworkTask task = it.next();
				if (task.param.url.equals(url) && task.param.cancelAble) {
					it.remove();
				}
			}
		}
		for (LinkedList<BaseTask> taskList : tasks) {
			synchronized (taskList) {
				Iterator<BaseTask> it = taskList.iterator();
				while (it.hasNext()) {
					BaseTask task = it.next();
					if (task.networkTask.param.url.equals(url)) {
						if (task.cancelWithUrl(url)) {
							it.remove();
						}
					}
				}
			}
		}

	}

	public void cancelTaskByParam(NetworkParam param) {
		synchronized (this.listSequence) {
			Iterator<NetworkTask> it = this.listSequence.iterator();
			while (it.hasNext()) {
				NetworkTask task = it.next();
				if (task.param.equals(param) && task.param.cancelAble) {
					it.remove();
				}
			}
		}
		for (LinkedList<BaseTask> taskList : tasks) {
			synchronized (taskList) {
				Iterator<BaseTask> it = taskList.iterator();
				while (it.hasNext()) {
					BaseTask task = it.next();
					if (task.networkTask.param.equals(param)) {
						if (task.cancelWithParam(param)) {
							it.remove();
						}
					}
				}
			}
		}

	}

	public void checkTasks() {
		if (this.listSequence.size() == 0) {
			return;
		}
		boolean flag = true;
		synchronized (this.listSequence) {
			Iterator<NetworkTask> it = this.listSequence.iterator();
			while (it.hasNext()) {
				final NetworkTask nt = it.next();
				final BaseTask task = newInstanceByType(nt);
				flag = addCurrentTask(nt.param.key.getCode(), task);
				if (flag) {
					it.remove();
					//find cache
					if (!nt.cancel) {
						if (nt.param.memCache && ResponseMemCache.containsKey(nt.param)) {
							nt.serverStatus = TaskStatus.SUCCESS;
							nt.param.result = ResponseMemCache.get(nt.param);

							if (nt.handler != null) {
								Message m = nt.handler.obtainMessage(TaskStatus.SUCCESS, nt.param);
								nt.handler.sendMessage(m);
							}
							onTaskComplete(nt);
						} else {
							if (nt.param.diskCache) {
								AsyncTask.CACHE_THREAD_EXECUTOR.execute(new Runnable() {
									public void run() {
										BaseTask.findDiskCache(nt.param,nt.handler);
									}
								}) ;
							}
							task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
						}
					} else {
						onTaskCancel(nt);
					}
				}
			}
		}
	}



	private BaseTask newInstanceByType(NetworkTask task) {
		if(task.param.key.getCode() == IServiceMap.NET_TASKTYPE_SECURE) {

		}
		if(task.param.key.getCode() == IServiceMap.NET_TASKTYPE_POLL) {

		}
		if(task.param.key.getCode() == IServiceMap.NET_TASKTYPE_MULTI) {

		}
		return new CommonTask(task,this);
	}

	public void destroy() {
		if (singleInstance != null) {
			singleInstance.cancelTaskByType(IServiceMap.NET_TASKTYPE_ALL);
		}
		singleInstance = null;
	}

	public int getCurrentTaskCount(int type) {
		int count = 0;
		if (type == IServiceMap.NET_TASKTYPE_ALL) {
			for (LinkedList<BaseTask> taskList : tasks) {
				synchronized (taskList) {
					count += taskList.size();
				}
			}
		} else {
			LinkedList<BaseTask> taskList = tasks[type];
			synchronized (taskList) {
				count = taskList.size();
			}
		}
		return count;
	}

	private void removeCurrentTask(NetworkTask netTask) {
		if (netTask.handler != null) {
			Message m = netTask.handler.obtainMessage(TaskStatus.END, netTask.param);
			netTask.handler.sendMessage(m);
		}
		LinkedList<BaseTask> taskList = tasks[netTask.param.key.getCode()];
		synchronized (taskList) {
			Iterator<BaseTask> it = taskList.iterator();
			while (it.hasNext()) {
				BaseTask task = it.next();
				if (task.networkTask == netTask) {
					it.remove();
				}
			}
		}
	}

	@Override
	public void onTaskCancel(NetworkTask task) {
		removeCurrentTask(task);
		checkTasks();
	}

	@Override
	public void onTaskComplete(NetworkTask task) {
		removeCurrentTask(task);
		checkTasks();
	}


}