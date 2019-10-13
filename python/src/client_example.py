# coding: utf-8
##Documentation for this module.
# This module is used to simulate a client of the restaurant
#

import logging
import pickle
import zmq
from utils import ORDER, PICKUP, work, wait, traveling, food

logging.basicConfig(level=logging.DEBUG,
                    format='%(asctime)s %(name)-12s %(levelname)-8s %(message)s',
                    datefmt='%m-%d %H:%M:%S')

## Documentation for this function.
# This function will create a logger for the client and setup the ZMQ
# Additionally it will travel to and from the restaurant and order various food items each time and pickup the order
def main(ip, port):
    logger = logging.getLogger('Client')
    logger.info('Setup ZMQ')
    context = zmq.Context()
    socket = context.socket(zmq.REQ)
    socket.connect('tcp://{}:{}'.format(ip, port))

    logger.info("Im going to the restaurant!")
    traveling()

    logger.info('Request some food')

    p = pickle.dumps({"method": ORDER, "args": {"hamburger": food(), "batata": food(), "bebida": food()}})
    socket.send(p)

    p = socket.recv()
    o = pickle.loads(p)
    logger.info('Received %s', o)

    work()
    while True:
        logger.info('Pickup order %s', o)
        p = pickle.dumps({"method": PICKUP, "args": o})
        socket.send(p)

        p = socket.recv()
        s = pickle.loads(p)
        logger.info('Got %s', s)

        if s != "EMPTY":
            logger.info("I GOT MY ORDER! BYE!!!")
            break
        wait()

    socket.close()
    context.term()
    return 0


if __name__ == '__main__':
    main("127.0.0.1", "5001")
