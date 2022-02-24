package threads;

abstract public class ControllableThread extends Thread {
	protected int level;
	protected int id;
	protected Queue queue;
	protected ThreadController tc;
	protected MessageReceiver mr;
	public void setId(int _id) {
		id = _id;
	}
	public void setLevel(int _level) {
		level = _level;
	}
	public void setQueue(Queue _queue) {
		queue = _queue;
	}
	public void setThreadController(ThreadController _tc) {
		tc = _tc;
	}
	public void setMessageReceiver(MessageReceiver _mr) {
		mr = _mr;
	}
	public ControllableThread() {
	}
	public void run() {

		for (Object nt = queue.pop(level); nt != null; nt = queue.pop(level)) {

			mr.receiveMessage(nt, id);

			process(nt);

			if (tc.getMaxThreads() > tc.getRunningThreads()) {
				try {
					tc.startThreads();
				} catch (Exception e) {
					System.err.println("[" + id + "] " + e.toString());
				}
			}
		}

		tc.finished(id);
	}

	public abstract void process(Object o);
}
