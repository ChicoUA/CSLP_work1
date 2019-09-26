import time
import argparse
import math
import random

ORDER = 0
REQ_ORDER = 1
TASK_READY = 2
PICKUP = 3
ORDER_PENDING = 4
REQ_TASK = 5
REQ_GRILL = 6
REQ_DRINK = 7
REQ_FRIES = 8
REQ_FOOD = 9
ORDER_FOR_PICKUP = 10


def work():
    time.sleep(math.fabs(random.gauss(1, 2)))  # math.fabs(random.gauss(1, 2))


def wait():
    time.sleep(2)


def traveling():
    time.sleep(math.fabs(random.gauss(2, 3)))

def food():
    return random.randint(1, 5)

