FROM python:3

WORKDIR /usr/src/app

ADD /src/run.sh /
COPY src/requirements.txt ./
RUN pip install --no-cache-dir -r requirements.txt

COPY . .

CMD [ "bash", "src/run.sh" ]