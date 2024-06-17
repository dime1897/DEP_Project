# Communication styles (ZeroMQ)

## Introduction

ZeroMQ (ØMQ, 0MQ, or zmq) is a high-performance asynchronous and brokerless messaging library designed to simplify the construction of distributed and parallel applications. It provides a uniform API that allows programmers to send and receive messages between different processes, devices, and networks, hiding the underlying complexity of network communication.

The main objectives of ZeroMQ are:

* **Ease of Use**: Provide a simple and intuitive API that simplifies inter-process communication.
* **High Performance**: Optimize message transmission speed by reducing latency and increasing throughput.
* **Scalability**: Enable the construction of applications that can easily scale across multiple nodes.
* **Flexibility**: Support various messaging patterns, such as request-reply, publish-subscribe, push-pull, and more.
* **Portability**: Be compatible with numerous programming languages and platforms.

All the mechanism is based on 