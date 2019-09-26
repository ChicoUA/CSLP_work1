# coding: utf-8

import logging
import pickle
import zmq
from utils import work, REQ_TASK, TASK_READY, wait, REQ_GRILL, REQ_DRINK, REQ_FRIES

logging.basicConfig(level=logging.DEBUG,
                    format='%(asctime)s %(name)-12s %(levelname)-8s %(message)s',
                    datefmt='%m-%d %H:%M:%S')


def main(ip, port):
    # create a logger for the client
    logger = logging.getLogger('Cook')
    # setup zmq
    logger.info('Setup ZMQ')
    context = zmq.Context()
    socket = context.socket(zmq.REQ)
    socket.connect('tcp://{}:{}'.format(ip, port))

    while True:
        logger.info('Request Task')
        p = pickle.dumps({"method": REQ_TASK})
        socket.send(p)

        p = socket.recv()
        o = pickle.loads(p)
        logger.info('Received %s', o)

        if o == "EMPTY":
            logger.info("Going to wait...")
            wait()
            continue

        num = o["args"][1]["hamburger"]
        while num > 0:
            logger.info("Going to make an hamburger...")
            p = pickle.dumps({"method": REQ_GRILL})
            socket.send(p)

            p = socket.recv()
            s = pickle.loads(p)
            logger.info("I cooked a %s", s)
            num = num - 1

        num = o["args"][1]["batata"]
        while num > 0:
            logger.info("Going to make french fries...")
            p = pickle.dumps({"method": REQ_FRIES})
            socket.send(p)

            p = socket.recv()
            s = pickle.loads(p)
            logger.info("I got some %s", s)
            num = num - 1

        num = o["args"][1]["bebida"]
        while num > 0:
            logger.info("Going to get a drink...")
            p = pickle.dumps({"method": REQ_DRINK})
            socket.send(p)

            p = socket.recv()
            s = pickle.loads(p)
            logger.info("I got a %s", s)
            num = num - 1

        logger.info("Task Ready")
        logger.info("o[0]=%s", o["args"][0])
        p = pickle.dumps({"method": TASK_READY, "args": o["args"][0]})
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
