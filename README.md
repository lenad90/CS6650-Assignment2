# CS6650-Assignment2
## LiftServer
	The LiftServer contains a SkiersServlet which validates the URL for both doPost/doGet requests, and it also validates whether a request is a JSON payload and that is sent with the correct attributes.
ChannelBufferFactory, which extends BasedPoolObjectFactory, is used to generate a Channel pool connection to RabbitMQ by instantiating it with the GenericObjectPool. The init method adds the channel objects into the pool using a for-loop. After the Channel pool is created, doPost then borrows the object from the pool to publish the JSON string to a queue in RabbitMQ. It will return the object into the pool after it has been published.

## Consumer
	The Consumer program is a POJO that creates threads connecting to the same queue in the LiftServer. It runs one channel per thread in the ConsumerRunnable, and the runnable consumes the messages and places the information in a HashMap. The skier’s ID is extracted from the string and is used as the key, while the lift information is inserted as its value. A message acknowledgement is also implemented to ensure there can only be up to 1 unacknowledged message per thread. This will make sure that the message has been successfully delivered.

## LiftRecord
DataGeneration
	SkiersDataGeneration uses an Abstract Factory design pattern to generate random data for the JSON file.
Model
	SkiersRunner is a POJO that stores information regarding the generated data.
Threads
	The producer class puts the SkiersRunner into a BlockingQueue and the consumer runnable then takes the SkiersRunner object to run the POST requests each phase. There’s one producer thread producing and multiple consumer threads consuming. The SkiersClient runs 32, 64, and 128 threads in three different phases, and each phase displays the number of successful and unsuccessful posts, Wall Time, and the actual/expected throughput in the Phase class. Thread execution occurs in the Phase class and it’s where most of the thread creation, termination, and printing of the statistics of each phase happens. A thread-pool is used in this class to manage the consumer thread and ensure that threads are always running. Concurrency tools, such as AtomicIntegers and Collections.synchronizedList, were also used to ensure that the variables were thread-safe during the entire process.
Client
SkiersClient manages calling the threads, initializing the ApiClient, and handling the print statements.
Calculations
A calculations class is created in part2 of the assignment, and it is used to compute the mean, median, 99th percentile, min, and max.
