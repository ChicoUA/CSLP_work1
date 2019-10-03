# coding: utf-8
##Documentation for this module.
#More details.

import uuid
import logging
import threading
import pickle
from queue import Queue
# from set import Set
import zmq
from utils import ORDER, REQ_ORDER, TASK_READY, PICKUP, REQ_TASK, ORDER_PENDING, work, REQ_GRILL, REQ_FRIES, REQ_DRINK, \
    REQ_FOOD, ORDER_FOR_PICKUP

# configure the log with DEBUG level
logging.basicConfig(level=logging.DEBUG,
                    format='%(asctime)s %(name)-12s %(levelname)-8s %(message)s',
                    datefmt='%m-%d %H:%M:%S')

## Documentation for this class.
#More details.
class Worker(threading.Thread):
    ## Documentation for this function
    #Calls the constructer from the parents class.
    #Stores the necessary inputs
    def __init__(self, context, i, backend, restaurant):
        threading.Thread.__init__(self)

        self.socket = context.socket(zmq.REP)
        self.backend = backend
        self.restaurant = restaurant

        self.logger = logging.getLogger('Worker ' + str(i))

    ## Documentation for this function
    #More details.
    def run(self):
        self.socket.connect(self.backend)

        self.logger.info('Start working')
        while True:
            p = self.socket.recv()
            o = pickle.loads(p)

            if o["method"] == ORDER:
                order_id = self.restaurant.add_order(o["args"])
                p = pickle.dumps(order_id)

            elif o["method"] == REQ_ORDER:
                task = self.restaurant.get_order()

                p = pickle.dumps(task)

            elif o["method"] == REQ_TASK:
                task = self.restaurant.get_task()
                p = pickle.dumps(task)

            elif o["method"] == TASK_READY:
                self.restaurant.ready(o["args"])

                p = pickle.dumps(True)

            elif o["method"] == PICKUP:
                order = self.restaurant.pickup(o["args"])

                p = pickle.dumps(order)

            elif o["method"] == ORDER_PENDING:
                self.restaurant.add_order_to_pending(o)

                p = pickle.dumps(o)

            elif o["method"] == REQ_GRILL:
                self.restaurant.request_burger()
                p = pickle.dumps("BURGER")

            elif o["method"] == REQ_DRINK:
                self.restaurant.request_drink()
                p = pickle.dumps("DRINK")

            elif o["method"] == REQ_FRIES:
                self.restaurant.request_fries()
                p = pickle.dumps("FRIES")

            elif o["method"] == REQ_FOOD:
                order = self.restaurant.get_FoodReady()
                p = pickle.dumps(order)

            elif o["method"] == ORDER_FOR_PICKUP:
                self.restaurant.add_OrderForPickup(o["args"])

            self.socket.send(p)
        self.socket.close()

## Documentation for this class.
#More details.
class DriveThrough():

    ## Documentation for this function
    #More details.
    def __init__(self):
        self.orders = Queue()
        self.task = Queue()
        self.OrdersToDeliver = Queue()
        self.drink_machine = threading.Semaphore()
        self.grill = threading.Semaphore()
        self.frier = threading.Semaphore()
        self.pickupOrder = threading.Semaphore()
        self.OrdersToPickup = set()

    ## Documentation for this function
    #More details.
    def add_order(self, order):
        order_id = uuid.uuid1()
        self.orders.put((order_id, order))
        return order_id

    ## Documentation for this function
    #More details.
    def ready(self, order):
        self.task.put(order)

    ## Documentation for this function
    #More details.
    def pickup(self, order_id):
        self.pickupOrder.acquire()
        if order_id in self.OrdersToPickup:
            self.OrdersToPickup.remove(order_id)
            item = order_id
        else:
            item = "EMPTY"
        self.pickupOrder.release()
        return item

    ## Documentation for this function
    #More details.
    def get_order(self):
        try:
            return self.orders.get(block=True, timeout=1)
        except:
            return "EMPTY"

    ## Documentation for this function
    #More details.
    def request_drink(self):
        self.drink_machine.acquire()
        work()
        self.drink_machine.release()

    ## Documentation for this function
    #More details.
    def request_burger(self):
        self.grill.acquire()
        work()
        self.grill.release()

    ## Documentation for this function
    #More details.
    def request_fries(self):
        self.frier.acquire()
        work()
        self.frier.release()

    ## Documentation for this function
    #More details.
    def add_order_to_pending(self, order):
        self.OrdersToDeliver.put(order)

    ## Documentation for this function
    #More details.
    def get_task(self):
        try:
            return self.OrdersToDeliver.get(block=True, timeout=1)
        except:
            return "EMPTY"
    ## Documentation for this function
    #More details.
    def get_FoodReady(self):
        try:
            return self.task.get(block=True, timeout=1)
        except:
            return "EMPTY"
    ## Documentation for this function
    #More details.
    def add_OrderForPickup(self, order):
        self.OrdersToPickup.add(order)

## Documentation for this function
#Initiates the front end for the clients (socket type Router)
#initiates the backend for workers (socket type Dealer)
#Sets up the workers with every worker being a different thread
#Sets up proxy, this device, which is already implemented in ZMQ, connects the backend with the frontend
#Joins all the workers
#closes all the connections
def main(ip, port):
    logger = logging.getLogger('Drive-through')

    logger.info('Setup ZMQ')
    context = zmq.Context()
    restaurant = DriveThrough()

    frontend = context.socket(zmq.ROUTER)
    frontend.bind("tcp://{}:{}".format(ip, port))

    backend = context.socket(zmq.DEALER)
    backend.bind("inproc://backend")

    workers = []
    for i in range(5):
        worker = Worker(context, i, "inproc://backend", restaurant)
        worker.start()
        workers.append(worker)

    zmq.proxy(frontend, backend)

    for w in workers:
        w.join()

    frontend.close()
    backend.close()
    context.term()


if __name__ == '__main__':
    main("127.0.0.1", "5001")
