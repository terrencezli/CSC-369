connection = new Mongo();
db = connection.getDB("tzli");

print("Query 1");
cursor = db.thot.find({"user": "u100"}, {"text": 1, _id: 0})
while (cursor.hasNext()) {
x = cursor.next();
printjson(x);
}

print("\nQuery 2");
cursor = db.thot.find({}, {"text": 1, _id: 0}).skip(6).limit(6)
while (cursor.hasNext()) {
x = cursor.next();
printjson(x);
}

print("\nQuery 3");
cursor = db.thot.find({$and: [{"recepient": {$ne: "all"}}, {"recepient": {$ne: "subscribers"}}, {"recepient": {$ne: "self"}}]}, {"user": 1, _id: 0}).sort({"user": 1})
while (cursor.hasNext()) {
x = cursor.next();
printjson(x);
}

print("\nQuery 4");
cursor = db.thot.find({"status": "protected"}).count()
printjson(cursor);


print("\nQuery 5");
cursor = db.thot.find({"in-response": {$exists: true}}, {"text": 1, _id: 0})
while (cursor.hasNext()) {
x = cursor.next();
printjson(x);
}

print("\nQuery 6");
cursor = db.thot.find({"status": "public", "recepient": "self"}).sort({"messageId": -1})
while (cursor.hasNext()) {
x = cursor.next();
printjson(x);
}

print("\nQuery 7");
cursor = db.thot.find({"status": "public", "recepient": "self", "in-response": {$exists: true}}, {"text": 1, _id: 0})
while (cursor.hasNext()) {
x = cursor.next();
printjson(x);
}

print("\nQuery 8");
cursor = db.thot.find({}, {"recepient": 1, "text": 1, _id: 0}).sort({"messageId": -1}).limit(4)
while (cursor.hasNext()) {
x = cursor.next();
printjson(x);
}

print("\nQuery 9");
cursor = db.thot.find({"status": "private", "recepient": "self"}, {"user": 1, _id: 0})
while (cursor.hasNext()) {
x = cursor.next();
printjson(x);
}

print("\nQuery 10");
cursor = db.thot.find({"status": "public", "in-response": {$exists: false}}, {"text": 1, _id: 0}).sort({"messageId": 1})
while (cursor.hasNext()) {
x = cursor.next();
printjson(x);
}