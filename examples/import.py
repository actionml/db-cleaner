"""
Import sample data for recommendation engine
"""

import predictionio
import argparse
import random
import datetime
import pytz

RATE_ACTIONS_DELIMITER = ","
PROPERTIES_DELIMITER = ":"
SEED = 1


def import_events(client, file):
    f = open(file, 'r')
    random.seed(SEED)
    count = 0
    current_date  = datetime.datetime.now(pytz.utc) # - datetime.timedelta(days=2.7)
    print "Importing data..."

    for line in f:
        data = line.rstrip('\r\n').split(RATE_ACTIONS_DELIMITER)
        # For demonstration purpose action names are taken from input along with secondary actions on
        # For the UR add some item metadata

        if (data[1] != "$set"):

            client.create_event(
                    event=data[1],
                    entity_type="user",
                    entity_id=data[0],
                    target_entity_type="item",
                    target_entity_id=data[2],
                    event_time = current_date + datetime.timedelta(days= float(data[3]))
            )
            print "Event: " + data[1] + " entity_id: " + data[0] + " target_entity_id: " + data[2] + \
                  " event_time: " + (current_date + datetime.timedelta(days= float(data[3]))).isoformat()
        elif (data[1] == "$set"):
            properties = data[2].split(PROPERTIES_DELIMITER)
            prop_name = properties.pop(0)
            prop_value = properties
            client.create_event(
                    event=data[1],
                    entity_type = "item",
                    entity_id = data[0],
                    event_time = current_date + datetime.timedelta(days= float(data[3])),
                    properties = {prop_name: prop_value}
            )
            print "Event: " + data[1] + " entity_id: " + data[0] + " properties/"+prop_name+": " + str(properties) + \
                  " event_time: " + (current_date + datetime.timedelta(days= float(data[3]))).isoformat()
        count += 1

    """
    items = ['Iphone 6', 'Ipad-retina', 'Nexus', 'Surface', 'Iphone 4', 'Galaxy', 'Iphone 5']
    print "All items: " + str(items)
    for item in items:

      client.create_event(
        event="$set",
        entity_type="item",
        entity_id=item,
        properties={"expires": expire_date.isoformat(),
                    "available": available_date.isoformat(),
                    "date": event_date.isoformat()}
      )
      print "Event: $set entity_id: " + item + \
              " properties/availableDate: " + available_date.isoformat() + \
              " properties/date: " + event_date.isoformat() + \
              " properties/expireDate: " + expire_date.isoformat()
      expire_date += available_date_increment
      event_date += available_date_increment
      available_date += available_date_increment
      count += 1
    """
    f.close()
    print "%s events are imported." % count


if __name__ == '__main__':
    parser = argparse.ArgumentParser(
            description="Import sample data for recommendation engine")
    parser.add_argument('--access_key', default='invald_access_key')
    parser.add_argument('--url', default="http://localhost:7070")
    parser.add_argument('--file', default="./data/sample-data.txt")

    args = parser.parse_args()
    print args

    client = predictionio.EventClient(
            access_key=args.access_key,
            url=args.url,
            threads=5,
            qsize=500)
    import_events(client, args.file)
