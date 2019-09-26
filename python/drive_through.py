# coding: utf-8
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


class Worker(threading.Thread):
    def __init__(self, context, i, backend, restaurant):
        # call the constructor form the parent class
        threading.Thread.__init__(self)

        self.socket = context.socket(zmq.REP)
        self.backend = backend
        self.restaurant = restaurant

        # store the necessary inputs
        self.logger = logging.getLogger('Worker ' + str(i))

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


class DriveThrough():
    def __init__(self):
        self.orders = Queue()
        self.task = Queue()
        self.OrdersToDeliver = Queue()
        self.drink_machine = threading.Semaphore()  # go see lock to learn about
        self.grill = threading.Semaphore()
        self.frier = threading.Semaphore()
        self.pickupOrder = threading.Semaphore()
        self.OrdersToPickup = set()

    def add_order(self, order):
        order_id = uuid.uuid1()
        self.orders.put((order_id, order))
        return order_id

    def ready(self, order):
        self.task.put(order)

    def pickup(self, order_id):
        self.pickupOrder.acquire()
        if order_id in self.OrdersToPickup:
            self.OrdersToPickup.remove(order_id)
            item = order_id
        else:
            item = "EMPTY"
        self.pickupOrder.release()
        return item

    def get_order(self):
        try:
            return self.orders.get(block=True, timeout=1)
        except:
            return "EMPTY"

    def request_drink(self):
        self.drink_machine.acquire()
        work()
        self.drink_machine.release()

    def request_burger(self):
        self.grill.acquire()
        work()
        self.grill.release()

    def request_fries(self):
        self.frier.acquire()
        work()
        self.frier.release()

    def add_order_to_pending(self, order):
        self.OrdersToDeliver.put(order)

    def get_task(self):
        try:
            return self.OrdersToDeliver.get(block=True, timeout=1)
        except:
            return "EMPTY"

    def get_FoodReady(self):
        try:
            return self.task.get(block=True, timeout=1)
        except:
            return "EMPTY"

    def add_OrderForPickup(self, order):
        self.OrdersToPickup.add(order)


def main(ip, port):
    logger = logging.getLogger('Drive-through')

    logger.info('Setup ZMQ')
    context = zmq.Context()
    restaurant = DriveThrough()

    # frontend for clients (socket type Router)
    frontend = context.socket(zmq.ROUTER)
    frontend.bind("tcp://{}:{}".format(ip, port))

    # backend for workers (socket type Dealer)
    backend = context.socket(zmq.DEALER)
    backend.bind("inproc://backend")

    # Setup workers
    workers = []
    for i in range(5):
        # each worker is a different thread
        worker = Worker(context, i, "inproc://backend", restaurant)
        worker.start()
        workers.append(worker)

    # Setup proxy
    # This device (already implemented in ZMQ) connects the backend with the frontend
    zmq.proxy(frontend, backend)

    # join all the workers
    for w in workers:
        w.join()

    # close all the connections
    frontend.close()
    backend.close()
    context.term()


if __name__ == '__main__':
    main("127.0.0.1", "5001")
