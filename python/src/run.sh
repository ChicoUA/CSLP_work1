#!/bin/bash
python3 src/drive_through.py &
python3 src/cook.py &
python3 src/deliver.py &
python3 src/clerk_example.py &
python3 src/client_example.py &