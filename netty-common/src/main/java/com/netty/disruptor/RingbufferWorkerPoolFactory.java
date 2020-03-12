package com.netty.disruptor;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.ProducerType;
import com.netty.model.TransformDataWrapper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

public class RingbufferWorkerPoolFactory {

	/**
	 * 消费者池
	 */
	private Map<String, AbstractMessageConsumer> consumers = new ConcurrentHashMap<String, AbstractMessageConsumer>();

	/**
	 * 生产者池
	 */
	private Map<String, MessageProducer> producers = new ConcurrentHashMap<String, MessageProducer>();

	private RingBuffer<TransformDataWrapper> ringBuffer;
	private SequenceBarrier sequenceBarrier;
	private WorkerPool<TransformDataWrapper> workerPool;

	private RingbufferWorkerPoolFactory() {

	}

	public void initAndStart(ProducerType type, int bufferSize, WaitStrategy waitStrategy,
			AbstractMessageConsumer[] consumers) {

		// 创建ringbuffer对象
		this.ringBuffer = RingBuffer.create(type, new EventFactory<TransformDataWrapper>() {
			public TransformDataWrapper newInstance() {
				return new TransformDataWrapper();
			}
		}, bufferSize, waitStrategy);

		// 设置序号栅栏
		this.sequenceBarrier = this.ringBuffer.newBarrier();

		// 设置workerpool
		this.workerPool = new WorkerPool<TransformDataWrapper>(ringBuffer, sequenceBarrier, new EventExceptionHandler(),
				consumers);

		// 将构建的消费者放入池中
		for (AbstractMessageConsumer consumer : consumers) {
			this.consumers.put(consumer.getConsumerId(), consumer);
		}

		// 添加sequences
		this.ringBuffer.addGatingSequences(workerPool.getWorkerSequences());

		// 启动工作池
		this.workerPool.start(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));
	}

	public MessageProducer getProducer(String producerId) {
		MessageProducer producer = this.producers.get(producerId);
		if (null == producer) {
			producer = new MessageProducer(producerId, this.ringBuffer);
			// 放入池中
			this.producers.put(producerId, producer);
		}
		return producer;
	}

	public static RingbufferWorkerPoolFactory getInstance() {
		return SingletonHolder.RINGBUFFER_WORK_POOL_FACTORY;
	}

	/**
	 * 使用静态内部类的方式实现单例模式
	 */
	private static class SingletonHolder {
		static final RingbufferWorkerPoolFactory RINGBUFFER_WORK_POOL_FACTORY = new RingbufferWorkerPoolFactory();
	}

	static class EventExceptionHandler implements ExceptionHandler<TransformDataWrapper> {
		public void handleEventException(Throwable ex, long sequence, TransformDataWrapper event) {

		}

		public void handleOnStartException(Throwable ex) {

		}

		public void handleOnShutdownException(Throwable ex) {

		}
	}
}
