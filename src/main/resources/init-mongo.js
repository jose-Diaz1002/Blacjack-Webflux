db = db.getSiblingDB("blackjack");

db.players.insertMany([
    {
        name: "Initial Player 1",
        score: 200,
        createdAt: new Date()
    },
    {
        name: "Initial Player 2",
        score: 150,
        createdAt: new Date()
    }
]);
