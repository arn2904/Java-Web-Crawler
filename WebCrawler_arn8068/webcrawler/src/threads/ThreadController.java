package threads;

public class ThreadController {
	
	int level;
	int maxLevel;
	int maxThreads;

	Queue tasks;

	MessageReceiver receiver;

	Class threadClass;

	int counter;
	int nThreads;

	public ThreadController(Class _threadClass, int _maxThreads, int _maxLevel, Queue _tasks, int _level
			,MessageReceiver _receiver) throws InstantiationException, IllegalAccessException {
		
		threadClass = _threadClass;
		maxThreads = _maxThreads;
		maxLevel = _maxLevel;
		tasks = _tasks;
		level = _level;
		receiver = _receiver;
		counter = 0;
		nThreads = 0;
		startThreads();
	}

	public synchronized int getUniqueNumber() {
		return counter++;
	}

	public synchronized void setMaxThreads(int _maxThreads)
		throws InstantiationException, IllegalAccessException {
		maxThreads = _maxThreads;
		startThreads();
	}

	public int getMaxThreads() {
		return maxThreads;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public int getRunningThreads() {
		return nThreads;
	}

	public synchronized void finished(int threadId) {
		nThreads--;
		receiver.finished(threadId);
		if (nThreads == 0) {
			level++;
			if (level > maxLevel) {
				receiver.finishedAll();
				return;
			}

			if (tasks.getQueueSize(level) == 0) {
				receiver.finishedAll();
				return;
			}
			try {
				startThreads();
			} catch (InstantiationException e) {

			} catch (IllegalAccessException e) {

			}
		}
	}

	public synchronized void startThreads()
		throws InstantiationException, IllegalAccessException {

		int m = maxThreads - nThreads;
		int ts = tasks.getQueueSize(level);
		if (ts < m || maxThreads == -1) {
			m = ts;
		}

		for (int n = 0; n < m; n++) {
			ControllableThread thread = (ControllableThread) threadClass.newInstance();
			thread.setThreadController(this);
			thread.setMessageReceiver(receiver);
			thread.setLevel(level);
			thread.setQueue(tasks);
			thread.setId(nThreads++);
			thread.start();
		}
	}
}

