# coding: utf-8
##Documentation for this module.
# This module is used to determine how the deliver of the restaurant will function
#

import logging
import pickle
import zmq
from utils import work, REQ_ORDER, ORDER_PENDING, wait, REQ_FOOD, ORDER_FOR_PICKUP

logging.basicConfig(level=logging.DEBUG,
                    format='%(asctime)s %(name)-12s %(levelname)-8s %(message)s',
                    datefmt='%m-%d %H:%M:%S')

## Documentation for this function
# This function will create a logger for the deliver and setup the ZMQ
# It will wait until food is prepared before delievering it to the client which requested it
def main(ip, port):
    logger = logging.getLogger('Deliver')
    logger.info('Setup ZMQ')
    context = zmq.Context()
    socket = context.socket(zmq.REQ)
    socket.connect('tcp://{}:{}'.format(ip, port))

    while True:
        logger.info('Request Food')
        p = pickle.dumps({"method": REQ_FOOD})
        socket.send(p)


        p = socket.recv()
        o = pickle.loads(p)
        logger.info('Received %s', o)

        if o == "EMPTY":
            logger.info("Going to wait...")
            wait()
            continue

        work()

        logger.info("Sending Food For Pickup")
        p = pickle.dumps({"method": ORDER_FOR_PICKUP, "args": o})
        socket.send(p)

        p = socket.recv()
        o = pickle.loads(p)

        if not o:
            break

    socket.close()
    context.term()

    return 0


if __name__ == '__main__':
    main("127.0.0.1", "5001")