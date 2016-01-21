connection = new Mongo();
db = connection.getDB("tzli");

print("Query 1");
cursor = db.fudd.find({game: 1}).sort({action: 1})
while (cursor.hasNext()) {
x = cursor.next();
printjson(x);
}

print("\nQuery 2");
cursor = db.fudd.find({"game":1, "action.actionType":"Move"}).sort({"action.actionNumber":1}).limit(3)
while (cursor.hasNext()) {
x = cursor.next();
printjson(x);
}

print("\nQuery 3");
cursor = db.fudd.find({"action.actionType":"Move", "action.location.x": 11, "action.location.y": 12}).sort({"action.points":-1})
while (cursor.hasNext()) {
x = cursor.next();
printjson(x);
}

print("\nQuery 4");
cursor = db.fudd.find({"action.actionType": "SpecialMove", "action.pointsAdded": {$lt: 0}})
printjson(cursor);


print("\nQuery 5");
cursor = db.fudd.find({"action.actionType": "GameEnd"}, {user: 1, _id: 0})
while (cursor.hasNext()) {
x = cursor.next();
printjson(x);
}

print("\nQuery 6");
cursor = db.fudd.find({},{"action.points": 1, _id:0}).sort({"action.points": -1}).limit(3)
while (cursor.hasNext()) {
x = cursor.next();
printjson(x);
}

print("\nQuery 7");
cursor = db.fudd.find({$or: [{"action.actionType":"Move", "action.pointsAdded": {$gt: 10}}, {"action.actionType": "SpecialMove", "action.move": "Clear"}]}).sort({"action.actionType": -1, "action.pointsAdded": -1})
while (cursor.hasNext()) {
x = cursor.next();
printjson(x);
}

print("\nQuery 8");
cursor = db.fudd.find({$and: [ {$and: [ {"action.location.x": {$gte: 8}}, {"action.location.x": {$lte: 12}}, {"action.location.y": {$gte: 8}}, {"action.location.y": {$gte: 12}}] }, {$or: [ {"action.points": {$lt: 0}}, {"action.points": {$gt: 10}} ] } ]})
while (cursor.hasNext()) {
x = cursor.next();
printjson(x);
}

print("\nQuery 9");
cursor = db.fudd.find({"action.actionType": "GameStart"}).count()
printjson(cursor);


print("\nQuery 10");
cursor = db.fudd.find({"action.actionType": "Move", $or: [{"action.pointsAdded": 5}, {"action.pointsAdded": 6}]}, {"action.location.x": 1, "action.location.y": 1, "game": 1, "action.actionNumber": 1, _id: 0}).sort({"game": 1, "action.actionNumber": 1})
while (cursor.hasNext()) {
x = cursor.next();
printjson(x);
}